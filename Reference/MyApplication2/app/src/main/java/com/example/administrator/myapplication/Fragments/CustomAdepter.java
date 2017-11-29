package com.example.administrator.myapplication.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.administrator.myapplication.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by test02 on 2017-11-29.
 */

public abstract class CustomAdepter extends BaseAdapter {

    protected LayoutInflater layoutInflater;
    private int count = 0;
    protected int mainLayout;
    private ArrayList<String> itemContainer = new ArrayList<String>();

    public CustomAdepter(Context _context, int layout)
    {
        layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainLayout = layout;
    }

    public boolean addItem(String item){

        for( String str : itemContainer){

            if(str.equals(item)){

                return false;
            }
        }

        itemContainer.add(item);
        return true;
    }

    public boolean popItem(String item){

        for(String str : itemContainer){

            if(str.equals(item)){
                itemContainer.remove(str);
                --count;
                return true;
            }
        }

        return false;
    }

    public boolean popItem(int index){

        if(index >= count) return false;

        itemContainer.remove(index);
        --count;

        return true;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int i) {
        return itemContainer.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}
