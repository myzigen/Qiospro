package com.mhr.mobile.ui.sheet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.SheetDetailPembelianBinding;
import com.mhr.mobile.manage.request.TransaksiIAKRequest;
import com.mhr.mobile.manage.response.TransaksiIAKResponse.Data;
import com.mhr.mobile.ui.content.ContentPaymentStatusIAK;
import com.mhr.mobile.ui.dialog.LoadingDialogFragment;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;

public class SheetPembelianMobile extends InjectionSheetFragment {
  private SheetDetailPembelianBinding binding;
  private LoadingDialogFragment dialog;
  private String typeApi;
  private String produkCode;
  private double harga;

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetDetailPembelianBinding.inflate(getLayoutInflater());
    terimaData();

    dialog = new LoadingDialogFragment();
    return binding.getRoot();
  }

  private void terimaData() {
    if (typeApi != null) {
      if (typeApi.equalsIgnoreCase("apiIak")) {
        binding.tvTitleBrand.setText(produkCode);
        binding.brand.setText(typeApi);
        binding.totalPrice.setText(FormatUtils.formatRupiah(harga));
        binding.btnPembayaran.setOnClickListener(this::startPembayaran);
      }
    }
  }

  private void startPembayaran(View v) {
    TransaksiIAKRequest request = new TransaksiIAKRequest(requireActivity());
    request.setApikey();
    request.setUsername();
	request.setCustomerId("083168612666");
    request.setCodeProduk(produkCode);
    request.startTransaksiRequest(
        new TransaksiIAKRequest.RequestTransaksiCallback() {
          @Override
          public void onStartRequest() {
            dialog.show(getParentFragmentManager(), "TAG");
          }

          @Override
          public void onResponse(Data response) {
            dialog.dismiss();
            showDetailTransaksi();
          }

          @Override
          public void onFailure(String errorMessage) {
            dialog.dismiss();
            AndroidViews.showSnackbar(binding.getRoot(), errorMessage);
          }
        });
  }

  private void showDetailTransaksi() {
    Intent intent = new Intent(requireActivity(), ContentPaymentStatusIAK.class);
    startActivity(intent);
  }

  public void setTypeApi(String typeApi) {
    this.typeApi = typeApi;
  }

  public void setProdukCode(String produkCode) {
    this.produkCode = produkCode;
  }

  public void setHarga(double harga) {
    this.harga = harga;
  }
}
