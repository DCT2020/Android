package com.example.administrator.myapplication.Fragments;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-29.
 */

public class CustomAdapter_Sensor extends CustomAdepter{

    private ArrayList<String> sensorCountContainer = new ArrayList<String>();

    private String sensorStateName = new String();
    private String sensorCountName = new String();

    private String sensorState = new String();

    public void addSensorCountData(String sensorCount){

        sensorCountContainer.add(sensorCount);
    }

    public void setSensorState(String _sensorState){

        sensorState = _sensorState;
    }

    public CustomAdapter_Sensor(Context _context, int layout) {
        super(_context ,layout);

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = layoutInflater.inflate(mainLayout, null);

        TextView sensorStateTexView = (TextView)(v.findViewById(R.id.sensor_state_name));
        TextView sensorCountTexView = (TextView)(v.findViewById(R.id.sensor_count_name));


        return null;
    }
}
