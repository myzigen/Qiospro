package com.mhr.mobile.ui.intro;

import android.app.Activity;
import android.util.Log;
import com.mhr.mobile.util.AndroidViews;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WhatsappHelper {
  private static final String TAG = "WhatsAppService";

  public void sendOtp(String phoneNumber, String otp, Activity activity) {
    // Ganti dengan ID nomor bisnis Anda
    String phoneNumberId = "581630778356864";

    // Durasi OTP berlaku
    String duration = "10";

    // Nama template yang telah disetujui
    String templateName = "qiosotp";

    WhatsappService apiService = WhatsappClient.getClient().create(WhatsappService.class);

    // Siapkan data pesan
    WhatsappMessage message = new WhatsappMessage(phoneNumber, templateName, otp, duration);

    // Kirim permintaan
    apiService
        .sendOtp(phoneNumberId, message)
        .enqueue(
            new Callback<Void>() {
              @Override
              public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                  Log.d(TAG, "OTP berhasil dikirim.");
                  AndroidViews.showToast("Otp berhasil dikirim",activity);
                } else {
                  AndroidViews.showToast("Gagal" + response.errorBody(), activity);
                  Log.e(TAG, "Gagal mengirim OTP. Response: " + response.errorBody());
                }
              }

              @Override
              public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Gagal menghubungi API: " + t.getMessage());
              }
            });
  }
}
