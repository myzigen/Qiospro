package com.mhr.mobile.ui.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.NavQiosproPromosiBinding;
import com.mhr.mobile.ui.inject.InjectionFragment;

public class NavQiosproPromosi extends InjectionFragment {
  NavQiosproPromosiBinding binding;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setRetainInstance(true);
  }

  @Override
  public View onCreateQiosFragment(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = NavQiosproPromosiBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }
  
  @Override
  public void onDataReload() {
	  super.onDataReload();
  }
}
