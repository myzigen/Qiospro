package com.mhr.mobile.manage.client;

import android.util.Log;
import com.google.gson.Gson;
import com.mhr.mobile.manage.call.Endpoint;
import com.mhr.mobile.manage.request.ListPascaRequest;
import com.mhr.mobile.manage.response.ListPascaResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListPascaClient {
  private Endpoint endpoint;

  private ListPascaClient() {
    OkHttpClient client = new OkHttpClient.Builder().build();
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://mobilepulsa.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    endpoint = retrofit.create(Endpoint.class);
  }

  public void execute(
      ListPascaRequest request,
      String commands,
      String username,
      String sign,
      String status,
      String apikey,
      ListPascaRequest.RequestListPascaCallback callback) {

    // Menggunakan langsung ListPascaRequest untuk membuat JSON body
    String jsonBody = new Gson().toJson(new ListPascaRequest(commands,username,sign,status));
    Log.d("JSON RESPONSE", jsonBody);
    RequestBody rb =
        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    Call<ListPascaResponse> call = endpoint.getListPasca(rb);

    call.enqueue(
        new Callback<ListPascaResponse>() {
          @Override
          public void onResponse(
              Call<ListPascaResponse> call, Response<ListPascaResponse> response) {
            Log.d("API Response", "onResponse called");
            if (response.isSuccessful() && response.body() != null) {
              Log.d("API Response", "Response is successful");
              callback.onResponse(response.body().getData().getPasca());
            } else {
              try {
                if (response.errorBody() != null) {
                  Log.e("API Response", "Error: " + response.errorBody().string());
                }
              } catch (Exception e) {
                Log.e("API Response", "Error parsing error body", e);
              }
              callback.onFailure("Error: " + response.code());
            }
          }

          @Override
          public void onFailure(Call<ListPascaResponse> call, Throwable t) {
            Log.e("API Error", "Request failed: ", t);
            callback.onFailure("Network error: " + t.getMessage());
          }
        });
  }

  private static ListPascaClient mInstance;

  public static synchronized ListPascaClient getInstance() {
    if (mInstance == null) {
      mInstance = new ListPascaClient();
    }
    return mInstance;
  }
}
