package com.mhr.mobile.manage.request;

import android.app.Activity;
import com.mhr.mobile.manage.client.MidtransClient;
import com.mhr.mobile.manage.response.MidtransResponse;
import java.util.Base64;
import com.mhr.mobile.util.Config;

public class MidtransRequest {
  public Activity activity;
  private String orderId;
  private int grossAmount;
  private long expiryDuration;
  private String expiryUnit;
  private String paymentType;
  private String bankName;

  public MidtransRequest(Activity activity) {
    this.activity = activity;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public void setGrossAmount(int amount) {
    this.grossAmount = amount;
  }

  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public void setExpiryDuration(long duration, String unit) {
    this.expiryDuration = duration;
    this.expiryUnit = unit;
  }

  public String getAuthHeader() {
    String serverKey = Config.midtransKey;
    return "Basic " + Base64.getEncoder().encodeToString(serverKey.getBytes());
  }

  public void startRequestTransaction(MidtransRequestCallback callback) {
    MidtransTransaction transaction = new MidtransTransaction();
    transaction.setMidtransTransactionDuration(expiryDuration, expiryUnit);
    transaction.setMidtransTransactionPaymentMethod(paymentType,bankName);
    transaction.setMidtransTransactionDetails(orderId, grossAmount);
    
    MidtransClient.getInstance().execute(this, transaction, callback);
  }

  public interface MidtransRequestCallback {
    public void onResponse(MidtransResponse response);

    public void onFailure(String error);
  }
}
