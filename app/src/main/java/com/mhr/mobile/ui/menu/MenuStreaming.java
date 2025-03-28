package com.mhr.mobile.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.adapter.ListMobileGridAdapter;
import com.mhr.mobile.databinding.QiosRecyclerviewBinding;
import com.mhr.mobile.databinding.QiosRecyclerviewGridBinding;
import com.mhr.mobile.loader.PricelistLoader;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.ui.content.ContentStreaming;
import com.mhr.mobile.ui.inject.InjectionActivity;
import java.util.ArrayList;
import java.util.List;

public class MenuStreaming extends InjectionActivity {
  private QiosRecyclerviewGridBinding binding;
  private ListMobileGridAdapter adapter;

  @Override
  protected String getTitleToolbar() {
    return "Paket Streaming";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = QiosRecyclerviewGridBinding.inflate(getLayoutInflater());
    initRecyclerview();
    loadData();
    return binding.getRoot();
  }

  private void initRecyclerview() {
    binding.recyclerview.addItemDecoration(getSpacingItemDecoration(3, 30, true));
    binding.recyclerview.setLayoutManager(getGridLayoutManager(3));
    adapter = new ListMobileGridAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnProdukClickListener(
        produk -> {
          Intent intent = new Intent(this, ContentStreaming.class);
          intent.putExtra("paketstreaming", produk.getProductDescription());
          startActivity(intent);
        });
  }

  private void loadData() {
    PricelistLoader.loadPricelist(
        this,
        "streaming",
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
