package com.mhr.mobile.ui.sheet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.FrameLayout;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.BottomSheetDetailContentDataBinding;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.ui.content.ContentDetailPembelian;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.ProviderUtils;
import com.mhr.mobile.util.WindowPreferencesManager;

public class SheetContentData extends BottomSheetDialogFragment implements TextWatcher {
  private BottomSheetDetailContentDataBinding binding;
  private String imageUrl, brand, typeApi, produkCode, produkName, produkDetail;
  private double harga;
  private PricelistResponse.Product data;

  public static SheetContentData newInstance(PricelistResponse.Product data) {
    SheetContentData fragment = new SheetContentData();
    Bundle args = new Bundle();
    args.putParcelable("data", data);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    BottomSheetDialog dialog =
        new BottomSheetDialog(requireContext(), R.style.FullscreenBottomSheetDialogFragment);
    new WindowPreferencesManager(requireContext()).applyEdgeToEdgePreference(dialog.getWindow());

    // Inflasi View Binding
    binding = BottomSheetDetailContentDataBinding.inflate(getLayoutInflater());
    View bottomSheetContent = binding.getRoot();
    bottomSheetContent.setBackgroundResource(R.drawable.shape_corners_top);
    dialog.setContentView(bottomSheetContent); // Set binding sebagai isi dialog

    terimaData();
    initUi();

    // Mengambil referensi dari BottomSheet
    FrameLayout bottomSheet =
        dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
    if (bottomSheet != null) {
      BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
      behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      behavior.setSkipCollapsed(true);
      behavior.setDraggable(true);
    }

    // Penanganan Window Insets
    AndroidViews.applyInsets(bottomSheetContent);

    return dialog;
  }

  private void initUi() {
    binding.btnAktivasi.setText("Aktifkan");
    binding.editText.addTextChangedListener(this);
    binding.editText.setShowSoftInputOnFocus(false);
    InputConnection ic = binding.editText.onCreateInputConnection(new EditorInfo());
    binding.keyboard.setInputConnection(ic);
    binding.keyboard.setTargetEditText(binding.editText);
    binding.btnAktivasi.setOnClickListener(this::lanjutTransaksi);
  }

  private void terimaData() {
    data = getArguments().getParcelable("data");
	
    Glide.with(requireActivity()).load(imageUrl).into(binding.logoProvider);
    binding.produkName.setText(produkCode);
    binding.textDetailProduk.setText(Html.fromHtml(produkDetail, Html.FROM_HTML_MODE_COMPACT));
    binding.price.setText(FormatUtils.formatRupiah(harga));
  }

  private void lanjutTransaksi(View v) {
    binding.expandable.expand();
    binding.btnAktivasi.setText("Lanjutkan Transaksi");
    String validasi = binding.editText.getText().toString();
    Intent intent = null;

    if (validasi.isEmpty()) {
      binding.textInput.setError("Nomor Tujuan Belum Di Isi");
    } else if (validasi.length() >= 10) {
      intent = new Intent(requireActivity(), ContentDetailPembelian.class);
      Bundle args = new Bundle();
      args.putParcelable("pricelist", data);
      intent.putExtras(args);
      intent.putExtra("nomor", validasi);
	  intent.putExtra("brand", brand);
	  intent.putExtra("image", imageUrl);
      if (intent != null) startActivity(intent);
    }
    validateNumber(validasi);
  }

  private void detectProvider(String nomor) {
    if (nomor.length() < 4) return;
    String provider = ProviderUtils.detectProvider(nomor);
    if (provider.equals("Unknown")) {
      binding.textInput.setError("Nomor tidak dikenal");
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    String validasiNomor = s.toString().trim();
    validateNumber(validasiNomor);
  }

  private void validateNumber(String validasi) {
    if (validasi.isEmpty()) {
      binding.textInput.setError(null);
      binding.btnAktivasi.setEnabled(false);
    } else if (validasi.length() < 10) {
      binding.textInput.setError(null);
      binding.btnAktivasi.setEnabled(false);
    } else {
      binding.textInput.setError(null);
      binding.btnAktivasi.setEnabled(true);
      detectProvider(validasi);
    }
  }

  /**
   * @Params
   *
   * @params
   */
  public void setHarga(double harga) {
    this.harga = harga;
  }

  public void setTypeApi(String typeApi) {
    this.typeApi = typeApi;
  }

  public void setProdukCode(String produkCode) {
    this.produkCode = produkCode;
  }

  public void setProdukName(String produkName) {
    this.produkName = produkName;
  }

  public void setProdukDetail(String produkDetail) {
    this.produkDetail = produkDetail;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }
}
