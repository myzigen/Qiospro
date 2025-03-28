package com.mhr.mobile.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.adapter.ListMobileGridAdapter;
import com.mhr.mobile.databinding.QiosRecyclerviewGridBinding;
import com.mhr.mobile.loader.PricelistLoader;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.ui.content.ContentVoucherBelanja;
import com.mhr.mobile.ui.inject.InjectionActivity;
import java.util.ArrayList;
import java.util.List;

public class MenuVoucherBelanja extends InjectionActivity {
  private QiosRecyclerviewGridBinding binding;
  private ListMobileGridAdapter adapter;

  @Override
  public String getTitleToolbar() {
    return "Voucher Belanja";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = QiosRecyclerviewGridBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    applyRecycler();
    loadVoucherBelanja();
  }

  private void applyRecycler() {
    binding.recyclerview.addItemDecoration(getSpacingItemDecoration(3, 50, true));
    binding.recyclerview.setLayoutManager(getGridLayoutManager(3));
    adapter = new ListMobileGridAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnProdukClickListener(
        produk -> {
          Intent intent = new Intent(this, ContentVoucherBelanja.class);
          intent.putExtra("voucher", produk.getProductDescription());
          startActivity(intent);
        });
  }

  private void loadVoucherBelanja() {
    PricelistLoader.loadPricelist(
        this,
        "voucher",
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
