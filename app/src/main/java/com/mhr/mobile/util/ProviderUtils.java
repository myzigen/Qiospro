package com.mhr.mobile.util;

import android.widget.ImageView;
import com.mhr.mobile.R;
import java.util.HashMap;
import java.util.Map;

public class ProviderUtils {

  public static String getProviderName(int position) {
    switch (position) {
      case 0:
        return "TELKOMSEL";
      case 1:
        return "INDOSAT";
      case 2:
        return "AXIS";
      case 3:
        return "XL";
      case 4:
        return "TRI";
      case 5:
        return "BY.U";
      default:
        return "";
    }
  }

  public static void updateProviderIcon(ImageView img, String provider) {
    int providerIcon = getProviderIconResId(provider);
	img.setImageResource(providerIcon);
  }
  
  public static int getProviderIconResId(String provider) {
    switch (provider.toLowerCase()) {
        case "telkomsel":
            return R.drawable.provider_telkomsel;
        case "axis":
            return R.drawable.provider_axis;
        case "xl":
            return R.drawable.provider_xl;
        case "indosat":
            return R.drawable.provider_indosat;
        case "tri":
            return R.drawable.provider_tri;
        case "smartfren":
            return R.drawable.provider_smartfren;
        default:
            return R.drawable.simcard; // Ikon default
    }
}

  public static String detectProvider(String nomor) {
    // Peta prefix ke nama provider
    Map<String, String> prefixToProvider = new HashMap<>();
    prefixToProvider.put("0812", "Telkomsel");
    prefixToProvider.put("0813", "Telkomsel");
    prefixToProvider.put("0852", "Telkomsel");
    prefixToProvider.put("0853", "Telkomsel");
    prefixToProvider.put("0821", "Telkomsel");
    prefixToProvider.put("0823", "Telkomsel");
    prefixToProvider.put("0822", "Telkomsel");
    prefixToProvider.put("0851", "Telkomsel");

    prefixToProvider.put("0814", "Indosat");
    prefixToProvider.put("0815", "Indosat");
    prefixToProvider.put("0816", "Indosat");
    prefixToProvider.put("0855", "Indosat");
    prefixToProvider.put("0856", "Indosat");
    prefixToProvider.put("0857", "Indosat");
    prefixToProvider.put("0858", "Indosat");

    prefixToProvider.put("0817", "Xl");
    prefixToProvider.put("0818", "Xl");
    prefixToProvider.put("0819", "Xl");
    prefixToProvider.put("0859", "Xl");
    prefixToProvider.put("0878", "Xl");
    prefixToProvider.put("0877", "Xl");

    prefixToProvider.put("0838", "Axis");
    prefixToProvider.put("0837", "Axis");
    prefixToProvider.put("0831", "Axis");
    prefixToProvider.put("0832", "Axis");

    prefixToProvider.put("0881", "Smartfren");
    prefixToProvider.put("0882", "Smartfren");
    prefixToProvider.put("0883", "Smartfren");
    prefixToProvider.put("0884", "Smartfren");
    prefixToProvider.put("0885", "Smartfren");
    prefixToProvider.put("0886", "Smartfren");
    prefixToProvider.put("0887", "Smartfren");
    prefixToProvider.put("0888", "Smartfren");

    prefixToProvider.put("0896", "Tri");
    prefixToProvider.put("0897", "Tri");
    prefixToProvider.put("0898", "Tri");
    prefixToProvider.put("0899", "Tri");
    prefixToProvider.put("0895", "Tri");

    prefixToProvider.put("085154", "by.U");
    prefixToProvider.put("085155", "by.U");
    prefixToProvider.put("085156", "by.U");
    prefixToProvider.put("085157", "by.U");
    prefixToProvider.put("085158", "by.U");

    // Cari provider berdasarkan prefix
    for (String prefix : prefixToProvider.keySet()) {
      if (nomor.startsWith(prefix)) {
        return prefixToProvider.get(prefix);
      }
    }
    return "Unknown";
  }
}
