package com.indigo.gyrodropmanager;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by S.B Hwang on 2017-09-01.
 */

public class IndigoIO {

    public static void makeFile(Packet filePacket) {
        try{
            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();


            File dir = new File(sdPath, "BeaconData");

            if (!dir.exists())
                dir.mkdir();


            String fileName = IndigoTime.getCurrentTime("yyyy-MM-dd HH:mm:ss") + ".txt";

            File file = new File(dir, fileName);

            FileOutputStream fos = new FileOutputStream(file);

            String blackboxName;
            String saCount, sbCount, haCount, hbCount, raCount, rbCount;
            List<String> saTime, sbTime, haTime, hbTime, raTime, rbTime;
            List<String>  saRssi, sbRssi, haRssi, hbRssi, raRssi, rbRssi;
            String line_data;
            int space_length, loop_size;

            blackboxName = filePacket.blackboxName;
            saCount = filePacket.startSensorACount;
            saTime = filePacket.startSensorATime;
            saRssi = filePacket.startSensorARssi;
            sbCount = filePacket.startSensorBCount;
            sbTime = filePacket.startSensorBTime;
            sbRssi = filePacket.startSensorBRssi;
            haCount = filePacket.heightSensorACount;
            haTime = filePacket.heightSensorATime;
            haRssi = filePacket.heightSensorARssi;
            hbCount = filePacket.heightSensorBCount;
            hbTime = filePacket.heightSensorBTime;
            hbRssi = filePacket.heightSensorBRssi;
            raCount = filePacket.rotationSensorACount;
            raTime = filePacket.rotationSensorATime;
            raRssi = filePacket.rotationSensorARssi;
            rbCount = filePacket.rotationSensorBCount;
            rbTime = filePacket.rotationSensorBTime;
            rbRssi = filePacket.rotationSensorBRssi;

            // ex) Start Sensor
            // ex) [A]3            | [B]3
            // ex) 10:30:15.123 -65 | 10:30:15.456 -75
            // ex) 10:30:16.123 -65 | 10:30:16.456 -75
            // ex) 10:30:17.123 -65 | 10:30:17.456 -75
            fos.write(blackboxName.getBytes());
            fos.write("\n\n\n".getBytes());
            fos.write("Start Sensor\n".getBytes());
            fos.write("[A]".getBytes());
            fos.write(saCount.getBytes());
            space_length = 14 - saCount.length();
            for(int i = 0; i < space_length; ++i) {
                fos.write(" ".getBytes());
            }
            fos.write("| [B]".getBytes());
            fos.write(sbCount.getBytes());
            fos.write("\n".getBytes());
            loop_size = Integer.parseInt(saCount) >= Integer.parseInt(sbCount) ? saTime.size() : sbTime.size();
            for(int i = 0; i < loop_size; ++i) {
                if(i < saTime.size()) {
                    line_data = saTime.get(i) + " " + saRssi.get(i);
                }
                else {
                    line_data = "| ";
                }
                fos.write(line_data.getBytes());
                space_length = 17 - line_data.length();
                for(int j = 0; j < space_length; ++j) {
                    fos.write(" ".getBytes());
                }
                if(i < sbTime.size()) {
                    line_data = "| " + sbTime.get(i) + " " + sbRssi.get(i);
                }
                else {
                    line_data = "| ";
                }
                fos.write(line_data.getBytes());
                fos.write("\n".getBytes());
            }
            fos.write("\n\n\n".getBytes());


            fos.write("Height Sensor\n".getBytes());
            fos.write("[A]".getBytes());
            fos.write(haCount.getBytes());
            space_length = 14 - haCount.length();
            for(int i = 0; i < space_length; ++i) {
                fos.write(" ".getBytes());
            }
            fos.write("| [B]".getBytes());
            fos.write(hbCount.getBytes());
            fos.write("\n".getBytes());
            loop_size = Integer.parseInt(haCount) >= Integer.parseInt(hbCount) ? haTime.size() : hbTime.size();
            for(int i = 0; i < loop_size; ++i) {
                if(i < haTime.size()) {
                    line_data = haTime.get(i) + " " + haRssi.get(i);
                }
                else {
                    line_data = "| ";
                }
                fos.write(line_data.getBytes());
                space_length = 17 - line_data.length();
                for(int j = 0; j < space_length; ++j) {
                    fos.write(" ".getBytes());
                }
                if(i < hbTime.size()) {
                    line_data = "| " + hbTime.get(i) + " " + hbRssi.get(i);
                }
                else {
                    line_data = "| ";
                }
                fos.write(line_data.getBytes());
                fos.write("\n".getBytes());
            }
            fos.write("\n\n\n".getBytes());


            fos.write("Rotation Sensor\n".getBytes());
            fos.write("[A]".getBytes());
            fos.write(raCount.getBytes());
            space_length = 14 - raCount.length();
            for(int i = 0; i < space_length; ++i) {
                fos.write(" ".getBytes());
            }
            fos.write("| [B]".getBytes());
            fos.write(rbCount.getBytes());
            fos.write("\n".getBytes());
            loop_size = Integer.parseInt(raCount) >= Integer.parseInt(rbCount) ? raTime.size() : rbTime.size();
            for(int i = 0; i < loop_size; ++i) {
                if(i < raTime.size()) {
                    line_data = raTime.get(i) + " " + raRssi.get(i);
                }
                else {
                    line_data = "| ";
                }
                fos.write(line_data.getBytes());
                space_length = 17 - line_data.length();
                for(int j = 0; j < space_length; ++j) {
                    fos.write(" ".getBytes());
                }
                if(i < rbTime.size()) {
                    line_data = "| " + rbTime.get(i) + " " + rbRssi.get(i);
                }
                else {
                    line_data = "| ";
                }
                fos.write(line_data.getBytes());
                fos.write("\n".getBytes());
            }

            fos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
