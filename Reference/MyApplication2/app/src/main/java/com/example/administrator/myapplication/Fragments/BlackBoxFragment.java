package com.example.administrator.myapplication.Fragments;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.myapplication.Algorithm.Algorithm;
import com.example.administrator.myapplication.IndigoFragment;
import com.example.administrator.myapplication.Module.BlackBoxModule.IndigoBle;
import com.example.administrator.myapplication.Module.BlackBoxModule.Packet;
import com.example.administrator.myapplication.R;

import java.util.ArrayList;

public class BlackBoxFragment extends IndigoFragment implements View.OnClickListener{

    private View view = null;
    private IndigoBle indigoBle = new IndigoBle();
    private String additionalDirectory = new String();
    private String filterBlackboxID  = new String();
    private int lastRssi;
    Packet data = new Packet();
    Packet lastData = new Packet();

    @Override
    protected void onCreateView(View rootView) {
        super.onCreateView(rootView);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity.getApplicationContext());

        View v = getLayoutInflater().inflate(R.layout.dialog_select, null);
        Button gyroButton = (Button)v.findViewById(R.id.btn_gyro);
        Button frenchButton = (Button)v.findViewById(R.id.btn_french);

        gyroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additionalDirectory = getResources().getString(R.string.Gyrodrop);
            }
        });

        frenchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additionalDirectory = getResources().getString(R.string.French);
            }
        });

        mBuilder.setView(v);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

        indigoBle.init(activity);
        indigoBle.setScanActionCallBack(new IndigoBle.Scan() {
            @Override
            public void scanActionCallBack(String address, String deviceName, int rssi, String scanRecord) {
                lastData = data;
                lastRssi = rssi;

                data.clear();
                data = Packet.packetParsing(scanRecord);

                if(filterBlackboxID != data.BlackboxID){
                    Algorithm.DoOnce.Do("Set Default BlackboxID",()->{

                        filterBlackboxID = data.BlackboxID;
                    });
                }
                if(data.BlackboxID != lastData.BlackboxID){
                    Algorithm.DoOnce.Do("Data Verification", ()->{

                        if(lastRssi >= rssi)
                            filterBlackboxID = lastData.BlackboxID;
                        else
                            filterBlackboxID = data.BlackboxID;
                    });
                }
                if(filterBlackboxID != data.BlackboxID) return;

                String temp = new String();
                temp = data.BlackboxID + " : " + data.BluetoothID + " : " + data.FullID + " : " +  data.Sensor1_Count + " : " + data.Sensor1_State + " : " + data.Sensor2_Count + " : " + data.Sensor2_State + " : " + data.Sensor3_Count + " : " + data.Sensor3_State + " : " + data.Sensor4_Count + " : " + data.Sensor4_State;
                Toast.makeText(activity.getApplicationContext(),temp,Toast.LENGTH_SHORT).show();
            }
        });
        indigoBle.startScan();
    }

    @Override
    public void onResume() {
        super.onResume();
        indigoBle.startScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        indigoBle.stopScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        indigoBle.stopScan();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void setLayoutId() {
        layoutId = R.layout.fragment_blackbox;
    }
}
