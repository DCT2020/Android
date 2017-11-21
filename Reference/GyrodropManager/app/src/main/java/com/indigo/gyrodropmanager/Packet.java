package com.indigo.gyrodropmanager;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by S.B Hwang on 2017-08-25.
 */

public class Packet {
    Packet() {
        blackboxName = "";
        startSensorACount = "";
        startSensorATime = new ArrayList<>();
        startSensorARssi = new ArrayList<>();
        startSensorBCount = "";
        startSensorBTime = new ArrayList<>();
        startSensorBRssi = new ArrayList<>();
        heightSensorACount = "";
        heightSensorATime = new ArrayList<>();
        heightSensorARssi = new ArrayList<>();
        heightSensorBCount = "";
        heightSensorBTime = new ArrayList<>();
        heightSensorBRssi = new ArrayList<>();
        rotationSensorACount = "";
        rotationSensorATime = new ArrayList<>();
        rotationSensorARssi = new ArrayList<>();
        rotationSensorBCount = "";
        rotationSensorBTime = new ArrayList<>();
        rotationSensorBRssi = new ArrayList<>();
    }

    String blackboxName;
    String startSensorACount;
    List<String> startSensorATime;
    List<String> startSensorARssi;
    String startSensorBCount;
    List<String> startSensorBTime;
    List<String> startSensorBRssi;
    String heightSensorACount;
    List<String> heightSensorATime;
    List<String> heightSensorARssi;
    String heightSensorBCount;
    List<String> heightSensorBTime;
    List<String> heightSensorBRssi;
    String rotationSensorACount;
    List<String> rotationSensorATime;
    List<String> rotationSensorARssi;
    String rotationSensorBCount;
    List<String> rotationSensorBTime;
    List<String> rotationSensorBRssi;

    void clear() {
        blackboxName = "";
        startSensorACount = "";
        startSensorATime.clear();
        startSensorARssi.clear();
        startSensorBCount = "";
        startSensorBTime.clear();
        startSensorBRssi.clear();
        heightSensorACount = "";
        heightSensorATime.clear();
        heightSensorARssi.clear();
        heightSensorBCount = "";
        heightSensorBTime.clear();
        heightSensorBRssi.clear();
        rotationSensorACount = "";
        rotationSensorATime.clear();
        rotationSensorARssi.clear();
        rotationSensorBCount = "";
        rotationSensorBTime.clear();
        rotationSensorBRssi.clear();
    }
}
