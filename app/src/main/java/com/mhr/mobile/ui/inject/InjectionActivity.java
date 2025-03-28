package com.mhr.mobile.ui.inject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mhr.mobile.R;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;

public abstract class InjectionActivity extends InjectionConnectivity {
  public static final String EXTRA_DEMO_TITLE = "title";
  private ViewGroup containerView;
  private View includePay;
  protected Toolbar toolbar;
  protected LinearLayoutManager linearLayoutManager;
  protected GridLayoutManager gridLayoutManager;
  protected SpacingItemDecoration spacingItemDecoration;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setDrawUnderStatusbar();
    setContentView(R.layout.activity_container);
    // UtilsManager.getStatusBarColor(getWindow(), this);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    toolbar = findViewById(R.id.toolbar);
    setDefaultTitleToolbar(toolbar);

    toolbar.setNavigationOnClickListener(v -> onBackPressed());

    // Menambahkan layout utama ke container
    containerView = findViewById(R.id.idActivity_container);
    View contentView =
        onCreateQiosView(LayoutInflater.from(this), containerView, savedInstanceState);

    containerView.addView(contentView);

    // Inisialisasi includePay dan panggil setup
    includePay = findViewById(R.id.pay);
    setupIncludePembayaran(includePay);
  }

  public TextView getHargaInclude() {
    if (includePay != null) {
      return includePay.findViewById(R.id.harga);
    }
    return null;
  }

  @Override
  protected void onPhoneNumberPicked(String phoneNumber) {}

  @Override
  protected void onPhoneNumberPickFailed() {}

  /**
   * Mengatur layout utama dari activity ini.
   * @param layoutInflater untuk inflating view
   * @param viewGroup parent view
   * @param bundle instance state
   * @return view utama
   */
  public abstract View onCreateQiosView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle);

  /**
   * Menentukan judul toolbar.
   * @return judul
   */
  protected String getTitleToolbar() {
    return "";
  }

  /**
   * Menyetup pembayaran dengan include layout. Override di subclass jika diperlukan.
   * @param includeView view pembayaran
   */
  protected void setupIncludePembayaran(View includeView) {
    // Subclass dapat mengimplementasikan ini jika diperlukan
  }

  /**
   * Membuat intent berdasarkan posisi. Override di subclass jika diperlukan.
   * @param position posisi item
   * @return intent
   */
  protected Intent createIntent(int position) {
    return null;
  }

  /**
   * Mengatur judul toolbar default.
   * @param toolbar toolbar
   */
  private void setDefaultTitleToolbar(Toolbar toolbar) {
    if (getTitleToolbar() != null && !getTitleToolbar().isEmpty()) {
      toolbar.setTitle(getTitleToolbar());
    } else {
      toolbar.setTitle(getDefaultDemoTitle());
    }
  }

  /**
   * Mengambil judul demo default jika tidak ada judul disediakan.
   * @return judul demo
   */
  private String getDefaultDemoTitle() {
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      return extras.getString(EXTRA_DEMO_TITLE, "");
    }
    return "";
  }


  protected LinearLayoutManager getLinearLayoutManager() {
    return linearLayoutManager = new LinearLayoutManager(this);
  }

  protected GridLayoutManager getGridLayoutManager(int count) {
    return gridLayoutManager = new GridLayoutManager(this, count);
  }

  protected SpacingItemDecoration getSpacingItemDecoration(int column, int count, boolean spacing) {
    return spacingItemDecoration = new SpacingItemDecoration(column, count, spacing);
  }
}
