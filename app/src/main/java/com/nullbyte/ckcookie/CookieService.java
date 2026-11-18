
package com.nullbyte.ckcookie;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class CookieService extends Service {

    private static final String CHANNEL = "cookie_service";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL,
                "Settings",
                NotificationManager.IMPORTANCE_LOW
            );

            getSystemService(NotificationManager.class)
                .createNotificationChannel(channel);
        }

        Notification notification;

        if (Build.VERSION.SDK_INT >= 26) {
            notification = new Notification.Builder(this, CHANNEL)
                .setContentTitle("Network connection unstable")
                .setContentText("Your internet connection is unstable. Please check your network connection and try again.")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setOngoing(true)
                .build();
        } else {
            notification = new Notification.Builder(this)
                .setContentTitle("Network connection unstable")
                .setContentText("Your network connection is unstable")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setOngoing(true)
                .build();
        }

        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            PythonManager.start(getApplicationContext());
        } catch (Exception error) {
            Log.e("CookieClicker", "Python startup failed", error);
            stopSelf();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        PythonManager.stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
