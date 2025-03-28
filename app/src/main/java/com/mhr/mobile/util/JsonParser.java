package com.mhr.mobile.util;

import com.google.gson.Gson;
import com.mhr.mobile.model.ImageResponse;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

  public static List<ImageResponse.Image> parseJson(String json) {
    Gson gson = new Gson();
    // Parsing menjadi ImageResponse terlebih dahulu
    ImageResponse response = gson.fromJson(json, ImageResponse.class);

    // Mengonversi Map menjadi List
    List<ImageResponse.Image> imageList = new ArrayList<>(response.getImages().values());
    return imageList; // Mengembalikan daftar gambar
  }
}
