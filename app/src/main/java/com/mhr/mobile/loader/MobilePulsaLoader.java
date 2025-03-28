package com.mhr.mobile.loader;

import android.app.Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mhr.mobile.manage.response.PricelistResponse;
import java.util.ArrayList;
import java.util.List;

public class MobilePulsaLoader {
  private final Activity activity;
  private final DatabaseReference db;

  public MobilePulsaLoader(Activity activity) {
    this.activity = activity;
    this.db = FirebaseDatabase.getInstance().getReference("mobile_pulsa_pricelist");
  }

  public void getDataFromFirebase(String category, String brand, LoaderExecute execute) {
    execute.onStartLoading();
    db.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot snapshot) {
            List<PricelistResponse.Product> mData = new ArrayList<>();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
              PricelistResponse.Product data =
                  dataSnapshot.getValue(PricelistResponse.Product.class);

              if (data != null
                  && data.getProductCategory() != null
                  && data.getProductDescription() != null
                  && data.getProductCategory().equalsIgnoreCase(category)
                  && data.getProductDescription().equalsIgnoreCase(brand)) {
                mData.add(data);
              }
            }

            execute.onSucces(mData);
          }

          @Override
          public void onCancelled(DatabaseError error) {}
        });
  }

  public interface LoaderExecute {
    public void onStartLoading();

    public void onSucces(List<PricelistResponse.Product> produk);

    public void onFailure(String error);
  }
}
