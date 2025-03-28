package com.mhr.mobile.ui.content;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.ContentTopupStatusBinding;
import com.mhr.mobile.model.RiwayatTransaksi;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import com.mhr.mobile.util.QiosPreferences;
import com.mhr.mobile.widget.textview.RoundedBackgroundSpan;

public class ContentTopupStatus extends InjectionActivity {
  private ContentTopupStatusBinding binding;
  private RiwayatTransaksi riwayatTransaksi;
  private QiosPreferences preferences;
  private long timeLeft;

  @Override
  protected String getTitleToolbar() {
    return "Status Topup";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentTopupStatusBinding.inflate(getLayoutInflater());
    initializeData();
    return binding.getRoot();
  }

  private void initializeData() {
    preferences = new QiosPreferences(this);
    riwayatTransaksi = getIntent().getParcelableExtra("riwayat_topup");
    timeLeft = preferences.getCountdownTimer(riwayatTransaksi.getRefId());
    long expireTimestamp = System.currentTimeMillis() + timeLeft;
    String expiryDateTime = FormatUtils.formatTimestamp(expireTimestamp);
    String status = getTransactionStatus(riwayatTransaksi.getStatusTransaksiMidtrans());

    startCountdownTimer(timeLeft);
    binding.tvExpiredTime.setText("Transfer Sebelum " + riwayatTransaksi.getExpiryTime());
    Glide.with(this).load(riwayatTransaksi.getImageUrl()).into(binding.image);
    binding.tvOrderId.setText(riwayatTransaksi.getRefId());
    String jumlahTf = FormatUtils.formatRupiah(riwayatTransaksi.getHarga());
    binding.tvJumlahTf.setText(highlightLastThreeDigits(jumlahTf));
    binding.tvNoRekening.setText(riwayatTransaksi.getCustomerId());
    binding.tvStatus.setText(status);
    binding.tvBankName.setAllCaps(true);
    binding.tvBankName.setText(riwayatTransaksi.getMerchantName());
    binding.copy.setOnClickListener(this::copyNoRekening);
  }

  private void copyNoRekening(View v) {
    String copySn = binding.tvNoRekening.getText().toString();
    AndroidViews.copyToClipboard(this, copySn, "Berhasil Di Salin", binding.getRoot());
  }

  private void startCountdownTimer(long expired) {
    AndroidViews.startTimer(
        expired,
        new AndroidViews.CountdownListener() {
          @Override
          public void onTick(long minutes, long seconds) {
            binding.tvExpired.setText(String.format("%02d:%02d", minutes, seconds));
          }

          @Override
          public void onFinish() {
            binding.tvExpired.setText("Waktu Habis");
          }
        });
  }

  @Override
  protected void onDestroy() {
    AndroidViews.cancelCountdown();
    super.onDestroy();
  }

  private String getTransactionStatus(String midtransStatus) {
    switch (midtransStatus) {
      case "pending":
        binding.tvStatus.setTextColor(QiosColor.getColor(this, R.color.status_canceled));
        return "Menunggu Pembayaran";
      case "settlement":
      case "capture":
        return "Pembayaran Berhasil";
      case "deny":
        return "Pembayaran Ditolak";
      case "cancel":
        return "Dibatalkan";
      case "expire":
        return "Kadaluarsa";
      case "refund":
        return "Dikembalikan";
      case "partial_refund":
        return "Dikembalikan Sebagian";
      case "authorize":
        return "Menunggu Otorisasi";
      default:
        return "Status Tidak Diketahui";
    }
  }

  private SpannableString highlightLastThreeDigits(String text) {
    SpannableString spannableString = new SpannableString(text);
    String[] words = text.split(" ");
    int start = 0;

    for (String word : words) {
      if (word.matches(".*\\d{3}$")) { // Cek apakah kata mengandung 3 digit angka terakhir
        int end = start + word.length();
        spannableString.setSpan(
            new RoundedBackgroundSpan(
                QiosColor.getColor(this, R.color.status_pending), Color.BLACK, 8),
            end - 3,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      }
      start += word.length() + 1; // +1 karena ada spasi antar kata
    }

    return spannableString;
  }
}
