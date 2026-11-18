
package com.nullbyte.ckcookie;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

public class CookieService extends Service {

    private static final String CHANNEL = "cookie_service";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL,
                "Cookie Clicker",
                NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager =
                getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }

        Notification notification;

        if (Build.VERSION.SDK_INT >= 26) {
            notification = new Notification.Builder(this, CHANNEL)
                .setContentTitle("Cookie Clicker")
                .setContentText("Cookie Clicker is running")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setOngoing(true)
                .build();
        } else {
            notification = new Notification.Builder(this)
                .setContentTitle("Cookie Clicker")
                .setContentText("Cookie Clicker is running")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setOngoing(true)
                .build();
        }

        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
              }
