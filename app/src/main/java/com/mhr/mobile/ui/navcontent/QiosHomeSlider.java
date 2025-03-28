package com.mhr.mobile.ui.navcontent;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mhr.mobile.adapter.SliderHomeAdapter;
import com.mhr.mobile.databinding.QiosHomesliderFragmentBinding;
import com.mhr.mobile.manage.request.MarketplaceRequest;
import com.mhr.mobile.manage.response.SliderHomeResponse;
import com.mhr.mobile.model.ItemSliderHome;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.viewmodel.HomeViewModel;
import java.util.ArrayList;
import java.util.List;

public class QiosHomeSlider extends InjectionFragment {
  private QiosHomesliderFragmentBinding binding;
  private HomeViewModel viewModel;
  private SliderHomeAdapter adapter;
  private List<ItemSliderHome> mData = new ArrayList<>();
  private Handler handler = new Handler();
  private int currentPosition;

  @Override
  public View onCreateQiosFragment(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = QiosHomesliderFragmentBinding.inflate(getLayoutInflater());

    viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    initRecyclerview();
    return binding.getRoot();
  }

  private void initRecyclerview() {
    // binding.recyclerview.addItemDecoration(new SpacingItemDecoration(4, 50, true));
    binding.recyclerview.setLayoutManager(
        new LinearLayoutManager(getMainActivity(), LinearLayoutManager.HORIZONTAL, false));
    adapter = new SliderHomeAdapter(new ArrayList<>(), getMainActivity());
    binding.recyclerview.setAdapter(adapter);
    initSliderData();
  }

  private void initSliderData() {
    MarketplaceRequest request = new MarketplaceRequest(requireActivity());
    request.startRequestSlide(
        new MarketplaceRequest.RequestSlide() {
          @Override
          public void onStartRequest() {}

          @Override
          public void onResponse(SliderHomeResponse response) {
			  mData.clear();
			  
			  mData.addAll(response.getData());
			  adapter.notifyDataSetChanged();
		  }

          @Override
          public void onFailure(String error) {}
        });
    // startAutoSlide();
  }

  private void startAutoSlide() {
    final Runnable autoScroll =
        new Runnable() {
          @Override
          public void run() {
            if (currentPosition == mData.size()) {
              currentPosition = 0;
            }
            binding.recyclerview.smoothScrollToPosition(currentPosition++);
            handler.postDelayed(this, 3000);
          }
        };
    handler.postDelayed(autoScroll, 3000);
  }

  @Override
  public void onDataReload() {
    initSliderData();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    handler.removeCallbacksAndMessages(null);
  }
}
