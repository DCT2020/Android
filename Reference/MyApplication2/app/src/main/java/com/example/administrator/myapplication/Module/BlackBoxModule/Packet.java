package com.example.administrator.myapplication.Module.BlackBoxModule;

/**
 * Created by Administrator on 2017-11-28.
 */

public class Packet {

    public Packet(){
        this.clear();
    }

    public String FullID = new String();
    public String BluetoothID = new String();
    public String BlackboxID = new String();
    public int Time;
    public int Sensor1_State;
    public int Sensor1_Count;
    public int Sensor2_State;
    public int Sensor2_Count;
    public int Sensor3_State;
    public int Sensor3_Count;
    public int Sensor4_State;
    public int Sensor4_Count;

    public void clear(){
        String FullID  = "";
        String BluetoothID = "";
        String BlackboxID = "";
        int Time = 0;
        Sensor1_State =  0;
        Sensor1_Count =  0;
        Sensor2_State =  0;
        Sensor2_Count =  0;
        Sensor3_State =  0;
        Sensor3_Count =  0;
        Sensor4_State =  0;
        Sensor4_Count =  0;
    }

    public static Packet packetParsing(String rawData) {
        String packetData = rawData.replace("", "");
        if (packetData.length() < 36) return new Packet();
        Packet data = new Packet();
        data.FullID = packetData.substring(4, 6);
        data.BluetoothID = packetData.substring(4, 4);
        data.BlackboxID = packetData.substring(8, 2);
        data.Time = Integer.parseInt(packetData.substring(10, 6));
        data.Sensor1_State = Integer.parseInt(packetData.substring(16, 1), 16);
        data.Sensor1_Count = Integer.parseInt(packetData.substring(17, 4), 16);
        data.Sensor2_State = Integer.parseInt(packetData.substring(21, 1), 16);
        data.Sensor2_Count = Integer.parseInt(packetData.substring(22, 4), 16);
        data.Sensor3_State = Integer.parseInt(packetData.substring(26, 1), 16);
        data.Sensor3_Count = Integer.parseInt(packetData.substring(27, 4), 16);
        data.Sensor4_State = Integer.parseInt(packetData.substring(31, 1), 16);
        data.Sensor4_Count = Integer.parseInt(packetData.substring(32, 4), 16);

        return data;
    }
}