package com.mhr.mobile.manage.request;

import android.app.Activity;
import android.content.Context;
import com.mhr.mobile.manage.client.TransaksiIAKClientStatus;
import com.mhr.mobile.manage.response.TransaksiIAKResponseStatus;
import com.mhr.mobile.util.Config;
import com.mhr.mobile.util.SignMaker;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class TransaksiIAKRequestStatus {

  public Activity activity;
  // Request
  private String username;
  private String ref_id;
  private String sign;
  //
  private String apikey;
  private int tr_id;
  private String customer_id;

  public TransaksiIAKRequestStatus(Activity activity) {
    this.activity = activity;
  }

  public TransaksiIAKRequestStatus(
      String username, String ref_id, String sign, int tr_id, String customer_id) {
    this.username = username;
    this.ref_id = ref_id;
    this.sign = sign;
    this.tr_id = tr_id;
    this.customer_id = customer_id;
  }

  public void setUsername() {
    this.username = Config.USERNAME;
  }

  public void setApikey() {
    this.apikey = Config.APIKEY_DEV;
  }

  public void setRefId(String ref_id) {
    this.ref_id = ref_id;
  }

  public void setTrId(int trId) {
    this.tr_id = trId;
  }

  public void setCustomerId(String customer_id) {
    this.customer_id = customer_id;
  }

  public void startRequestStatusTransaksi(RequestStatusTransaksi callback) {
    // ref_id = SignMaker.getSignRefId();
    sign = SignMaker.getSign(username, apikey, ref_id);
    TransaksiIAKClientStatus.getInstance()
        .execute(this, username, ref_id, sign, apikey, tr_id, customer_id, callback);
  }

  public interface RequestStatusTransaksi {
    void onStartRequest();

    void onResponse(TransaksiIAKResponseStatus.Data data);

    void onFailure(String errorMessage);
  }
}
