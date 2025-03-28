package com.mhr.mobile.ui.inject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import com.mhr.mobile.interfaces.ConnectivityListener;
import com.mhr.mobile.util.NetworkMonitoringUtil;
import com.mhr.mobile.util.UtilsManager;
import com.mhr.mobile.viewmodel.NetworkViewModel;

public abstract class InjectionConnectivity extends AppCompatActivity
    implements ConnectivityListener {

  protected NetworkViewModel networkViewModel;

  private final Observer<Boolean> activeNetworkStateObserver =
      new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isConnected) {
          onNetworkConnected(isConnected);
        }
      };

  // Inisialisasi ActivityResultLauncher untuk memilih kontak
  protected final ActivityResultLauncher<Intent> contactPickerLauncher =
      registerForActivityResult(
          new ActivityResultContracts.StartActivityForResult(),
          result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
              Uri contactUri = result.getData().getData();
              if (contactUri != null) {
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                try (Cursor cursor =
                    getContentResolver().query(contactUri, projection, null, null, null)) {
                  if (cursor != null && cursor.moveToFirst()) {
                    String phoneNumber =
                        cursor.getString(
                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    // Menghapus karakter "+" "-" dan spasi
                    phoneNumber =
                        phoneNumber.replaceAll("[\\+\\s-]", ""); // Hapus "+", spasi, dan "-"
                    if (phoneNumber.startsWith("62")) {
                      phoneNumber = "0" + phoneNumber.substring(2); // Ubah "62" menjadi "0"
                    }
                    onPhoneNumberPicked(phoneNumber); // Callback method untuk handle nomor
                  }
                }
              }
            } else {
              // Jika tidak ada kontak dipilih, bisa memberi tahu pengguna
              onPhoneNumberPickFailed();
            }
          });

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // getSharedAxis();

    // Pastikan NetworkMonitoringUtil memonitor status jaringan
    NetworkMonitoringUtil networkMonitoringUtil = new NetworkMonitoringUtil(this);
    networkMonitoringUtil
        .registerNetworkCallbackEvents(); // Pastikan ini dipanggil untuk memantau status jaringan
    networkMonitoringUtil.checkNetworkState(); // Cek status jaringan saat aplikasi dimulai

    // Inisialisasi ViewModel untuk memantau status jaringan
    networkViewModel = NetworkViewModel.getInstance();
    networkViewModel.getNetworkConnectivityStatus().observe(this, activeNetworkStateObserver);
  }

  @Override
  public void onNetworkConnected(boolean isConnected) {
    // Tangani perubahan status koneksi (misalnya, reload data jika terhubung)
    if (isConnected) {
      onDataReload(); // Panggil pemuatan ulang data jika koneksi tersedia
    } else {
      // Logika lain saat tidak ada koneksi, bisa memberi pesan atau lainnya
    }
  }

  @Override
  public void onDataReload() {
    // Override di aktivitas atau fragmen untuk reload data
  }

  // Callback yang menangani nomor telepon yang dipilih
  protected abstract void onPhoneNumberPicked(String phoneNumber);

  // Callback jika tidak ada kontak yang dipilih atau terjadi kegagalan
  protected abstract void onPhoneNumberPickFailed();

  // Definisikan judul toolbar untuk aktivitas
  protected abstract String getTitleToolbar();

  // Untuk aktivitas yang memiliki item berbeda di toolbar
  protected abstract Intent createIntent(int position);

  protected abstract void setupIncludePembayaran(View includeView);

  protected void setDrawUnderStatusbar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
      UtilsManager.setAllowDrawUnderStatusBar(getWindow());
    else UtilsManager.setStatusBarTranslucent(getWindow());
  }
  
  // Menyediakan metode untuk meluncurkan pemilih kontak
  protected void launchContactPicker() {
    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
    contactPickerLauncher.launch(intent);
  }
}
