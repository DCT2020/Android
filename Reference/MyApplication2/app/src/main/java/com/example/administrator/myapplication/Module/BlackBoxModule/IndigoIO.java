package com.example.administrator.myapplication.Module.BlackBoxModule;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by S.B Hwang on 2017-09-01.
 */

public class IndigoIO {

    static public IndigoIO instance;

    public static IndigoIO getInstance()
    {
        if(instance == null)
            instance = new IndigoIO();

        return instance;
    }

    public static void makeFile(Packet filePacket, String additionalDirectory) {
        try {
            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();

            File dir = new File(sdPath, additionalDirectory);

            if (!dir.exists())
                dir.mkdir();

            String fileName = IndigoTime.getCurrentTime("yyyy-MM-dd HH:mm:ss") + ".txt";

            File file = new File(dir, fileName);
            if(!file.isFile()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);

            String buffer = new String();

            buffer = "Blackbox ID : " + filePacket.BlackboxID + " - " + "Time : " + String.valueOf(filePacket.Time) + " - "
                    + "S0( " + String.valueOf(filePacket.Sensor_State[0])  + ", " + String.valueOf(filePacket.Sensor_Count[0]) + " )" + ", "
                    + "S1( " + String.valueOf(filePacket.Sensor_State[1])  + ", " + String.valueOf(filePacket.Sensor_Count[1]) + " )" + ", "
                    + "S2( " + String.valueOf(filePacket.Sensor_State[2])  + ", " + String.valueOf(filePacket.Sensor_Count[2]) + " )" + ", "
                    + "S3( " + String.valueOf(filePacket.Sensor_State[3])  + ", " + String.valueOf(filePacket.Sensor_Count[3]) + " )";

            fos.write(buffer.getBytes());
            fos.flush();
            fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
