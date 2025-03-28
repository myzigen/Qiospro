package com.mhr.mobile.manage.response;

import com.google.gson.annotations.SerializedName;

public class TransaksiIAKResponse {
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
    private String type_api;

    @SerializedName("ref_id")
    private String ref_id;

    @SerializedName("status")
    double status;

    @SerializedName("product_code")
    String produk_code;

    @SerializedName("customer_id")
    String customer_id;

    @SerializedName("price")
    double price;

    @SerializedName("message")
    String message;

    @SerializedName("balance")
    double balance;

    @SerializedName("tr_id")
    int tr_id;

    @SerializedName("rc")
    String rc;

    @SerializedName("timestamp")
    private long timestamp;

    private String statusText;

    private String brand;

    // Constructor kosong (diperlukan untuk Gson dan inisialisasi manual)
    public Data() {}

    // Getter Setter

    public String getTypeApi() {
      return type_api;
    }

    public void setTypeApi(String type_api) {
      this.type_api = type_api;
    }

    public int getTrId() {
      return tr_id;
    }

    public void setTrId(int tr_id) {
      this.tr_id = tr_id;
    }

    public String getRefId() {
      return ref_id;
    }

    public String getRef_id() {
      return ref_id;
    }

    public void setRefId(String ref_id) {
      this.ref_id = ref_id;
    }

    public double getStatus() {
      return status;
    }

    public void setStatus(double status) {
      this.status = status;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public double getBalance() {
      return balance;
    }

    public void setBalance(double balance) {
      this.balance = balance;
    }

    public String getStatusText() {
      return statusText;
    }

    public void setStatusText(String statusText) {
      this.statusText = statusText;
    }

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
      return produk_code;
    }

    public void setProdukCode(String product_code) {
      this.produk_code = product_code;
    }

    public String getCustomerId() {
      return customer_id;
    }

    public void setCustomerId(String customer_id) {
      this.customer_id = customer_id;
    }

    public int getHarga() {
      return 0;
    }

    public void setHarga(int price) {}

    public double getHargaDouble() {
      return price;
    }

    public void setHargaDouble(double price) {
      this.price = price;
    }

    public String getStatusDigiflazz() {
      return null;
    }

    public void setStatusDigiflazz(String status) {}

    public String getBrand() {
      return brand;
    }

    public void setBrand(String brand) {
      this.brand = brand;
    }
  }
}
