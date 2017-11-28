package com.example.administrator.myapplication.Module.BlackBoxModule;

import android.os.Handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by S.B Hwang on 2017-09-01.
 */

public class IndigoTime {

    public void initTimeLock() {
        if(!lockedProcesses.isEmpty()) {
            lockedProcesses.clear();
        }
    }

    public void timeLock(final String processID, final Runnable runnable, final int millisecond, final int whenRun) {
        if(whenRun > millisecond) throw new IndexOutOfBoundsException();

        if(!lockedProcesses.containsKey(processID)) {
            lockedProcesses.put(processID, "UnLock");
        }
        if(lockedProcesses.get(processID).equals("UnLock")) {
            if(whenRun == TIME_LOCK_RUN_FIRST) {
                runnable.run();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(whenRun == TIME_LOCK_RUN_LAST) {
                        runnable.run();
                    }
                    lockedProcesses.put(processID, "UnLock");
                }
            }, millisecond);
            lockedProcesses.put(processID, "Lock");
        }
    }

    public static String getCurrentTime(String format) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat(format);
        return sdfNow.format(date);
    }

    public static int TIME_LOCK_RUN_FIRST = 0;
    public static int TIME_LOCK_RUN_LAST = -1;

    private HashMap<String, String> lockedProcesses = new HashMap<>();
}
