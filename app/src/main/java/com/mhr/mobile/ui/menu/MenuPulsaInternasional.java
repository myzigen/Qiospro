package com.mhr.mobile.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.MenuPulsaBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class MenuPulsaInternasional extends InjectionActivity {
  private MenuPulsaBinding binding;

  @Override
  public String getTitleToolbar() {
    return "Pulsa Internasional";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuPulsaBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }
}
