package com.example.administrator.synthesizeaplication;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Intent;

import com.example.administrator.synthesizeaplication.BasePresenter;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by khl75 on 2017-11-27.
 */

public class AutoLauncherPresenterImpl implements BasePresenter {

    private AppOpsManager appOpsManager;
    private Activity activity;
    private BasePresenter.View view;

    public AutoLauncherPresenterImpl(Activity _activity, AppOpsManager _appOpsManager){
        appOpsManager =_appOpsManager;
        activity = _activity;
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void onConfirm() {

    }
}
