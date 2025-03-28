package com.mhr.mobile.manage.request;

import android.app.Activity;
import com.mhr.mobile.manage.client.PricelistDigiflazzClient;
import com.mhr.mobile.manage.response.PricelistDigiflazzResponse;
import com.mhr.mobile.util.SignMaker;
import java.util.List;

public class PricelistDigiflazzRequest {
  public Activity activity;
  private String cmd;
  private String username;
  private String sign;
  private String apikey;

  public PricelistDigiflazzRequest(Activity activity) {
    this.activity = activity;
  }

  public PricelistDigiflazzRequest(String cmd, String username, String sign) {
    this.cmd = cmd;
    this.username = username;
    this.sign = sign;
  }

  public void setUsername() {
    this.username = "kazugeo0r8GW";
  }

  public void setApikey() {
    this.apikey = "dev-57e10270-a65e-11ef-acb2-116dd9f6a5ed";
  }

  public void startRequestProduk(RequestPricelistListener listener, boolean prabayar) {
    sign = SignMaker.getSign(username, apikey, "pricelist");

    if (prabayar) {
      cmd = "prepaid";
    } else {
      cmd = "pasca";
    }

    PricelistDigiflazzClient.getInstance().execute(this, cmd, username, sign, apikey, listener);
  }

  public interface RequestPricelistListener {
    void onStartLoading();

    void onResponse(List<PricelistDigiflazzResponse.Data> response);

    void onFailure(String message);
  }
}
