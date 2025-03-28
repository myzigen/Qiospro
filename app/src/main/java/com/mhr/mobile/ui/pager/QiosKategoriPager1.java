package com.mhr.mobile.ui.pager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import com.mhr.mobile.adapter.QiosMenuAdapter;
import com.mhr.mobile.databinding.QiosKategoriPager1Binding;
import com.mhr.mobile.loader.KategoriLoader;
import com.mhr.mobile.model.MenuKategoriModel;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.menu.MenuData;
import com.mhr.mobile.ui.menu.MenuEmaterai;
import com.mhr.mobile.ui.menu.MenuEwallet;
import com.mhr.mobile.ui.menu.MenuGames;
import com.mhr.mobile.ui.menu.MenuPascabayar;
import com.mhr.mobile.ui.menu.MenuPdam;
import com.mhr.mobile.ui.menu.MenuPln;
import com.mhr.mobile.ui.menu.MenuPulsa;
import com.mhr.mobile.ui.menu.MenuVoucherBelanja;
import com.mhr.mobile.ui.menu.MenuPulsaInternasional;
import com.mhr.mobile.ui.menu.MenuStreaming;
import com.mhr.mobile.ui.menu.MenuWifi;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class QiosKategoriPager1 extends InjectionFragment {
  private QiosKategoriPager1Binding binding;
  private QiosMenuAdapter adapter;
  private KategoriLoader kategoriLoader;
  private Handler handler;
  private List<MenuKategoriModel> mData = new ArrayList<>();
  private String kategori;

  public static QiosKategoriPager1 newInstance(String kategori) {
    QiosKategoriPager1 fragment = new QiosKategoriPager1();
    Bundle args = new Bundle();
    args.putString("kategori", kategori);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = QiosKategoriPager1Binding.inflate(getLayoutInflater());

    kategori = getArguments().getString("kategori");
    kategoriLoader = new KategoriLoader(kategori);
    binding.recyclerview.setHasFixedSize(true);
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(4, 30, false));
    binding.recyclerview.setLayoutManager(new GridLayoutManager(requireActivity(), 4));

    // Inisialisasi Adapter
    adapter = new QiosMenuAdapter(requireActivity(), new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);

    // Tambahkan listener klik pada item RecyclerView
    adapter.setOnItemClickListener(posisiKlik -> endToDetail(posisiKlik));

    loadDataKategori();
    return binding.getRoot();
  }

  private void loadDataKategori() {
    binding.recyclerview.showShimmerAdapter();
    kategoriLoader.applyKategori(
        new KategoriLoader.OnKategoriCallback() {
          @Override
          public void onLoading() {
            binding.recyclerview.showShimmerAdapter(); // Menampilkan shimmer
          }

          @Override
          public void onDataLoaded(List<MenuKategoriModel> kategoriList) {
            binding.recyclerview.hideShimmerAdapter();
            mData = kategoriList;
            adapter.updateData(mData);
          }

          @Override
          public void onError(Exception e) {
            // Menangani kesalahan, misalnya menampilkan pesan error
          }
        });
  }

  private void endToDetail(int position) {

    Intent intent = null;
    if ("kategori".equalsIgnoreCase(kategori)) {
      switch (position) {
        case 0:
          intent = new Intent(requireActivity(), MenuPulsa.class);
          break;
        case 1:
          intent = new Intent(requireActivity(), MenuData.class);
          break;
        case 2:
          intent = new Intent(requireActivity(), MenuPln.class);
          break;
        case 3:
          intent = new Intent(requireActivity(), MenuEwallet.class);
          break;
        case 4:
          intent = new Intent(requireActivity(), MenuWifi.class);
          break;
        case 5:
          intent = new Intent(requireActivity(), MenuPascabayar.class);
          intent.putExtra("category", "bpjs");
          break;
        case 6:
          intent = new Intent(requireActivity(), MenuPdam.class);
          break;
        case 7:
          intent = new Intent(requireActivity(), MenuGames.class);
          break;
      }
    } else if ("kategori2".equalsIgnoreCase(kategori)) {
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
    }
    startActivity(intent);
  }

  @Override
  public void onNetworkConnected(boolean isConnected) {
    // Implementasikan logika koneksi jaringan jika diperlukan
  }

  @Override
  public void onDataReload() {
    loadDataKategori(); // Reload data ketika dibutuhkan
  }
}
