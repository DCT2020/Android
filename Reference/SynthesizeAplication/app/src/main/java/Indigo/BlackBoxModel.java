package Indigo;

import android.util.Log;

/**
 * Created by Administrator on 2017-11-24.
 */

public class BlackBoxModel implements IndigoBluetooth.BluetoothEventListener{

    private IndigoBluetooth mBluetoothManager;
    private IndigoTime mTimeManager;

    public void Init(){

        mBluetoothManager = new IndigoBluetooth(IndigoBluetooth.getDefaultScanCallback());
        mBluetoothManager.setBluetoothEventListener(this);
        mBluetoothManager.addFilterDeviceAddress(START_SENSOR_A_ADDRESS);
        mBluetoothManager.addFilterDeviceAddress(START_SENSOR_B_ADDRESS);
        mBluetoothManager.addFilterDeviceAddress(HEIGHT_SENSOR_A_ADDRESS);
        mBluetoothManager.addFilterDeviceAddress(HEIGHT_SENSOR_B_ADDRESS);
        mBluetoothManager.addFilterDeviceAddress(ROTATION_SENSOR_A_ADDRESS);
        mBluetoothManager.addFilterDeviceAddress(ROTATION_SENSOR_B_ADDRESS);
        mBluetoothManager.addFilterDeviceAddress(RESET_SENSOR_A_ADDRESS);
        mBluetoothManager.addFilterDeviceAddress(RESET_SENSOR_B_ADDRESS);
        mBluetoothManager.scan();

        mTimeManager  = new IndigoTime();
    }

    public  void Scan(){
        mBluetoothManager.scan();
    }

    public  void Stop(){
        mBluetoothManager.stop();
    }

    @Override
    public void onBeaconFound(Beacon beacon) {
        Log.i("BlackBoxModel", beacon.address +  " "  + beacon.deviceName  + " " + beacon.rssi + "  " + beacon.uuid);
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    //  Defines
    private int BEACON_BAD_RSSI                 = -100;
    private String BLACKBOX_NAME                = "1st Black Box";
    private String START_SENSOR_A_ADDRESS       = "60:64:05:D1:30:00";
    private String START_SENSOR_B_ADDRESS       = "A8:1B:6A:AE:63:BD";
    private String HEIGHT_SENSOR_A_ADDRESS      = "A8:1B:6A:AE:5F:E1";
    private String HEIGHT_SENSOR_B_ADDRESS      = "60:64:05:D1:34:03";
    private String ROTATION_SENSOR_A_ADDRESS    = "60:64:05:D1:3A:E9";
    private String ROTATION_SENSOR_B_ADDRESS    = "60:64:05:D1:2B:50";
    private String RESET_SENSOR_A_ADDRESS       = "60:64:05:D1:29:76";
    private String RESET_SENSOR_B_ADDRESS       = "A8:1B:6A:AE:63:99";
}
