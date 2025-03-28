package com.mhr.mobile.manage.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PricelistDigiflazzResponse {
  @SerializedName("data")
  private List<Data> data;

  public List<Data> getData() {
    return data;
  }

  public static class Data implements Parcelable {
    private int id;
    public int icon;

    private String typeApi;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("brand")
    private String brand;

    @SerializedName("type")
    private String type;

    @SerializedName("category")
    private String category;

    @SerializedName("price")
    private int price;

    private int hargaJual;

    private double diskon;

    private double hargaDiskon;

    @SerializedName("buyer_sku_code")
    private String buyerSkuCode;

    @SerializedName("multi")
    private String multi;

    @SerializedName("desc")
    private String description;

    @SerializedName("customer_name")
    private String customerName;

    public Data() {}

    // Constructor Parcelable
    protected Data(Parcel in) {
      typeApi = in.readString();
      id = in.readInt();
      icon = in.readInt();
      productName = in.readString();
      brand = in.readString();
      type = in.readString();
      category = in.readString();
      price = in.readInt();
      hargaJual = in.readInt();
      diskon = in.readDouble();
      hargaDiskon = in.readDouble();
      buyerSkuCode = in.readString();
      multi = in.readString();
      description = in.readString();
      customerName = in.readString();
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

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(typeApi);
      dest.writeInt(id);
      dest.writeInt(icon);
      dest.writeString(productName);
      dest.writeString(brand);
      dest.writeString(type);
      dest.writeString(category);
      dest.writeInt(price);
      dest.writeInt(hargaJual);
      dest.writeDouble(diskon);
      dest.writeDouble(hargaDiskon);
      dest.writeString(buyerSkuCode);
      dest.writeString(multi);
      dest.writeString(description);
      dest.writeString(customerName);
    }

    // Getters and Setters
    public String getTypeApi() {
      return "apiDigiflazz";
    }

    public void setTypeApi(String typeApi) {
      this.typeApi = typeApi;
    }

    public int getId() {
      return id;
    }

    public int getIcon() {
      return icon;
    }

    public void setIcon(int icon) {
      this.icon = icon;
    }

    public String getCustomerName() {
      return customerName;
    }

    public String getProductName() {
      return productName;
    }

    public String getBrand() {
      return brand;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public void setBrand(String brand) {
      this.brand = brand;
    }

    public String getCategory() {
      return category;
    }

    public int getPrice() {
      return price;
    }

    public int getHargaJual() {
      return hargaJual;
    }

    public double getDiskon() {
      return diskon;
    }

    public double getHargaDiskon() {
      return hargaDiskon;
    }

    public void setHargaDiskon(double hargaDiskon) {
      this.hargaDiskon = hargaDiskon;
    }

    public String getBuyerSkuCode() {
      return buyerSkuCode;
    }

    public String getMulti() {
      return multi;
    }

    public String getDescription() {
      return description;
    }
  }
}
