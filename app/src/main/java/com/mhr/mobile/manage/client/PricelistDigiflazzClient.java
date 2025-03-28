package com.mhr.mobile.manage.client;

import android.util.Log;
import com.google.gson.Gson;
import com.mhr.mobile.manage.call.Endpoint;
import com.mhr.mobile.manage.request.PricelistDigiflazzRequest;
import com.mhr.mobile.manage.response.PricelistDigiflazzResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PricelistDigiflazzClient {

  private Endpoint endpoint;

  private PricelistDigiflazzClient() {
    OkHttpClient client = new OkHttpClient.Builder().build();
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://api.digiflazz.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    endpoint = retrofit.create(Endpoint.class);
  }

  public void execute(
      PricelistDigiflazzRequest request,
      String cmd,
      String username,
      String sign,
      String apikey,
      PricelistDigiflazzRequest.RequestPricelistListener listener) {
    listener.onStartLoading();
    String jsonBody = new Gson().toJson(new PricelistDigiflazzRequest(cmd, username, sign));
    Log.d("JSON RESPONSE", jsonBody);
    RequestBody rb =
        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    Call<PricelistDigiflazzResponse> call = endpoint.getPricelistDigiflazz(rb);
    call.enqueue(
        new Callback<PricelistDigiflazzResponse>() {
          @Override
          public void onResponse(
              Call<PricelistDigiflazzResponse> call,
              Response<PricelistDigiflazzResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
              request.activity.runOnUiThread(() -> listener.onResponse(response.body().getData()));
            }
          }

          @Override
          public void onFailure(Call<PricelistDigiflazzResponse> call, Throwable t) {
            request.activity.runOnUiThread(() -> listener.onFailure(t.getLocalizedMessage()));
          }
        });
  }

  private static PricelistDigiflazzClient mInstance;

  public static synchronized PricelistDigiflazzClient getInstance() {
    if (mInstance == null) {
      mInstance = new PricelistDigiflazzClient();
    }
    return mInstance;
  }
}
