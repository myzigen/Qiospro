package com.mhr.mobile.loader;

import android.app.Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mhr.mobile.manage.response.PricelistDigiflazzResponse;
import java.util.ArrayList;
import java.util.List;

public class DigiflazzLoader {
  private final Activity activity;
  private final DatabaseReference db;

  public DigiflazzLoader(Activity activity) {
    this.activity = activity;
    this.db = FirebaseDatabase.getInstance().getReference("digiflazz_pricelist");
  }

  public void getDataFromFirebase(String categori, LoaderExecute execute) {
    db.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot snapshot) {
            List<PricelistDigiflazzResponse.Data> mData = new ArrayList<>();

            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
              PricelistDigiflazzResponse.Data data =
                  dataSnapshot.getValue(PricelistDigiflazzResponse.Data.class);

              if (data != null && data.getCategory().equalsIgnoreCase(categori)) {
                mData.add(data);
              }
            }

            execute.onSuccess(mData);
          }

          @Override
          public void onCancelled(DatabaseError error) {
            execute.onError(error.getMessage());
          }
        });
  }

  public interface LoaderExecute {
    void onSuccess(List<PricelistDigiflazzResponse.Data> dataList);

    void onError(String errorMessage);
  }
}
