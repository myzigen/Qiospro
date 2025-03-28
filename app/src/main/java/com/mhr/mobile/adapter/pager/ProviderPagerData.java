package com.mhr.mobile.adapter.pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.mhr.mobile.ui.pager.ProviderDataPagerContent;
import com.mhr.mobile.ui.provider.ProviderData;
import java.util.HashMap;

public class ProviderPagerData /*extends FragmentStateAdapter */{
	/*
  private final String[] brands = {"Telkomsel Paket Internet", "Indosat Paket Internet", "Axis Paket Internet", "Xl Paket Internet", "Tri Paket Internet", "by.U Paket Intetnet"};
  private final HashMap<Integer, Fragment> fragmentMap = new HashMap<>();

  public ProviderPagerData(FragmentManager fragmentManager, Lifecycle lifecycle) {
    super(fragmentManager, lifecycle);
  }

  @Override
  public int getItemCount() {
    return brands.length;
  }

  @Override
  public Fragment createFragment(int position) {
        // Periksa apakah fragment sudah ada di fragmentMap
    if (!fragmentMap.containsKey(position)) {
      // Buat fragment baru dan simpan dalam fragmentMap
      Fragment fragment = ProviderDataPagerContent.newInstance(brands[position]); 
      fragmentMap.put(position, fragment);
    }
    return fragmentMap.get(position);
  }
  
  public Fragment getFragmentAt(int position) {
    return fragmentMap.get(position); // Mengembalikan fragment dari fragmentMap
  }
  */
}
