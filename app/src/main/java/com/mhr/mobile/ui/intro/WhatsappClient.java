package com.mhr.mobile.ui.intro;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WhatsappClient {
  private static final String BASE_URL = "https://graph.facebook.com/";

  private static Retrofit retrofit;

  public static Retrofit getClient() {
    if (retrofit == null) {
      retrofit =
          new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
    }
    return retrofit;
  }
}
