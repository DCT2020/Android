package com.example.administrator.myapplication.Module.NFCModule;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-27.
 */

public class Car {

    private int number;
    private ArrayList<StringBuffer> tagIdContainer = new ArrayList<StringBuffer>();

    public Car(int _number){
        number = _number;
    }

    public void addTag(StringBuffer _tagId){
        tagIdContainer.add(_tagId);
    }

    public int getNumber(){
        return number;
    }

    public ArrayList<StringBuffer> getTags(){
        return (ArrayList<StringBuffer>)tagIdContainer.clone();
    }
}
