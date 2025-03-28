package com.mhr.mobile.ui.menu;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.button.MaterialButton;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.PricelistDigiflazzAdapter;
import com.mhr.mobile.databinding.MenuPulsaBinding;
import com.mhr.mobile.loader.DigiflazzLoader;
import com.mhr.mobile.manage.response.PricelistDigiflazzResponse;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.sheet.BottomSheetDetailPembelian;
import com.mhr.mobile.ui.sheet.SheetDetailPembelian;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.ProviderUtils;
import com.mhr.mobile.util.QiosPreferences;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuPulsa extends InjectionActivity implements TextWatcher {
  public static final String ARG_PULSA = "pulsa";
  private MenuPulsaBinding binding;
  private PricelistDigiflazzAdapter adapter;
  private List<PricelistDigiflazzResponse.Data> mData = new ArrayList<>();
  private PricelistDigiflazzResponse.Data pilihProduk;
  private Handler handler = new Handler();
  private View bottomPay;
  private QiosPreferences preferences;
  private boolean isKeyboardVisible = false;
  private DigiflazzLoader digiflazzLoader;

  @Override
  protected String getTitleToolbar() {
    return "Isi Pulsa";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuPulsaBinding.inflate(layoutInflater, viewGroup, false);
    initUi();
    setupIncludePembayaran(binding.getRoot().findViewById(R.id.pay));
    return binding.getRoot();
  }

  @Override
  protected void setupIncludePembayaran(View includeView) {
    if (includeView == null) return;
    bottomPay = includeView;
    MaterialButton btn = includeView.findViewById(R.id.btn_lanjut_bayar);
    TextView harga = includeView.findViewById(R.id.harga);
    adapter.setOnDataClickListener(
        data -> {
          pilihProduk = data;
          String validasi = binding.etPhoneNumber.getText().toString().trim();
          if (validasi.isEmpty()) {
            showExpandError("Nomor belum di isi");
          } else if (validasi.length() < 10) {
            showExpandError("Pastikan nomor hp benar");
          } else {
            harga.setText(FormatUtils.formatRupiah(pilihProduk.getHargaDiskon()));
            detectProvider(validasi);
            bottomPay.setVisibility(View.VISIBLE);
          }
        });

    btn.setOnClickListener(
        v -> {
          String nomor = binding.etPhoneNumber.getText().toString();
		  String providerId = ProviderUtils.detectProvider(binding.etPhoneNumber.getText().toString());
		  int iconResId = ProviderUtils.getProviderIconResId(providerId);
		  SheetDetailPembelian sheet = SheetDetailPembelian.newInstance(pilihProduk);
		  sheet.setNomor(nomor);
		  sheet.setBrand(pilihProduk.getBrand());
		  sheet.setProdukCode(pilihProduk.getBuyerSkuCode());
		  sheet.setHarga(pilihProduk.getHargaDiskon());
		  sheet.setTypeApi(pilihProduk.getTypeApi());
		  sheet.setIcon(iconResId);
		  sheet.setProdukName(pilihProduk.getProductName());
		  sheet.show(getSupportFragmentManager());
        });
  }
 
  private void initUi() {
    preferences = new QiosPreferences(this);
    digiflazzLoader = new DigiflazzLoader(this);
    // Glide.with(this).load(Config.NO_IMAGE).into(binding.imgNoProduk);
    binding.recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(2, 50, true));
    adapter = new PricelistDigiflazzAdapter(mData);
    binding.recyclerview.setAdapter(adapter);
    binding.textInputLayoutPhoneNumber.setEndIconDrawable(R.drawable.pick_contact);
    binding.textInputLayoutPhoneNumber.setEndIconOnClickListener(
        v -> {
          launchContactPicker();
          // startActivity(new Intent(MenuPulsa.this, ContentMenuKontak.class));
        });
    binding.etPhoneNumber.addTextChangedListener(this);
  }

  @Override
  protected void onPhoneNumberPicked(String phoneNumber) {
    binding.etPhoneNumber.setText(phoneNumber);
    detectProvider(phoneNumber);
    showContentVisibility(true);
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    String nomorCustomer = s.toString().trim();
    // preferences.saveNomerTerakhirInput(nomorCustomer);
    if (nomorCustomer.isEmpty()) {
      hidePesanError();
      adapter.setInputValid(false);
      adapter.resetSelectedPosition();
    } else if (nomorCustomer.matches("^0[0-9]{3,}$")) {
      adapter.resetSelectedPosition();
      detectProvider(nomorCustomer);
    } else {
      ProviderUtils.updateProviderIcon(binding.logoProvider, "Unknown");
      showContentVisibility(false);
    }
  }

  private void detectProvider(String nomor) {
    adapter.setInputValid(true);
    String provider = ProviderUtils.detectProvider(nomor);
    ProviderUtils.updateProviderIcon(binding.logoProvider, provider);
    // dapatkan resource id ikon yg di gunakan
    int providerIconResId = ProviderUtils.getProviderIconResId(provider);
    if (provider.equals("Unknown")) {
      showExpandError("Nomor pelanggan tidak dikenali");
      showContentVisibility(false);
    } else {
      hidePesanError();
      // Filter data berdasarkan provider
      if (!mData.isEmpty()) {
        List<PricelistDigiflazzResponse.Data> filterProduk = getFilterProduk(provider);
        perbaruiRecyclerview(filterProduk);
      } else {
        // Jika data belum diunduh, muat data dahulu
        loadPricelist(provider);
      }
    }
  }

  private void perbaruiRecyclerview(List<PricelistDigiflazzResponse.Data> dataList) {
    if (dataList.isEmpty()) {
      showContentVisibility(false);
    } else {
      showContentVisibility(!dataList.isEmpty());
      adapter.perbaruiData(dataList);
    }
  }

  private List<PricelistDigiflazzResponse.Data> getFilterProduk(String provider) {
    return mData.stream()
        .filter(product -> product.getBrand().equalsIgnoreCase(provider))
        .collect(Collectors.toList());
  }

  private void showContentVisibility(boolean showVisibility) {
    if (showVisibility) {
      binding.recyclerview.hideShimmerAdapter();
      binding.containerNoProduk.setVisibility(View.GONE);
      binding.recyclerview.setVisibility(View.VISIBLE);
    } else {
      binding.recyclerview.showShimmerAdapter();
      binding.containerNoProduk.setVisibility(View.VISIBLE);
      binding.recyclerview.setVisibility(View.GONE);
    }
  }

  private void showExpandError(String message) {
    AndroidViews.ShowExpandError(message, binding.expandable, binding.txtPesanKePengguna);
  }

  private void hidePesanError() {
    bottomPay.setVisibility(View.GONE);
    binding.txtPesanKePengguna.setText("");
    if (binding.expandable.isExpanded()) {
      binding.expandable.collapse();
    }
  }

  private void loadPricelist(String provider) {
    if (!mData.isEmpty()) {
	      // Filter produk berdasarkan provider jika data sudah ada
      List<PricelistDigiflazzResponse.Data> filteredData = getFilterProduk(provider);
      perbaruiRecyclerview(filteredData);
      return;
    }

    digiflazzLoader.getDataFromFirebase(
        "Pulsa",
        new DigiflazzLoader.LoaderExecute() {
          @Override
          public void onSuccess(List<PricelistDigiflazzResponse.Data> dataList) {
            mData = dataList;
            // Filter berdasarkan provider yang terdeteksi
            List<PricelistDigiflazzResponse.Data> filteredByProvider = getFilterProduk(provider);
            perbaruiRecyclerview(filteredByProvider);
          }

          @Override
          public void onError(String errorMessage) {
            showExpandError(errorMessage);
          }
        });
  }

  private void loadLastPhoneNumber() {
    String lastPhoneNumber = preferences.getNomerTerakhirInput();
    if (!lastPhoneNumber.isEmpty()) {
      binding.etPhoneNumber.setText(lastPhoneNumber);
      detectProvider(lastPhoneNumber);
    }
  }
}
