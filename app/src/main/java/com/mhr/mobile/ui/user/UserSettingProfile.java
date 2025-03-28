package com.mhr.mobile.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.UserEditProfileBinding;
import com.mhr.mobile.loader.QiosFirebaseHelper;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class UserSettingProfile extends InjectionActivity {
  private UserEditProfileBinding binding;
  private QiosFirebaseHelper firebaseHelper;

  @Override
  protected String getTitleToolbar() {
    return "Edit Akun";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserEditProfileBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    initialize();
    binding.deleteAkun.setOnClickListener(this::deleteAkun);
  }

  private void initialize() {
    firebaseHelper = new QiosFirebaseHelper(this);
  }

  private void deleteAkun(View v) {
    firebaseHelper.getDeleteAkun(binding.getRoot());
  }
}
