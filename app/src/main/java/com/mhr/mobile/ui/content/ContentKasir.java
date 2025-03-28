package com.mhr.mobile.ui.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.ContentKasirBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class ContentKasir extends InjectionActivity {
  private ContentKasirBinding binding;
  
  @Override
  protected String getTitleToolbar() {
    return "Kasir";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentKasirBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }
}
