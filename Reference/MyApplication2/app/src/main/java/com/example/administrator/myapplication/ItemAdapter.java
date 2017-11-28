package com.example.administrator.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-23.
 */



public class ItemAdapter extends BaseAdapter {

    //bean class
    private class ElementInfo{
        IElementInitializer initializer;
        int childViewId;
        Object[] dataContainerForInitialize;

        public ElementInfo(IElementInitializer _initializer, int _childViewId, Object[] _dataContainerForInitialize) {
            initializer = _initializer;
            childViewId = _childViewId;
            dataContainerForInitialize = _dataContainerForInitialize;
        }
    }
    /////////////////////////////
    public interface IElementInitializer {

        void InitAction(View v, int _childViewId, Object[] _dataContainerForInitialize, int index);
    }
    static public <T> T GetViewById(int id, Class<T> type, View v)
    {
        return type.cast(v.findViewById(id));
    }


    // Vector에서 동기화가 빠진 ArrayList
    // 단일 스레드에서는 ArrayList가 더 빠르다.
    private ArrayList<ElementInfo> mElementInfoContainer;
    private int mRepresentElementNumber;
    private ElementInfo mRepresentElement;

    private LayoutInflater mInflater;
    private int mlayoutId;
    private int mLength = -1;

    static int mElementCounter;

    public ItemAdapter(Context _c, int _layoutid, int _numberOfElement, int _representElementNumber ){
        mInflater = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlayoutId = _layoutid;

        mElementInfoContainer = new ArrayList<ElementInfo>(_numberOfElement);
        mRepresentElementNumber = _representElementNumber;
    }

    //default represent element number is 0
    public ItemAdapter(Context _c, int _layoutid, int _numberOfElement){
        this(_c, _layoutid, _numberOfElement, 0);
    }

    //default number of element is 0, represent element number is 0 too
    public ItemAdapter(Context _c, int _layoutid){
        this(_c, _layoutid, 0, 0);
    }

    @Override
    public int getCount(){
        return mLength;
    }

    @Override
    public Object getItem(int i){
        return Array.get(mRepresentElement,i);
    }

   @Override
    public long getItemId(int i){
        return i;
   }

    @Override
   public View getView(int i, View view, ViewGroup viewGroup)
   {
       View v = mInflater.inflate(mlayoutId,null);


       for(ElementInfo it : mElementInfoContainer)
       {
           it.initializer.InitAction(v, it.childViewId, it.dataContainerForInitialize, i);
       }

       return v;
   }

   public void setLayout(int layoutid)
   {
       mlayoutId = layoutid;
   }

   public void addElementInfo(IElementInitializer _initializer, int _childViewId, Object[] _dataContainerForInitialize){
       mElementInfoContainer.add(new ElementInfo(_initializer, _childViewId, _dataContainerForInitialize));
       if(mElementCounter == mRepresentElementNumber)
       {
           setRepresentElement();
       }
        ++mElementCounter;
   }

   public void setRepresentElementNumber(int num){
       mRepresentElementNumber = num;
   }

   private void setRepresentElement() {
       mRepresentElement = mElementInfoContainer.get(mRepresentElementNumber);
       mLength = Array.getLength(mRepresentElement.dataContainerForInitialize);
   }
}
