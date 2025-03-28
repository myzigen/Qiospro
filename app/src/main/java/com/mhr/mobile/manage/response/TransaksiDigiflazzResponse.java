package com.mhr.mobile.manage.response;

import com.google.gson.annotations.SerializedName;

public class TransaksiDigiflazzResponse {
  @SerializedName("data")
  private Data data;

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public static class Data {
    @SerializedName("type_api")
    private String typeApi;

    @SerializedName("ref_id")
    private String refId;

    @SerializedName("customer_no")
    private String customerNo;

    @SerializedName("buyer_sku_code")
    private String buyerSkuCode;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    @SerializedName("rc")
    private String rc;

    @SerializedName("sn")
    private String sn;

    @SerializedName("buyer_last_saldo")
    private float buyerLastSaldo;

    @SerializedName("price")
    private int price;

    @SerializedName("tele")
    private String telegram;

    @SerializedName("wa")
    private String wa;

    @SerializedName("timestamp")
    private long timestamp;

    // Constructor kosong
    public Data() {}

    // Getter and Setter
    public String getTypeApi() {
      return typeApi;
    }

    public void setTypeApi(String typeApi) {
      this.typeApi = typeApi;
    }

    public String getRef_id() {
      return refId;
    }

    public void setRefId(String ref_id) {
      this.refId = ref_id;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public String getStatusText() {
      return null;
    }

    public void setStatusText(String statusText) {}

    public long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
    }

    public String getRc() {
      return rc;
    }

    public void setRc(String rc) {
      this.rc = rc;
    }

    public String getProdukCode() {
      return buyerSkuCode;
    }

    public void setProdukCode(String product_code) {
      buyerSkuCode = product_code;
    }

    public String getCustomerId() {
      return customerNo;
    }

    public void setCustomerId(String customer_id) {
      this.customerNo = customer_id;
    }

    public int getHarga() {
      return price;
    }

    public void setHarga(int price) {
      this.price = price;
    }
  }
}
