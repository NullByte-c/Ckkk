package com.nullbyte.ckcookie;

import android.content.Context;
import android.util.Log;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class PythonManager {
    private static ScheduledExecutorService executor;
    private static ScheduledFuture<?> task;
    private static PyObject module;

    private PythonManager() {
    }

    public static synchronized void start(Context context) {
        if (executor != null && !executor.isShutdown()) {
            return;
        }

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(context.getApplicationContext()));
        }

        module = Python.getInstance().getModule("main");
        module.callAttr("initialize");

        executor = Executors.newSingleThreadScheduledExecutor();
        task = executor.scheduleAtFixedRate(() -> {
            try {
                module.callAttr("run_once");
            } catch (Exception error) {
                Log.e("CookieClicker", "Python task failed", error);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public static synchronized void stop() {
        if (executor != null) {
            if (task != null) {
                task.cancel(false);
                task = null;
            }

            PyObject currentModule = module;
            executor.execute(() -> {
                try {
                    if (currentModule != null) {
                        currentModule.callAttr("shutdown");
                    }
                } catch (Exception error) {
                    Log.e("CookieClicker", "Python shutdown failed", error);
                }
            });
            executor.shutdown();
            executor = null;
            module = null;
        }
    }
}
