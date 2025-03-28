package com.mhr.mobile.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.MenuMarketplaceBinding;
import com.mhr.mobile.manage.response.MarketplaceResponse;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class MenuMarketplace extends InjectionActivity {
  private MenuMarketplaceBinding binding;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuMarketplaceBinding.inflate(getLayoutInflater());
    // toolbar.setBackgroundColor(QiosColor.getColor(this, R.color.white));
    terimaData();
    return binding.getRoot();
  }

  private void terimaData() {
    MarketplaceResponse.Data data = getIntent().getParcelableExtra("marketplace");
    /*Glide.with(this)
        .load(data.getImageUrl().get(0))
		.placeholder(R.drawable.ic_no_image)
        .into(binding.image);*/
  }
}
