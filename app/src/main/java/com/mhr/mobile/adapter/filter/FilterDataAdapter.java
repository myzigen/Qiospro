package com.mhr.mobile.adapter.filter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.filter.FilterDataAdapter.FilterVH;
import com.mhr.mobile.manage.response.PricelistResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterDataAdapter extends RecyclerView.Adapter<FilterDataAdapter.FilterVH> {
  private List<PricelistResponse.Product> allData; // Semua data
  private int selectedPosition = -1;
  private OnFilterSelectedListener listener; // Callback untuk mengirimkan filter
  private boolean isMasaAktifOrKuota;

  public interface OnFilterSelectedListener {
    void onFilterSelected(PricelistResponse.Product filter);
  }

  public void setOnFilterSelected(OnFilterSelectedListener listener) {
    this.listener = listener;
  }

  public FilterDataAdapter(List<PricelistResponse.Product> allData, boolean isMasaAktifOrKuota) {
    this.allData = removeDuplicates(allData); // Salin data
    this.isMasaAktifOrKuota = isMasaAktifOrKuota;
  }

  @NonNull
  @Override
  public FilterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_data, parent, false);
    return new FilterVH(view);
  }

  @Override
  public void onBindViewHolder(@NonNull FilterVH holder, int position) {
    PricelistResponse.Product filter = allData.get(position); // Ambil item dari semua data

    String produkDetail = filter.getProductDetails();
    if (produkDetail == null || produkDetail.trim().equals("-")) return;

    if (isMasaAktifOrKuota) {
      holder.button.setText(filter.getActivePeriod() + " Hari");
    } else {
      holder.button.setText(extractKuota(produkDetail));
    }

    // Tandai item yang dipilih
    if (selectedPosition == position) {
      holder.button.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#000000")));
    } else {
      holder.button.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
    }

    // Event klik untuk memilih filter
    holder.button.setOnClickListener(
        v -> {
          updateSelectedPosition(position); // Tetapkan filter yang dipilih
          // Kirimkan filter yang dipilih melalui listener
          if (listener != null) {
            listener.onFilterSelected(filter);
          }
        });
  }

  public String extractKuota(String productNominal) {
    // Cek apakah productNominal kosong atau hanya "-"
    if (productNominal == null || productNominal.trim().equals("-")) {
      return ""; // Mengembalikan string kosong jika tidak valid
    }

    // Menghapus simbol atau karakter lain yang bukan angka, koma, titik, atau unit GB/MB
    String cleanedNominal = productNominal.replaceAll("[^\\d,\\.\\sGBMB]", "").trim();

    // Regex untuk mencocokkan angka desimal (baik koma atau titik) yang diikuti oleh unit GB atau
    // MB
    Pattern pattern =
        Pattern.compile("(\\d+[,.]?\\d*)\\s*(GB|MB)"); // Menangkap angka desimal dan unit
    Matcher matcher = pattern.matcher(cleanedNominal);

    if (matcher.find()) {
      // Gabungkan angka dan unit tanpa spasi di antara keduanya
      return matcher.group(1).replace(",", ".")
          + matcher.group(2); // Mengganti koma dengan titik untuk format desimal
    }
    return ""; // Jika tidak ada kecocokan, kembalikan string kosong
  }

  @Override
  public int getItemCount() {
    return allData.size();
  }

  public void updateDataMasaAktif(List<PricelistResponse.Product> productList) {
    this.allData = new ArrayList<>(removeDuplicates(productList));
    notifyDataSetChanged();
  }

  private List<PricelistResponse.Product> removeDuplicates(
      List<PricelistResponse.Product> productList) {
    Map<String, PricelistResponse.Product> uniqueProducts = new HashMap<>();

    for (PricelistResponse.Product product : productList) {
      String key = product.getActivePeriod(); // Gunakan atribut yang unik untuk identifikasi
      uniqueProducts.put(key, product);
    }

    return new ArrayList<>(uniqueProducts.values());
  }

  public void sortByActivePeriod() {
    // Langkah 1: Urutkan data berdasarkan ActivePeriod
    Collections.sort(
        allData,
        new Comparator<PricelistResponse.Product>() {
          @Override
          public int compare(PricelistResponse.Product o1, PricelistResponse.Product o2) {
            try {
              // Mengonversi getActivePeriod() dari string ke integer
              int activePeriod1 = Integer.parseInt(o1.getActivePeriod().trim()); // Ambil angka
              int activePeriod2 = Integer.parseInt(o2.getActivePeriod().trim()); // Ambil angka

              return Integer.compare(activePeriod1, activePeriod2); // Bandingkan angka
            } catch (NumberFormatException e) {
              // Jika terjadi error (misalnya getActivePeriod() tidak bisa dikonversi ke integer)
              return 0; // Bisa diganti dengan logika lain jika diperlukan
            }
          }
        });

    // Langkah 2: Hapus angka "0" jika berada di posisi pertama setelah pengurutan
    if (!allData.isEmpty() && allData.get(0).getActivePeriod().equals("0")) {
      allData.remove(0); // Hapus angka "0" dari posisi pertama
    }

    // Setelah pengurutan dan pembersihan angka "0", refresh adapter
    notifyDataSetChanged();
  }

  public static class FilterVH extends RecyclerView.ViewHolder {
    MaterialButton button;

    public FilterVH(View itemView) {
      super(itemView);
      button = itemView.findViewById(R.id.btnMasaAktif);
    }
  }

  public void updateSelectedPosition(int newSelectedPosition) {
    int previousPosition = selectedPosition;
    selectedPosition = newSelectedPosition;
    // Refresh tampilan item yang berubah
    notifyItemChanged(previousPosition);
    notifyItemChanged(newSelectedPosition);
  }
}
