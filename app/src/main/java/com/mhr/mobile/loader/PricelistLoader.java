package com.mhr.mobile.loader;

import android.app.Activity;
import android.util.Log;
import com.mhr.mobile.manage.request.ListPascaRequest;
import com.mhr.mobile.manage.request.PricelistDigiflazzRequest;
import com.mhr.mobile.manage.request.PricelistRequest;
import com.mhr.mobile.manage.response.ListPascaResponse;
import com.mhr.mobile.manage.response.PricelistResponse;
import com.mhr.mobile.util.Config;
import java.util.ArrayList;
import java.util.List;

public class PricelistLoader {

  public void loadPricelistDigiflazz(Activity activity) {
	  PricelistDigiflazzRequest request = new PricelistDigiflazzRequest(activity);
	  request.setUsername();
	  request.setApikey();  
  }

  public static void loadPricePasca(Activity activity, String type, LoaderExecutePasca executePasca) {
    executePasca.onStartLoading();
    ListPascaRequest request = new ListPascaRequest(activity);
    request.setUserName(Config.USERNAME);
    request.setApiKey("665673a451e6ff89nTx0");

    ListPascaRequest.RequestListPascaCallback newRequest =
        new ListPascaRequest.RequestListPascaCallback() {
          @Override
          public void onResponse(List<ListPascaResponse.Pasca> pascaList) {
            // Tidak perlu pengecekan terhadap `ListPascaResponse` lagi
            if (pascaList != null && !pascaList.isEmpty()) {
              // Filter produk berdasarkan kategori "postpaid"
              List<ListPascaResponse.Pasca> filteredProducts = new ArrayList<>();

              for (ListPascaResponse.Pasca product : pascaList) {
                if (type != null && product.getType() != null && type.equals(product.getType())) {
                  filteredProducts.add(product); // Menambahkan produk yang sesuai kategori
                }
              }

              // Mengecek apakah ada produk yang cocok dengan kategori "postpaid"
              if (!filteredProducts.isEmpty()) {
                executePasca.onSucces(filteredProducts); // Kirim data yang sudah difilter ke callback
              } else {
                Log.e("PricelistResponse", "No matching products found.");
                executePasca.onFailure("No matching products found.");
              }

            } else {
              Log.e("PricelistResponse", "No products found in response.");
              executePasca.onFailure("No products found in response.");
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            Log.e("API Error", "Request failed: " + errorMessage);
            executePasca.onFailure(errorMessage); // Menangani kegagalan API
          }
        };

    // Mulai request
    request.startRequestListPasca(newRequest);
  }

  public static void loadPricelist(Activity context, String category, LoaderExecute execute) {
    // Make the API request
    execute.onStartLoading();
    PricelistRequest request = new PricelistRequest(context);
    request.setUserName();
    request.setApiKey();

    PricelistRequest.RequestPricelistListener newRequest =
        new PricelistRequest.RequestPricelistListener() {
          @Override
          public void onResponse(Object response) {
            if (response instanceof PricelistResponse) {
              PricelistResponse pricelistResponse = (PricelistResponse) response;
              if (pricelistResponse.getData() != null && pricelistResponse.getData().getPricelist() != null) {

                List<PricelistResponse.Product> products = pricelistResponse.getData().getPricelist();
                List<PricelistResponse.Product> filteredProducts = new ArrayList<>();
                for (PricelistResponse.Product product : products) {
                  if (category != null && product.getProductCategory() != null && category.equals(product.getProductCategory())) {
                    filteredProducts.add(product); // Filter the products by category
                  }
                }

                execute.onSucces(filteredProducts); // Kirim data ke callback

              } else {
                Log.e("PricelistResponse", "No products found in response.");
              }
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            Log.e("PricelistResponse", "Request failed: " + errorMessage);
          }
        };

    request.startRequestPricelist(newRequest); // Start the request
  }

  public interface LoaderExecute {
    public void onStartLoading();

    public void onSucces(List<PricelistResponse.Product> produk);
  }

  public interface LoaderExecutePasca {
    public void onStartLoading();

    public void onSucces(List<ListPascaResponse.Pasca> produk);

    public void onFailure(String error);
  }
}
