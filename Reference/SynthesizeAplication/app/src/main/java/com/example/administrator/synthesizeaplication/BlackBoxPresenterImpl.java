package com.example.administrator.synthesizeaplication;

import android.app.Activity;
import Indigo.BlackBoxModel;

/**
 * Created by Administrator on 2017-11-24.
 */

public class BlackBoxPresenterImpl implements  BasePresenter {

    private Activity activity;
    private BasePresenter.View view;
    private BlackBoxModel model;

    public BlackBoxPresenterImpl(Activity activity){
        this.activity = activity;
        this.model = new BlackBoxModel();
        this.model.Init();
    }

    @Override
    public void setView(BasePresenter.View view) {
        this.view = view;
    }

    @Override
    public void onConfirm() {
    }
}
