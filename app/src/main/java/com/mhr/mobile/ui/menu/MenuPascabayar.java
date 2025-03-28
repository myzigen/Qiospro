package com.mhr.mobile.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.MenuPascabayarBinding;
import com.mhr.mobile.inquiry.request.InquiryRequest;
import com.mhr.mobile.inquiry.response.InquiryResponse;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.sheet.SheetPascabayar;
import com.mhr.mobile.util.AndroidViews;

public class MenuPascabayar extends InjectionActivity {
  private MenuPascabayarBinding binding;
  private String category;
  private SheetPascabayar sheetPascabayar;
  private String codeProduk;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuPascabayarBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    sheetPascabayar = new SheetPascabayar();
    category = getIntent().getStringExtra("category");
    // binding.btnCekTagihan.setEnabled(false);
    binding.btnCekTagihan.setText("Cek Pengguna");
    binding.btnCekTagihan.setOnClickListener(this::startCheckUser);

    if ("bpjs".equalsIgnoreCase(category)) {
      binding.provider.setOnClickListener(v -> sheetPascabayar("bpjs"));
    }

    if ("gas".equalsIgnoreCase(category)) {
      binding.provider.setOnClickListener(v -> sheetPascabayar("gas"));
    }

    if ("tv".equalsIgnoreCase(category)) {
      binding.provider.setOnClickListener(v -> sheetPascabayar("tv"));
    }

    if ("pbb".equalsIgnoreCase(category)) {
      binding.provider.setOnClickListener(v -> sheetPascabayar("pbb"));
    }

    if ("finance".equalsIgnoreCase(category)) {
      binding.provider.setOnClickListener(v -> sheetPascabayar("finance"));
    }
  }

  private void sheetPascabayar(String type) {
    sheetPascabayar.setCategory(type);
    sheetPascabayar.setOnSelectProviderListener(
        (provider) -> {
          binding.txtLabelProvider.setText(provider);
          codeProduk = provider;
          binding.btnCekTagihan.setText(codeProduk);
        });
    sheetPascabayar.show(getSupportFragmentManager(), "TAG");
  }

  private void startCheckUser(View v) {
    String nomor = binding.editText.getText().toString();
    if (nomor.isEmpty()) {
      // binding.btnCekTagihan.setEnabled(false);
      showExpandError("Nomor Pelanggan Belum Di Isi");
    } else {
      binding.btnCekTagihan.setEnabled(true);
      checkUser(nomor);
    }
  }

  private void checkUser(String nomor) {
    InquiryRequest request = new InquiryRequest(this);
    request.setUsername();
    request.setApiKey();
    request.setHp(nomor);
    request.setCodeProduk(codeProduk);
    request.startInquiryRequest(
        new InquiryRequest.InquiryCallback() {
          @Override
          public void onStartLoading() {
            showExpandSuccess("Memeriksa Nama Pemilik...");
          }

          @Override
          public void onResponse(InquiryResponse response) {
            InquiryResponse.Data data = response.getData();
            if (data != null && data.getTrName() != null && data.getTrName().isEmpty()) {
              showExpandSuccess(data.getTrName());
            } else {
              showExpandError(data.getMessage());
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            showExpandError(errorMessage);
          }
        });
  }

  private void showExpandSuccess(String message) {
    AndroidViews.showExpandSuccess(message, binding.expandable, binding.txtCekPengguna);
  }

  private void showExpandError(String errorMessage) {
    AndroidViews.ShowExpandError(errorMessage, binding.expandable, binding.txtCekPengguna);
  }
}
