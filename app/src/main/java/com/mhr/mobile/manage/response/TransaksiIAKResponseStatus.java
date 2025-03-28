package com.mhr.mobile.manage.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class TransaksiIAKResponseStatus {
  @SerializedName("data")
  public Data data;

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public static class Data implements Parcelable {
    @SerializedName("ref_id")
    private String ref_id;

    @SerializedName("status")
    private double status;

    @SerializedName("product_code")
    private String product_code;

    @SerializedName("customer_id")
    private String customer_id;

    @SerializedName("price")
    private double price;

    @SerializedName("message")
    private String message;

    @SerializedName("sn")
    private String sn;

    @SerializedName("balance")
    private double balance;

    @SerializedName("tr_id")
    private int tr_id;

    @SerializedName("rc")
    private String rc;

    public Data() {}

    public String getRefId() {
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

    public String getProductCode() {
      return product_code;
    }

    public void setProductCode(String product_code) {
      this.product_code = product_code;
    }

    public String getCustomerId() {
      return customer_id;
    }

    public void setCustomerId(String customer_id) {
      this.customer_id = customer_id;
    }

    public double getPrice() {
      return price;
    }

    public void setPrice(double price) {
      this.price = price;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public String getSn() {
      return sn;
    }

    public void setSn(String sn) {
      this.sn = sn;
    }

    public double getBalance() {
      return balance;
    }

    public void setBalance(double balance) {
      this.balance = balance;
    }

    public int getTrId() {
      return tr_id;
    }

    public void setTrId(int tr_id) {
      this.tr_id = tr_id;
    }

    public String getRc() {
      return rc;
    }

    public void setRc(String rc) {
      this.rc = rc;
    }

    // Parcelable implementation for the Data class
    protected Data(Parcel in) {
      ref_id = in.readString();
      status = in.readDouble();
      product_code = in.readString();
      customer_id = in.readString();
      price = in.readDouble();
      message = in.readString();
      sn = in.readString();
      balance = in.readDouble();
      tr_id = in.readInt();
      rc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(ref_id);
      dest.writeDouble(status);
      dest.writeString(product_code);
      dest.writeString(customer_id);
      dest.writeDouble(price);
      dest.writeString(message);
      dest.writeString(sn);
      dest.writeDouble(balance);
      dest.writeInt(tr_id);
      dest.writeString(rc);
    }

    @Override
    public int describeContents() {
      return 0;
    }

    public static final Creator<Data> CREATOR =
        new Creator<Data>() {
          @Override
          public Data createFromParcel(Parcel in) {
            return new Data(in);
          }

          @Override
          public Data[] newArray(int size) {
            return new Data[size];
          }
        };
  }
}
