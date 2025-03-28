package com.mhr.mobile.util;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetJsonReader {

  public static String loadJSONFromAsset(Context context, String fileName) {
    StringBuilder json = new StringBuilder();

    try {
      // Membuka file JSON dari assets
      InputStream is = context.getAssets().open(fileName);
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));

      String line;
      while ((line = reader.readLine()) != null) {
        json.append(line);
      }

      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return json.toString();
  }
}
