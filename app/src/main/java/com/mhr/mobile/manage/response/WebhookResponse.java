package com.mhr.mobile.manage.response;

import com.google.gson.annotations.SerializedName;

public class WebhookResponse {

  @SerializedName("data")
  private Data data; // Kunci 'data' yang mengandung informasi lain

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public static class Data {
    @SerializedName("ref_id")
    private String ref_id;

    @SerializedName("status")
    private double status;

    @SerializedName("message")
    private String message;

    @SerializedName("sn")
    private String sn;

    @SerializedName("timestamp")
    private String timestamp;

    public Data() {}

    public Data(String ref_id, double status, String message, String timestamp, String sn) {
      this.ref_id = ref_id;
      this.status = status;
      this.message = message;
      this.timestamp = timestamp;
      this.sn = sn;
    }

    // Getter and Setter methods
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

    public String getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(String timestamp) {
      this.timestamp = timestamp;
    }
  }
}
