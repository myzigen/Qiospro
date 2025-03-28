package com.mhr.mobile.ui.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.ContentPaymentStatusBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class ContentPaymentStatusIAK extends InjectionActivity {

  private ContentPaymentStatusBinding binding;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentPaymentStatusBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }
}
