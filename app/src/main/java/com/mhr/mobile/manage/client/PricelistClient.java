package com.mhr.mobile.manage.client;

import android.util.Log;
import com.google.gson.Gson;
import com.mhr.mobile.manage.call.Endpoint;
import com.mhr.mobile.manage.request.PricelistRequest;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.util.Config;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PricelistClient {
  Endpoint endpoint;

  private PricelistClient() {
    OkHttpClient client = new OkHttpClient.Builder().build();
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://prepaid.iak.id/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    endpoint = retrofit.create(Endpoint.class);
  }

  public void execute(
      PricelistRequest request,
      String commands,
      String username,
      String sign,
      String apiKey,
      PricelistRequest.RequestPricelistListener listener) {

    String jsonBody = new Gson().toJson(new PricelistRequest(commands, username, sign));
    Log.d("JSON RESPONSE", jsonBody);
    RequestBody rb =
        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    Call<PricelistResponse> call = endpoint.getPricelist(rb);
    call.enqueue(
        new Callback<PricelistResponse>() {
          @Override
          public void onResponse(
              Call<PricelistResponse> call, Response<PricelistResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
              request.activity.runOnUiThread(() -> listener.onResponse(response.body()));
            }
          }

          @Override
          public void onFailure(Call<PricelistResponse> call, Throwable t) {
            request.activity.runOnUiThread(() -> listener.onFailure(t.getLocalizedMessage()));
          }
        });
  }

  private static PricelistClient mInstance;

  public static synchronized PricelistClient getInstance() {
    if (mInstance == null) {
      mInstance = new PricelistClient();
    }
    return mInstance;
  }
}
