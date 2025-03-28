package com.mhr.mobile.ui.menu;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DatabaseReference;
import com.mhr.mobile.databinding.MenuWifiBinding;
import com.mhr.mobile.inquiry.helper.WifiHelper;
import com.mhr.mobile.inquiry.response.InquiryResponse;
import com.mhr.mobile.inquiry.response.InquiryResponse.Data;
import com.mhr.mobile.inquiry.response.InquiryResponse.WifiData;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.sheet.BottomSheetWifi;
import com.mhr.mobile.ui.tagihan.DetailTagihanInternet;
import com.mhr.mobile.util.AndroidViews;

public class MenuWifi extends InjectionActivity implements TextWatcher {
  private MenuWifiBinding binding;
  private String noPelanggan;
  private String selectedCodeProduk = null;
  private InquiryResponse.Data mData;
  private InquiryResponse.WifiData mDataWifi;
  private DatabaseReference db;
  private String mImageUrl;
  private WifiHelper wifiHelper;

  @Override
  protected String getTitleToolbar() {
    return "Tagihan Internet";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuWifiBinding.inflate(layoutInflater, viewGroup, false);

    initUI();
    showBottomSheet();
    return binding.getRoot();
  }

  private void initUI() {
    wifiHelper = new WifiHelper(this);
    binding.editText.addTextChangedListener(this);
    noPelanggan = binding.editText.getText().toString();
    binding.btnCekTagihan.setEnabled(false);
    binding.btnCekTagihan.setText("Cek Pengguna");

    binding.btnCekTagihan.setOnClickListener(
        v -> {
          if (mData != null || mDataWifi != null) {
            openDetailTagihan();
          } else {
            checkCustomerName(noPelanggan); // Fetch from Firebase or API
          }
        });
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    noPelanggan = s.toString().trim();
    validateForm(noPelanggan);
  }

  private void validateForm(String noPelanggan) {
    if (noPelanggan.isEmpty() || selectedCodeProduk == null || noPelanggan.length() < 6) {
      binding.btnCekTagihan.setEnabled(false);
      binding.btnCekTagihan.setText("Cek Pengguna");
    } else if (mData != null || mDataWifi != null) {
      binding.btnCekTagihan.setEnabled(true);
      binding.btnCekTagihan.setText("Lihat Tagihan");
    } else {
      binding.btnCekTagihan.setEnabled(true);
      binding.btnCekTagihan.setText("Cek Pengguna");
    }
  }

  private void checkCustomerName(String noPelanggan) {
    wifiHelper.checkCustomerName(
        noPelanggan,
        selectedCodeProduk,
        new WifiHelper.WifiCallback() {
          @Override
          public void onLoading() {
            showExpandSuccess("Memeriksa data pengguna");
          }

          @Override
          public void onDataApi(Data data) {
            // Jika data API ditemukan, langsung kirimkan dan perbarui
            mData = data;
            showExpandSuccess(mData.getTrName());
            validateForm(noPelanggan);
            Log.d("MenuWifi", "Data API diterima: " + mData);
          }

          @Override
          public void onDataFound(WifiData data) {
            // Jika data ditemukan di Firebase, ambil nama pengguna (trName)
            mDataWifi = data;
            showExpandSuccess(data.getTrName());
            validateForm(noPelanggan);
            Log.d("mDataWifi", "Dari Firebase: " + mDataWifi);

            // Setelah data Firebase, panggil API hanya untuk mengambil data lengkap jika perlu
            if (mData == null) { // Jika data API belum ada, baru ambil data lengkap
              wifiHelper.fetchFromApiWithoutSaving(
                  noPelanggan,
                  selectedCodeProduk,
                  new WifiHelper.WifiCallback() {
                    @Override
                    public void onLoading() {
                      showExpandSuccess("Mengambil data lengkap...");
                    }

                    @Override
                    public void onDataApi(Data data) {
                      // Data lengkap diterima dari API
                      mData = data;
                      validateForm(noPelanggan);
                    }

                    @Override
                    public void onDataFound(WifiData data) {}

                    @Override
                    public void onDataSaved() {}

                    @Override
                    public void onError(String errorMessage) {
                      showExpandError("Gagal mengambil data lengkap: " + errorMessage);
                    }
                  });
            }
          }

          @Override
          public void onDataSaved() {}

          @Override
          public void onError(String errorMessage) {
            showExpandError("Gagal memeriksa data pengguna: " + errorMessage);
          }
        });
  }

  private void openDetailTagihan() {
    if (mData == null) {
      Log.e("MenuWifi", "Data API tidak tersedia. Tidak dapat membuka detail.");
      showExpandError("Data tidak ditemukan. Silakan coba lagi.");
      return;
    }

    // InquiryHelper.getInstance().setData(mData != null ? mData : mDataWifi);
    Intent intent = new Intent(MenuWifi.this, DetailTagihanInternet.class);
    Bundle bundle = new Bundle();
    bundle.putParcelable("data", mData); // Hanya kirim mData
    intent.putExtras(bundle);
    intent.putExtra("imageUrl", mImageUrl);
    startActivity(intent);
  }

  private void showExpandSuccess(String success) {
    AndroidViews.showExpandSuccess(success, binding.expandable, binding.txtCekPengguna);
  }

  private void showExpandError(String errorMessage) {
    AndroidViews.ShowExpandError(errorMessage, binding.expandable, binding.txtCekPengguna);
  }

  private void showBottomSheet() {
    binding.provider.setOnClickListener(
        v -> {
          BottomSheetWifi sheetWifi = new BottomSheetWifi();
          sheetWifi.setOnProdukItemSelected(
              (imgUrl, providerName) -> {
                updateProvider(imgUrl, providerName);
              });
          sheetWifi.show(getSupportFragmentManager(), "TAG");
        });
  }

  private void updateProvider(String imgUrl, String providerName) {
    binding.txtLabelProvider.setText(providerName);
    mImageUrl = imgUrl;
    selectedCodeProduk = providerName;
    if (binding.expandable.isExpanded()) {
      binding.expandable.collapse();
    }
    mData = null;
    mDataWifi = null;
    validateForm(noPelanggan);
    Glide.with(this)
        .load(imgUrl)
        .into(
            new SimpleTarget<Drawable>() {
              @Override
              public void onResourceReady(Drawable resouce, Transition<? super Drawable> arg1) {
                binding.logoProvider.setVisibility(View.VISIBLE);
                binding.logoProvider.setImageDrawable(resouce);
              }
            });
  }
}
