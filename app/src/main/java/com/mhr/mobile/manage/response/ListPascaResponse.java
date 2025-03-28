package com.mhr.mobile.manage.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ListPascaResponse {
  @SerializedName("data")
  private Data data;

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public static class Data {
    @SerializedName("pasca")
    private List<Pasca> pasca;

    public List<Pasca> getPasca() {
      return pasca;
    }

    public void setPasca(List<Pasca> pasca) {
      this.pasca = pasca;
    }
  }

  public static class Pasca {
    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private int status;

    @SerializedName("fee")
    private double fee;

    @SerializedName("komisi")
    private double komisi;

    @SerializedName("type")
    private String type;

    @SerializedName("category")
    private String category;

    private String logoUrl;

    public String getLogoUrl() {
      return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
      this.logoUrl = logoUrl;
    }

    public String getCode() {
      return code;
    }

    public String getName() {
      return name;
    }

    public int getStatus() {
      return status;
    }

    public double getFee() {
      return fee;
    }

    public double getKomisi() {
      return komisi;
    }

    public String getType() {
      return type;
    }

    public String getKategori() {
      return category;
    }
  }
}
