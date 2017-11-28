package com.indigo.gyrodropmanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by S.B Hwang on 2017-08-25.
 */

class IndigoBluetooth {
    interface BluetoothEventListener {
        void onBeaconFound(Beacon beacon);
    }

    private BluetoothLeScanner bluetoothLeScanner;
    private List<ScanFilter> scanFilterList;
    private List<String> uuidFilterList;
    private ScanSettings scanSettings;
    private ScanCallback scanCallback;
    private static BluetoothEventListener bluetoothEventListener;
    private static IndigoBluetooth context;
    private boolean isScanning;

    IndigoBluetooth(ScanCallback callback) {
        context = this;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.enable()) {
            bluetoothAdapter.enable();
        }
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        scanFilterList = new ArrayList<>();
        uuidFilterList = new ArrayList<>();
        scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(0).build();
        scanCallback = callback;
        isScanning = false;
    }

    static ScanCallback getDefaultScanCallback() {
        return new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, final ScanResult result) {
                super.onScanResult(callbackType, result);
                if((result != null) && (result.getScanRecord() != null)) {

                    String deviceName = result.getScanRecord().getDeviceName();
                    String address = result.getDevice().getAddress();
                    String uuid = convertUuid(result.getScanRecord().getManufacturerSpecificData(76)); // 76: 0x004C Apple inc
                    String rssi = String.valueOf(result.getRssi());

                    if(context.uuidFilterList == null) return;
                    if(!context.uuidFilterList.isEmpty() && uuid.equals("")) return;
                    if(!context.uuidFilterList.isEmpty() && !context.uuidFilterList.contains(uuid)) return;

                    Beacon beacon = new Beacon();
                    beacon.address = address;
                    beacon.deviceName = deviceName;
                    beacon.uuid = uuid;
                    beacon.rssi = rssi;

                    result.getScanRecord().getBytes();

                    bluetoothEventListener.onBeaconFound(beacon);
                }
            }
        };
    }

    void setBluetoothEventListener(BluetoothEventListener listener) {
        bluetoothEventListener = listener;
    }

    void addFilterDeviceAddress(String address) {
        ScanFilter scanFilter = new ScanFilter.Builder().setDeviceAddress(address).build();
        scanFilterList.add(scanFilter);
    }

    void addFilterDeviceName(String name) {
        ScanFilter scanFilter = new ScanFilter.Builder().setDeviceName(name).build();
        scanFilterList.add(scanFilter);
    }

    void addFilterUUID(String uuid) {
        this.uuidFilterList.add(uuid);
    }

    void clearFilters() {
        if(!scanFilterList.isEmpty()) {
            scanFilterList.clear();
        }
    }

    void scan() {
        if(!isScanning) {
            if(scanFilterList.isEmpty()) {
                bluetoothLeScanner.startScan(null, scanSettings, scanCallback);
            } else {
                bluetoothLeScanner.startScan(scanFilterList, scanSettings, scanCallback);
            }
            this.isScanning = true;
        }
    }

    void stop() {
        if(isScanning) {
            bluetoothLeScanner.stopScan(scanCallback);
            isScanning = false;
        }
    }

    private static String convertUuid(byte[] uuidBytes) {
        if(uuidBytes != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 2; i < 18 ; ++i) {
                if(uuidBytes.length <= i) {
                    return "";
                }
                if(i == 6 || i == 8 || i == 10 || i == 12) {
                    stringBuilder.append('-');
                }

                final int itb = uuidBytes[i] & 0xFF;
                if(itb <= 0xF) {
                    stringBuilder.append('0');
                }
                stringBuilder.append(Integer.toHexString(itb));
            }
            return stringBuilder.toString();
        }
        return "";
    }

    protected static double calculateDistance(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine distance, return -1.
        }

        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            return (0.89976)*Math.pow(ratio,7.7095) + 0.111;
        }
    }
}
