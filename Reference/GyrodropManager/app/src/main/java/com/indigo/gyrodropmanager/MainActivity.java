package com.indigo.gyrodropmanager;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IndigoBluetooth.BluetoothEventListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "권한 거부\n" + deniedPermissions.toString(),Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("[설정]>[권한]에서 권한을 허용할 수 있습니다")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        indigoBluetooth = new IndigoBluetooth(IndigoBluetooth.getDefaultScanCallback());
        indigoBluetooth.setBluetoothEventListener(this);
        indigoBluetooth.addFilterDeviceAddress(START_SENSOR_A_ADDRESS);
        indigoBluetooth.addFilterDeviceAddress(START_SENSOR_B_ADDRESS);
        indigoBluetooth.addFilterDeviceAddress(HEIGHT_SENSOR_A_ADDRESS);
        indigoBluetooth.addFilterDeviceAddress(HEIGHT_SENSOR_B_ADDRESS);
        indigoBluetooth.addFilterDeviceAddress(ROTATION_SENSOR_A_ADDRESS);
        indigoBluetooth.addFilterDeviceAddress(ROTATION_SENSOR_B_ADDRESS);
        indigoBluetooth.addFilterDeviceAddress(RESET_SENSOR_A_ADDRESS);
        indigoBluetooth.addFilterDeviceAddress(RESET_SENSOR_B_ADDRESS);
        indigoBluetooth.scan();

        indigoTime = new IndigoTime();

        stateText = (TextView)findViewById(R.id.state_text);
        rssiAText = (TextView)findViewById(R.id.rssi_a_text);
        rssiBText = (TextView)findViewById(R.id.rssi_b_text);
        blackBoxPower = (EditText) findViewById(R.id.black_box_edit);
        startSensorA = (EditText)findViewById(R.id.start_sensor_a_edit);
        startSensorB = (EditText)findViewById(R.id.start_sensor_b_edit);
        heightSensorA = (EditText)findViewById(R.id.height_sensor_a_edit);
        heightSensorB = (EditText)findViewById(R.id.height_sensor_b_edit);
        rotationSensorA = (EditText)findViewById(R.id.rotation_sensor_a_edit);
        rotationSensorB = (EditText)findViewById(R.id.rotation_sensor_b_edit);
        resetSensorA = (EditText)findViewById(R.id.reset_sensor_a_edit);
        resetSensorB = (EditText)findViewById(R.id.reset_sensor_b_edit);
        blackboxButton = (Button)findViewById(R.id.black_box_button);
        startButton = (Button)findViewById(R.id.start_button);
        blackboxButton.setOnClickListener(this);
        startButton.setOnClickListener(this);

        testStartedA = false;
        testStartedB = false;
        testCompleteA = false;
        testCompleteB = false;
        startSensorACount = 0;
        heightSensorACount = 0;
        rotationSensorACount = 0;
        startSensorBCount = 0;
        heightSensorBCount = 0;
        rotationSensorBCount = 0;

        badFlagA = false;
        badFlagB = false;
        resetedA = false;
        resetedB = false;
        processingA = false;
        processingB = false;
        resetSensorASensing = false;
        resetSensorBSensing = false;
        powerSensing = false;

        powerHandler.post(powerRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        indigoBluetooth.scan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        indigoBluetooth.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        indigoBluetooth.stop();
    }

    @Override public void onClick(View v) {
        switch(v.getId()) {
            case R.id.black_box_button:
                Intent intent = new Intent(this, SelectBlackboxActivity.class);
                startActivityForResult(intent, SELECT_BLACKBOX_REQUESTCODE);
                break;
            case R.id.start_button:
                //stateText.setText("Please fix the device");
                startSensorA.setText("0");
                startSensorB.setText("0");
                heightSensorA.setText("0");
                heightSensorB.setText("0");
                rotationSensorA.setText("0");
                rotationSensorB.setText("0");
                resetSensorA.setText("Need Test");
                resetSensorB.setText("Need Test");

                startTesting();
                break;
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_BLACKBOX_REQUESTCODE) {
            if(resultCode == Activity.RESULT_OK) {
                blackBoxPower.setText("Off");
                startSensorA.setText("0");
                startSensorB.setText("0");
                heightSensorA.setText("0");
                heightSensorB.setText("0");
                rotationSensorA.setText("0");
                rotationSensorB.setText("0");
                resetSensorA.setText("Need Test");
                resetSensorB.setText("Need Test");

                BLACKBOX_NAME = data.getStringExtra("blackbox_name");
                START_SENSOR_A_ADDRESS = data.getStringExtra("start_sensor_a");
                START_SENSOR_B_ADDRESS = data.getStringExtra("start_sensor_b");
                HEIGHT_SENSOR_A_ADDRESS = data.getStringExtra("height_sensor_a");
                HEIGHT_SENSOR_B_ADDRESS = data.getStringExtra("height_sensor_b");
                ROTATION_SENSOR_A_ADDRESS = data.getStringExtra("rotation_sensor_a");
                ROTATION_SENSOR_B_ADDRESS = data.getStringExtra("rotation_sensor_b");
                RESET_SENSOR_A_ADDRESS = data.getStringExtra("reset_sensor_a");
                RESET_SENSOR_B_ADDRESS = data.getStringExtra("reset_sensor_b");

                stateText.setText("None");
                blackboxButton.setText(data.getStringExtra("blackbox_name"));

                indigoBluetooth.stop();
                indigoBluetooth.clearFilters();
                indigoBluetooth.addFilterDeviceAddress(START_SENSOR_A_ADDRESS);
                indigoBluetooth.addFilterDeviceAddress(START_SENSOR_B_ADDRESS);
                indigoBluetooth.addFilterDeviceAddress(HEIGHT_SENSOR_A_ADDRESS);
                indigoBluetooth.addFilterDeviceAddress(HEIGHT_SENSOR_B_ADDRESS);
                indigoBluetooth.addFilterDeviceAddress(ROTATION_SENSOR_A_ADDRESS);
                indigoBluetooth.addFilterDeviceAddress(ROTATION_SENSOR_B_ADDRESS);
                indigoBluetooth.addFilterDeviceAddress(RESET_SENSOR_A_ADDRESS);
                indigoBluetooth.addFilterDeviceAddress(RESET_SENSOR_B_ADDRESS);
                indigoBluetooth.scan();
            }
        }
    }

    private void startTesting() {
        testStartedA = true;
        testStartedB = true;
        testCompleteA = false;
        testCompleteB = false;
        startSensorACount = 0;
        heightSensorACount = 0;
        rotationSensorACount = 0;
        startSensorBCount = 0;
        heightSensorBCount = 0;
        rotationSensorBCount = 0;
        badFlagA = false;
        badFlagB = false;
        resetedA = false;
        resetedB = false;
        processingA = false;
        processingB = false;
        resetSensorASensing = false;
        resetSensorBSensing = false;
        resetSensorAHandler.removeCallbacksAndMessages(null);
        resetSensorBHandler.removeCallbacksAndMessages(null);
        indigoTime.initTimeLock();
        startButton.setEnabled(false);
        blackboxButton.setEnabled(false);
        packet.clear();
    }

    private void dataVerification() {
        if(testCompleteA && testCompleteB) {
            stateText.setText("Complete!");
            startButton.setEnabled(true);
            blackboxButton.setEnabled(true);
            packet.blackboxName = BLACKBOX_NAME;
            IndigoIO.makeFile(packet);
        }
    }

    @Override public void onBeaconFound(final Beacon beacon) {
        blackBoxPower.setText("On");
        powerSensing = true;

        if(beacon.address.equals(RESET_SENSOR_A_ADDRESS)) {                     //만약 감지된 비콘의 address가 A 센서 어드레스와 동일하다면 rssiAText를 beacon.rssi로 설정한다
            rssiAText.setText(beacon.rssi);
        }
        if(beacon.address.equals(RESET_SENSOR_B_ADDRESS)) {                     //만약 감지된 비콘의 address가 B 센서 어드레스와 동일하다면 rssiBText를 beacon.rssi로 설정한다
            rssiBText.setText(beacon.rssi);
        }

        if(testStartedA) {                                                      //만약 A의 테스트가 시작되었다면
            stateText.setText("Processing...");
            if(Integer.parseInt(beacon.rssi) < BEACON_BAD_RSSI) {               //beacon의 rssi가 BEACON_BAD_RSSI보다 작다면
                badFlagA = true;                                                //A는 올바르지 못한 데이터 이다.
            }

            if(beacon.address.equals(START_SENSOR_A_ADDRESS)) {                                                 //비콘의 address가 START_SENSOR_A_ADDRESS와 같다면
                indigoTime.timeLock(START_SENSOR_A_ADDRESS, new Runnable() {                                    //timeLock을 걸어준다        // (String processID, final Runnable runnable, final int millisecond, final int whenRun)
                    @Override
                    public void run() {                                                                         //이 run  함수가 호출될때~
                        ++startSensorACount;
                        packet.startSensorATime.add(IndigoTime.getCurrentTime("HH:mm:ss.SSS"));        //이 순간의 시간을 패킷의 startSensorATime<list>에 추가해준다.
                        packet.startSensorARssi.add(beacon.rssi);                                              //이 순간 beacon의  rssi를 startSensorARssi<list>에 추가해준다.
                        startSensorA.setText(String.valueOf(startSensorACount));                               //UI로 현재 시작된 샌서들의 수를 출력해준다.
                    }
                }, 6000, IndigoTime.TIME_LOCK_RUN_FIRST);                                          //Runnable을 콜백함수로,  6000ms, 실행될 때
            }
            if(beacon.address.equals(HEIGHT_SENSOR_A_ADDRESS)) {                                               //위와 흐름이 비슷
                indigoTime.timeLock(HEIGHT_SENSOR_A_ADDRESS, new Runnable() {
                    @Override
                    public void run() {
                        ++heightSensorACount;
                        packet.heightSensorATime.add(IndigoTime.getCurrentTime("HH:mm:ss.SSS"));
                        packet.heightSensorARssi.add(beacon.rssi);
                        heightSensorA.setText(String.valueOf(heightSensorACount));
                    }
                }, 1500, IndigoTime.TIME_LOCK_RUN_FIRST);
            }
            if(beacon.address.equals(ROTATION_SENSOR_A_ADDRESS)) {
                indigoTime.timeLock(ROTATION_SENSOR_A_ADDRESS, new Runnable() {
                    @Override
                    public void run() {
                        rotationSensorACount += 4;
                        packet.rotationSensorATime.add(IndigoTime.getCurrentTime("HH:mm:ss.SSS"));
                        packet.rotationSensorARssi.add(beacon.rssi);
                        rotationSensorA.setText(String.valueOf(rotationSensorACount));
                    }
                }, 1800, IndigoTime.TIME_LOCK_RUN_FIRST);
            }
            if(beacon.address.equals(RESET_SENSOR_A_ADDRESS)) {                                 //A센서에 대한 리셋요청이 들어옴
                resetSensorASensing = true;                                                     //A센서에대한 rest condition을 true
                if(resetedA) {
                    if(!badFlagA) {                                                             //badFlag가 아니라면
                        resetSensorA.setText("Good");
                    }
                    else {
                        resetSensorA.setText("Bad");
                    }
                    packet.startSensorACount = String.valueOf(startSensorACount);               //packet에 가지고 있던 데이터를 copy
                    packet.heightSensorACount = String.valueOf(heightSensorACount);
                    packet.rotationSensorACount = String.valueOf(rotationSensorACount);
                    testCompleteA = true;
                    testStartedA = false;
                    dataVerification();                                                         //데이터 검사
                }
                if(!processingA) {                                                              //A에 대한 처리 실패
                    resetSensorAHandler.post(resetSensorARunnable);                             //resetSensorAHandler에 resetSensorARunnable을 보냄
                    processingA = true;                                                         //A에 대한 처리 확인
                }
            }
        }

        if(testStartedB) {                                                                      //위으 B 동작과 동일
            stateText.setText("Processing...");

            if(Integer.parseInt(beacon.rssi) < BEACON_BAD_RSSI) {
                badFlagB = true;
            }
            if(beacon.address.equals(START_SENSOR_B_ADDRESS)) {
                indigoTime.timeLock(START_SENSOR_B_ADDRESS, new Runnable() {
                    @Override
                    public void run() {
                        ++startSensorBCount;
                        packet.startSensorBTime.add(IndigoTime.getCurrentTime("HH:mm:ss.SSS"));
                        packet.startSensorBRssi.add(beacon.rssi);
                        startSensorB.setText(String.valueOf(startSensorBCount));
                    }
                }, 6000, IndigoTime.TIME_LOCK_RUN_FIRST);
            }
            if(beacon.address.equals(HEIGHT_SENSOR_B_ADDRESS)) {
                indigoTime.timeLock(HEIGHT_SENSOR_B_ADDRESS, new Runnable() {
                    @Override
                    public void run() {
                        ++heightSensorBCount;
                        packet.heightSensorBTime.add(IndigoTime.getCurrentTime("HH:mm:ss.SSS"));
                        packet.heightSensorBRssi.add(beacon.rssi);
                        heightSensorB.setText(String.valueOf(heightSensorBCount));
                    }
                }, 1500, IndigoTime.TIME_LOCK_RUN_FIRST);
            }
            if(beacon.address.equals(ROTATION_SENSOR_B_ADDRESS)) {
                indigoTime.timeLock(ROTATION_SENSOR_B_ADDRESS, new Runnable() {
                    @Override
                    public void run() {
                        rotationSensorBCount += 4;
                        packet.rotationSensorBTime.add(IndigoTime.getCurrentTime("HH:mm:ss.SSS"));
                        packet.rotationSensorBRssi.add(beacon.rssi);
                        rotationSensorB.setText(String.valueOf(rotationSensorBCount));
                    }
                }, 1800, IndigoTime.TIME_LOCK_RUN_FIRST);
            }
            if(beacon.address.equals(RESET_SENSOR_B_ADDRESS)) {
                resetSensorBSensing = true;
                if(resetedB) {
                    if(!badFlagB) {
                        resetSensorB.setText("Good");
                    }
                    else {
                        resetSensorB.setText("Bad");
                    }
                    packet.startSensorBCount = String.valueOf(startSensorBCount);
                    packet.heightSensorBCount = String.valueOf(heightSensorBCount);
                    packet.rotationSensorBCount = String.valueOf(rotationSensorBCount);
                    testCompleteB = true;
                    testStartedB = false;
                    dataVerification();
                }
                if(!processingB) {
                    resetSensorBHandler.post(resetSensorBRunnable);
                    processingB = true;
                }
            }
        }
    }



    private static int SELECT_BLACKBOX_REQUESTCODE = 1;

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

    private IndigoBluetooth indigoBluetooth;
    private IndigoTime indigoTime;
    private TextView stateText;
    private TextView rssiAText;
    private TextView rssiBText;
    private EditText blackBoxPower;
    private EditText startSensorA;
    private EditText startSensorB;
    private EditText heightSensorA;
    private EditText heightSensorB;
    private EditText rotationSensorA;
    private EditText rotationSensorB;
    private EditText resetSensorA;
    private EditText resetSensorB;
    private Button blackboxButton;
    private Button startButton;

    private boolean testStartedA;
    private boolean testStartedB;
    private boolean testCompleteA;
    private boolean testCompleteB;
    private int startSensorACount;
    private int heightSensorACount;
    private int rotationSensorACount;
    private int startSensorBCount;
    private int heightSensorBCount;
    private int rotationSensorBCount;

    private boolean badFlagA;
    private boolean badFlagB;
    private boolean resetedA;
    private boolean resetedB;
    private boolean processingA;
    private boolean processingB;
    private boolean resetSensorASensing;
    private boolean resetSensorBSensing;
    private boolean powerSensing;

    private Handler powerHandler = new Handler();
    private Runnable powerRunnable = new Runnable() {
        @Override
        public void run() {
            if(powerSensing) {
                powerSensing = false;
            }
            else {
                rssiAText.setText("No Signal");
                rssiBText.setText("No Signal");
                blackBoxPower.setText("Off");
            }
            powerHandler.postDelayed(powerRunnable, 1000);
        }
    };
    private Handler resetSensorAHandler = new Handler();
    private Runnable resetSensorARunnable = new Runnable() {
        @Override
        public void run() {
            if(resetSensorASensing) {
                resetSensorASensing = false;
                resetSensorAHandler.postDelayed(resetSensorARunnable, 1000);
            }
            else {
                if(processingA) {
                    resetedA = true;
                    resetSensorA.setText("Processing");
                }
            }
        }
    };
    private Handler resetSensorBHandler = new Handler();
    private Runnable resetSensorBRunnable = new Runnable() {
        @Override
        public void run() {
            if(resetSensorBSensing) {
                resetSensorBSensing = false;
                resetSensorBHandler.postDelayed(resetSensorBRunnable, 1000);
            }
            else {
                if(processingB) {
                    resetedB = true;
                    resetSensorB.setText("Processing");
                }
            }
        }
    };

    private Packet packet = new Packet();
}
