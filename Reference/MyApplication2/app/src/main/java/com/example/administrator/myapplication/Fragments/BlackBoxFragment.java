package com.example.administrator.myapplication.Fragments;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.view.View;

import com.example.administrator.myapplication.IndigoFragment;
import com.example.administrator.myapplication.Module.BlackBoxModule.IndigoBle;

import java.util.ArrayList;

public class BlackBoxFragment extends IndigoFragment implements View.OnClickListener{

    private View view = null;
    private IndigoBle indigoBle = new IndigoBle();

    @Override
    protected void onCreateView(View rootView) {
        super.onCreateView(rootView);

        indigoBle.init(activity);
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

    }
}
