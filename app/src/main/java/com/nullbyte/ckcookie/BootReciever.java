
package com.nullbyte.ckcookie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "CookieClicker";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) return;

        Log.i(TAG, "Received broadcast: " + intent.getAction());

        if (!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            return;
        }

        try {
            Intent service = new Intent(context, CookieService.class);

            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(service);
            } else {
                context.startService(service);
            }

            Log.i(TAG, "CookieService startup requested");

        } catch (Exception error) {
            Log.e(TAG, "Boot startup failed", error);
        }
    }
          }
