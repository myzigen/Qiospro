package com.mhr.mobile.manage.worker;


public class UpdateFirebaseService /*extends Service */ {
  /*
  private DatabaseReference db;
  private String userId, transaksiId;

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    userId = intent.getStringExtra("user_id");
    transaksiId = intent.getStringExtra("transaksi_id");

    db = FirebaseDatabase.getInstance().getReference("transaksi");

    // Pantau perubahan di Firebase Database
    listenToFirebaseUpdates(userId, transaksiId);

    return START_STICKY;
  }

  private void listenToFirebaseUpdates(String userId, String transaksiId) {
    DatabaseReference transaksiRef = db.child(userId).child(transaksiId);
    transaksiRef.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot snapshot) {
            TransaksiIAKResponseStatus.Data data =
                snapshot.getValue(TransaksiIAKResponseStatus.Data.class);
            if (data != null) {
              Log.d("Service", "Firebase Update: " + data.getMessage());

              // Jika status memerlukan pembaruan, panggil API
              if (shouldFetchFromApi(data.getMessage())) {
                fetchStatusFromApi();
              }

              // Kirim broadcast ke UI
              Intent intent = new Intent("com.mhr.mobile.UPDATE_STATUS");
              intent.putExtra("status_message", data.getMessage());
              sendBroadcast(intent);
            }
          }

          @Override
          public void onCancelled(DatabaseError error) {
            Log.e("Service", "Firebase Error: " + error.getMessage());
          }
        });
  }

  private boolean shouldFetchFromApi(String currentStatus) {
    // Hanya panggil API jika status tertentu
    return currentStatus.equals("PROCESS") || currentStatus.equals("SUCCESS") || currentStatus.equals("FAILED");
  }

  private void fetchStatusFromApi() {

    TransaksiIAKRequestStatus requestStatus = new TransaksiIAKRequestStatus(getApplicationContext());
    requestStatus.setApikey();
    requestStatus.setUsername();
    requestStatus.setRefId(transaksiId);
    requestStatus.startRequestStatusTransaksi(
        new TransaksiIAKRequestStatus.RequestStatusTransaksi() {
          @Override
          public void onStartRequest() {}

          @Override
          public void onResponse(TransaksiIAKResponseStatus.Data data) {
            Log.d("Service", "API Response: " + data.getMessage());

            // Perbarui data di Firebase
            db.child(userId)
  	  .child(transaksiId)
  	  .setValue(data)
  	  .addOnSuccessListener(task ->{

  	  });
          }

          @Override
          public void onFailure(String errorMessage) {
            Log.e("Service", "API Request Failed: " + errorMessage);
          }
        });

  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
  */
}
