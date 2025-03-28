package com.mhr.mobile.loader;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mhr.mobile.model.RiwayatTransaksi;
import com.mhr.mobile.ui.intro.UserLogin;
import com.mhr.mobile.util.AndroidViews;
import java.util.ArrayList;
import java.util.List;

public class QiosFirebaseHelper {
  private static String TAG = QiosFirebaseHelper.class.getSimpleName();
  private DatabaseReference db;
  private FirebaseAuth auth, authLogin;
  private FirebaseUser user;
  private String username;
  private Activity activity;

  public QiosFirebaseHelper(Activity activity) {
    this.activity = activity;
    authLogin = FirebaseAuth.getInstance();
  }

  public QiosFirebaseHelper(String dbName) {
    auth = FirebaseAuth.getInstance();
    user = auth.getCurrentUser();

    if (user != null && user.getDisplayName() != null) {
      username = user.getDisplayName();
    } else {
      username = "John Doe";
    }

    db = FirebaseDatabase.getInstance().getReference(dbName).child(username);
  }

  public void getLogin(String email, String password, FirebaseLoginCallback callback) {
    callback.onStartLogin();
    authLogin
        .signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                callback.onDataLogin();
              } else {
                callback.onFailure(task.getException().getLocalizedMessage());
              }
            });
  }

  public void getDeleteAkun(View v) {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userId = authLogin.getCurrentUser().getUid();
    firestore
        .collection("users")
        .document(userId)
        .delete()
        .addOnSuccessListener(
            aVoid -> {
              AndroidViews.showSnackbar(v, "Berhasil Di Hapus");
            })
        .addOnFailureListener(e -> {});
  }

  public void getLogout() {
    new MaterialAlertDialogBuilder(activity)
        .setTitle("Konfirmasi Logout")
        .setMessage("Apakah Anda Yakin Ingin Keluar?")
        .setPositiveButton(
            "Ya",
            (dialog, which) -> {
              authLogin.signOut();
              Intent intent = new Intent(activity, UserLogin.class);
              activity.startActivity(intent);
              activity.finish();
            })
        .setNegativeButton(
            "Batal",
            (dialog, which) -> {
              dialog.dismiss();
            })
        .show();
  }

  public void getDataTransaksi(FirebaseCallback callback) {
    db.keepSynced(true);
    callback.onStartRequest();
    db.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot snapshot) {
            List<RiwayatTransaksi> combineDataList = new ArrayList<>();
            for (DataSnapshot dataSnap : snapshot.getChildren()) {
              RiwayatTransaksi riwayat = dataSnap.getValue(RiwayatTransaksi.class);
              combineDataList.add(riwayat);
              if (riwayat.getTypeApi().equalsIgnoreCase("apiIak")) {
                callback.onUpdateStatus(riwayat.getRefId());
              } else if (riwayat.getTypeApi().equalsIgnoreCase("apiDigiflazz")){
				  
			  }
            }
            callback.onDataChanged(combineDataList);
          }

          @Override
          public void onCancelled(DatabaseError arg0) {}
        });
  }

  public void saveTransaksi(RiwayatTransaksi riwayat) {
    db.child(riwayat.getRefId())
        .setValue(riwayat)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {

              } else {

              }
            });

    db.child(riwayat.getRefId()).child("timestamp").setValue(ServerValue.TIMESTAMP);
  }

  public interface FirebaseLoginCallback {
    public void onStartLogin();

    public void onDataLogin();

    public void onFailure(String error);
  }

  public interface FirebaseCallback {
    public void onStartRequest();

    public void onDataChanged(List<RiwayatTransaksi> list);

    public void onUpdateStatus(String refId);
  }
}
