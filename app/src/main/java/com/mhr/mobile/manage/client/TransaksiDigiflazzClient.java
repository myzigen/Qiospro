package com.mhr.mobile.manage.client;

import android.util.Log;
import com.google.gson.Gson;
import com.mhr.mobile.manage.call.Endpoint;
import com.mhr.mobile.manage.request.TransaksiDigiflazzRequest;
import com.mhr.mobile.manage.response.TransaksiDigiflazzResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransaksiDigiflazzClient {

  private Endpoint endpoint;

  private TransaksiDigiflazzClient() {
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
      TransaksiDigiflazzRequest request,
      String username,
      String buyer_sku_code,
      String customer_no,
      String ref_id,
      String sign,
      String apikey,
      int total,
      TransaksiDigiflazzRequest.RequestTransaksiCallback listener) {
    listener.onLoading();
    String jsonBody =
        new Gson()
            .toJson(
                new TransaksiDigiflazzRequest(
                    username, buyer_sku_code, customer_no, ref_id, sign, total, true));
    Log.d("JSON RESPONSE", jsonBody);
    RequestBody rb =
        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    Call<TransaksiDigiflazzResponse> call = endpoint.getTransaksiDigiflazz(rb);
    call.enqueue(
        new Callback<TransaksiDigiflazzResponse>() {
          @Override
          public void onResponse(
              Call<TransaksiDigiflazzResponse> call,
              Response<TransaksiDigiflazzResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
              request.activity.runOnUiThread(() -> listener.onResponse(response.body().getData()));
            }
          }

          @Override
          public void onFailure(Call<TransaksiDigiflazzResponse> call, Throwable t) {
            request.activity.runOnUiThread(() -> listener.onFailure(t.getLocalizedMessage()));
          }
        });
  }

  private static TransaksiDigiflazzClient mInstance;

  public static synchronized TransaksiDigiflazzClient getInstance() {
    if (mInstance == null) {
      mInstance = new TransaksiDigiflazzClient();
    }
    return mInstance;
  }
}
