package com.mhr.mobile.ui.content;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mhr.mobile.MainActivity;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.ContentPaymentStatusBinding;
import com.mhr.mobile.manage.response.WebhookResponse;
import com.mhr.mobile.model.RiwayatTransaksi;
import com.mhr.mobile.notification.NotificationStatusTransaction;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.menu.MenuPln;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;

public class ContentPaymentStatus extends InjectionActivity {
  private ContentPaymentStatusBinding binding;
  private DatabaseReference db;
  private RiwayatTransaksi riwayat;

  @Override
  protected String getTitleToolbar() {
    return "Status Transaksi";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentPaymentStatusBinding.inflate(layoutInflater, viewGroup, false);

    terimaData();

    return binding.getRoot();
  }

  private void terimaData() {
    riwayat = getIntent().getParcelableExtra("kirim");
    int iconResId = riwayat.getIconResId();
    long timestamp = riwayat.getTimestamp();
    String date = FormatUtils.formatTimestamp(timestamp);
    String typeApi = riwayat.getTypeApi();

    String imageUrl = riwayat.getImageUrl();
    if (imageUrl != null && !imageUrl.isEmpty()) {
      Glide.with(this).load(imageUrl).into(binding.logoProvider);
    } else {
      binding.logoProvider.setImageResource(iconResId); // Gunakan iconResId sebagai default
    }

    if (riwayat.getTypeApi().equalsIgnoreCase("apiIak")) {
      binding.trId.setText(String.valueOf(riwayat.getTrId()));
      binding.refId.setVisibility(View.GONE);
      checkStatusTransaksi(riwayat.getRefId());
    } else if (riwayat.getTypeApi().equalsIgnoreCase("apiDigiflazz")) {
      binding.trId.setText(riwayat.getRefId());
      binding.refId.setVisibility(View.GONE);
    }
    binding.brand.setText(riwayat.getBrand());
    binding.hp.setText(riwayat.getCustomerId());
    binding.tgl.setText(date);
    binding.harga.setText(FormatUtils.formatRupiah(riwayat.getHarga()));
    binding.hp.setOnClickListener(this::copy);
  }

  private void copy(View v) {
    String copyNomor = binding.hp.getText().toString();
    AndroidViews.copyToClipboard(this, copyNomor, "Berhasil Di Salin",binding.getRoot());
  }

  private void copySn(View v) {
    String copySn = binding.sn.getText().toString();
    AndroidViews.copyToClipboard(this, copySn, "Berhasil Di Salin", binding.getRoot());
  }

  private void checkStatusTransaksi(String ref_id) {
    DatabaseReference db = FirebaseDatabase.getInstance().getReference("status_transaksi").child(ref_id);
    db.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot snapshot) {
            WebhookResponse.Data data = snapshot.getValue(WebhookResponse.Data.class);
            if (data != null) {
              String message = data.getMessage();
              if (message.isEmpty()) {
                updateUIWithStatus("PROSES", data.getStatus());
              } else {
                updateUIWithStatus(data.getMessage(), data.getStatus());
                NotificationStatusTransaction.showNotification(
                    ContentPaymentStatus.this, "Pembelian Sukses", data.getSn());
                String input = data.getSn();
                String output = input.split("/")[0];
                binding.rootSn.setVisibility(View.VISIBLE);
                binding.sn.setText("ID: " + output);
                binding.sn.setOnClickListener(v -> copySn(v));
                binding.btnBeliLagi.setVisibility(View.VISIBLE);
                binding.btnBeliLagi.setOnClickListener(
                    v -> {
                      Intent intent = new Intent(ContentPaymentStatus.this, MenuPln.class);
                      intent.putExtra("nomorCustomer", riwayat.getCustomerId());
                      startActivity(intent);
                    });
              }
            } else {
              updateUIWithStatus("PROSES", 0);
            }
          }

          @Override
          public void onCancelled(DatabaseError arg0) {}
        });
  }

  private void updateUIWithStatus(String statusMessage, double statusCode) {
    if (statusCode == 0) {
      binding.status.setTextColor(QiosColor.getColor(this, R.color.status_pending));
      binding.status.setText(statusMessage);
    } else if (statusCode == 1) {
      binding.status.setTextColor(QiosColor.getColor(this, R.color.status_approved));
      binding.status.setText(statusMessage);
    } else if (statusCode == 2) {
      binding.status.setTextColor(QiosColor.getColor(this, R.color.status_canceled));
      binding.status.setText(statusMessage);
    }
  }
  
  @Override
  public void onBackPressed(){
	  /*
	  Intent intent = new Intent(ContentPaymentStatus.this,MainActivity.class);
	  startActivity(intent);
	  finish();*/
	  super.onBackPressed();
  }
}
