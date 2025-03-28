package com.mhr.mobile.ui.navcontent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.adapter.MarketplaceAdapter;
import com.mhr.mobile.databinding.QiosRecyclerviewBinding;
import com.mhr.mobile.manage.request.MarketplaceRequest;
import com.mhr.mobile.manage.response.MarketplaceResponse;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.menu.MenuMarketplace;
import com.mhr.mobile.util.AndroidViews;
import java.util.ArrayList;
import java.util.List;

public class QiosMarketplace extends InjectionFragment {
  private QiosRecyclerviewBinding binding;
  private MarketplaceAdapter adapter;

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = QiosRecyclerviewBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    applyRecycler();
    applyMarket();
  }

  private void applyRecycler() {
    binding.recyclerview.addItemDecoration(getSpacingDecoration(2, 30, true));
    binding.recyclerview.setLayoutManager(getGridLayoutManager(2));
    adapter = new MarketplaceAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnProdukClickListener(
        produk -> {
          Intent intent = new Intent(requireActivity(), MenuMarketplace.class);
		  Bundle args = new Bundle();
		  args.putParcelable("marketplace",produk);
		  intent.putExtras(args);
          startActivity(intent);
        });
  }

  private void applyMarket() {
    MarketplaceRequest request = new MarketplaceRequest(requireActivity());
    request.setApiKey();
    request.startRequestProduk(
        new MarketplaceRequest.RequestProduk() {
          @Override
          public void onStartRequest() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onResponse(List<MarketplaceResponse.Data> data) {
            binding.recyclerview.hideShimmerAdapter();
            adapter.updateData(data);
          }

          @Override
          public void onFailure(String error) {
            AndroidViews.showSnackbar(binding.getRoot(), error);
          }
        });
  }

  @Override
  public void onDataReload() {
    applyMarket();
  }
}
