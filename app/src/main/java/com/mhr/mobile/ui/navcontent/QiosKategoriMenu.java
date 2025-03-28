package com.mhr.mobile.ui.navcontent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mhr.mobile.adapter.pager.QiosKategoriPager;
import com.mhr.mobile.databinding.QiosKategoriMenuBinding;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.pager.QiosKategoriPager1;
import com.mhr.mobile.ui.pager.QiosKategoriPager2;

public class QiosKategoriMenu extends InjectionFragment {
  private QiosKategoriMenuBinding binding;
  private QiosKategoriPager pager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = QiosKategoriMenuBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View arg0, Bundle arg1) {
    super.onViewCreated(arg0, arg1);
    pager = new QiosKategoriPager(getMainActivity());
    binding.viewpager2.setAdapter(pager);
    initViewpager();
  }

  private void initViewpager() {

    new TabLayoutMediator(
            binding.tablayout,
            binding.viewpager2,
            (tab, position) -> {
              switch (position) {
                case 0:
                  tab.setText("Favorite");
                  break;
                case 1:
                  tab.setText("Pascabayar");
                  break;
                case 2:
                  tab.setText("Convert");
                  break;
              }
            })
        .attach();
  }

  @Override
  public void onDataReload() {

    if (pager != null) {
      // Iterasi melalui semua halaman dan panggil onDataReload jika diperlukan
      for (int i = 0; i < pager.getItemCount(); i++) {
        Fragment fragment = pager.getFragmentAt(i);
        if (fragment instanceof QiosKategoriPager1) {
          ((QiosKategoriPager1) fragment).onDataReload();
        } else if (fragment instanceof QiosKategoriPager2) {
          ((QiosKategoriPager2) fragment).onDataReload();
        }
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    // onDataReload();
  }
}
