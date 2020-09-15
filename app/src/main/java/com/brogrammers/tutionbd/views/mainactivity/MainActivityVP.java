package com.brogrammers.tutionbd.views.mainactivity;

import android.content.Intent;

import com.brogrammers.tutionbd.BasePresenter;
import com.brogrammers.tutionbd.BaseView;

public interface MainActivityVP {
    interface View extends BaseView<Presenter>{
        void showLoading();
        void dismissLoading();
        void showSnackBarMessage(String message);
        void navigateToActivity(Intent intent);
    }

    interface Presenter extends BasePresenter{

    }
}
