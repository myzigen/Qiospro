package com.mhr.mobile.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mhr.mobile.R;
import com.mhr.mobile.model.User;
import com.mhr.mobile.util.AndroidUtilities;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.UtilsManager;

public class UserRegister extends AppCompatActivity {

  private EditText editTextName, editTextNomor, editTextEmail, editTextPassword;
  private Button buttonRegister;
  private FirebaseAuth mAuth;
  private FirebaseFirestore db;
  private TextView txtLogin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_register);

    editTextName = findViewById(R.id.editTextName);
    editTextEmail = findViewById(R.id.editTextEmail);
    editTextPassword = findViewById(R.id.editTextPassword);
    buttonRegister = findViewById(R.id.buttonRegister);
    txtLogin = findViewById(R.id.txt_login);
	editTextNomor = findViewById(R.id.editTextNomor);

    mAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();
	FirebaseFirestore.setLoggingEnabled(true);

    buttonRegister.setOnClickListener(v -> registerUser());
    txtLogin.setOnClickListener(v -> startActivity(new Intent(UserRegister.this, UserLogin.class)));
  }

  private void registerUser() {
    String userName = editTextName.getText().toString().trim();
	String nomor = editTextNomor.getText().toString().trim();
    String userEmail = editTextEmail.getText().toString().trim();
    String password = editTextPassword.getText().toString().trim();
    String deviceId = UtilsManager.getDeviceId();
    String deviceName = UtilsManager.getDeviceName();

    // Validasi Input
    if (TextUtils.isEmpty(userName)) {
      Toast.makeText(this, "Masukkan nama!", Toast.LENGTH_SHORT).show();
      return;
    }
    if (TextUtils.isEmpty(userEmail)) {
      Toast.makeText(this, "Masukkan email!", Toast.LENGTH_SHORT).show();
      return;
    }
    if (!isValidEmail(userEmail)) {
      Toast.makeText(this, "Email tidak valid!", Toast.LENGTH_SHORT).show();
      return;
    }
    if (TextUtils.isEmpty(password) || password.length() < 6) {
      Toast.makeText(this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
      return;
    }

    // Buat akun dengan Firebase
    mAuth
        .createUserWithEmailAndPassword(userEmail, password)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                // Tambahkan nama pengguna ke profil Firebase
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                  UserProfileChangeRequest profileUpdates =
                      new UserProfileChangeRequest.Builder().setDisplayName(userName).build();

                  user.updateProfile(profileUpdates)
                      .addOnCompleteListener(
                          profileTask -> {
                            if (profileTask.isSuccessful()) {
                              // Simpan data pengguna ke Firestore
							 // AndroidUtilities.getLocationFromIP(UserRegister.this,(latitude,longitude) ->{
								  saveUserToFirestore(user.getUid(), nomor, userName, userEmail,deviceId,deviceName/*,latitude,longitude*/);
							 // });
                              
                            } else {
                              AndroidViews.showToast("Gagal menyimpan nama pengguna", this);
                            }
                          });
                }
              } else {
                AndroidViews.showToast("Registrasi Gagal" + task.getException().getMessage(), this);
              }
            });
  }

  private void saveUserToFirestore(String userId, String nomor, String userName, String userEmail, String userDeviceId, String userDeviceName/*, double latitude, double longitude*/) {
    User userModel = new User();
    userModel.setUserId(userId);
	userModel.setUserNomor(nomor);
    userModel.setUserName(userName);
    userModel.setUserEmail(userEmail);
    userModel.setUserDeviceId(userDeviceId);
    userModel.setUserSaldo(0);
    userModel.setUserDeviceName(userDeviceName);
	//userModel.setLatitude(latitude);
	//userModel.setLongitude(longitude);

    db.collection("users")
        .document(userId)
        .set(userModel)
        .addOnSuccessListener(
            aVoid -> {
              AndroidViews.showToast("Registrasi berhasil", this);
              // Pindah ke LoginActivity setelah registrasi berhasil
              Intent intent = new Intent(UserRegister.this, UserLogin.class);
              startActivity(intent);
              finish();
            })
        .addOnFailureListener(
            e -> {
              Toast.makeText(this, "Gagal menyimpan data pengguna!", Toast.LENGTH_SHORT).show();
            });
  }

  // Validasi format email
  private boolean isValidEmail(String email) {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches();
  }
}
