package com.mhr.mobile.adapter.pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.mhr.mobile.ui.pager.PagerDataContent;

public class PagerMenuData extends FragmentStateAdapter {

  public PagerMenuData(FragmentActivity fa) {
    super(fa);
  }

  @Override
  public int getItemCount() {
    return 2;
  }

  @Override
  public Fragment createFragment(int position) {
    Fragment fragment;
    switch (position) {
      case 0:
        fragment = new PagerDataContent();
        break;
      case 1:
        fragment = new PagerDataContent();
        break;
      default:
        fragment = new PagerDataContent();
        break;
    }
    return fragment;
  }
}
