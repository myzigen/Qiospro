package com.mhr.mobile.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import com.mhr.mobile.R;

public class QiosColor {

  public static int getColor(Context context, int color) {
    return ContextCompat.getColor(context, color);
  }

  public static int getActiveColor(Context context) {
    return ContextCompat.getColor(context, R.color.crane_theme_light_primary);
  }

  public static int getDisableColor(Context context) {
    return ContextCompat.getColor(context, R.color.qiospay_stroke_card);
  }

  public static int getSuccessColor(Context context, int color) {
    return ContextCompat.getColor(context, color);
  }

  public static int getErrorColor(Context context) {
    return ContextCompat.getColor(context, R.color.status_canceled);
  }

  public static void applyDominantColorToView(Bitmap resource, View view) {
    if (resource != null) {
      Palette.from(resource)
          .generate(
              new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                  if (palette != null) {
                    // Dapatkan warna dominan
                    int dominantColor = palette.getDominantColor(Color.BLACK);

                    // Terapkan warna dominan ke tampilan
                    view.setBackgroundColor(dominantColor);
                  }
                }
              });
    }
  }
}
