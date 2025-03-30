package com.mhr.mobile.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.fragment.app.DialogFragment;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.DialogPopupBinding;

public class PopupDialog extends DialogFragment {
  private DialogPopupBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup v, Bundle savedInstanceState) {
    binding = DialogPopupBinding.inflate(getLayoutInflater());
    // Menghapus header dialog
    if (getDialog() != null && getDialog().getWindow() != null) {
      getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }
    // Inflate layout loading
    View view = binding.getRoot();
	binding.close.setOnClickListener(close -> dismiss());
    return view;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);
    if (dialog.getWindow() != null) {
      // dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    dialog.setCanceledOnTouchOutside(false);
    dialog.setCancelable(false);
    // Add back button listener
    /*
      dialog.setOnKeyListener(
          new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
              // getAction to make sure this doesn't double fire
              if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                // Your code here
    	  if (isAdded()){
    		  requireActivity().onBackPressed();
    	  }
                return true; // Capture onKey
              }
              return false; // Don't capture
            }
          });
    */
    return dialog;
  }
}
