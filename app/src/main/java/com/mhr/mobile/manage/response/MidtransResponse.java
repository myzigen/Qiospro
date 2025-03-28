package com.mhr.mobile.manage.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MidtransResponse {

  private String typeApi;

  @SerializedName("status_code")
  private String statusCode;

  @SerializedName("status_message")
  private String statusMessage;

  @SerializedName("transaction_id")
  private String transactionId;

  @SerializedName("order_id")
  private String orderId;

  @SerializedName("payment_type")
  private String paymentType;

  @SerializedName("acquirer")
  private String acquirer;

  @SerializedName("transaction_time")
  private String transactionTime;

  @SerializedName("transaction_status")
  private String transactionStatus;

  @SerializedName("expiry_time")
  private String expiryTime;

  @SerializedName("va_numbers")
  private VirtualNumber[] virtualNumbers;

  @SerializedName("permata_va_number")
  private String permataVaNumber;

  @SerializedName("actions")
  private List<Action> actions;

  public String getPermataVaNumber() {
    return permataVaNumber;
  }

  public String getQrCodeUrl() {
    if (actions != null) {
      for (Action action : actions) {
        if ("generate-qr-code".equals(action.getName())) {
          return action.getUrl();
        }
      }
    }
    return null;
  }

  public String getTypeApi() {
    return "apiMidtrans";
  }

  public void setTypeApi(String typeApi) {
    this.typeApi = typeApi;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public String getStatusMessage() {
    return statusMessage;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getPaymentType() {
    return paymentType;
  }

  public String getAcquirer() {
    return acquirer;
  }

  public String getTransactionTime() {
    return transactionTime;
  }

  public String getExpiryTime() {
    return expiryTime;
  }

  public String getTransactionStatus() {
    return transactionStatus;
  }

  // Model untuk Virtual Account Number (VA)
  public static class VirtualNumber {
    @SerializedName("bank")
    private String bank;

    @SerializedName("va_number")
    private String virtualNumber;

    public String getBank() {
      return bank;
    }

    public String getVirtualNumber() {
      return virtualNumber;
    }
  }

  public VirtualNumber[] getVirtualNumbers() {
    return virtualNumbers;
  }

  public List<Action> getAction() {
    return actions;
  }

  public static class Action {
    @SerializedName("name")
    private String name;

    @SerializedName("method")
    private String method;

    @SerializedName("url")
    private String url;

    public String getName() {
      return name;
    }

    public String getMethod() {
      return method;
    }

    public String getUrl() {
      return url;
    }
  }
}
