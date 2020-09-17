package com.brogrammers.tutionbd.views.loginactivity;

import android.content.Intent;

import com.brogrammers.tutionbd.BasePresenter;
import com.brogrammers.tutionbd.BaseView;

public interface LoginActivityVP {
    interface View extends BaseView<Presenter>{
        void onNavigateToActivity(Intent intent);
        void onNotifyEdittextMobile(String message);
        void onShowNoNetworkDialog();
        void onDismissNoNetworkDialog();
    }
    interface Presenter extends BasePresenter{
        void onGoButtonClicked(String mobileNumber);
    }
}
