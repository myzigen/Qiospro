package com.mhr.mobile.ui.pager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.mhr.mobile.adapter.QiosMenuAdapter;
import com.mhr.mobile.databinding.QiosKategoriPager2Binding;
import com.mhr.mobile.loader.KategoriLoader;
import com.mhr.mobile.model.MenuKategoriModel;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.menu.MenuEmaterai;
import com.mhr.mobile.ui.menu.MenuPascabayar;
import com.mhr.mobile.ui.menu.MenuPulsaInternasional;
import com.mhr.mobile.ui.menu.MenuStreaming;
import com.mhr.mobile.ui.menu.MenuVoucherBelanja;
import com.mhr.mobile.viewmodel.HomeViewModel;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class QiosKategoriPager2 extends InjectionFragment {
  private QiosKategoriPager2Binding binding;
  private QiosMenuAdapter adapter;
  private HomeViewModel homeViewModel;
  private List<MenuKategoriModel> mData = new ArrayList<>();
  private KategoriLoader kategoriLoader;

  @Override
  public View onCreateQiosFragment(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = QiosKategoriPager2Binding.inflate(getLayoutInflater());
    homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    initRecyclerview();

    return binding.getRoot();
  }

  private void initRecyclerview() {
    kategoriLoader = new KategoriLoader("kategori2");
    binding.recyclerview.setHasFixedSize(true);
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(4, 30, false));
    binding.recyclerview.setLayoutManager(new GridLayoutManager(requireActivity(), 4));

    loadData();

    adapter = new QiosMenuAdapter(requireActivity(), new ArrayList<>());
    adapter.setOnItemClickListener(posisiKlik -> showContent(posisiKlik));
    binding.recyclerview.setAdapter(adapter);
  }

  private void loadData() {
    kategoriLoader.applyKategori(
        new KategoriLoader.OnKategoriCallback() {
          @Override
          public void onLoading() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onDataLoaded(List<MenuKategoriModel> kategoriList) {
            binding.recyclerview.hideShimmerAdapter();
            // Tambahkan sedikit delay sebelum memperbarui data
            adapter.updateData(kategoriList);
          }

          @Override
          public void onError(Exception e) {}
        });
  }

  private void showContent(int position) {
    Intent intent = null;
    switch (position) {
      case 0:
        intent = new Intent(requireActivity(), MenuStreaming.class);
        break;
      case 1:
        intent = new Intent(requireActivity(), MenuPulsaInternasional.class);
        break;
      case 2:
        intent = new Intent(requireActivity(), MenuVoucherBelanja.class);
        break;
      case 3:
        intent = new Intent(requireActivity(), MenuPascabayar.class);
        intent.putExtra("category", "finance");
        break;
      case 4:
        intent = new Intent(requireActivity(), MenuPascabayar.class);
        intent.putExtra("category", "gas");
        break;
      case 5:
        intent = new Intent(requireActivity(), MenuPascabayar.class);
        intent.putExtra("category", "tv");
        break;
      case 6:
        intent = new Intent(requireActivity(), MenuEmaterai.class);
        break;
      case 7:
        intent = new Intent(requireActivity(), MenuPascabayar.class);
        intent.putExtra("category", "pbb");
        break;
    }
    if (intent != null) startActivity(intent);
  }

  @Override
  public void onDataReload() {
    loadData();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }
}
