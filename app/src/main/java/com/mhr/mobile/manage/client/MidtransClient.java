package com.mhr.mobile.manage.client;

import com.mhr.mobile.manage.call.Endpoint;
import com.mhr.mobile.manage.request.MidtransRequest;
import com.mhr.mobile.manage.request.MidtransTransaction;
import com.mhr.mobile.manage.response.MidtransResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MidtransClient {
  private Endpoint endpoint;
  private OkHttpClient client;
  private HttpLoggingInterceptor interceptor;

  private MidtransClient() {
    interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://api.sandbox.midtrans.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    endpoint = retrofit.create(Endpoint.class);
  }

  public void execute(
      MidtransRequest request,
      MidtransTransaction transaction,
      MidtransRequest.MidtransRequestCallback callback) {

    endpoint
        .getTransaction(request.getAuthHeader(), transaction)
        .enqueue(
            new Callback<MidtransResponse>() {
              @Override
              public void onResponse(
                  Call<MidtransResponse> call, Response<MidtransResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                  request.activity.runOnUiThread(() -> callback.onResponse(response.body()));
                } else {
                  request.activity.runOnUiThread(
                      () -> callback.onFailure("Response error: " + response.code()));
                }
              }

              @Override
              public void onFailure(Call<MidtransResponse> call, Throwable e) {
                request.activity.runOnUiThread(() -> callback.onFailure(e.getLocalizedMessage()));
              }
            });
  }

  private static MidtransClient mInstance;

  public static synchronized MidtransClient getInstance() {
    if (mInstance == null) {
      mInstance = new MidtransClient();
    }
    return mInstance;
  }
}
