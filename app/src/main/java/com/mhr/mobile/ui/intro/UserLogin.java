package com.mhr.mobile.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.mhr.mobile.MainActivity;
import com.mhr.mobile.R;
import com.mhr.mobile.loader.QiosFirebaseHelper;
import com.mhr.mobile.ui.dialog.LoadingDialogFragment;
import com.mhr.mobile.util.AndroidViews;

public class UserLogin extends AppCompatActivity {
  private EditText editTextEmail, editTextPassword;
  private Button buttonLogin;
  private FirebaseAuth mAuth;
  private TextView daftar;
  private LoadingDialogFragment dialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_login);
    dialog = new LoadingDialogFragment();
    editTextEmail = findViewById(R.id.editTextEmail);
    editTextPassword = findViewById(R.id.editTextPassword);
    buttonLogin = findViewById(R.id.buttonLogin);
    daftar = findViewById(R.id.btn_daftar);
    mAuth = FirebaseAuth.getInstance();

    buttonLogin.setOnClickListener(
        v -> {
          loginUser();
        });
    daftar.setOnClickListener(
        v -> {
          startActivity(new Intent(UserLogin.this, UserRegister.class));
        });
  }

  private void loginUser() {
    String email = editTextEmail.getText().toString().trim();
    String password = editTextPassword.getText().toString().trim();

    if (TextUtils.isEmpty(email)) {
      Toast.makeText(this, "Masukkan email!", Toast.LENGTH_SHORT).show();
      return;
    }
    if (TextUtils.isEmpty(password)) {
      Toast.makeText(this, "Masukkan password!", Toast.LENGTH_SHORT).show();
      return;
    }

    QiosFirebaseHelper helper = new QiosFirebaseHelper(this);
    helper.getLogin(
        email,
        password,
        new QiosFirebaseHelper.FirebaseLoginCallback() {
          @Override
          public void onStartLogin() {
            dialog.show(getSupportFragmentManager(), "TAG");
          }

          @Override
          public void onDataLogin() {
            dialog.showSuccess(() -> loginSuccess());
          }

          @Override
          public void onFailure(String error) {
            dialog.dismiss();
            AndroidViews.showSnackbar(buttonLogin, error);
          }
        });
  }

  @Override
  protected void onStart() {
    super.onStart();
    // Periksa apakah pengguna sudah login
    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
      // Jika sudah login, arahkan langsung ke MainActivity
      startActivity(new Intent(this, MainActivity.class));
      finish();
    }
  }

  private void loginSuccess() {
    startActivity(new Intent(this, MainActivity.class)); // Pindah ke halaman utama
    finish();
  }
}
