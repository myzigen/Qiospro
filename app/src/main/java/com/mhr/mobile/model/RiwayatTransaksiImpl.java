package com.mhr.mobile.model;


public class RiwayatTransaksiImpl {
  /*
  @SerializedName("type_api")
  private String type_api;

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

  private int priceI;

  @SerializedName("message")
  private String message;

  @SerializedName("balance")
  private double balance;

  @SerializedName("statusText")
  private String statusText;

  @SerializedName("timestamp")
  private long timestamp;

  @SerializedName("rc")
  private String rc;

  @SerializedName("tr_id")
  private int tr_id;

  public RiwayatTransaksiImpl(RiwayatTransaksi data) {
    this.type_api = data.getTypeApi();
    this.ref_id = data.getRef_id();
    this.status = data.getStatus(); // Default
    this.product_code = data.getProdukCode();
    this.customer_id = data.getCustomerId();
    this.priceI = data.getHarga();
    this.price = data.getHargaDouble();
    this.message = data.getMessage();
    this.balance = data.getBalance();
    this.timestamp = data.getTimestamp(); // Bisa disesuaikan
    this.rc = data.getRc();
    this.statusText = data.getStatusText(); // Contoh status
    this.tr_id = data.getTrId(); // Default
  }

  // Implementasi method RiwayatTransaksi
  @Override
  public String getTypeApi() {
    return type_api;
  }

  @Override
  public void setTypeApi(String type_api) {
    this.type_api = type_api;
  }

  @Override
  public int getTrId() {
    return tr_id;
  }

  @Override
  public void setTrId(int tr_id) {
    this.tr_id = tr_id;
  }

  @Override
  public String getRef_id() {
    return ref_id;
  }

  @Override
  public void setRefId(String ref_id) {
    this.ref_id = ref_id;
  }

  @Override
  public double getStatus() {
    return status;
  }

  @Override
  public void setStatus(double status) {
    this.status = status;
  }

  @Override
  public String getStatusDigiflazz() {
    return null;
  }

  @Override
  public void setStatusDigiflazz(String status) {}

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public double getBalance() {
    return balance;
  }

  @Override
  public void setBalance(double balance) {
    this.balance = balance;
  }

  @Override
  public String getStatusText() {
    return statusText;
  }

  @Override
  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }

  @Override
  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String getRc() {
    return rc;
  }

  @Override
  public void setRc(String rc) {
    this.rc = rc;
  }

  @Override
  public String getProdukCode() {
    return product_code;
  }

  @Override
  public void setProdukCode(String product_code) {
    this.product_code = product_code;
  }

  @Override
  public String getCustomerId() {
    return customer_id;
  }

  @Override
  public void setCustomerId(String customer_id) {
    this.customer_id = customer_id;
  }

  @Override
  public int getHarga() {
    return priceI;
  }

  @Override
  public void setHarga(int priceI) {
   priceI = priceI;
  }

  @Override
  public double getHargaDouble() {
    return price;
  }

  @Override
  public void setHargaDouble(double price) {
    this.price = price;
  }

  // Parcelable Implementation
  protected RiwayatTransaksiImpl(Parcel in) {
    type_api = in.readString();
    ref_id = in.readString();
    status = in.readDouble();
    product_code = in.readString();
    customer_id = in.readString();
    price = in.readDouble();
    message = in.readString();
    balance = in.readDouble();
    tr_id = in.readInt();
    rc = in.readString();
    timestamp = in.readLong();
    statusText = in.readString();
  }

  public static final Creator<RiwayatTransaksiImpl> CREATOR =
      new Creator<RiwayatTransaksiImpl>() {
        @Override
        public RiwayatTransaksiImpl createFromParcel(Parcel in) {
          return new RiwayatTransaksiImpl(in);
        }

        @Override
        public RiwayatTransaksiImpl[] newArray(int size) {
          return new RiwayatTransaksiImpl[size];
        }
      };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(type_api);
    dest.writeInt(tr_id);
    dest.writeDouble(status);
    dest.writeString(product_code);
    dest.writeString(customer_id);
    dest.writeDouble(price);
    dest.writeString(message);
    dest.writeDouble(balance);
    dest.writeString(ref_id);
    dest.writeString(rc);
    dest.writeLong(timestamp);
    dest.writeString(statusText);
  }
  */
}
