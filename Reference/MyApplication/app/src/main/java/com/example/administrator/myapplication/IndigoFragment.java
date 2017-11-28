package com.example.administrator.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017-11-27.
 */
public abstract class IndigoFragment extends Fragment {

    private static ArrayList<IndigoFragment> indigoFragments = new ArrayList<IndigoFragment>();
    protected int layoutId;
    protected static Activity activity;

    // 자신의 LayoutId를 설정한다.
    protected abstract void setLayoutId();

    // Fragment의 onCreateView 내부에서 사용되는 함수입니다.
    // 추가 액션을 여기서 취합니다.
    protected void onCreateView(View rootView){

    }

    public IndigoFragment(){
        setLayoutId();
    }

    public static int getFragmentCount(){
        return indigoFragments.size();
    }

    public static void addFragment(IndigoFragment fragment, Activity _activity){
        if(fragment != null) {
            indigoFragments.add(fragment);
            activity = _activity;
        }
        else{
            Log.i("Error","fragment is null can't add to list");
        }
    }

    public static IndigoFragment newInstance(int sectionNumber) {
        IndigoFragment fragment = indigoFragments.get(sectionNumber);

        if(fragment == null){
            Log.i("Error","fragment is null");
        }

        return fragment;
    }

    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(layoutId, container, false);
        onCreateView(rootView);
        return rootView;
    }
}
