package com.mhr.mobile.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mhr.mobile.manage.response.TransaksiIAKResponseStatus;

public class NotifStatusService /*extends Service*/ {
	/*

  private DatabaseReference db;
  private FirebaseAuth auth;

  @Override
  public void onCreate() {
    super.onCreate();
    db = FirebaseDatabase.getInstance().getReference("transaksi");
    auth = FirebaseAuth.getInstance();
  }

  private void listenUpdate(String userId, int transaksiId) {
    db.child(userId)
        .child(String.valueOf(transaksiId))
        .addValueEventListener(
            new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot snapshot) {
                TransaksiIAKResponseStatus.Data data = snapshot.getValue(TransaksiIAKResponseStatus.Data.class);
                if (data != null) {
                  showSuccessNotification(data);
                }
              }

              @Override
              public void onCancelled(DatabaseError error) {}
            });
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int arg1, int arg2) {
    String userId = auth.getCurrentUser().getUid();
    int transaksiId = intent.getIntExtra("trId", -1);
    listenUpdate(userId, transaksiId);
    return START_STICKY;
  }

  private void showSuccessNotification(TransaksiIAKResponseStatus.Data data) {
    NotificationStatusTransaction.showNotification(
        getApplicationContext(), "Status ", "" + data.getStatus());
  }
  */
}
