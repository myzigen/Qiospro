package com.mhr.mobile.ui.sheet;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.DataInternetAdapter;
import com.mhr.mobile.adapter.filter.FilterDataAdapter;
import com.mhr.mobile.adapter.filter.FilterKuotaAdapter;
import com.mhr.mobile.databinding.SheetFilterKuotaBinding;
import com.mhr.mobile.loader.PricelistLoader;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SheetFilterKuota extends InjectionSheetFragment {
  private SheetFilterKuotaBinding binding;
  private DataInternetAdapter adapter;
  private RecyclerView recyclerview, recyclerMasaAktif;
  private FilterDataAdapter filterAdapter;
  private FilterKuotaAdapter filterKuotaAdapter;
  private LinearLayoutManager lM, lM2;
  private String filterHarga = "";
  private String filterMasaAktif = "";
  private String filterKuota = "";
  private String code;
  private FilterApplyListener listener;
  private List<PricelistResponse.Product> filteredData = new ArrayList<>();

  public interface FilterApplyListener {
    void onFilterApply(List<PricelistResponse.Product> filteredList);
  }

  public void setOnFilterApplyListener(FilterApplyListener listener) {
    this.listener = listener;
  }

  public static SheetFilterKuota newInstance(String code) {
    SheetFilterKuota fragment = new SheetFilterKuota();
    Bundle args = new Bundle();
    args.putString("code", code);
    fragment.setArguments(args);
    return fragment;
  }

  public void init(DataInternetAdapter adapter, RecyclerView recyclerview) {
    this.adapter = adapter;
    this.recyclerview = recyclerview;
  }

  @Override
  protected String getSheetTitle() {
    return "Filter Produk";
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetFilterKuotaBinding.inflate(inflater, viewGroup, false);

    if (getArguments() != null) {
      code = getArguments().getString("code");
    }

    initUi();
    setupRecyclerViewForFilter();
    return binding.getRoot();
  }

  private void initUi() {
    recyclerMasaAktif = binding.recyclerview;
    binding.btnReset.setOnClickListener(this::resetFilter);
    binding.btnHargaTertinggi.setOnClickListener(this::setHargaTertinggi);
    binding.btnHargaTerendah.setOnClickListener(this::setHargaTerendah);
    binding.btnTerapkan.setOnClickListener(this::applyFilter);
  }

  private void setupRecyclerViewForFilter() {
    // RECYCLERVIEW MASA AKTIF
    recyclerMasaAktif.addItemDecoration(new SpacingItemDecoration(1, 14, true));
    lM = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
    lM2 = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
    recyclerMasaAktif.setLayoutManager(lM);
    filterAdapter = new FilterDataAdapter(new ArrayList<>(), true);
    recyclerMasaAktif.setAdapter(filterAdapter);

    filterAdapter.setOnFilterSelected(
        applyFilterMasaAktif -> {
          filterMasaAktif = applyFilterMasaAktif.getActivePeriod();
        });
    binding.recyclerKuota.addItemDecoration(new SpacingItemDecoration(1, 14, true));
    binding.recyclerKuota.setLayoutManager(lM2);
    List<String> stringKuota = new ArrayList<>();

    stringKuota.add("<1GB");
    stringKuota.add("1-10GB");
    stringKuota.add("10-50GB");
    stringKuota.add("50-100GB");
    stringKuota.add("100GB>");
    filterKuotaAdapter = new FilterKuotaAdapter(stringKuota);
    binding.recyclerKuota.setAdapter(filterKuotaAdapter);
    filterKuotaAdapter.setOnFilterKuota(filter -> filterKuota = filter);

    loadData();
  }

  public void loadData() {
    PricelistLoader.loadPricelist(
        requireActivity(),
        "data",
        new PricelistLoader.LoaderExecute() {
          @Override
          public void onStartLoading() {}

          @Override
          public void onSucces(List<PricelistResponse.Product> produk) {
            filteredData = produk;
            List<PricelistResponse.Product> filtered = new ArrayList<>();
            for (PricelistResponse.Product filterMasaAktif : filteredData) {
              if (filterMasaAktif.getProductDescription().equalsIgnoreCase(code)) {
                filtered.add(filterMasaAktif);
              }
            }

            // Salin filtered data ke adapter
            filterAdapter.updateDataMasaAktif(filtered);
            filterAdapter.sortByActivePeriod();
          }
        });
  }

  private void setHargaTerendah(View v) {
    filterHarga = "Terendah";
    resetButtonColors();
    binding.btnHargaTerendah.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#000000")));
  }

  private void setHargaTertinggi(View v) {
    filterHarga = "Tertinggi";
    resetButtonColors();
    binding.btnHargaTertinggi.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#000000")));
  }

  public void applyFilter(View v) {
    List<PricelistResponse.Product> originalData = new ArrayList<>(adapter.getOriginalData());

    if (originalData == null || originalData.isEmpty()) return;

    // Salin data asli untuk filter
    filteredData = new ArrayList<>(originalData);

    if (filterHarga.isEmpty() && filterMasaAktif.isEmpty() && filterKuota.isEmpty()) {
      AndroidViews.showToast("Tidak ada filter yang di pilih", getActivity());
      dismiss();
      return;
    }

    // Terapkan filter berdasarkan harga, masa aktif, kuota
    if (filterHarga.equals("Terendah")) {
      filteredData.sort(Comparator.comparingDouble(PricelistResponse.Product::getProductPrice));
    } else if (filterHarga.equals("Tertinggi")) {
      filteredData.sort((p1, p2) -> Double.compare(p2.getProductPrice(), p1.getProductPrice()));
    }

    // Terapkan filter lainnya
    if (!filterMasaAktif.isEmpty()) {
      filteredData =
          filteredData.stream()
              .filter(product -> product.getActivePeriod().equalsIgnoreCase(filterMasaAktif))
              .collect(Collectors.toList());
      filteredData.sort(Comparator.comparingDouble(PricelistResponse.Product::getProductPrice));
    }

    // Terapkan filter berdasarkan kuota
    if (!filterKuota.isEmpty()) {
      filteredData =
          filteredData.stream()
              .filter(
                  product -> filterByKuota(product, filterKuota)) // Gunakan fungsi filterByKuota
              .collect(Collectors.toList());
      filteredData.sort(Comparator.comparingDouble(PricelistResponse.Product::getProductPrice));
    }

    // Periksa dan hapus produk yang tidak memiliki kuota yang valid
    filteredData =
        filteredData.stream()
            .filter(
                product -> {
                  double kuota = extractKuotaFromDetails(product.getProductDetails());
                  return kuota > 0; // Hanya produk yang memiliki kuota yang valid (lebih dari 0)
                })
            .collect(Collectors.toList());

    // Perbarui adapter dengan data yang difilter
    filterAdapter.updateDataMasaAktif(filteredData);
    filterAdapter.sortByActivePeriod();
    recyclerview.scrollToPosition(0);
    adapter.resetSelectedPosition();

    // Kirim data ke listener
    if (listener != null) {
      listener.onFilterApply(new ArrayList<>(filteredData)); // Pastikan salinan data
    }

    dismiss();
  }

  private void resetButtonColors() {
    int defaultColor = Color.parseColor("#E9E9E9");
    binding.btnHargaTertinggi.setStrokeColor(ColorStateList.valueOf(defaultColor));
    binding.btnHargaTerendah.setStrokeColor(ColorStateList.valueOf(defaultColor));
  }

  private void resetFilter(View v) {
    adapter.updateData(new ArrayList<>(adapter.getOriginalData()));
    adapter.sortProductListByPrice();
    adapter.resetSelectedPosition();
	recyclerview.scrollToPosition(0);
  }

  private boolean filterByKuota(PricelistResponse.Product product, String kuota) {
    String productDetails = product.getProductDetails(); // Ambil detail produk
    double productKuota = extractKuotaFromDetails(productDetails); // Kuota dalam satuan GB atau MB

    if (productKuota <= 0) return false;

    switch (kuota) {
      case "<1GB":
        // Tampilkan produk dengan kuota kurang dari atau sama dengan 1GB
        return productKuota <= 1; // Kuota <= 1GB (termasuk 1GB)
      case "1-10GB":
        return productKuota >= 1 && productKuota <= 10; // Kuota antara 1GB hingga 10GB
      case "10-50GB":
        return productKuota > 10 && productKuota <= 50; // Kuota antara 10GB hingga 50GB
      case "50-100GB":
        return productKuota > 50 && productKuota <= 100;
      case "100GB>":
        return productKuota >= 100;
      default:
        return false;
    }
  }

  private double extractKuotaFromDetails(String details) {
    try {
      // Regex untuk menangkap angka dan unit (MB atau GB)
      Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s*(MB|GB)", Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(details);

      if (matcher.find()) {
        double value = Double.parseDouble(matcher.group(1)); // Ambil angka kuota
        String unit = matcher.group(3).toUpperCase(); // Ambil satuan (MB atau GB)

        if (unit.equals("MB")) {
          return value / 1024; // Konversi MB ke GB (jika perlu untuk perbandingan)
        } else if (unit.equals("GB")) {
          return value; // Sudah dalam GB
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0; // Jika parsing gagal
  }
}
