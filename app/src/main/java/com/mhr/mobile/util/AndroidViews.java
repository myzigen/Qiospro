package com.mhr.mobile.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.snackbar.Snackbar;
import com.mhr.mobile.R;
import net.cachapa.expandablelayout.ExpandableLayout;

public class AndroidViews {
  private static CountDownTimer timer;

  public interface CountdownListener {
    void onTick(long minutes, long seconds);

    void onFinish();
  }

  public static void showExpandSuccess(String success, ExpandableLayout expandable, TextView text) {
    expandable.setBackgroundResource(R.drawable.shape_corners_bottom_succes);
    expandable.expand();
    text.setTextColor(Color.parseColor("#00AA5B"));
    text.setText(success);
  }

  public static void ShowExpandError(
      String error, ExpandableLayout expandableLayout, TextView textView) {
    expandableLayout.setBackgroundResource(R.drawable.shape_corners_bottom_error);
    expandableLayout.expand();
    textView.setTextColor(Color.parseColor("#D62F57"));
    textView.setText(error);
  }

  public static void showToast(String message, Context context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }

  public static void showSnackbar(View view, String message) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
  }

  public static void copyToClipboard(Context context, String text, String message, View view) {
    ClipboardManager clipboardManager =
        (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newPlainText("copy", text);
    clipboardManager.setPrimaryClip(clip);
    showSnackbar(view, message);
  }

  public static void applyInsets(View content) {
    ViewUtils.doOnApplyWindowInsets(
        content,
        (v, insets, initialPadding) -> {
          content.setPaddingRelative(
              initialPadding.start,
              initialPadding.top,
              initialPadding.end,
              initialPadding.bottom
                  + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
          return insets;
        });
  }

  public static int dpToPx(float dp, Context context) {
    return dpToPx(dp, context.getResources());
  }

  public static int dpToPx(float dp, Resources resources) {
    float px =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    return (int) px;
  }

  /**
   * Mengembalikan SpannableString dengan teks yang di-highlight.
   *
   * @param originalText Teks asli
   * @param queryText Teks pencarian
   * @param highlightColor Warna highlight (contoh: Color.RED)
   * @return SpannableString dengan teks yang di-highlight
   */
  public static SpannableString getHighlightedText(
      String originalText, String queryText, int highlightColor) {
    SpannableString spannableString = new SpannableString(originalText);

    if (queryText == null || queryText.isEmpty()) {
      return spannableString; // Jika tidak ada query, kembalikan teks asli
    }

    String lowerOriginal = originalText.toLowerCase();
    String lowerQuery = queryText.toLowerCase();

    int startIndex = lowerOriginal.indexOf(lowerQuery);
    while (startIndex >= 0) {
      int endIndex = startIndex + lowerQuery.length();
      spannableString.setSpan(
          new ForegroundColorSpan(highlightColor),
          startIndex,
          endIndex,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      startIndex = lowerOriginal.indexOf(lowerQuery, endIndex); // Cari instance berikutnya
    }

    return spannableString;
  }

  public static void startTimer(long duration, CountdownListener listener) {
    timer =
        new CountDownTimer(duration, 1000) {
          @Override
          public void onTick(long millisUntilFinished) {
            long minutes = millisUntilFinished / (60 * 1000);
            long seconds = (millisUntilFinished / 1000) % 60;
            listener.onTick(minutes, seconds);
          }

          @Override
          public void onFinish() {
            listener.onFinish();
          }
        }.start();
  }

  public static void cancelCountdown() {
    if (timer != null) {
      timer.cancel();
    }
  }
}
