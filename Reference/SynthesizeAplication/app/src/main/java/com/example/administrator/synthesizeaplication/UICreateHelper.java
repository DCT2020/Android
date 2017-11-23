package com.example.administrator.synthesizeaplication;

import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-11-23.
 */

public final class UICreateHelper {

    //Run at the end of UIRegister
    public interface AdditionalTaskInUIRegister
    {
        void Do();
    }

    public static <T> void UIRegister(View v, int Id)
    {
       T ui = (T)v.findViewById(Id);
       if(ui == null){
           Toast.makeText(v.getContext(),"UIRegister is failed",Toast.LENGTH_SHORT).show();
       }
    }

    public static <T> void UIRegister(View v, int Id, AdditionalTaskInUIRegister task)
    {
        T ui = (T)v.findViewById(Id);
        if(ui == null){
            Toast.makeText(v.getContext(),"UIRegister is failed",Toast.LENGTH_SHORT).show();
        }

        task.Do();
    }
}
