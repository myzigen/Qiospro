package com.mhr.mobile.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.mhr.mobile.databinding.SettingPinAppBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidViews;
import com.poovam.pinedittextfield.SquarePinField;
import java.util.HashMap;
import java.util.Map;

public class UserSettingPin extends InjectionActivity {
  private SettingPinAppBinding binding;

  @Override
  protected String getTitleToolbar() {
    return "Atur PIN";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SettingPinAppBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {

    binding.etPin.setOnTextCompleteListener(
        new SquarePinField.OnTextCompleteListener() {
          @Override
          public boolean onTextComplete(String arg0) {
            //String etPin = arg0.getText().toString();
            applyPin(arg0);
            return true;
          }
        });
  }

  private void applyPin(String pin) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();
    Map<String, Object> pinData = new HashMap<>();
    pinData.put("pin", pin);

    db.collection("users")
        .document(userId)
        .set(pinData, SetOptions.merge())
        .addOnSuccessListener(
            aVoid -> {
              AndroidViews.showToast("Pin Berhasil Di Simpan", this);
            });
  }
}
