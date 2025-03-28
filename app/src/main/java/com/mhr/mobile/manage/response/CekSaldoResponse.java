package com.mhr.mobile.manage.response;

import com.google.gson.annotations.SerializedName;

public class CekSaldoResponse {
  @SerializedName("data")
  private Saldo saldo;
  
  public Saldo getSaldo() {
    return saldo;
  }

  public static class Saldo {
    @SerializedName("balance")
    private Double balance;

    @SerializedName("message")
    private String message;

    @SerializedName("rc")
    private String rc;

    public Double getBalance() {
      return balance;
    }
  }
}
