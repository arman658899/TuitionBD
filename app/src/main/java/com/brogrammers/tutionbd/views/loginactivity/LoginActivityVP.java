package com.brogrammers.tutionbd.views.loginactivity;

import android.content.Intent;

import com.brogrammers.tutionbd.BasePresenter;
import com.brogrammers.tutionbd.BaseView;

public interface LoginActivityVP {
    interface View extends BaseView<Presenter>{
        void onShowNoNetworkDialog();
        void onDismissNoNetworkDialog();
        void onNavigateToActivity(Intent intent);
        void onShowSnackbarMessage(String message);
        void onNotifyEdittextName();
        void onNotifyEdittextPhone();
    }

    interface Presenter extends BasePresenter{
        void onGoButtonClicked(String userName, String mobileNumber) throws Exception;
        void onRetryButtonClicked();
        void onExceptionHandle(String message);
    }
}
