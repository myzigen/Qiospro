package com.mhr.mobile.manage.client;

import android.util.Log;
import com.google.gson.Gson;
import com.mhr.mobile.manage.call.Endpoint;
import com.mhr.mobile.manage.request.CekSaldoRequest;
import com.mhr.mobile.manage.response.CekSaldoResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CekSaldoClient {
  Endpoint endpoint;

  private CekSaldoClient() {
    OkHttpClient client = new OkHttpClient.Builder().build();
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://prepaid.iak.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    endpoint = retrofit.create(Endpoint.class);
  }

  public void execute(
      CekSaldoRequest request,
      String username,
      String sign,
      String key,
      CekSaldoRequest.RequestSaldoCallback listener) {
    listener.onStartLoading();
    String jsonBody = new Gson().toJson(new CekSaldoRequest(username, sign));
    Log.d("JSON RESPONSE", jsonBody);
    RequestBody rb =
        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    Call<CekSaldoResponse> call = endpoint.getCekSaldo(rb);
    call.enqueue(
        new Callback<CekSaldoResponse>() {
          @Override
          public void onResponse(Call<CekSaldoResponse> call, Response<CekSaldoResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
              request.activity.runOnUiThread(() -> listener.onResponse(response.body()));
            }
          }

          @Override
          public void onFailure(Call<CekSaldoResponse> call, Throwable t) {
            request.activity.runOnUiThread(() -> listener.onFailure(t.getLocalizedMessage()));
          }
        });
  }

  private static CekSaldoClient mInstance;

  public static synchronized CekSaldoClient getInstance() {
    if (mInstance == null) {
      mInstance = new CekSaldoClient();
    }
    return mInstance;
  }
}
