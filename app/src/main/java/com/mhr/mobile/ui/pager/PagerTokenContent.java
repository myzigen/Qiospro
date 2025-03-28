package com.mhr.mobile.ui.pager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.button.MaterialButton;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.ListMobileAdapter;
import com.mhr.mobile.databinding.PagerTokenContentBinding;
import com.mhr.mobile.loader.MobilePulsaLoader;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.menu.MenuPln;
import com.mhr.mobile.ui.sheet.SheetDetailPembelian;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import java.util.ArrayList;
import java.util.List;

public class PagerTokenContent extends InjectionFragment {
  private PagerTokenContentBinding binding;
  private ListMobileAdapter adapter;
  private MenuPln menuPln;
  private Context context;
  private MobilePulsaLoader mobilePulsaLoader;
  private String noMeter;
  private String trName;
  private PricelistResponse.Product mModel;
  private View bottomPay;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.context = context;
  }

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = PagerTokenContentBinding.inflate(getLayoutInflater());
    initUi();
    loadDataPln();
    return binding.getRoot();
  }

  private void initUi() {
    mobilePulsaLoader = new MobilePulsaLoader(requireActivity());
    menuPln = (MenuPln) context;
    binding.recyclerview.addItemDecoration(
        getSpacingDecoration(2, AndroidViews.dpToPx(18, getActivity()), true));
    binding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    adapter = new ListMobileAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    applyIncludePembayaran(binding.getRoot().findViewById(R.id.pay));
  }

  @Override
  protected void applyIncludePembayaran(View includeView) {
    if (includeView == null) return;
    bottomPay = includeView;
    menuPln.setBottomPay(bottomPay); // Berikan referensi ke MenuPln
    TextView harga = includeView.findViewById(R.id.harga);
    MaterialButton button = includeView.findViewById(R.id.btn_lanjut_bayar);
    adapter.setOnDataClickListener(
        model -> {
          mModel = model;
          // Validasi EditText kosong atau data tidak valid
          if (menuPln != null && menuPln.isEditTextEmpty()) {
            menuPln.showExpandError("Masukan No Pelanggan");
            adapter.setInputValid(false);
            adapter.resetSelectedPosition();
            includeView.setVisibility(View.GONE);
          } else if (menuPln.sendData().length() < 11) {
            menuPln.showExpandError("No Meter Salah");
            adapter.setInputValid(false);
            adapter.resetSelectedPosition();
            includeView.setVisibility(View.GONE);
          } else {
            includeView.setVisibility(View.VISIBLE);
            // Jika nomor belum dicek, periksa nama pelanggan
            if (!menuPln.isNomorChecked) {
              adapter.setInputValid(true);
              checkCustomerName();
              menuPln.isNomorChecked = true;
            }

            harga.setText(FormatUtils.formatRupiah(model.getHargaDiskon()));
            button.setOnClickListener(
                v -> {
                  int iconResId = R.drawable.provider_pln;
                  SheetDetailPembelian sheet = SheetDetailPembelian.newInstance(model);
                  sheet.setIcon(iconResId);
                  sheet.setBrand("Token Listrik");
                  sheet.setNomor(noMeter);
                  sheet.setCustomerName(trName);
                  sheet.show(getParentFragmentManager());
                });
          }
        });
  }

  private void checkCustomerName() {
    noMeter = menuPln.sendData();
    menuPln.checkCustomerName(
        noMeter,
        new MenuPln.InquirySendData() {
          @Override
          public void onSendData(String data) {
            trName = data;
          }
        });
  }

  private void loadDataPln() {
    mobilePulsaLoader.getDataFromFirebase(
        "pln",
        "pln",
        new MobilePulsaLoader.LoaderExecute() {
          @Override
          public void onStartLoading() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onSucces(List<PricelistResponse.Product> produk) {
            binding.recyclerview.hideShimmerAdapter();
            adapter.updateData(produk);
          }

          @Override
          public void onFailure(String error) {}
        });
  }
}
