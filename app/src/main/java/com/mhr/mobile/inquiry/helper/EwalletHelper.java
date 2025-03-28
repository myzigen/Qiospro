package com.mhr.mobile.inquiry.helper;

import android.app.Activity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mhr.mobile.inquiry.request.InquiryRequest;
import com.mhr.mobile.inquiry.response.InquiryResponse;
import com.mhr.mobile.util.Config;

public class EwalletHelper {

  private DatabaseReference db;
  private Activity context;

  public EwalletHelper(Activity context) {
    this.context = context;
    db = FirebaseDatabase.getInstance().getReference("check_username_ewallet");
  }

  // Cek data pelanggan di Firebase
  public void checkCustomerName(String nomorPelanggan, String brand, int price, final EwalletCallback callback) {
    callback.onLoading();
    db.child(nomorPelanggan)
        .child(brand)
        .get()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful() && task.getResult().exists()) {
                InquiryResponse.EwalletData data = task.getResult().getValue(InquiryResponse.EwalletData.class);
                if (data != null) {
                  callback.onDataFound(data);
                } else {
                  fetchFromApiAndSaveToFirebase(nomorPelanggan, price, brand, callback);
                }
              } else {
                fetchFromApiAndSaveToFirebase(nomorPelanggan, price, brand, callback);
              }
            });
  }

  // Ambil data dari API dan simpan ke Firebase
  private void fetchFromApiAndSaveToFirebase(String nomorPelanggan, int price, String brand, final EwalletCallback callback) {
    callback.onLoading();
    InquiryRequest request = new InquiryRequest(context);
    request.setUsername();
    request.setApiKey();
    request.setCodeProduk(brand);
    request.setHp(nomorPelanggan);
    request.setAmount(price);

    request.startInquiryRequest(
        new InquiryRequest.InquiryCallback() {
          @Override
          public void onStartLoading() {
            callback.onLoading();
          }

          @Override
          public void onResponse(InquiryResponse response) {
            InquiryResponse.Data data = response.getData();
            if (data != null && data.getTrName() != null && !data.getTrName().isEmpty()) {
              InquiryResponse.EwalletData simpleData = new InquiryResponse.EwalletData(data.getHp(), data.getTrName(), data.getCode());
              if (data.getCode().equalsIgnoreCase(brand)) {
				callback.onDataFound(simpleData);
                saveToFirebase(nomorPelanggan, simpleData, brand, callback);
              }
            } else {
              callback.onError(data != null ? data.getMessage() : "Nama pengguna tidak ditemukan");
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onError(errorMessage);
          }
        });
  }

  // Simpan data ke Firebase
  private void saveToFirebase(String nomorPelanggan,InquiryResponse.EwalletData simpleData,String brand,EwalletCallback callback) {
      db.child(nomorPelanggan)
        .child(brand)
        .setValue(simpleData)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                callback.onDataSaved();
              } else {
                callback.onError("Gagal menyimpan data");
              }
            })
        .addOnFailureListener(e -> callback.onError("Error menyimpan data: " + e.getMessage()));
  }

  // Interface callback untuk memberikan respon ke UI
  public interface EwalletCallback {
    void onLoading();

    void onDataFound(InquiryResponse.EwalletData data);

    void onDataSaved();

    void onError(String errorMessage);
  }
}
