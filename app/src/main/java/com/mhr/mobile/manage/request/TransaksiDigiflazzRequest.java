package com.mhr.mobile.manage.request;

import android.app.Activity;
import com.mhr.mobile.manage.client.TransaksiDigiflazzClient;
import com.mhr.mobile.manage.response.TransaksiDigiflazzResponse;
import com.mhr.mobile.util.Config;
import com.mhr.mobile.util.SignMaker;

public class TransaksiDigiflazzRequest {
  public Activity activity;
  private String username;
  private String buyer_sku_code;
  private String customer_no;
  private String ref_id;
  private String sign;
  private String apikey;
  private int total;
  private boolean testing = true;

  public TransaksiDigiflazzRequest(Activity activity) {
    this.activity = activity;
  }

  public TransaksiDigiflazzRequest(
      String username,
      String buyer_sku_code,
      String customer_no,
      String ref_id,
      String sign,
      int total,
      boolean testing) {
    this.username = username;
    this.buyer_sku_code = buyer_sku_code;
    this.customer_no = customer_no;
    this.ref_id = ref_id;
    this.sign = sign;
    this.total = total;
    this.testing = testing;
  }

  public void setUsername() {
    this.username = Config.DIGIFLAZZ_USERNAME;
  }

  public void setApikey() {
    this.apikey = Config.DIGIFLAZZ_KEY;
  }

  public void setBuyerSkuCode(String sku) {
    this.buyer_sku_code = sku;
  }

  public void setHp(String nomor) {
    this.customer_no = nomor;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public void startRequestTransaksi(RequestTransaksiCallback callback) {
    ref_id = SignMaker.getSignRefId();
    sign = SignMaker.getSign(username, apikey, ref_id);

    TransaksiDigiflazzClient.getInstance()
        .execute(
            this, username, buyer_sku_code, customer_no, ref_id, sign, apikey, total, callback);
  }

  public interface RequestTransaksiCallback {
    void onLoading();

    void onResponse(TransaksiDigiflazzResponse.Data data);

    void onFailure(String error);
  }
}
