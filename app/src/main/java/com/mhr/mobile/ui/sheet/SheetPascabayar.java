package com.mhr.mobile.ui.sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mhr.mobile.adapter.MenuListLinearAdapter;
import com.mhr.mobile.databinding.SheetPascabayarBinding;
import com.mhr.mobile.loader.PricelistLoader;
import com.mhr.mobile.manage.response.ListPascaResponse;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.util.AndroidViews;
import java.util.ArrayList;
import java.util.List;

public class SheetPascabayar extends InjectionSheetFragment {
  private SheetPascabayarBinding binding;
  private MenuListLinearAdapter adapter;
  private String category;
  private SelectProviderListener listener;

  public interface SelectProviderListener {
    void onSelect(String provider);
  }

  public void setOnSelectProviderListener(SelectProviderListener listener) {
    this.listener = listener;
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetPascabayarBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    applyRecycler();
    loadPertagas();
  }

  private void applyRecycler() {
    binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireActivity()));
    adapter = new MenuListLinearAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setShowChecked(false);
    adapter.setOnItemClickListener(
        produk -> {
          dismiss();
          if (listener != null) {
            listener.onSelect(produk.getCode());
          }
        });
  }

  private void loadPertagas() {
    PricelistLoader.loadPricePasca(
        requireActivity(),
        category,
        new PricelistLoader.LoaderExecutePasca() {
          @Override
          public void onStartLoading() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onSucces(List<ListPascaResponse.Pasca> produk) {
            binding.recyclerview.hideShimmerAdapter();
            adapter.perbaruiData(produk);
          }

          @Override
          public void onFailure(String error) {
            AndroidViews.showSnackbar(binding.getRoot(), error);
          }
        });
  }

  public void setCategory(String category) {
    this.category = category;
  }
}
