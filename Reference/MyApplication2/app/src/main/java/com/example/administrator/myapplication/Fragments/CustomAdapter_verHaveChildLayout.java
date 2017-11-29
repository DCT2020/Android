package com.example.administrator.myapplication.Fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.myapplication.R;

import java.util.ArrayList;

/**
 * Created by test02 on 2017-11-29.
 */

public class CustomAdapter_verHaveChildLayout extends CustomAdepter{

    private CustomAdepter childAdapter;

    public boolean addChildItem(String item){

        return childAdapter.addItem(item);
    }

    public boolean popChildItem(String item){

        return childAdapter.popItem(item);
    }

    public boolean popChildItem(int index){

        return childAdapter.popItem(index);
    }

    public CustomAdapter_verHaveChildLayout(Context context, int mainLayout, CustomAdepter _childAdapter){
        super(context, mainLayout);

        childAdapter = _childAdapter;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(view.getContext(),mainLayout,null);

        ListView sensorListView = (ListView)(v.findViewById(R.id.sensor_info_list));
        sensorListView.setAdapter(childAdapter);

        return v;
    }
}
