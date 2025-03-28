package com.mhr.mobile.ui.sheet;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.BtmSheetKeyboardBinding;

public class SheetKeyboard extends BottomSheetDialogFragment {
  private BtmSheetKeyboardBinding binding;
  private EditText editText;
  private OnKeyboardDismissListener listener;
  private FrameLayout frameLayout;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup v, Bundle savedInstanceState) {
    binding = BtmSheetKeyboardBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();

    // Menghubungkan keyboard dengan EditText jika ada
    if (editText != null) {
      InputConnection ic = editText.onCreateInputConnection(new EditorInfo());
      if (ic != null) {
        binding.keyboard.setInputConnection(ic);
        binding.keyboard.setTargetEditText(editText);
      }
    }

    view.setFocusableInTouchMode(true);
    view.requestFocus();

    return view;
  }

  @Override
  public void onDismiss(DialogInterface arg0) {
    super.onDismiss(arg0);
    if (listener != null) {
      listener.onDismiss();
    }
  }

  private void showSoftKeyboard(View view) {
    InputMethodManager imm =
        (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
  }

  public void setEditText(EditText editText) {
    this.editText = editText;
  }

  public void setOnKeyboardDismissListener(OnKeyboardDismissListener listener) {
    this.listener = listener;
  }

  public interface OnKeyboardDismissListener {
    void onDismiss();
  }

  @Override
  public int getTheme() {
    return R.style.KeyboardStyle; // Gunakan style untuk tema keyboard
  }
}
