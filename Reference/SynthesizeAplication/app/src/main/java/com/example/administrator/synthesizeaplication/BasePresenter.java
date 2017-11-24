package com.example.administrator.synthesizeaplication;

/**
 * Created by Administrator on 2017-11-24.
 */

public interface BasePresenter {

    void setView(BasePresenter.View view);

    void onConfirm();

    public interface View {
        void  setConfirmText(String text);
    }
}
