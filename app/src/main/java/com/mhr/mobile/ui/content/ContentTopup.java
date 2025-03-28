package com.mhr.mobile.ui.content;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.bumptech.glide.Glide;
import com.mhr.mobile.adapter.TopupAdapter;
import com.mhr.mobile.databinding.ContentTopupBinding;
import com.mhr.mobile.loader.QiosFirebaseHelper;
import com.mhr.mobile.manage.request.MidtransRequest;
import com.mhr.mobile.manage.response.MidtransResponse;
import com.mhr.mobile.model.RiwayatTransaksi;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.sheet.SheetBankList;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import com.mhr.mobile.util.QiosPreferences;
import java.util.Arrays;
import java.util.List;
import okhttp3.*;

public class ContentTopup extends InjectionActivity implements TextWatcher {
  private ContentTopupBinding binding;
  private TopupAdapter adapter;
  private String pilihBank, imageUrl;
  private QiosPreferences preferences;
  private long expired = 60;

  @Override
  protected String getTitleToolbar() {
    return "Top Up Saldo";
  }

  @Override
  public View onCreateQiosView(LayoutInflater i, ViewGroup c, Bundle b) {
    binding = ContentTopupBinding.inflate(getLayoutInflater());
    initialize();
    initUi();
    return binding.getRoot();
  }

  private void initialize() {
    preferences = new QiosPreferences(this);
  }

  private void initUi() {
    applyEditext();
    applyRecycler();

    binding.btnPilihBank.setOnClickListener(
        v -> {
          SheetBankList sheet = new SheetBankList();
          sheet.setOnSheetClickListener(
              (logo, codeProduk, namaBank, norek, type) -> {
                hideError();
                imageUrl = logo;
                pilihBank = codeProduk;
                binding.btnPilihBank.setStrokeColor(QiosColor.getActiveColor(this));
                binding.tvNamaBank.setText(namaBank);
                binding.type.setText(type);
                Glide.with(this).load(imageUrl).into(binding.logo);
              });

          sheet.show(getSupportFragmentManager());
        });

    binding.btnPembayaran.setOnClickListener(this::makePayment);
  }

  private void applyEditext() {
    binding.editext.addTextChangedListener(this);
    // binding.editext.setTextAppearance(android.R.style.TextAppearance_Large);
    binding.editext.setShowSoftInputOnFocus(false);
    InputConnection ic = binding.editext.onCreateInputConnection(new EditorInfo());
    binding.keyboard.setInputConnection(ic);
    binding.keyboard.setTargetEditText(binding.editext);
  }

  private void applyRecycler() {
    List<Integer> jumlahTopup = Arrays.asList(10000, 20000, 50000, 100000, 200000, 500000);
    binding.recyclerview.addItemDecoration(getSpacingItemDecoration(3, 50, true));
    binding.recyclerview.setLayoutManager(getGridLayoutManager(3));
    adapter = new TopupAdapter(jumlahTopup);
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnClickListener(
        jumlah -> {
          hideError();
          adapter.setInputValid(true);
          binding.editext.setText(String.valueOf(jumlah));
        });
  }

  private void showError(String error) {
    AndroidViews.ShowExpandError(error, binding.include.expandable, binding.include.cekUser);
  }

  private void hideError() {
    if (binding.include.expandable.isExpanded()) {
      binding.include.expandable.collapse();
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    String input = s.toString();
    if (input.isEmpty()) {
      adapter.setInputValid(false); // Set ke false agar warna kembali ke default
      adapter.resetSelectedPosition();
    } else {
      adapter.setInputValid(true);
    }
  }

  private void makePayment(View v) {
    String inputTopup = binding.editext.getText().toString();
    if (inputTopup.isEmpty()) {
      showError("Masukan Jumlah Topup");
      return;
    }

    if (pilihBank == null || pilihBank.isEmpty()) {
      showError("Silahkan Pilih Pembayaran Terlebih Dahulu");
      binding.btnPilihBank.setStrokeColor(QiosColor.getErrorColor(this));
      return;
    }

    int jumlahTopup = FormatUtils.parseRupiahToInt(inputTopup);
    int kodeUnik = (int) (Math.random() * 300) + 100;
    int jumlahTopupWithKodeUnik = jumlahTopup + kodeUnik;

    String orderId = "INV-" + System.currentTimeMillis();
    MidtransRequest request = new MidtransRequest(this);
    request.setOrderId(orderId);
    request.setGrossAmount(jumlahTopupWithKodeUnik);
    request.setPaymentType("bank_transfer");
    request.setBankName(pilihBank);
    request.setExpiryDuration(expired, "minute");
    request.startRequestTransaction(
        new MidtransRequest.MidtransRequestCallback() {
          @Override
          public void onResponse(MidtransResponse response) {
            RiwayatTransaksi transaksi = new RiwayatTransaksi();
            if (pilihBank.equalsIgnoreCase("bca")) {
              for (MidtransResponse.VirtualNumber va : response.getVirtualNumbers()) {
                String bcaVa = va.getVirtualNumber();
                transaksi.setCustomerId(bcaVa);
              }
            } else if (pilihBank.equalsIgnoreCase("bni")) {
              for (MidtransResponse.VirtualNumber va : response.getVirtualNumbers()) {
                String bcaVa = va.getVirtualNumber();
                transaksi.setCustomerId(bcaVa);
              }
            } else if (pilihBank.equalsIgnoreCase("bri")) {
              for (MidtransResponse.VirtualNumber va : response.getVirtualNumbers()) {
                String bcaVa = va.getVirtualNumber();
                transaksi.setCustomerId(bcaVa);
              }
            } else if (pilihBank.equalsIgnoreCase("mandiri")) {
              for (MidtransResponse.VirtualNumber va : response.getVirtualNumbers()) {
                String bcaVa = va.getVirtualNumber();
                transaksi.setCustomerId(bcaVa);
              }
            } else if (pilihBank.equalsIgnoreCase("permata")) {
              String permataVa = response.getPermataVaNumber();
              transaksi.setCustomerId(permataVa);
            } else if (pilihBank.equalsIgnoreCase("qris")) {

            }
            preferences.saveCountdownTimer(expired, response.getOrderId());
            transaksi.setExpiryDuration(expired);
            transaksi.setExpiryTime(response.getExpiryTime());
            transaksi.setTransactionTime(response.getTransactionTime());
            transaksi.setTypeApi(response.getTypeApi());
            transaksi.setRefId(response.getOrderId());
            transaksi.setStatusTransaksiMidtrans(response.getTransactionStatus());
            transaksi.setBrand("Isi Saldo");
            transaksi.setMerchantName(pilihBank);
            transaksi.setHarga((int) jumlahTopupWithKodeUnik);
            transaksi.setImageUrl(imageUrl);
            detailTopup(transaksi);
            saveToFirebase(transaksi);
          }

          @Override
          public void onFailure(String error) {
            showError(error);
          }
        });
  }

  private void saveToFirebase(RiwayatTransaksi riwayatTransaksi) {
    QiosFirebaseHelper firebaseHelper = new QiosFirebaseHelper("history_transaksi");
    firebaseHelper.saveTransaksi(riwayatTransaksi);
  }

  private void detailTopup(RiwayatTransaksi riwayatTransaksi) {
    Bundle args = new Bundle();
    args.putParcelable("riwayat_topup", riwayatTransaksi);
    Intent intent = new Intent(this, ContentTopupStatus.class);
    intent.putExtras(args);
    startActivity(intent);
  }
}
