package com.mhr.mobile.manage.request;

import android.app.Activity;
import com.mhr.mobile.manage.client.TransaksiIAKClient;
import com.mhr.mobile.manage.response.TransaksiIAKResponse;
import com.mhr.mobile.util.Config;
import com.mhr.mobile.util.SignMaker;

public class TransaksiIAKRequest {
  public Activity activity;
  private String username;
  private String ref_id;
  private String customer_id;
  private String product_code;
  private String apiKey;
  private String sign;

  public TransaksiIAKRequest(Activity activity) {
    this.activity = activity;
  }

  public TransaksiIAKRequest(
      String username, String ref_id, String customer_id, String product_code, String sign) {
    this.username = username;
    this.ref_id = ref_id;
    this.customer_id = customer_id;
    this.product_code = product_code;
    this.sign = sign;
  }

  public void setUsername() {
    this.username = Config.USERNAME;
  }

  public void setApikey() {
    this.apiKey = Config.APIKEY_DEV;
  }

  public void setCustomerId(String customer_id) {
    this.customer_id = customer_id;
  }
  
  public void setRefId(String ref_id){
	  this.ref_id = ref_id;
  }

  public void setCodeProduk(String product_code) {
    this.product_code = product_code;
  }

  public void startTransaksiRequest(RequestTransaksiCallback callback) {
    ref_id = SignMaker.getSignRefId();
    sign = SignMaker.getSign(username, apiKey, ref_id);
    TransaksiIAKClient.getInstance().execute(this, username, ref_id, customer_id, product_code, sign, apiKey, callback);
  }

  public interface RequestTransaksiCallback {
    void onStartRequest();

    void onResponse(TransaksiIAKResponse.Data response);

    void onFailure(String errorMessage);
  }
}
