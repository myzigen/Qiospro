package com.mhr.mobile.manage.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PricelistResponse {
  @SerializedName("data")
  private Data data;

  public Data getData() {
    return data;
  }

  public static class Data implements Parcelable {
    @SerializedName("pricelist")
    private List<Product> pricelist;

    public List<Product> getPricelist() {
      return pricelist;
    }

    // Parcelable implementation
    protected Data(Parcel in) {
      pricelist = in.createTypedArrayList(Product.CREATOR);
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
      dest.writeTypedList(pricelist);
    }
  }

  public static class Product implements Parcelable {

    public Product() {}

    private String typeApi;

    @SerializedName("product_code")
    private String productCode;

    @SerializedName("product_description")
    private String productDescription;

    @SerializedName("product_nominal")
    private String productNominal;

    @SerializedName("product_details")
    private String productDetails;

    @SerializedName("product_price")
    private double productPrice;

    private double hargaJual;

    private double diskon;

    private double hargaDiskon;

    @SerializedName("product_type")
    private String productType;

    @SerializedName("active_period")
    private String activePeriod;

    @SerializedName("status")
    private String status;

    @SerializedName("icon_url")
    private String iconUrl;

    @SerializedName("product_category")
    private String productCategory;

    private String brand;

    // Getters

    public String getTypeApi() {
      return "apiIak";
    }


    public void setTypeApi(String typeApi) {
      this.typeApi = typeApi;
    }

    public String getProductCode() {
      return productCode;
    }

    public String getProductDescription() {
      return productDescription;
    }

    public String getProductNominal() {
      return productNominal;
    }

    public void setProductNominal(String code) {
      this.productNominal = code;
    }

    public String getProductDetails() {
      return productDetails;
    }

    public double getProductPrice() {
      return productPrice;
    }

    public void setProductPrice(double price) {
      this.productPrice = price;
    }

    public double getHargaJual() {
      return hargaJual;
    }

    public double getDiskon() {
      return diskon;
    }

    public double getHargaDiskon() {
      return hargaDiskon;
    }

    public String getProductType() {
      return productType;
    }

    public void setProductType(String productType) {
      this.productType = productType;
    }

    public String getActivePeriod() {
      return activePeriod;
    }

    public void setActivePeriod(String period) {
      this.activePeriod = period;
    }

    public String getStatus() {
      return status;
    }

    public String getIconUrl() {
      return iconUrl;
    }

    public String getProductCategory() {
      return productCategory;
    }

    public String getBrand() {
      return brand;
    }

    public void setBrand(String brand) {
      this.brand = brand;
    }

    // Parcelable implementation
    protected Product(Parcel in) {
      typeApi = in.readString();
      productCode = in.readString();
      productDescription = in.readString();
      productNominal = in.readString();
      productDetails = in.readString();
      productPrice = in.readDouble();
      hargaJual = in.readDouble();
      diskon = in.readDouble();
      hargaDiskon = in.readDouble();
      productType = in.readString();
      activePeriod = in.readString();
      status = in.readString();
      iconUrl = in.readString();
      productCategory = in.readString();
    }

    public static final Creator<Product> CREATOR =
        new Creator<Product>() {
          @Override
          public Product createFromParcel(Parcel in) {
            return new Product(in);
          }

          @Override
          public Product[] newArray(int size) {
            return new Product[size];
          }
        };

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(typeApi);
      dest.writeString(productCode);
      dest.writeString(productDescription);
      dest.writeString(productNominal);
      dest.writeString(productDetails);
      dest.writeDouble(productPrice);
      dest.writeDouble(hargaJual);
      dest.writeDouble(diskon);
      dest.writeDouble(hargaDiskon);
      dest.writeString(productType);
      dest.writeString(activePeriod);
      dest.writeString(status);
      dest.writeString(iconUrl);
      dest.writeString(productCategory);
    }
  }
}
