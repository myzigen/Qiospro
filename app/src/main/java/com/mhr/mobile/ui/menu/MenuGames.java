package com.mhr.mobile.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import com.mhr.mobile.adapter.pager.ItemGridAdapter;
import com.mhr.mobile.databinding.MenuGamesBinding;
import com.mhr.mobile.loader.PricelistLoader;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class MenuGames extends InjectionActivity {
  private MenuGamesBinding binding;
  private List<PricelistResponse.Product> mData = new ArrayList<>();
  private ItemGridAdapter adapter;

  @Override
  protected String getTitleToolbar() {
    return "Games";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuGamesBinding.inflate(getLayoutInflater());
    loadDataGames();
    return binding.getRoot();
  }

  private void loadDataGames() {
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(3, 30, true));
    binding.recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
    adapter = new ItemGridAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    PricelistLoader.loadPricelist(
        this,
        "game",
        new PricelistLoader.LoaderExecute() {
          @Override
          public void onStartLoading() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onSucces(List<PricelistResponse.Product> produk) {
            binding.recyclerview.hideShimmerAdapter();
            adapter.perbaruiData(produk);
          }
        });
  }
}
