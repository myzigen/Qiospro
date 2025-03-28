package com.mhr.mobile.manage.client;

import android.util.Log;
import com.google.gson.Gson;
import com.mhr.mobile.manage.call.Endpoint;
import com.mhr.mobile.manage.request.TransaksiIAKRequest;
import com.mhr.mobile.manage.response.TransaksiIAKResponse;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransaksiIAKClient {

  private Endpoint endpoint;
  private OkHttpClient client;

  private TransaksiIAKClient() {
    client = new OkHttpClient.Builder().build();
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://prepaid.iak.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    endpoint = retrofit.create(Endpoint.class);
  }

  public void execute(
      TransaksiIAKRequest request,
      String username,
      String ref_id,
	  String customer_id,
      String product_code,
      String sign,
      String apikey,
      TransaksiIAKRequest.RequestTransaksiCallback listener) {
    listener.onStartRequest();
    String jsonBody =
        new Gson().toJson(new TransaksiIAKRequest(username, ref_id, customer_id, product_code, sign));
    Log.d("JSON RESPONSE TransaksiIAKClient", jsonBody);
    RequestBody rb =
        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    Call<TransaksiIAKResponse> call = endpoint.getTransaksi(rb);
    call.enqueue(
        new Callback<TransaksiIAKResponse>() {
          @Override
          public void onResponse(
              Call<TransaksiIAKResponse> call, Response<TransaksiIAKResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
              request.activity.runOnUiThread(() -> listener.onResponse(response.body().getData()));
            } else {
              request.activity.runOnUiThread(() -> listener.onFailure("Error " + response.errorBody().toString()));
            }
          }

          @Override
          public void onFailure(Call<TransaksiIAKResponse> call, Throwable t) {
            request.activity.runOnUiThread(() -> listener.onFailure(t.getLocalizedMessage()));
          }
        });
  }

  private static TransaksiIAKClient mInstance;

  public static synchronized TransaksiIAKClient getInstance() {
    if (mInstance == null) {
      mInstance = new TransaksiIAKClient();
    }
    return mInstance;
  }
}
