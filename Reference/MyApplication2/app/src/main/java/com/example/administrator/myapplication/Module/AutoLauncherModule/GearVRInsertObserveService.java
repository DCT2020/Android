package com.example.administrator.myapplication.Module.AutoLauncherModule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;

import com.example.administrator.myapplication.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by S.B Hwang on 2017-06-30.
 */

public class GearVRInsertObserveService extends Service {
    private NotificationManager notificationManager;
    private Notification notification;
    private FileManager fileManager;
    private String autoLaunchAppName;
    private String autoLaunchPackageName;
    private boolean isRunning;
    private Handler checkInsertEventHandler = new Handler();
    private Runnable checkInsertEventRunnable = new Runnable() {
        @Override
        public void run() {
            String packageName = getForegroundPackageName();
            if(packageName != null) {
                if(!isRunning) {
                    if(packageName.equals("com.oculus.vrshell") || packageName.equals("com.oculus.home")) {
                        startActivity(getPackageManager().getLaunchIntentForPackage(autoLaunchPackageName));
                    }
                }

                if(packageName.equals(autoLaunchPackageName)) {
                    isRunning = true;
                } else {
                    isRunning = false;
                }
            }
            checkInsertEventHandler.postDelayed(checkInsertEventRunnable, TimeUnit.SECONDS.toMillis(1L));
        }
    };

    private BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals(Intent.ACTION_TIME_TICK)) {
                checkDayTime();
                pushNotification();
            }
            if(action.equals(Intent.ACTION_TIME_CHANGED) || action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                checkDayTime();
                pushNotification();
            }
            if(action.equals("ACTION_APP_CHANGED")) {
                checkDayTime();
                pushNotification();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = false;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction("ACTION_APP_CHANGED");
        registerReceiver(timeReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        fileManager = new FileManager(Environment.getExternalStorageDirectory().getAbsolutePath() + "/INDIGO/AutoLauncher/", "AutoLaunchAppInfo");
        checkDayTime();
        pushNotification();
        startForeground(0, notification);

        checkInsertEventHandler.post(checkInsertEventRunnable);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancelAll();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getForegroundPackageName() {
        String packageName = null;
        UsageStatsManager usageStatsManager = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
        final long endTime = System.currentTimeMillis();
        final long beginTime = endTime - 10000;
        final UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
        while (usageEvents.hasNextEvent()) {
            UsageEvents.Event event = new UsageEvents.Event();
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                packageName = event.getPackageName();
            }
        }
        return packageName;
    }

    private void checkDayTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat form = new SimpleDateFormat("HH");
        int hour = Integer.parseInt(form.format(date));

        // AM: 0:00  ~ 17:59
        // PM: 18:00 ~ 23:59
        if (hour < 18) {
            autoLaunchAppName = fileManager.category("AM").tag("AppName").read();
            autoLaunchPackageName = fileManager.category("AM").tag("PackageName").read();
        } else {
            autoLaunchAppName = fileManager.category("PM").tag("AppName").read();
            autoLaunchPackageName = fileManager.category("PM").tag("PackageName").read();
        }
    }

    private void pushNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification.Builder(getApplicationContext())
                .setContentText("Auto Launch: " + autoLaunchAppName)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentIntent(pendingIntent)
                .build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        notificationManager.notify(0, notification);
    }
}