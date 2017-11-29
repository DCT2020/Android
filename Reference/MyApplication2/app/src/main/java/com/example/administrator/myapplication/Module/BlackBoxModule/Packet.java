package com.example.administrator.myapplication.Module.BlackBoxModule;

/**
 * Created by Administrator on 2017-11-28.
 */

public class Packet {

    public Packet(){
        this.clear();
    }

    static public final int sensorCount = 4;
    public String FullID = new String();
    public String BluetoothID = new String();
    public String BlackboxID = new String();
    public int Time;
    public int[] Sensor_State = new int[sensorCount];
    public int[] Sensor_Count = new int[sensorCount];

    public Packet packetParsing(String rawData) {
        String packetData = rawData.replace(" ", "");
        if (packetData.length() < 36) return new Packet();
        Packet data = new Packet();
        data.FullID = packetData.substring(4, 10);
        data.BluetoothID = packetData.substring(4, 8);
        data.BlackboxID = packetData.substring(8, 10);
        data.Time = Integer.parseInt(packetData.substring(10, 16), 16);

        int position = 16;
        for(int i = 0; i < sensorCount; ++i) {

            data.Sensor_State[i] = Integer.parseInt(packetData.substring(position, ++position), 16);
            data.Sensor_Count[i] = Integer.parseInt(packetData.substring(position, position+4), 16);
            position+=4;
        }

        /*
        data.FullID = packetData.substring(4, 6);
        data.BluetoothID = packetData.substring(4, 4);
        data.BlackboxID = packetData.substring(8, 2);
        data.Time = Integer.parseInt(packetData.substring(10, 6), 16);
        data.Sensor1_State = Integer.parseInt(packetData.substring(16, 1), 16);
        data.Sensor1_Count = Integer.parseInt(packetData.substring(17, 4), 16);
        data.Sensor2_State = Integer.parseInt(packetData.substring(21, 1), 16);
        data.Sensor2_Count = Integer.parseInt(packetData.substring(22, 4), 16);
        data.Sensor3_State = Integer.parseInt(packetData.substring(26, 1), 16);
        data.Sensor3_Count = Integer.parseInt(packetData.substring(27, 4), 16);
        data.Sensor4_State = Integer.parseInt(packetData.substring(31, 1), 16);
        data.Sensor4_Count = Integer.parseInt(packetData.substring(32, 4), 16);

        */

        return data;
    }

    public void clear(){
        String FullID  = "";
        String BluetoothID = "";
        String BlackboxID = "";
        int Time = 0;
        for(int i = 0; i < sensorCount; ++i){

            Sensor_State[i] = 0;
            Sensor_Count[i] = 0;
        }
    }
}