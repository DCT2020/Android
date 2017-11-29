package com.example.administrator.myapplication.Fragments;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Algorithm.Algorithm;
import com.example.administrator.myapplication.IndigoFragment;
import com.example.administrator.myapplication.Module.BlackBoxModule.IndigoBle;
import com.example.administrator.myapplication.Module.BlackBoxModule.IndigoIO;
import com.example.administrator.myapplication.Module.BlackBoxModule.Packet;
import com.example.administrator.myapplication.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BlackBoxFragment extends IndigoFragment implements View.OnClickListener{

    private String filterBlackboxID  = new String();
    private int lastRssi;
    private int Rssi;
    Packet data = new Packet();
    Packet lastData = new Packet();

    private ArrayAdapter<String> blackboxIdSpinnerAdapter;
    private ArrayAdapter<String> packetTimeSpinnerAdapter;
    private ArrayAdapter<CharSequence> blackboxTagSpinnerAdapter;
    private CustomAdapter_Packet adapterSensor;

    private BlackBoxPacketManager blackBoxDataManager = new BlackBoxPacketManager();

    private String currentWriteDir;

    @Override
    protected void onCreateView(View rootView) {
        super.onCreateView(rootView);


        TextView blackboxBlackboxId_TexView = (TextView)(rootView.findViewById(R.id.blackbox_packet_data_blackboxId_view));

        Spinner packetTimeSpinner = (Spinner)(rootView.findViewById(R.id.blackbox_packet_times_spinner));
        {
            packetTimeSpinnerAdapter = new ArrayAdapter<String>(activity.getApplicationContext(),android.R.layout.simple_spinner_dropdown_item);
            packetTimeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            packetTimeSpinner.setAdapter(packetTimeSpinnerAdapter);

            packetTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        Spinner blackboxTagSpinner = (Spinner)(rootView.findViewById(R.id.blackbox_tag_spinner));
        {
            blackboxTagSpinnerAdapter = ArrayAdapter.createFromResource(activity.getApplicationContext(),R.array.BlackboxTags,android.R.layout.simple_spinner_dropdown_item);
            blackboxTagSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            blackboxTagSpinner.setAdapter(blackboxTagSpinnerAdapter);

            blackboxTagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    currentWriteDir = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            currentWriteDir = blackboxTagSpinnerAdapter.getItem(0).toString();
        }
        Spinner blackboxSpinner = (Spinner)(rootView.findViewById(R.id.blackboxs_spinner));
        {

            // 내일 수정
            blackboxIdSpinnerAdapter = new ArrayAdapter<String>(activity.getApplicationContext(),android.R.layout.simple_spinner_dropdown_item);
            blackboxIdSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            blackboxSpinner.setAdapter(blackboxIdSpinnerAdapter);

            blackboxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    BlackBoxPacketManager.blackBoxPacketContainer container = blackBoxDataManager.getPacketContainer(i);
                    filterBlackboxID = container.getKey();

                    packetTimeSpinnerAdapter.clear();
                    for(Packet p : container.getDatas()) {

                        packetTimeSpinnerAdapter.add(String.valueOf(p.Time));
                    }
                    adapterSensor.setPacketData(blackBoxDataManager.getPacketContainer(i).getLatestPacket());
                    adapterSensor.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        Packet packet1 = new Packet();
        packet1 = packet1.packetParsing("154963256984561235486549875643212345");

        Packet packet2 = new Packet();
        packet2 = packet1.packetParsing("154869223333226875546213215632123156");


        if(blackBoxDataManager.addBlackBox(packet1.BlackboxID)){

            blackboxIdSpinnerAdapter.add(packet1.BlackboxID);
        }
        blackBoxDataManager.addPacketdata(packet1.BlackboxID,packet1);
        packetTimeSpinnerAdapter.add(String.valueOf(packet1.Time));
        IndigoIO.getInstance().makeFile(packet1,currentWriteDir);

        if(blackBoxDataManager.addBlackBox(packet2.BlackboxID)){

            blackboxIdSpinnerAdapter.add(packet2.BlackboxID);
        }
        blackBoxDataManager.addPacketdata(packet2.BlackboxID,packet2);
        packetTimeSpinnerAdapter.add(String.valueOf(packet2.Time));
        IndigoIO.getInstance().makeFile(packet2,currentWriteDir);

        if(blackBoxDataManager.addBlackBox(packet1.BlackboxID)){

            blackboxIdSpinnerAdapter.add(packet1.BlackboxID);
        }
        blackBoxDataManager.addPacketdata(packet1.BlackboxID,packet1);
        packetTimeSpinnerAdapter.add(String.valueOf(packet1.Time));
        IndigoIO.getInstance().makeFile(packet1,currentWriteDir);



        ListView blackBoxSensorListView = (ListView) rootView.findViewById(R.id.sensor_info_list);
        adapterSensor = new CustomAdapter_Packet( activity.getApplicationContext(), R.layout.sensor_thumbnail);

        adapterSensor.setPacketData(packet1);


        blackBoxSensorListView.setAdapter(adapterSensor);

        IndigoBle.getInstance().init(activity);
        IndigoBle.getInstance().setScanActionCallBack(new IndigoBle.Scan() {
            @Override
            public void scanActionCallBack(String address, String deviceName, int rssi, String scanRecord) {

                Rssi = rssi;

                data.clear();
                data = data.packetParsing(scanRecord);

                if(data.BlackboxID == "") return;

               if(blackBoxDataManager.addBlackBox(data.BlackboxID)){

                   blackboxIdSpinnerAdapter.add(data.BlackboxID);
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

                    if(blackBoxDataManager.addPacketdata(data.BlackboxID,data)){
                        Toast.makeText(activity.getApplicationContext(),"패킷을 수신하지 못하였습니다(ID에 해당하는 블랙박스를 찾을 수 없습니다).",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        updateBlackboxUI();
                    }
               }

                lastData = data;
                lastRssi = rssi;
            }
        });
        IndigoBle.getInstance().startScan();
    }

    private void updateBlackboxUI(){

        BlackBoxPacketManager.blackBoxPacketContainer container = blackBoxDataManager.getPacketContainer(filterBlackboxID);
        Packet packet = container.getLatestPacket();

        packetTimeSpinnerAdapter.add(String.valueOf(packet.Time));
        adapterSensor.setPacketData(packet);
        adapterSensor.notifyDataSetChanged();
        packetTimeSpinnerAdapter.add(String.valueOf(packet.Time));
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
