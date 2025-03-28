package com.mhr.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;

public class QiosPreferences {
  public static final String PREF_NAME = "pref";
  public static final String SAVE_NOMOR = "nomor";
  public static final String SAVE_INQUIRY = "nometer";
  public static final String IS_HIDDEN_SALDO = "hidden";
  private static final String KEY_SALDO = "saldo";
  private static final String COUNTDOWN = "expired";
  private SharedPreferences preferences;
  private SharedPreferences.Editor editor;

  public QiosPreferences(Context context) {
    preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    editor = preferences.edit();
  }

  // Simpan nomor hp terakhir input;
  public void saveNomerTerakhirInput(String nomer) {
    editor.putString(SAVE_NOMOR, nomer).apply();
  }

  // Ambil nomor hp terakhir input
  public String getNomerTerakhirInput() {
    return preferences.getString(SAVE_NOMOR, "");
  }

  // Hapus nomor Hp yg tersimpan
  public void hapusNomerTerakhirDisimpan() {
    editor.remove(SAVE_NOMOR).apply();
  }

  public void saveIdMeterTerakhirInput(String noMeter) {
    editor.putString(SAVE_INQUIRY, noMeter).apply();
  }

  public String getNomerMeterTerakhirInput() {
    return preferences.getString(SAVE_INQUIRY, "");
  }

  public void setHiddenSaldo(boolean isHidden) {
    editor.putBoolean(IS_HIDDEN_SALDO, isHidden).apply();
  }

  public boolean isHiddenSaldo() {
    return preferences.getBoolean(IS_HIDDEN_SALDO, false);
  }

  public void setSaldo(String saldo) {
    editor.putString(KEY_SALDO, saldo).apply();
  }

  public String getSaldo() {
    return preferences.getString(KEY_SALDO, "0"); // Default: 0
  }

  public void saveCountdownTimer(long duration, String orderId) {
    long expired = System.currentTimeMillis() + (duration * 60 * 1000);
    editor.putLong(COUNTDOWN + "_" + orderId, expired).apply();
  }

  public long getCountdownTimer(String orderId) {
    long expiryTime = preferences.getLong(COUNTDOWN + "_" + orderId, 0);
    long currentTime = System.currentTimeMillis();
    long timeLeft = expiryTime - currentTime;
    return Math.max(timeLeft, 0); // Jika sudah habis, kembalikan 0
  }
}
