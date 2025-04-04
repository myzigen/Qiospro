package com.mhr.mobile.adapter.pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.mhr.mobile.ui.pager.QiosKategoriPager1;
import java.util.HashMap;

public class QiosKategoriPager extends FragmentStateAdapter {

  private String[] kategori = {"kategori", "kategori2"};
  private final HashMap<Integer, Fragment> fragmentMap = new HashMap<>();

  public QiosKategoriPager(FragmentActivity activity) {
    super(activity);
  }

  @Override
  public int getItemCount() {
    return kategori.length; // Total jumlah halaman
  }

  @Override
  public Fragment createFragment(int position) {
    if (!fragmentMap.containsKey(position)) {
      Fragment fragment = QiosKategoriPager1.newInstance(kategori[position]);
      fragmentMap.put(position, fragment);
    }
    return fragmentMap.get(position);
  }
  
  public Fragment getFragmentAt(int position) {
    return fragmentMap.get(position); // Mengembalikan fragment dari fragmentMap
  }
}
