package com.mhr.mobile.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mhr.mobile.adapter.MenuListAdapter;
import com.mhr.mobile.databinding.MenuEwalletBinding;
import com.mhr.mobile.model.MenuList;
import com.mhr.mobile.ui.content.ContentEwallet;
import com.mhr.mobile.ui.inject.InjectionActivity;
import java.util.ArrayList;
import java.util.List;

public class MenuEwallet extends InjectionActivity {
  private MenuEwalletBinding binding;
  private MenuListAdapter adapter;
  private DatabaseReference db;
  private List<MenuList> mData = new ArrayList<>();

  @Override
  public String getTitleToolbar() {
    return "Dompet Digital";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuEwalletBinding.inflate(layoutInflater, viewGroup, false);
    initRecyclerview();
    return binding.getRoot();
  }

  private void initRecyclerview() {
    binding.recyclerview.addItemDecoration(getSpacingItemDecoration(3, 50, true));
    binding.recyclerview.setLayoutManager(getGridLayoutManager(3));
    initUi();
    loadDataEmoney();
  }

  private void initUi() {
    adapter = new MenuListAdapter(mData);
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnKlikDetailContent(
        position -> {
          MenuList menu = mData.get(position);
          Intent intent = new Intent(MenuEwallet.this, ContentEwallet.class);
          intent.putExtra("imageUrl", menu.getImageUrl());
          intent.putExtra("brand", menu.getBrand());
          intent.putExtra("code", menu.getCode());
          startActivity(intent);
        });
  }

  private void loadDataEmoney() {
    db = FirebaseDatabase.getInstance().getReference("list_ewallet");
    binding.recyclerview.showShimmerAdapter();
    db.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot snapshot) {
            binding.recyclerview.hideShimmerAdapter();
            mData.clear();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
              MenuList model = dataSnapshot.getValue(MenuList.class);
              mData.add(model);
            }
            adapter.notifyDataSetChanged();
          }

          @Override
          public void onCancelled(DatabaseError error) {}
        });
  }
}
