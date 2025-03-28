package com.mhr.mobile.util;

import android.app.Activity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class AndroidUtilities {

    public static void getLocationFromIP(Activity activity, AndroidUtilitiesLocation callback) {
        new Thread(() -> {
            try {
                URL url = new URL("http://ip-api.com/json/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(response.toString());
                double latitude = json.getDouble("lat");
                double longitude = json.getDouble("lon");

                activity.runOnUiThread(() -> callback.onDataLocation(latitude, longitude));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public interface AndroidUtilitiesLocation {
        void onDataLocation(double latitude, double longitude);
    }
}