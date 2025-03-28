package com.mhr.mobile.ui.content;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mhr.mobile.MainActivity;
import com.mhr.mobile.databinding.ContentInputPinBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class ContentInputPin extends InjectionActivity {
  private ContentInputPinBinding binding;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentInputPinBinding.inflate(getLayoutInflater());
    
	String pin = binding.etPin.getText().toString();
	verifyPin(pin);
	
	return binding.getRoot();
  }

  private void verifyPin(String enteredPin) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    if (user != null) {
      String userId = user.getUid();

      db.collection("users")
          .document(userId)
          .get()
          .addOnSuccessListener(
              documentSnapshot -> {
                if (documentSnapshot.exists()) {
                  String savedPin = documentSnapshot.getString("pin");

                  if (enteredPin.equals(savedPin)) {
                    // PIN benar, masuk ke home
                    startActivity(new Intent(ContentInputPin.this, MainActivity.class));
                  } else {
                    // PIN salah
                    Toast.makeText(ContentInputPin.this, "PIN salah!", Toast.LENGTH_SHORT).show();
                    // showPinDialog(); // Minta ulang PIN
                  }
                }
              })
          .addOnFailureListener(
              e ->
                  Toast.makeText(
                          ContentInputPin.this, "Gagal memverifikasi PIN", Toast.LENGTH_SHORT)
                      .show());
    }
  }
}
