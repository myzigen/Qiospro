package com.mhr.mobile.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.mhr.mobile.databinding.SplashScreenBinding;
import com.mhr.mobile.util.UtilsManager;

public class SplashScreen extends AppCompatActivity {
  SplashScreenBinding binding;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    binding = SplashScreenBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    binding.tvVersion.setText("Version V" + UtilsManager.getVersionApp(this));
    /*
       FirebaseFirestore db = FirebaseFirestore.getInstance();
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       if (user != null) {
         String userId = user.getUid();

         db.collection("users")
             .document(userId)
             .get()
             .addOnSuccessListener(
                 documentSnapshot -> {
                   if (documentSnapshot.exists() && documentSnapshot.getBoolean("pin_enabled")) {
                     // PIN aktif, tampilkan dialog untuk memasukkan PIN
                     startActivity(new Intent(this,ContentInputPin.class));
                   } else {
                     // PIN tidak aktif, langsung masuk ke home
                     //goToHome();
                   }
                 })
             .addOnFailureListener(
                 e -> Toast.makeText(this, "Gagal mengambil data PIN", Toast.LENGTH_SHORT).show());
       }
    */
    new Handler()
        .postDelayed(
            new Runnable() {
              @Override
              public void run() {
                startActivity(new Intent(SplashScreen.this, UserLogin.class));
                finish();
                overridePendingTransition(0, 0);
              }
            },
            1500);
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
      startActivity(new Intent(this, UserLogin.class));
      finish();
    }
  }
}
