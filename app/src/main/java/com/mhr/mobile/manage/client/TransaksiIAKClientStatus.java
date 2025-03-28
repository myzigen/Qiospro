package com.mhr.mobile.manage.client;

import android.app.Activity;
import android.util.Log;
import com.google.gson.Gson;
import com.mhr.mobile.manage.call.Endpoint;
import com.mhr.mobile.manage.request.TransaksiIAKRequestStatus;
import com.mhr.mobile.manage.response.TransaksiIAKResponseStatus;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransaksiIAKClientStatus {
  Endpoint endpoint;
  private WebSocket webSocket;
  private OkHttpClient client;

  private TransaksiIAKClientStatus() {
    client = new OkHttpClient.Builder().build();
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://prepaid.iak.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    endpoint = retrofit.create(Endpoint.class);
  }

  // Metode untuk memulai koneksi WebSocket
  public void connectWebSocket(String url, WebSocketListener listener) {
    Request request =
        new Request.Builder()
            .url(url) // Ganti dengan URL WebSocket yang sesuai
            .build();
    webSocket = client.newWebSocket(request, listener);
  }

  // Metode untuk mengirim pesan melalui WebSocket
  public void sendMessage(String message) {
    if (webSocket != null) {
      webSocket.send(message);
    } else {
      Log.e("WebSocket", "WebSocket is not connected!");
    }
  }

  // Metode untuk menutup WebSocket
  public void closeWebSocket() {
    if (webSocket != null) {
      webSocket.close(1000, "Closing WebSocket");
      webSocket = null;
    }
  }

  public void execute(
      TransaksiIAKRequestStatus request,
      String username,
      String ref_id,
      String sign,
      String apiKey,
      int tr_id,
      String customer_id,
      TransaksiIAKRequestStatus.RequestStatusTransaksi listener) {
    listener.onStartRequest();
    String jsonBody = new Gson().toJson(new TransaksiIAKRequestStatus(username, ref_id, sign, tr_id, customer_id)); 
    Log.d("JSON RESPONSE TRANSAKSI IAK CLIENT STATUS", jsonBody);
    RequestBody rb =
        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
    Call<TransaksiIAKResponseStatus> call = endpoint.getStatusTransaksi(rb);
    call.enqueue(
        new Callback<TransaksiIAKResponseStatus>() {
          @Override
          public void onResponse(
              Call<TransaksiIAKResponseStatus> call,
              Response<TransaksiIAKResponseStatus> response) {
            if (response.isSuccessful() && response.body() != null) {
              request.activity.runOnUiThread(() -> listener.onResponse(response.body().getData()));
            }
          }

          @Override
          public void onFailure(Call<TransaksiIAKResponseStatus> call, Throwable t) {    
              request.activity.runOnUiThread(() -> listener.onFailure(t.getLocalizedMessage()));         
          }
        });
  }

  private static TransaksiIAKClientStatus mInstance;

  public static synchronized TransaksiIAKClientStatus getInstance() {
    if (mInstance == null) {
      mInstance = new TransaksiIAKClientStatus();
    }
    return mInstance;
  }
}
