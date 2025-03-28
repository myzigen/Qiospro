package com.mhr.mobile;

import android.app.Application;
import android.content.Context;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import com.mhr.mobile.manage.worker.CacheCleanupWorker;
import com.mhr.mobile.util.NetworkMonitoringUtil;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import java.util.concurrent.TimeUnit;

public class MyApp extends Application {
  public static final String TAG = MyApp.class.getSimpleName();
  private static Context appContext;

  private NetworkMonitoringUtil mNetworkMonitoringUtil;
  private static final String APP_ID = "0d416693-a7ec-4aa7-b598-a7600bd45732";
  // Logger logger;
  @Override
  public void onCreate() {
    super.onCreate();
	
    appContext = getApplicationContext();
    //cleanCache();

    OneSignal.initWithContext(this, APP_ID);
    OneSignal.getNotifications().requestPermission(false, Continue.none());
    OneSignal.getUser().getPushSubscription();
    // Inisialisasi NetworkMonitoringUtil
    mNetworkMonitoringUtil = new NetworkMonitoringUtil(getApplicationContext());
    mNetworkMonitoringUtil.checkNetworkState(); // Cek keadaan awal jaringan
    mNetworkMonitoringUtil.registerNetworkCallbackEvents(); // Daftarkan callback
  }

  public void cleanCache() {
    WorkRequest cleanCacheWork = new PeriodicWorkRequest.Builder(CacheCleanupWorker.class, 90, TimeUnit.MINUTES).build();
    WorkManager.getInstance(this).enqueue(cleanCacheWork);
  }

  public static Context getAppContext() {
    return appContext;
  }

  /*
  OneSignal.getNotifications()
        .addForegroundLifecycleListener(
            new INotificationLifecycleListener() {
              @Override
              public void onWillDisplay(@NonNull INotificationWillDisplayEvent event) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                  String userId = firebaseUser.getUid();

                  Context context = MyApp.getAppContext();
                  SharedPreferences prefs =
                      context.getSharedPreferences("notifications", Context.MODE_PRIVATE);

                  // Ambil jumlah notifikasi untuk userId tertentu
                  int count = prefs.getInt("notification_count" + userId, 0);

                  Log.d("Notification Count", "Sebelum: " + count);

                  // Tambahkan jumlah notifikasi
                  count++;
                  prefs.edit().putInt("notification_count" + userId, count).apply();

                  Log.d("Notification Count", "Sesudah: " + count);
                }
              }
            });

  */
}
