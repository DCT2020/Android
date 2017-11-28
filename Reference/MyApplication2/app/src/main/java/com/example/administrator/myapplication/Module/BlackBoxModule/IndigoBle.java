package com.example.administrator.myapplication.Module.BlackBoxModule;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;

/**
 * Created by S.B Hwang on 2017-10-24.
 */

public class IndigoBle {

    public interface Scan{
        void scanActionCallBack(String address, String deviceName, int rssi, String scanRecord);
    }

    private static IndigoBle instance = null;
    private Context context;

    private BluetoothManager manager;
    private BluetoothAdapter adapter;
    private BluetoothLeScanner scanner;

    private Scan scanActionCallBack;

    public static IndigoBle getInstance() {
        if (instance == null) {
            instance = new IndigoBle();
        }
        return instance;
    }

    public boolean init(Activity activity) {
        context = activity.getApplicationContext();
        if (context == null) return false;
        manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager == null) return false;
        adapter = manager.getAdapter();
        if (adapter == null) return false;
        scanner = adapter.getBluetoothLeScanner();
        if (scanner == null) return false;
        return true;
    }

    public void startScan() {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        builder.setReportDelay(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE);
        }
        ScanSettings settings = builder.build();
        scanner.startScan(null, settings, callback);
    }

    public void stopScan() {
        scanner.stopScan(callback);
    }

    public void setScanActionCallBack(Scan scan){
        scanActionCallBack = scan;
    }

    private ScanCallback callback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            BluetoothDevice device = result.getDevice();
            int rssi = result.getRssi();
            byte[] scanRecord = result.getScanRecord().getManufacturerSpecificData(76);

            if (scanRecord != null) {
                scanActionCallBack.scanActionCallBack(device.getAddress(),device.getName(),rssi,byteArrayToHex(scanRecord));
            }
        }
    };

    private String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for (final byte b : a)
            sb.append(String.format("%02x ", b & 0xff));
        return sb.toString();
    }
}
