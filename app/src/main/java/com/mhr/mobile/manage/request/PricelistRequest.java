package com.mhr.mobile.manage.request;

import android.app.Activity;
import com.mhr.mobile.manage.client.PricelistClient;
import com.mhr.mobile.util.Config;
import com.mhr.mobile.util.SignMaker;

public class PricelistRequest {
  public Activity activity;
  String commands = "pricelist";
  String username;
  String sign;
  String apiKey;

  public PricelistRequest(Activity activity) {
    this.activity = activity;
  }

  public PricelistRequest(String commands, String username, String sign) {
    this.commands = commands;
    this.username = username;
    this.sign = sign;
  }

  public void setUserName() {
    this.username = Config.USERNAME;
  }

  public void setApiKey() {
    this.apiKey = Config.API_KEY_PRODUCTION;
  }

  public void startRequestPricelist(RequestPricelistListener listener) {
    sign = SignMaker.getSign(username, apiKey, "pl");
    PricelistClient.getInstance().execute(this, commands, username, sign, apiKey, listener);
  }

  public interface RequestPricelistListener {
    public void onResponse(Object response);

    public void onFailure(String errorMessage);
  }
}
