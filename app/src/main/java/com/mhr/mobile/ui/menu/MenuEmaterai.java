package com.mhr.mobile.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.adapter.ListMobileAdapter;
import com.mhr.mobile.databinding.QiosRecyclerviewBinding;
import com.mhr.mobile.loader.PricelistLoader;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.sheet.SheetDetailPembelian;
import java.util.ArrayList;
import java.util.List;

public class MenuEmaterai extends InjectionActivity {
  private QiosRecyclerviewBinding binding;
  private ListMobileAdapter adapter;

  @Override
  public String getTitleToolbar() {
    return "E-Meterai";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = QiosRecyclerviewBinding.inflate(getLayoutInflater());
    applyRecycler();
    return binding.getRoot();
  }

  private void applyRecycler() {
    binding.recyclerview.addItemDecoration(getSpacingItemDecoration(2, 50, true));
    binding.recyclerview.setLayoutManager(getGridLayoutManager(2));
    adapter = new ListMobileAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnDataClickListener(
        produk -> {
          adapter.setInputValid(true);
          SheetDetailPembelian sheet = SheetDetailPembelian.newInstance(produk);
          sheet.setTypeApi(produk.getTypeApi());
          sheet.setProdukCode(produk.getProductCode());
          sheet.setHarga(produk.getHargaDiskon());
          sheet.setProdukName(produk.getProductNominal());
          sheet.setBrand(produk.getProductNominal());
          sheet.setNomor("083168613666");
          sheet.show(getSupportFragmentManager());
        });
    loadEmaterai();
  }

  private void loadEmaterai() {
    PricelistLoader.loadPricelist(
        this,
        "emeterai",
        new PricelistLoader.LoaderExecute() {
          @Override
          public void onStartLoading() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onSucces(List<PricelistResponse.Product> produk) {
            binding.recyclerview.hideShimmerAdapter();
            adapter.updateData(produk);
          }
        });
  }
}
