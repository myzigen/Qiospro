package com.mhr.mobile.manage.request;

import android.app.Activity;
import com.mhr.mobile.manage.client.CekSaldoClient;
import com.mhr.mobile.manage.response.CekSaldoResponse;
import com.mhr.mobile.util.Config;
import com.mhr.mobile.util.SignMaker;

public class CekSaldoRequest {
  public Activity activity;
  private String username = Config.USERNAME;
  private String sign;
  private String key = Config.APIKEY_DEV;

  public CekSaldoRequest(Activity activity) {
    this.activity = activity;
  }

  public CekSaldoRequest(String username, String sign) {
    this.username = username;
    this.sign = sign;
  }

  public void setUsername() {
    // this.username = Config.USERNAME;
  }

  public void setApiKey() {
    // this.key = Config.APIKEY_DEV;
  }

  public void startCekSaldo(RequestSaldoCallback saldo) {
    sign = SignMaker.getSign(username, key, "bl");
    CekSaldoClient.getInstance().execute(this, username, sign, key, saldo);
  }

  public interface RequestSaldoCallback {
    void onStartLoading();

    void onResponse(CekSaldoResponse saldo);

    void onFailure(String errorMessage);
  }
}
