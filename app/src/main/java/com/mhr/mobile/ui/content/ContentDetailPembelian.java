package com.mhr.mobile.ui.content;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.databinding.ContentDetailPembelianBinding;
import com.mhr.mobile.loader.QiosFirebaseHelper;
import com.mhr.mobile.manage.request.TransaksiIAKRequest;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.manage.response.TransaksiIAKResponse;
import com.mhr.mobile.model.RiwayatTransaksi;
import com.mhr.mobile.ui.dialog.LoadingDialogFragment;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;

public class ContentDetailPembelian extends InjectionActivity {
  private ContentDetailPembelianBinding binding;
  private LoadingDialogFragment dialog;
  private PricelistResponse.Product data;

  @Override
  protected String getTitleToolbar() {
    return "Paket Data";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentDetailPembelianBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    dialog = new LoadingDialogFragment();
    applyPromo();
    terimaData();
    binding.btnBayar.setOnClickListener(this::startTransaksiIak);
  }

  private void terimaData() {
    data = getIntent().getParcelableExtra("pricelist");
	
	Glide.with(this).load(getIntent().getStringExtra("image")).into(binding.img);
	binding.tvHp.setText(getIntent().getStringExtra("nomor"));
    binding.tvProdukName.setText(data.getProductNominal());
    binding.tvProdukDetail.setText(data.getProductCode());
	binding.tvProdukPrice.setText(FormatUtils.formatRupiah(data.getHargaDiskon()));
  }

  private void startTransaksiIak(View v) {
    TransaksiIAKRequest request = new TransaksiIAKRequest(this);
    request.setUsername();
    request.setApikey();
    request.setCustomerId(getIntent().getStringExtra("nomor"));
    request.setCodeProduk(data.getProductCode());
    request.startTransaksiRequest(
        new TransaksiIAKRequest.RequestTransaksiCallback() {
          @Override
          public void onStartRequest() {
            dialog.show(getSupportFragmentManager(), "Tag");
          }

          @Override
          public void onResponse(TransaksiIAKResponse.Data response) {
            RiwayatTransaksi riwayat = new RiwayatTransaksi();
			riwayat.setTypeApi(data.getTypeApi());
			riwayat.setRefId(response.getRefId());
			riwayat.setTrId(response.getTrId());
			riwayat.setStatus(response.getStatus());
			riwayat.setMessage(response.getMessage());
			riwayat.setHarga(data.getHargaDiskon());
			riwayat.setTimestamp(response.getTimestamp());
			riwayat.setCustomerId(response.getCustomerId());
			riwayat.setProductCode(response.getProdukCode());
			riwayat.setBrand(getIntent().getStringExtra("brand"));
			riwayat.setImageUrl(getIntent().getStringExtra("image"));
			dialog.showSuccess(() -> detailTransaksi(riwayat));
			saveToFirebase(riwayat);
          }

          @Override
          public void onFailure(String errorMessage) {
            AndroidViews.showSnackbar(binding.getRoot(), errorMessage);
          }
        });
  }

  private void saveToFirebase(RiwayatTransaksi riwayat) {
    QiosFirebaseHelper firebaseHelper = new QiosFirebaseHelper("history_transaksi");
    firebaseHelper.saveTransaksi(riwayat);
  }

  private void detailTransaksi(RiwayatTransaksi riwayatTransaksi) {
    AndroidViews.showSnackbar(binding.getRoot(), riwayatTransaksi.getRefId());
    riwayatTransaksi.setIconResId(0);
    Bundle args = new Bundle();
    args.putParcelable("kirim", riwayatTransaksi);
    Intent intent = new Intent(this, ContentPaymentStatus.class);
    intent.putExtras(args);
    startActivity(intent);
  }

  private void applyPromo() {
    binding.editext.setOnTouchListener(
        (v, event) -> {
          if (event.getAction() == MotionEvent.ACTION_DOWN) {
            binding.editext.requestFocus();
            binding.btnUserPromo.setVisibility(View.VISIBLE);
          }

          return true;
        });
  }

  @Override
  public void onBackPressed() {
    // Jika Edittext sedang focus, sembunyikan tombol promo
    if (binding.editext.hasFocus()) {
      binding.btnUserPromo.setVisibility(View.GONE);
      binding.editext.clearFocus();
    } else {
      super.onBackPressed();
    }
  }
}
