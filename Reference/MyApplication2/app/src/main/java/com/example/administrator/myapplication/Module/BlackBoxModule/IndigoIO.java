package com.example.administrator.myapplication.Module.BlackBoxModule;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by S.B Hwang on 2017-09-01.
 */

public class IndigoIO {

    public static void makeFile(Packet filePacket, String additionalDirectory) {
        try {
            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + additionalDirectory;

            File dir = new File(sdPath, "BeaconData");

            if (!dir.exists())
                dir.mkdir();

            String fileName = IndigoTime.getCurrentTime("yyyy-MM-dd HH:mm:ss") + ".txt";

            File file = new File(dir, fileName);
            FileOutputStream fos = new FileOutputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
