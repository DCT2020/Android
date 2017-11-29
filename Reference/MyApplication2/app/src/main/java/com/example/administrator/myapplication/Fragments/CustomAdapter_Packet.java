package com.example.administrator.myapplication.Fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.Algorithm.Algorithm;
import com.example.administrator.myapplication.Module.BlackBoxModule.Packet;
import com.example.administrator.myapplication.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-29.
 */

public class CustomAdapter_Packet extends CustomAdepter {

    private Packet currentPacket;
    private Context context;
    private boolean isComplited = false;

    public CustomAdapter_Packet(Context _context, int layout) {
        super(_context, layout);

        context = _context;
    }

    public void setPacketData(Packet packet) {

        currentPacket = packet;
    }

    @Override
    public int getCount() {
        return Packet.sensorCount;
    }

    @Override
    public Object getItem(int i) {
        return currentPacket;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = layoutInflater.inflate(mainLayout, null);

        TextView sensorStateNameTexView = (TextView) (v.findViewById(R.id.sensor_state_name_frag));
        TextView sensorCountNameTexView = (TextView) (v.findViewById(R.id.sensor_count_name_frag));
        TextView sensorStateTexView = (TextView) (v.findViewById(R.id.sensor_state));
        TextView sensorCountTexView = (TextView) (v.findViewById(R.id.sensor_count));

        sensorStateNameTexView.setText(context.getResources().getStringArray(R.array.SensorStates)[i]);
        sensorCountNameTexView.setText(context.getResources().getStringArray(R.array.SensorCounts)[i]);

        if (i >= 3) {
            isComplited = true;
        }

        if (currentPacket != null) {

            sensorStateTexView.setText(String.valueOf(currentPacket.Sensor_State[i]));
            sensorCountTexView.setText(String.valueOf(currentPacket.Sensor_Count[i]));
        } else {

            sensorStateTexView.setText("");
            sensorCountTexView.setText("");
        }

        return v;
    }
}
