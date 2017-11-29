package com.example.administrator.myapplication.Fragments;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Algorithm.Algorithm;
import com.example.administrator.myapplication.IndigoFragment;
import com.example.administrator.myapplication.Module.BlackBoxModule.IndigoBle;
import com.example.administrator.myapplication.Module.BlackBoxModule.Packet;
import com.example.administrator.myapplication.R;

import java.util.ArrayList;

public class BlackBoxFragment extends IndigoFragment implements View.OnClickListener{

    private View view = null;
    private String additionalDirectory = new String();
    private String filterBlackboxID  = new String();
    private int lastRssi;
    private int Rssi;
    Packet data = new Packet();
    Packet lastData = new Packet();

    private BlackBoxPacketManager blackBoxDataManager = new BlackBoxPacketManager();

    @Override
    protected void onCreateView(View rootView) {
        super.onCreateView(rootView);

        Spinner blackboxSpinner = (Spinner)(view.findViewById(R.id.blackboxs_spinner));
        Spinner packetTimeSpinner = (Spinner)(view.findViewById(R.id.blackbox_packet_times_spinner));
        Spinner blackboxTag = (Spinner)(view.findViewById(R.id.blackbox_tag_spinner));



        CustomAdapter_verHaveChildLayout adapter_child_childLayout = new CustomAdapter_verHaveChildLayout(R.layout.sensor_layout,)

        TextView blackboxBlackboxId_TexView = (TextView)(view.findViewById(R.id.blackbox_packet_blackboxId_name_view));

        IndigoBle.getInstance().init(activity);
        IndigoBle.getInstance().setScanActionCallBack(new IndigoBle.Scan() {
            @Override
            public void scanActionCallBack(String address, String deviceName, int rssi, String scanRecord) {

                Rssi = rssi;

                data.clear();
                data = data.packetParsing(scanRecord);

                if(data.BlackboxID == "") return;


                blackBoxDataManager.addBlackBox(data.BlackboxID);
                if(blackBoxDataManager.addPacketdata(data.BlackboxID,data)){
                    Toast.makeText(activity.getApplicationContext(),"패킷을 수신하지 못하였습니다(ID에 해당하는 블랙박스를 찾을 수 없습니다).",Toast.LENGTH_SHORT).show();
                }

                if(!filterBlackboxID.equals("")) {
                    if (!filterBlackboxID.equals(data.BlackboxID)) {
                        Algorithm.DoOnce.Do("Set Default BlackboxID", new Algorithm.Runnable() {
                            @Override
                            public void function() {

                                filterBlackboxID = data.BlackboxID;
                            }
                        });
                        if (!data.BlackboxID.equals(lastData.BlackboxID)) {
                            Algorithm.DoOnce.Do("Data Verification", new Algorithm.Runnable() {
                                @Override
                                public void function() {

                                    if (lastRssi >= Rssi)
                                        filterBlackboxID = lastData.BlackboxID;
                                    else
                                        filterBlackboxID = data.BlackboxID;
                                }
                            });
                        }
                    }
                    if(!filterBlackboxID.equals(data.BlackboxID)) return;
                }

                lastData = data;
                lastRssi = rssi;
            }
        });
        IndigoBle.getInstance().startScan();
    }

    @Override
    public void onResume() {
        super.onResume();
        IndigoBle.getInstance().startScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        IndigoBle.getInstance().stopScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IndigoBle.getInstance().stopScan();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void setLayoutId() {
        layoutId = R.layout.fragment_blackbox;
    }
}
