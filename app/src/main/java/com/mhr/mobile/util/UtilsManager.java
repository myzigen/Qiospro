package com.mhr.mobile.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.mhr.mobile.R;
import java.util.UUID;

public class UtilsManager {

  public static void getStatusBarColor(Window window, Context context) {
    window.setStatusBarColor(ContextCompat.getColor(context, R.color.qiospay_blue_dop));
  }

  public static void setStatusBarTranslucent(@NonNull Window window) {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
  }

  public static void setAllowDrawUnderStatusBar(@NonNull Window window) {
    window
        .getDecorView()
        .setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
  }

  public static String getDeviceId() {
    return UUID.randomUUID().toString();
  }

  public static String getDeviceName() {
    return Build.MANUFACTURER + " " + Build.MODEL; // Contoh: "Samsung Galaxy A52"
  }

  public static String getVersionApp(Context context) {
    try {
      PackageInfo appInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return appInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
	  return "Versi Tidak Di Ketahui";
    }
  }

  public static void getDoneEnter(EditText editText) {
    editText.setOnKeyListener(
        (v, keyCode, event) -> {
          if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            // Tangkap aksi Enter dan lakukan sesuatu (jika perlu)
            return true; // Return true untuk mencegah aksi default
          }
          return false;
        });
  }
}
