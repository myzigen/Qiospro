package com.mhr.mobile.manage.call;

import com.mhr.mobile.manage.request.MidtransTransaction;
import com.mhr.mobile.manage.response.CekSaldoResponse;
import com.mhr.mobile.manage.response.ListPascaResponse;
import com.mhr.mobile.manage.response.MidtransResponse;
import com.mhr.mobile.manage.response.PricelistDigiflazzResponse;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.manage.response.TransaksiDigiflazzResponse;
import com.mhr.mobile.manage.response.TransaksiIAKResponse;
import com.mhr.mobile.manage.response.TransaksiIAKResponseStatus;
import com.mhr.mobile.manage.response.WebhookResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Endpoint {
  @POST("pricelist")
  Call<PricelistResponse> getPricelist(@Body RequestBody body);

  @POST("api/top-up")
  Call<TransaksiIAKResponse> getTransaksi(@Body RequestBody body);

  @POST("api/check-status")
  Call<TransaksiIAKResponseStatus> getStatusTransaksi(@Body RequestBody body);

  // https://4899893a-0369-4565-b865-f8fc095c266b-00-2tqrfjhytks4t.sisko.replit.dev/callback/webhook.php
  @POST("callback/callback.php")
  Call<WebhookResponse> getWebhook(@Body RequestBody body);

  @POST("api/check-balance")
  Call<CekSaldoResponse> getCekSaldo(@Body RequestBody body);

  @Headers("Content-type: application/json")
  @POST("api/v1/bill/check/")
  Call<ListPascaResponse> getListPasca(@Body RequestBody body);

  // DIGIFLAZZ
  @POST("v1/price-list")
  Call<PricelistDigiflazzResponse> getPricelistDigiflazz(@Body RequestBody body);

  @POST("v1/transaction")
  Call<TransaksiDigiflazzResponse> getTransaksiDigiflazz(@Body RequestBody body);
  // Midtrans
  @Headers({"Accept: application/json", "Content-Type: application/json"})
  @POST("v2/charge")
  Call<MidtransResponse> getTransaction(@Header("Authorization") String authHeader, @Body MidtransTransaction transaction);
  
  @Multipart
  @POST("Qiospro/v1/data/upload_image.php")
  Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);
	  
}


/*
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("upload_image.php")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);
}
*/