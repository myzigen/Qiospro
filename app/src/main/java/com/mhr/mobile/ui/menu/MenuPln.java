package com.mhr.mobile.ui.menu;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mhr.mobile.adapter.pager.PagerMenuPln;
import com.mhr.mobile.databinding.MenuPlnBinding;
import com.mhr.mobile.inquiry.helper.PlnHelper;
import com.mhr.mobile.inquiry.response.InquiryPLNResponse.SaveData;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidViews;

public class MenuPln extends InjectionActivity implements TextWatcher {
  private MenuPlnBinding binding;
  private PagerMenuPln adapter;
  private String noMeter;
  private InquirySendData sendData;
  private PlnHelper plnHelper;
  private View bottomPay;
  public boolean isNomorChecked = false;

  public interface InquirySendData {
    void onSendData(String data);
  }

  @Override
  protected String getTitleToolbar() {
    return "Listrik PLN";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuPlnBinding.inflate(layoutInflater, viewGroup, false);
    initUi();
    initViewPager();
    return binding.getRoot();
  }

  private void initUi() {
    plnHelper = new PlnHelper(this);
    binding.editText.addTextChangedListener(this);
    if (binding.editText != null) {
      binding.editText.setText(getIntent().getStringExtra("nomorCustomer"));
    }
  }

  private void initViewPager() {
    adapter = new PagerMenuPln(this);
    binding.viewPager2.setAdapter(adapter);
    binding.viewPager2.registerOnPageChangeCallback(
        new ViewPager2.OnPageChangeCallback() {
          @Override
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
          }

          @Override
          public void onPageSelected(int position) {
            switch (position) {
              case 0:
                binding.textInput.setHint("No Meter");
                binding.editText.setHint("Masukan No Meter");
                break;
              case 1:
                binding.textInput.setHint("ID Pelanggan");
                binding.editText.setHint("Masukan Id Pelanggan");
                break;
            }
            super.onPageSelected(position);
          }

          @Override
          public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
          }
        });

    new TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager2,
            (tab, position) -> {
              switch (position) {
                case 0:
                  tab.setText("Token");
                  break;
                case 1:
                  tab.setText("PLN");
                  break;
              }
            })
        .attach();
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    noMeter = s.toString().trim();
    if (noMeter.isEmpty()) {
      hideExpand();
      // if (bottomPay != null) bottomPay.setVisibility(View.GONE);
    } else {
      hideExpand();
      // if (bottomPay != null) bottomPay.setVisibility(View.GONE);
      isNomorChecked = false;
    }
  }

  public void checkCustomerName(String nometer, InquirySendData sendData) {
    plnHelper.checkCustomerName(
        nometer,
        new PlnHelper.PlnCallback() {
          @Override
          public void onLoading() {
            showExpandSuccess("Memeriksa nama pemilik ");
          }

          @Override
          public void onDataFound(SaveData data) {
            showExpandSuccess(data.getTrName());
            sendData.onSendData(data.getTrName());
          }

          @Override
          public void onDataSaved() {}

          @Override
          public void onError(String errorMessage) {
            showExpandError(errorMessage);
          }
        });
  }

  public void showExpandSuccess(String message) {
    AndroidViews.showExpandSuccess(
        message, binding.incExpand.expandable, binding.incExpand.cekUser);
  }

  public void showExpandError(String error) {
    AndroidViews.ShowExpandError(error, binding.incExpand.expandable, binding.incExpand.cekUser);
  }

  public void hideExpand() {
    if (binding.incExpand.expandable.isExpanded()) {
      binding.incExpand.expandable.collapse();
    }
  }

  public boolean isEditTextEmpty() {
    return noMeter == null || noMeter.trim().isEmpty();
  }

  public String sendData() {
    return noMeter;
  }

  public void setBottomPay(View bottomPay) {
    this.bottomPay = bottomPay;
  }
}
