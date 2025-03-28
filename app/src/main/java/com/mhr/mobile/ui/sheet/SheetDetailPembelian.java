package com.mhr.mobile.ui.sheet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.SheetDetailPembelianBinding;
import com.mhr.mobile.loader.QiosFirebaseHelper;
import com.mhr.mobile.manage.request.TransaksiDigiflazzRequest;
import com.mhr.mobile.manage.request.TransaksiIAKRequest;
import com.mhr.mobile.manage.response.PricelistDigiflazzResponse;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.manage.response.TransaksiDigiflazzResponse;
import com.mhr.mobile.manage.response.TransaksiIAKResponse.Data;
import com.mhr.mobile.model.RiwayatTransaksi;
import com.mhr.mobile.ui.content.ContentPaymentStatus;
import com.mhr.mobile.ui.dialog.LoadingDialogFragment;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;

public class SheetDetailPembelian extends InjectionSheetFragment {
  private SheetDetailPembelianBinding binding;
  private LoadingDialogFragment dialog;
  //
  private String nomor;
  private String customerName;
  private String produkCode;
  private String produkName;
  private double harga;
  private String typeApi;
  private String brand;
  private int iconResId;
  private String imageUrl;

  // Digiflazz
  public static SheetDetailPembelian newInstance(PricelistDigiflazzResponse.Data digiflazz) {
    SheetDetailPembelian fragment = new SheetDetailPembelian();
    fragment.setTypeApi(digiflazz.getTypeApi());
    fragment.setProdukCode(digiflazz.getBuyerSkuCode());
    fragment.setHarga(digiflazz.getHargaDiskon());
    return fragment;
  }
  // Mobile Pulsa
  public static SheetDetailPembelian newInstance(PricelistResponse.Product mobilePulsa) {
    SheetDetailPembelian fragment = new SheetDetailPembelian();
    fragment.setTypeApi(mobilePulsa.getTypeApi());
    fragment.setProdukCode(mobilePulsa.getProductCode());
    fragment.setHarga(mobilePulsa.getHargaDiskon());
    return fragment;
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetDetailPembelianBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    initialize();
    terimaData();
  }

  private void initialize() {
    dialog = new LoadingDialogFragment();
    binding.ubah.setOnClickListener(v -> dismiss());
  }

  private void terimaData() {
    binding.hp.setText(nomor);
    binding.harga.setText(FormatUtils.formatRupiah(harga));
    binding.totalPrice.setText(FormatUtils.formatRupiah(harga));
    if (typeApi != null) {
      if (typeApi.equalsIgnoreCase("apiIak")) {
        binding.btnPembayaran.setOnClickListener(this::startPembayaranIak);
        if (brand.equals("Token Listrik")) {
          binding.brand.setText(customerName);
          binding.tvTitleBrand.setText("Nama Pengguna");
        } else if (brand.equals(brand)) {
          binding.tvTitleBrand.setText("Paket");
          binding.brand.setText(produkName);
        }
      } else if (typeApi.equalsIgnoreCase("apiDigiflazz")) {
        binding.btnPembayaran.setOnClickListener(this::startPembayaranDigiflazz);
        if (customerName != null) {
          binding.tvTitleBrand.setText("Pemilik");
          binding.brand.setText(customerName);
        } else {
          binding.tvTitleBrand.setText("Produk");
          binding.brand.setText(produkName);
        }
      }
    }
  }

  private void startPembayaranIak(View v) {
    TransaksiIAKRequest iAKRequest = new TransaksiIAKRequest(requireActivity());
    iAKRequest.setUsername();
    iAKRequest.setApikey();
    iAKRequest.setCustomerId(nomor);
    iAKRequest.setCodeProduk(produkCode);
    iAKRequest.startTransaksiRequest(
        new TransaksiIAKRequest.RequestTransaksiCallback() {
          @Override
          public void onStartRequest() {
            dialog.show(getParentFragmentManager(), "loadingIak");
          }

          @Override
          public void onResponse(Data response) {
            RiwayatTransaksi riwayatTransaksi =
                new RiwayatTransaksi(
                    typeApi,
                    response.getRef_id(),
                    response.getTrId(),
                    response.getStatus(),
                    response.getMessage(),
                    response.getStatusText(),
                    harga,
                    response.getTimestamp(),
                    response.getCustomerId(),
                    response.getProdukCode(),
                    brand,
                    iconResId,
                    imageUrl);
            dialog.showSuccess(() -> detailTransaksi(riwayatTransaksi));
            saveToFirebase(riwayatTransaksi);
          }

          @Override
          public void onFailure(String errorMessage) {
            AndroidViews.showSnackbar(binding.getRoot(), errorMessage);
          }
        });
  }

  private void startPembayaranDigiflazz(View v) {
    TransaksiDigiflazzRequest request = new TransaksiDigiflazzRequest(requireActivity());
    request.setUsername();
    request.setApikey();
    request.setHp(nomor);
    request.setBuyerSkuCode(produkCode);
    request.setTotal((int) harga);
    request.startRequestTransaksi(
        new TransaksiDigiflazzRequest.RequestTransaksiCallback() {
          @Override
          public void onLoading() {
            dialog.show(getParentFragmentManager(), "loadingDigiflazz");
          }

          @Override
          public void onResponse(TransaksiDigiflazzResponse.Data response) {
            RiwayatTransaksi riwayatTransaksi =
                new RiwayatTransaksi(
                    typeApi,
                    response.getRef_id(),
                    0,
                    0,
                    response.getMessage(),
                    "PROSES",
                    harga,
                    response.getTimestamp(),
                    response.getCustomerId(),
                    response.getProdukCode(),
                    brand,
                    iconResId,
                    imageUrl);
            dialog.showSuccess(() -> detailTransaksi(riwayatTransaksi));
            saveToFirebase(riwayatTransaksi);
          }

          @Override
          public void onFailure(String error) {}
        });
  }

  private void detailTransaksi(RiwayatTransaksi riwayatTransaksi) {
    riwayatTransaksi.setIconResId(iconResId);
    Bundle args = new Bundle();
    args.putParcelable("kirim", riwayatTransaksi);
    Intent intent = new Intent(requireActivity(), ContentPaymentStatus.class);
    intent.putExtras(args);
    startActivity(intent);
  }

  private void saveToFirebase(RiwayatTransaksi riwayat) {
    QiosFirebaseHelper firebaseHelper = new QiosFirebaseHelper("history_transaksi");
    firebaseHelper.saveTransaksi(riwayat);
  }

  public void setNomor(String nomor) {
    this.nomor = nomor;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public void setProdukCode(String produkCode) {
    this.produkCode = produkCode;
  }

  public void setHarga(double harga) {
    this.harga = harga;
  }

  public void setTypeApi(String typeApi) {
    this.typeApi = typeApi;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setIcon(int iconResId) {
    this.iconResId = iconResId;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setProdukName(String produkName) {
    this.produkName = produkName;
  }
}
