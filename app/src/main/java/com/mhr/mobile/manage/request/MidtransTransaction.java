package com.mhr.mobile.manage.request;

import com.google.gson.annotations.SerializedName;

public class MidtransTransaction {
  @SerializedName("transaction_details")
  private TransactionDetails transactionDetails;

  @SerializedName("bank_transfer")
  private BankTransfer bankTransfer;

  @SerializedName("qris")
  private Qris qris;

  @SerializedName("custom_expiry")
  private CustomExpiry customExpiry;

  @SerializedName("payment_type")
  private String paymentType;

  public MidtransTransaction() {}

  public void setMidtransTransactionDetails(String orderId, int grossAmount) {
    this.transactionDetails = new TransactionDetails(orderId, grossAmount);
  }

  public void setMidtransTransactionDuration(long duration, String unit) {
    this.customExpiry = new CustomExpiry(duration, unit);
  }

  public void setMidtransTransactionPaymentMethod(String paymentType, String bankName) {
    this.paymentType = paymentType;
    if (paymentType.equals("bank_transfer")) {
      this.bankTransfer = new BankTransfer(bankName);
    } else if (paymentType.equals("qris")) {
      this.qris = new Qris(bankName);
    }
  }

  public static class CustomExpiry {
    @SerializedName("expiry_duration")
    private long expiryDuration;

    @SerializedName("unit")
    private String unit;

    public CustomExpiry(long duration, String unit) {
      this.expiryDuration = duration;
      this.unit = unit;
    }
  }

  public static class TransactionDetails {
    @SerializedName("order_id")
    private String orderId;

    @SerializedName("gross_amount")
    private int grossAmount;

    public TransactionDetails(String orderId, int grossAmount) {
      this.orderId = orderId;
      this.grossAmount = grossAmount;
    }
  }

  public static class BankTransfer {
    @SerializedName("bank")
    private String bank;

    public BankTransfer(String bank) {
      this.bank = bank;
    }
  }

  public static class Qris {
    @SerializedName("acquirer")
    private String acquirer;

    public Qris(String acquirer) {
      this.acquirer = acquirer;
    }
  }
}
