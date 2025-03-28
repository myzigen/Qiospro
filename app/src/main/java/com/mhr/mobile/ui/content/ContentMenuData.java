package com.mhr.mobile.ui.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mhr.mobile.adapter.DataInternetAdapter;
import com.mhr.mobile.databinding.ContentMenuDataBinding;
import com.mhr.mobile.loader.MobilePulsaLoader;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.sheet.SheetContentData;
import com.mhr.mobile.ui.sheet.SheetFilterKuota;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class ContentMenuData extends InjectionActivity implements SearchView.OnQueryTextListener {
  private ContentMenuDataBinding binding;
  private DataInternetAdapter adapter;
  private List<PricelistResponse.Product> mData = new ArrayList<>();
  private MobilePulsaLoader mobilePulsaLoader;

  @Override
  public String getTitleToolbar() {
    return getIntent().getStringExtra("brand");
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentMenuDataBinding.inflate(layoutInflater, viewGroup, false);
    initUi();
    loadData();
    return binding.getRoot();
  }

  private void initUi() {
    mobilePulsaLoader = new MobilePulsaLoader(this);
    binding.searchview.setOnQueryTextListener(this);
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(1, 50, true));
    binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
    adapter = new DataInternetAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);

    adapter.setOnDataClickListener(
        produk -> {
          String imageUrl = getIntent().getStringExtra("imageUrl");
          SheetContentData sheet = SheetContentData.newInstance(produk);
          sheet.setImageUrl(imageUrl);
          sheet.setTypeApi(produk.getTypeApi());
          sheet.setProdukName(produk.getProductNominal());
          sheet.setHarga(produk.getHargaDiskon());
          sheet.setProdukDetail(produk.getProductDetails());
          sheet.setProdukCode(produk.getProductCode());
          sheet.setBrand("Paket Data");
          sheet.show(getSupportFragmentManager(), sheet.getTag());
        });

    binding.btnFilterHarga.setOnClickListener(
        v -> {
          String code = getIntent().getStringExtra("code");
          SheetFilterKuota sheet = SheetFilterKuota.newInstance(code);
          sheet.init(adapter, binding.recyclerview);
          sheet.setOnFilterApplyListener(
              filteredList -> {
                if (filteredList != null && !filteredList.isEmpty()) {
                  adapter.updateData(filteredList); // Update dengan data yang difilter
                  hitungJumlahProduk();
                } else {
                  adapter.updateData(
                      new ArrayList<>(
                          adapter.getOriginalData())); // Kembalikan data asli jika tidak ada hasil
                  hitungJumlahProduk();
                }
              });
          sheet.show(getSupportFragmentManager());
        });
  }

  private void loadData() {
    String type = getIntent().getStringExtra("code");
    mobilePulsaLoader.getDataFromFirebase(
        "data",
        type,
        new MobilePulsaLoader.LoaderExecute() {
          @Override
          public void onStartLoading() {
            binding.recyclerview.showShimmerAdapter();
            binding.expandable.collapse();
          }

          @Override
          public void onSucces(List<PricelistResponse.Product> produk) {
            binding.recyclerview.hideShimmerAdapter();
            binding.expandable.expand();
            mData = produk;
            adapter.setOriginalData(new ArrayList<>(mData)); // Set data asli ke adapter
            hitungJumlahProduk();
          }

          @Override
          public void onFailure(String error) {
            AndroidViews.showSnackbar(binding.getRoot(), error);
          }
        });
  }

  private void hitungJumlahProduk() {
    int count = adapter.getItemCount();
    binding.jumlahProduk.setText(count + " Produk");
  }

  @Override
  public boolean onQueryTextSubmit(String arg0) {
    return false;
  }

  @Override
  public boolean onQueryTextChange(String query) {
    cariKuota(query);
    return true;
  }

  private void cariKuota(String query) {
    List<PricelistResponse.Product> filterWilayah = new ArrayList<>();
    for (PricelistResponse.Product pasca : mData) {
      if (pasca.getProductNominal().toLowerCase().contains(query.toLowerCase())) {
        filterWilayah.add(pasca);
      }
    }

    adapter.setQueryText(query);
    adapter.updateData(filterWilayah);
    adapter.sortProductListByPrice();
    binding.recyclerview.scrollToPosition(0);
  }
}
