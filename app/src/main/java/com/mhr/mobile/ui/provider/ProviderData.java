package com.mhr.mobile.ui.provider;

import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mhr.mobile.adapter.FilterProdukAdapter;
import com.mhr.mobile.adapter.pager.ProviderPagerData;
import com.mhr.mobile.databinding.ProviderDataBinding;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.pager.ProviderDataPagerContent;
import com.mhr.mobile.util.AndroidUtilities;
import com.mhr.mobile.util.ProviderUtils;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class ProviderData /*extends InjectionActivity */{
	/*
  ProviderDataBinding binding;
  ProviderPagerData pagerAdapter;
  private FilterProdukAdapter filterAdapter;
  private SparseIntArray posisiYgDipilih = new SparseIntArray();
  private int posisiTerpilih = 0;

  @Override
  protected String getTitleToolbar() {
    return "Data Internet";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ProviderDataBinding.inflate(layoutInflater, viewGroup, false);
    initViewPager();
    initRecyclerView();
    return binding.getRoot();
  }

  private void initRecyclerView() {
    //binding.filterRecyclerview.addItemDecoration(new SpacingItemDecoration(1, AndroidUtilities.dpToPx(8, this), true));
    binding.filterRecyclerview.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    filterAdapter =
        new FilterProdukAdapter(
            new ArrayList<>(),
            (filter, posisi) -> {
              posisiYgDipilih.put(binding.viewPager2.getCurrentItem(), posisi);
              if (posisi == 0) {
                resetSemuaFilter();
              } else {
                perbaruiFilter(filter.getProductNominal());
                updatePosition(posisi);
              }
            });
    binding.filterRecyclerview.setAdapter(filterAdapter);
  }

  private void initViewPager() {
    pagerAdapter = new ProviderPagerData(getSupportFragmentManager(), getLifecycle());
    binding.viewPager2.setAdapter(pagerAdapter);
    new TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager2,
            (tab, position) -> {
              tab.setText(ProviderUtils.getProviderName(position));
              
                       binding.tabLayout.postDelayed(
                           () -> {
                             tab.setIcon(
                                 VectorDrawableCompat.create(getResources(), tabIcon[position], null));
                           },
                           100);
              
            })
        .attach();
    binding.viewPager2.registerOnPageChangeCallback(
        new ViewPager2.OnPageChangeCallback() {
          @Override
          public void onPageSelected(int position) {
            super.onPageSelected(position);
            int simpanPosisi = posisiYgDipilih.get(position, 0); // Ambil posisi tersimpan
            posisiTerpilih = simpanPosisi;
            filterAdapter.updateSelectedPositionForPage(position, simpanPosisi);
            // Perbarui data berdasarkan halaman
            perbaruiDataForPage(position);
          }
        });
  }

  public ViewPager2 getViewPager2() {
    return binding.viewPager2;
  }

  public int getSelectedPositionForPage(int page) {
    return posisiYgDipilih.get(page, 0);
  }

  public void updatePosition(int position) {
    int currentPage = binding.viewPager2.getCurrentItem();
    posisiYgDipilih.put(currentPage, position);

    // Update posisi yang dipilih di adapter
    if (filterAdapter != null) {
      filterAdapter.updateSelectedPositionForPage(currentPage, position);
    }
  }
  //  di gunakan di TelkomselFragment untuh menambahkan datanya
  public void tambahkanItemFilter(List<PricelistResponse.Product> data) {
    List<PricelistResponse.Product> filterData = new ArrayList<>();

    if (data.isEmpty() || !"Semua".equals(data.get(0).getProductNominal())) {
      // Tambahkan item "Semua" jika diperlukan
      PricelistResponse.Product semuaProduct = new PricelistResponse.Product();
      semuaProduct.setProductNominal("Semua");
      filterData.add(semuaProduct); // Item "Semua" ditambahkan di posisi pertama
    }

    // Tambahkan produk yang difilter
    filterData.addAll(data);
    if (filterAdapter != null) {
      filterAdapter.updateData(filterData); // Update adapter dengan data yang baru
    }
  }

  private void perbaruiFilter(String filter) {
    Fragment fragment = pagerAdapter.getFragmentAt(binding.viewPager2.getCurrentItem());
    if (fragment instanceof ProviderDataPagerContent && filter != null) {
      ((ProviderDataPagerContent) fragment).applyFilter(filter);
    }
  }

  private void resetSemuaFilter() {
    Fragment fragment = pagerAdapter.getFragmentAt(binding.viewPager2.getCurrentItem());
    if (fragment instanceof ProviderDataPagerContent) {
      ((ProviderDataPagerContent) fragment).resetFilter();
    }
    posisiYgDipilih.put(binding.viewPager2.getCurrentItem(), 0);
  }

  private void perbaruiDataForPage(int posisi) {
    Fragment fragment = pagerAdapter.getFragmentAt(posisi);
    if (fragment != null && fragment instanceof ProviderDataPagerContent) {
      List<PricelistResponse.Product> data =
          ((ProviderDataPagerContent) fragment).dapatkanFilterData();
      if (data != null && !data.isEmpty()) {
        tambahkanItemFilter(data); // Update data untuk halaman yang aktif
      }
      // terapkan filter
      int posisiFilter = posisiYgDipilih.get(posisi, 0);
      String filterType = posisiFilter == 0 ? "Semua" : filterAdapter.getFilterType(posisiFilter);
      ((ProviderDataPagerContent) fragment).applyFilter(filterType);
    }
  }*/
}
