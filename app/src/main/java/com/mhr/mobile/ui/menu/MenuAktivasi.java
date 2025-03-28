package com.mhr.mobile.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.ItemAktivasiAdapter;
import com.mhr.mobile.databinding.MenuAktivasiBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.provider.ProviderData;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class MenuAktivasi/* extends InjectionActivity*/ {
	/*
  private MenuAktivasiBinding binding;
  private ItemAktivasiAdapter adapter;
  private List<ItemAktivasiAdapter.ItemAktivasi> mData = new ArrayList<>();

  @Override
  protected String getTitleToolbar() {
    return "Aktivasi";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuAktivasiBinding.inflate(layoutInflater, viewGroup, false);
    setUpRecyclerView();
    return binding.getRoot();
  }

  private void setUpRecyclerView() {
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(3, 25, true));
    binding.recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
    mData = new ArrayList<>();
    mData.add(new ItemAktivasiAdapter.ItemAktivasi(R.drawable.itm_data_internet, "Kuota\nInternet"));
    mData.add(new ItemAktivasiAdapter.ItemAktivasi(R.drawable.itm_bank, "Aktivasi Voucher"));
    mData.add(new ItemAktivasiAdapter.ItemAktivasi(R.drawable.itm_bpjs, "Aktivasi Perdana"));
    adapter = new ItemAktivasiAdapter(mData);
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnItemClickItemAktivasiListener(
        position -> {
          goToActivity(position);
        });
  }

  private void goToActivity(int position) {
    Intent intent = createIntent(position);
    switch (position) {
      case 0:
        intent = new Intent(this, ProviderData.class);
        break;
    }
    if (intent != null) startActivity(intent);
  }
  */
}
