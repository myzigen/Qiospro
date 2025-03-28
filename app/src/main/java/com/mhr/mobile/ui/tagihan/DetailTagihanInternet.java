package com.mhr.mobile.ui.tagihan;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.mhr.mobile.databinding.DetailTagihanInternetBinding;
import com.mhr.mobile.inquiry.helper.InquiryHelper;
import com.mhr.mobile.inquiry.response.InquiryResponse;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.menu.MenuWifi;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.viewmodel.InquiryViewModel;

public class DetailTagihanInternet extends InjectionActivity {

  private DetailTagihanInternetBinding binding;
  private MenuWifi menuWifi;
  private ObjectAnimator animator;
  private InquiryViewModel inquiryViewModel;

  @Override
  protected String getTitleToolbar() {
    return "Checkout";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = DetailTagihanInternetBinding.inflate(layoutInflater, viewGroup, false);
    inquiryViewModel = new ViewModelProvider(this).get(InquiryViewModel.class);

    dataTagihan();

    return binding.getRoot();
  }

  private void dataTagihan() {
    InquiryResponse.Data data = getIntent().getExtras().getParcelable("data");
	//InquiryResponse.Data data = InquiryHelper.getInstance().getData();
	Log.d("terima data" ,"he" + data);
    if (data != null) {
      binding.namaPelanggan.setText(data.getTrName());
      binding.noPelanggan.setText(data.getHp());

      binding.tagihanPelanggan.setText(FormatUtils.formatRupiah(data.getNominal()));
      binding.biayaAdmin.setText(FormatUtils.formatRupiah(data.getAdmin()));

      double komisi = data.getPrice() - data.getSellingPrice();
      double kurangiHargaDariKomisi = data.getSellingPrice() - data.getPrice();
      binding.komisi.setText(FormatUtils.formatRupiah(komisi));
      binding.subTotal.setText(FormatUtils.formatRupiah(data.getPrice()));
      binding.totalBayarKurangiKomisi.setText(FormatUtils.formatRupiah(kurangiHargaDariKomisi));
      binding.jumLahBayar.setText(FormatUtils.formatRupiah(data.getSellingPrice()));
      Glide.with(this).load(getIntent().getStringExtra("imageUrl")).into(binding.logoProvider);
    }
  }
  /*
  private void detailTagihan() {
    binding.iconChevron.setRotation(binding.expandable.isExpanded() ? 180f : 0f);
    binding.containerChevron.setOnClickListener(
        v -> {
          if (binding.expandable.isExpanded()) {
            binding.expandable.collapse();
            animateArrow(180f, 0f);
          } else {
            binding.expandable.expand();
            animateArrow(0f, 180f);
          }
        });
  }

  private void animateArrow(float start, float end) {
    animator = ObjectAnimator.ofFloat(binding.iconChevron, "rotation", start, end);
    animator.setDuration(300);
    animator.start();
  }

  private void terimaData() {
    // DATA TAGIHAN
    InquiryResponse.Data data = getIntent().getParcelableExtra("data");
    if (data != null) {
      binding.namaPelanggan.setText(data.getTrName());
      binding.noPelanggan.setText(data.getHp());

      binding.tagihanPelanggan.setText(FormatUtils.formatRupiah(data.getNominal()));
      binding.biayaAdmin.setText(FormatUtils.formatRupiah(data.getAdmin()));

      double komisi = data.getPrice() - data.getSellingPrice();
      double kurangiHargaDariKomisi = data.getSellingPrice() - data.getPrice();
      binding.komisi.setText(FormatUtils.formatRupiah(komisi));
      binding.subTotal.setText(FormatUtils.formatRupiah(data.getPrice()));
      binding.totalBayarKurangiKomisi.setText(FormatUtils.formatRupiah(kurangiHargaDariKomisi));
      binding.jumLahBayar.setText(FormatUtils.formatRupiah(data.getSellingPrice()));
      String imgUrl = getIntent().getStringExtra("imgUrl");
      Glide.with(this).load(imgUrl).into(binding.logoProvider);
    }

    // DESKRIPSI
    InquiryResponse.Desc desc = data.getDesc();
    if (desc != null) {

      // TAGIHAN
      InquiryResponse.Tagihan tagihan = desc.getTagihan();
      if (tagihan != null) {
        // DETAIL
        List<InquiryResponse.Detail> details = tagihan.getDetail();
        if (details != null && !details.isEmpty()) {
          // Mengambil 3 nilai pertama dari detail
          int limit = Math.min(details.size(), 3); // Batasi ke 3 item jika ada lebih banyak

          StringBuilder detailText = new StringBuilder();
          for (int i = 0; i < limit; i++) {
            InquiryResponse.Detail detail = details.get(i);
            binding.periode.setText(detail.getPeriode());
            detailText
                .append("Periode: ")
                .append(detail.getPeriode())
                .append("\n")
                .append("Nilai Tagihan: ")
                .append(FormatUtils.formatRupiah(Integer.parseInt(detail.getNilaiTagihan())))
                .append("\n")
                .append("Admin: ")
                .append(FormatUtils.formatRupiah(Integer.parseInt(detail.getAdmin())))
                .append("\n")
                .append("Total: ")
                .append(FormatUtils.formatRupiah(detail.getTotal()))
                .append("\n\n");
          }
          binding.detailTagihan.setText(detailText.toString());
        } else {
          binding.detailTagihan.setText("Tidak ada detail tagihan.");
        }
      }
    }
  }

  */
}

/*
data": {
    "tr_id": 9732432,
    "code": "TELKOMPSTN",
    "hp": "6391600000",
    "tr_name": "JAORAH",
    "period": "201405,201407",
    "nominal": 129610,
    "admin": 7500,
    "ref_id": "091283746520",
    "response_code": "00",
    "message": "INQUIRY SUCCESS",
    "price": 137110,
    "selling_price": 133510,
    "desc": {
      "kode_area": "0751",
      "divre": "01",
      "datel": "0006",
      "jumlah_tagihan": 3,
      "tagihan": {
        "detail": [
          {
            "periode": "MEI 2014",
            "nilai_tagihan": "49870",
            "admin": "2500",
            "total": 52370
          },
          {
            "periode": "JUN 2014",
            "nilai_tagihan": "44870",
            "admin": "2500",
            "total": 47370
          },
          {
            "periode": "JUL 2014",
            "nilai_tagihan": "34870",
            "admin": "2500",
            "total": 37370
          }
        ]
      }
    }
  },
  "meta": []
*/
