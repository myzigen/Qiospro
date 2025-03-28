package com.mhr.mobile.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.NavQiosproHomeBinding;
import com.mhr.mobile.ui.content.ContentNotification;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.navcontent.QiosDashboardFragment;
import com.mhr.mobile.ui.navcontent.QiosHomeSlider;
import com.mhr.mobile.ui.navcontent.QiosKategoriMenu;
import com.mhr.mobile.ui.navcontent.QiosMarketplace;
import com.mhr.mobile.viewmodel.HomeViewModel;

public class NavQiosproHome extends InjectionFragment {
  private NavQiosproHomeBinding binding;
  private boolean isConnected = false;
  private boolean isDataLoaded = false; // Status pemuatan data
  private HomeViewModel homeViewModel;

  @Override
  public View onCreateQiosFragment(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = NavQiosproHomeBinding.inflate(inflater, viewGroup, false);
    homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    setupToolbar();
    initRefresh();
    return binding.getRoot();
  }

  private void initRefresh() {
    binding.refresh.setOnRefreshListener(
        () -> {
          homeViewModel.setIsRefreshing(true);
          // Jika koneksi tersedia, reload data
          onDataReload();
          binding.refresh.postDelayed(() -> homeViewModel.setIsRefreshing(false), 1000);
        });
    homeViewModel
        .getIsRefreshing()
        .observe(
            getViewLifecycleOwner(), isRefreshing -> binding.refresh.setRefreshing(isRefreshing));
  }

  private void setupToolbar() {
    ((AppCompatActivity) getActivity()).getDelegate().setSupportActionBar(binding.toolbar);
    // binding.toolbar.setAlpha(0f);
    binding.toolbar.setTitle(getActivity().getString(R.string.app_name));
    binding.toolbar.setOnMenuItemClickListener(
        item -> {
          if (item.getItemId() == R.id.action_notification) {
            startActivity(new Intent(requireActivity(), ContentNotification.class));
            return true;
          }
          return false;
        });
    ViewCompat.setOnApplyWindowInsetsListener(
        binding.toolbar,
        (v, insets) -> {
          int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
          v.setPaddingRelative(0, statusBarHeight, 0, 0);
          return insets;
        });
    // binding.nestedscroll.setOnScrollChangeListener(onScroll);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.main_menu, menu);

    // Temukan MenuItem berdasarkan ID
    if (menu != null) {
      for (int i = 0; i < menu.size(); i++) {
        /*
              Drawable drawable = menu.getItem(i).getIcon();
              if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
              }
        */
      }
    }
  }

  @Override
  public void onNetworkConnected(boolean isConnected) {
    if (isConnected) {
      // onDataReload();
    } else {
      // Snackbar.make(binding.root, "Tidak ada koneksi", Snackbar.LENGTH_INDEFINITE).show();
    }
  }

  @Override
  public void onDataReload() {
    // Tampilkan animasi refresh
    binding.refresh.setRefreshing(true);

    // Reload data saat koneksi tersedia
    FragmentManager fm = getChildFragmentManager();

    QiosDashboardFragment qiosDashboardFragment =
        (QiosDashboardFragment) fm.findFragmentById(R.id.fragment_dashboard);
    if (qiosDashboardFragment != null) {
      qiosDashboardFragment.onDataReload();
    }
    QiosKategoriMenu qiosKategoriMenu = (QiosKategoriMenu) fm.findFragmentById(R.id.fragment_menu);
    if (qiosKategoriMenu != null) {
      qiosKategoriMenu.onDataReload();
    }

    QiosHomeSlider qiosHomeSlider = (QiosHomeSlider) fm.findFragmentById(R.id.fragment_slider);
    if (qiosHomeSlider != null) {
      qiosHomeSlider.onDataReload();
    }

    QiosMarketplace marketplace = (QiosMarketplace) fm.findFragmentById(R.id.fragment_marketplace);
    if (marketplace != null) {
      marketplace.onDataReload();
    }
  }

  private NestedScrollView.OnScrollChangeListener onScroll =
      (nested, x, y, oldX, oldY) -> {
        float maxScroll = 400f;
        float alpha = Math.min(1, (float) y / maxScroll);
        binding.toolbar.setAlpha(alpha);
      };
}
