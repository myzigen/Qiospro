package com.mhr.mobile.ui.inject;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.internal.ViewUtils;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.InjectionSheetContainerBinding;

public abstract class InjectionSheetFragment extends BottomSheetDialogFragment {
  private InjectionSheetContainerBinding binding;
  private ViewGroup container;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = InjectionSheetContainerBinding.inflate(inflater, viewGroup, false);
    TextView textView = binding.title;
    setDefaultSheetTitle(textView);
    container = binding.getRoot();
    container.setBackgroundResource(R.drawable.shape_corners_top);
    View contentView = onCreateSheetView(inflater, viewGroup, bundle);
    binding.container.addView(contentView);
    binding.close.setOnClickListener(v -> dismiss());
    return binding.getRoot();
  }

  @Override
  public Dialog onCreateDialog(Bundle bundle) {
    return super.onCreateDialog(bundle);
  }

  public void show(FragmentManager fm) {
    show(fm, "TAG");
  }

  protected String getSheetTitle() {
    return "";
  }

  private void setDefaultSheetTitle(TextView textView) {
    if (!TextUtils.isEmpty(getSheetTitle())) {
      binding.containerTitle.setVisibility(View.VISIBLE);
      binding.drag.setVisibility(View.GONE);
      textView.setText(getSheetTitle());
    } else {
      binding.containerTitle.setVisibility(View.GONE);
      binding.drag.setVisibility(View.VISIBLE);
    }
  }

  protected void applyWindowInsets(View view) {
    // Penanganan Window Insets
    ViewUtils.doOnApplyWindowInsets(
        view,
        (v, insets, initialPadding) -> {
          view.setPaddingRelative(
              initialPadding.start,
              initialPadding.top,
              initialPadding.end,
              initialPadding.bottom
                  + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
          return insets;
        });
  }

  protected abstract View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle);
  
  //protected void onPhoneNumberPicked(String phoneNumber);
}
