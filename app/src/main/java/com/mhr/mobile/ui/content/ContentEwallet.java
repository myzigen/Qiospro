package com.mhr.mobile.ui.content;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.PricelistDigiflazzAdapter;
import com.mhr.mobile.databinding.ContentEwalletBinding;
import com.mhr.mobile.inquiry.helper.EwalletHelper;
import com.mhr.mobile.inquiry.response.InquiryResponse;
import com.mhr.mobile.loader.DigiflazzLoader;
import com.mhr.mobile.manage.response.PricelistDigiflazzResponse;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.sheet.SheetDetailPembelian;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.ProviderUtils;
import java.util.ArrayList;
import java.util.List;

public class ContentEwallet extends InjectionActivity implements TextWatcher {
  private ContentEwalletBinding binding;
  private PricelistDigiflazzAdapter adapter;
  private EwalletHelper ewalletHelper;
  private View bottomPay;
  private boolean isNomorChecked = false;
  private DigiflazzLoader digiLoader;
  private PricelistDigiflazzResponse.Data mData;
  private String trName;

  @Override
  protected String getTitleToolbar() {
    return "Top Up";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentEwalletBinding.inflate(layoutInflater, viewGroup, false);
    terimaData();
    initUi();
    setupIncludePembayaran(binding.getRoot().findViewById(R.id.pay));
    return binding.getRoot();
  }

  @Override
  protected void setupIncludePembayaran(View includeView) {
    if (includeView == null) return;
    TextView priceText = includeView.findViewById(R.id.harga);
    MaterialButton btn = includeView.findViewById(R.id.btn_lanjut_bayar);
    bottomPay = includeView;
    if (priceText == null) return;
    adapter.setOnDataClickListener(
        price -> {
          mData = price;
          String nomorPelanggan = binding.editText.getText().toString();
          if (nomorPelanggan.isEmpty()) {
            showExpandError("Nomor Hp Belum Di Isi");
          } else if (nomorPelanggan.length() < 10) {
            showExpandError("Pastikan No Hp Benar");
          } else {
            if (!isNomorChecked) {
              detectProvider(nomorPelanggan);
              checkCustomerName(nomorPelanggan, price.getHargaJual());
              isNomorChecked = true; // cegah klik 2x
            }

            priceText.setText(FormatUtils.formatRupiah(price.getHargaDiskon()));
            includeView.setVisibility(View.VISIBLE);
          }
        });

    btn.setOnClickListener(
        v -> {
          String nomor = binding.editText.getText().toString();
          SheetDetailPembelian sheet = SheetDetailPembelian.newInstance(mData);
          sheet.setNomor(nomor);
          sheet.setCustomerName(trName);
          sheet.setBrand(getIntent().getStringExtra("brand"));
          sheet.setImageUrl(getIntent().getStringExtra("imageUrl"));
          sheet.show(getSupportFragmentManager());
        });
  }

  private void initUi() {
    digiLoader = new DigiflazzLoader(this);
    ewalletHelper = new EwalletHelper(this);
    binding.ubah.setOnClickListener(v -> onBackPressed());
    binding.editText.addTextChangedListener(this);
    binding.txtInputLayout.setEndIconDrawable(R.drawable.pick_contact);
    binding.txtInputLayout.setEndIconOnClickListener(v -> launchContactPicker());
    binding.recyclerview.addItemDecoration(getSpacingItemDecoration(2, 50, true));
    binding.recyclerview.setLayoutManager(getGridLayoutManager(2));

    adapter = new PricelistDigiflazzAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    loadProdukEwallet();
  }

  private void loadProdukEwallet() {
    digiLoader.getDataFromFirebase(
        "E-Money",
        new DigiflazzLoader.LoaderExecute() {
          @Override
          public void onSuccess(List<PricelistDigiflazzResponse.Data> dataList) {
            List<PricelistDigiflazzResponse.Data> filteredData = new ArrayList<>();
            for (PricelistDigiflazzResponse.Data data : dataList) {
              if (data.getBrand().equalsIgnoreCase(getIntent().getStringExtra("brand"))) {
                filteredData.add(data);
              }
            }

            adapter.perbaruiData(filteredData);
          }

          @Override
          public void onError(String errorMessage) {}
        });
  }

  private void checkCustomerName(String nomorPelanggan, int price) {
    String code = getIntent().getStringExtra("code");

    ewalletHelper.checkCustomerName(
        nomorPelanggan,
        code,
        price,
        new EwalletHelper.EwalletCallback() {
          @Override
          public void onLoading() {
            binding.progress.setVisibility(View.VISIBLE);
            binding.progress.setIndeterminate(true);
            showExpandSuccess("Memeriksa Nomor E-Wallet...");
          }

          @Override
          public void onDataFound(InquiryResponse.EwalletData data) {
            binding.progress.setVisibility(View.GONE);
            binding.progress.setIndeterminate(false);
            updateCustomerNameUI(data);
            // isNomorChecked = false;
          }

          @Override
          public void onDataSaved() {
            // Handle jika data berhasil disimpan
            // isNomorChecked = false;
          }

          @Override
          public void onError(String errorMessage) {
            showExpandError(errorMessage);
            binding.progress.setVisibility(View.GONE);
            binding.progress.setIndeterminate(false);
            // isNomorChecked = false;
          }
        });
  }

  private void updateCustomerNameUI(InquiryResponse.EwalletData simpleData) {
    if (simpleData != null) {
      adapter.setInputValid(true);
      showExpandSuccess(simpleData.getTrName());
      trName = simpleData.getTrName();
      binding.expandableCekUser.setExpanded(true);
    } else {
      binding.cekNamaUser.setText("Nama pengguna tidak tersedia.");
    }
  }

  private void showExpandError(String error) {
    bottomPay.setVisibility(View.GONE);
    adapter.setInputValid(false);
    adapter.resetSelectedPosition();
    AndroidViews.ShowExpandError(error, binding.expandableCekUser, binding.cekNamaUser);
  }

  private void showExpandSuccess(String success) {
    AndroidViews.showExpandSuccess(success, binding.expandableCekUser, binding.cekNamaUser);
  }

  @Override
  protected void onPhoneNumberPicked(String phoneNumber) {
    binding.editText.setText(phoneNumber);
    detectProvider(phoneNumber);
    isNomorChecked = false;
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    String nomorPelanggan = s.toString().trim();
    if (nomorPelanggan.isEmpty()) {
      hideExpand();
    } else {
      hideExpand();
      detectProvider(nomorPelanggan);
      isNomorChecked = false;
    }
  }

  private void detectProvider(String nomorPelanggan) {
    if (nomorPelanggan.length() < 4) {
      return;
    }
    String provider = ProviderUtils.detectProvider(nomorPelanggan);
    if (provider.equals("Unknown")) {
      showExpandError("Provider tidak dikenal");
    }
  }

  private void hideExpand() {
    bottomPay.setVisibility(View.GONE);
    adapter.resetSelectedPosition();
    if (binding.expandableCekUser.isExpanded()) {
      binding.expandableCekUser.collapse();
    }
  }

  private void terimaData() {
    Intent intent = getIntent();
    String logoProvider = intent.getStringExtra("imageUrl");
    String provider = intent.getStringExtra("brand");
    Glide.with(this).load(logoProvider).into(binding.logoProvider);
    binding.brand.setText(provider);
    if (provider.equalsIgnoreCase("Dana")) {
      binding.containerBtnBebasNominal.setVisibility(View.VISIBLE);
    }
  }
}
