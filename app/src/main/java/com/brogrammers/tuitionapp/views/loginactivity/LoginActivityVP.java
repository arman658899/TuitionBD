package com.brogrammers.tuitionapp.views.loginactivity;

import android.content.Intent;

import com.brogrammers.tuitionapp.BasePresenter;
import com.brogrammers.tuitionapp.BaseView;

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
