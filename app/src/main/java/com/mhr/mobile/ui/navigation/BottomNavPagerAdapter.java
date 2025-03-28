package com.mhr.mobile.ui.navigation;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.mhr.mobile.R;
import java.util.ArrayList;
import java.util.List;

public class BottomNavPagerAdapter extends FragmentStatePagerAdapter {

  private Context context;
  private List<Fragment> mData = new ArrayList<>();

  public BottomNavPagerAdapter(Context ctx, FragmentManager fm) {
    super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    this.context = ctx;
    initFragment();
  }

  public void initFragment() {
    mData.add(new NavQiosproHome());
    mData.add(new NavQiosproTransaksi());
    mData.add(new NavQiosproPromosi());
    mData.add(new NavQiosproAkun());
  }

  @Override
  public int getCount() {
    return mData.size();
  }

  @Override
  public Fragment getItem(int position) {
    //if (position >= mData.size()) return mData.get(0);
    return mData.get(position);
  }
  // Returns the page title for the top indicator
  @Override
  public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return context.getResources().getString(R.string.app_name);
      case 1:
        return context.getResources().getString(R.string.app_name);
      case 2:
        return context.getResources().getString(R.string.app_name);
      default:
        return "";
    }
  }
}
