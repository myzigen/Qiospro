package com.mhr.mobile.ui.pager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.ItemAktivasiAdapter;
import com.mhr.mobile.databinding.PagerDataContentBinding;
import com.mhr.mobile.ui.content.ContentMenuData;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class PagerDataContent extends InjectionFragment {
  private PagerDataContentBinding binding;
  private DatabaseReference db;
  private ItemAktivasiAdapter adapter;
  private List<ItemAktivasiAdapter.ItemAktivasi> mData = new ArrayList<>();

  @Override
  public View onCreateQiosFragment(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
    binding = PagerDataContentBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(3, 30, true));
    binding.recyclerview.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
    mData = new ArrayList<>();
    adapter = new ItemAktivasiAdapter(mData);
    binding.recyclerview.setAdapter(adapter);

    loadProvider();
    adapter.setOnItemClickItemAktivasiListener(
        model -> {
          Intent intent = new Intent(requireActivity(), ContentMenuData.class);
          intent.putExtra("imageUrl", model.getImageUrl());
          intent.putExtra("code", model.getCode());
          intent.putExtra("brand", model.getBrand());
          startActivity(intent);
        });
  }

  private void loadProvider() {
    binding.recyclerview.showShimmerAdapter();
    db = FirebaseDatabase.getInstance().getReference("data");
	db.keepSynced(true);
    db.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot snapshot) {
            binding.recyclerview.hideShimmerAdapter();
            mData.clear();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
              ItemAktivasiAdapter.ItemAktivasi model =
                  dataSnapshot.getValue(ItemAktivasiAdapter.ItemAktivasi.class);
              mData.add(model);
            }
            adapter.notifyDataSetChanged();
          }

          @Override
          public void onCancelled(DatabaseError error) {}
        });
  }
}
