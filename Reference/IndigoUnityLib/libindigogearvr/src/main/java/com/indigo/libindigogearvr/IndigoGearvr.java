package com.indigo.libindigogearvr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.unity3d.player.UnityPlayer;

/**
 * Created by S.B Hwang on 2017-11-08.
 */

class IndigoGearvr {
    private static IndigoGearvr instance = null;
    private Context context;

    public static IndigoGearvr getInstance() {
        if (instance == null) {
            instance = new IndigoGearvr();
        }
        return instance;
    }

    public boolean init() {
        context = UnityPlayer.currentActivity.getApplicationContext();
        if (context == null) return false;
        return true;
    }

    public void enable() {
        IntentFilter filter = new IntentFilter("android.intent.action.proximity_sensor");
        context.registerReceiver(broadcastReceiver, filter);
    }

    public void disable() {
        context.unregisterReceiver(broadcastReceiver);
    }

    private void sendMessage(String func, String param) {
        UnityPlayer.UnitySendMessage("(singleton) Indigo.IndigoGearVR", func, param);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getType().equals("1")) {
                sendMessage("OnMount", "1");
            } else {
                sendMessage("OnMount", "0");
            }
        }
    };
}
