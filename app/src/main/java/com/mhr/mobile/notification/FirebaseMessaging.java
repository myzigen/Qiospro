package com.mhr.mobile.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mhr.mobile.MainActivity;
import com.mhr.mobile.R;
import com.onesignal.OneSignal;
import com.onesignal.notifications.INotificationReceivedEvent;
import com.onesignal.notifications.INotificationServiceExtension;
import com.onesignal.notifications.internal.common.OSNotificationOpenBehaviorFromPushPayload;

public class FirebaseMessaging /* extends FirebaseMessagingService */{
	
	

	/*

  @Override
  public void onNewToken(String token) {
    super.onNewToken(token);
  }

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);

    if (remoteMessage.getNotification() != null) {
      showNotification(
          remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }
  }

  private RemoteViews getCustomDesign(String title, String message) {
    RemoteViews remoteViews =
        new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
    remoteViews.setTextViewText(R.id.title, title);
    remoteViews.setTextViewText(R.id.message, message);
    remoteViews.setImageViewResource(R.id.icon, R.drawable.ic_launcher_background);
    return remoteViews;
  }

  public void showNotification(String title, String message) {
    Intent intent = new Intent(this, MainActivity.class);
    // Assign channel ID
    String channel_id = "notification_channel";
    // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
    // the activities present in the activity stack,
    // on the top of the Activity that is to be launched
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    // Pass the intent to PendingIntent to start the
    // next Activity
    PendingIntent pendingIntent =
        PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

    // Create a Builder object using NotificationCompat
    // class. This will allow control over all the flags
    NotificationCompat.Builder builder =
        new NotificationCompat.Builder(getApplicationContext(), channel_id)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
            .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent);

    // A customized design for the notification can be
    // set only for Android versions 4.1 and above. Thus
    // condition for the same is checked here.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      builder = builder.setContent(getCustomDesign(title, message));
    } // If Android Version is lower than Jelly Beans,
    // customized layout cannot be used and thus the
    // layout is set as follows
    else {
      builder = builder.setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.ic_launcher_background);
    }
    // Create an object of NotificationManager class to
    // notify the
    // user of events that happen in the background.
    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    // Check if the Android Version is greater than Oreo
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel notificationChannel =
          new NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH);
      notificationManager.createNotificationChannel(notificationChannel);
    }

    notificationManager.notify(0, builder.build());
  }
	@Override
	public void onNotificationReceived(INotificationReceivedEvent arg0) {
	}
  */
}
