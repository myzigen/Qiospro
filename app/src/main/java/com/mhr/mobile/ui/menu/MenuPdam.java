package com.mhr.mobile.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.MenuPdamBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.sheet.SheetPdam;

public class MenuPdam extends InjectionActivity {
  private MenuPdamBinding binding;

  @Override
  protected String getTitleToolbar() {
    return "PDAM";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuPdamBinding.inflate(layoutInflater, viewGroup, false);
    showBottomSheet();
    return binding.getRoot();
  }

  private void showBottomSheet() {
    binding.provider.setOnClickListener(
        v -> {
          SheetPdam sheetPdam = SheetPdam.newInstance();
          sheetPdam.setOnSelectProvider(
              (model) -> {
                binding.txtLabelProvider.setText(model);
              });
          sheetPdam.show(getSupportFragmentManager());
        });
  }
}
