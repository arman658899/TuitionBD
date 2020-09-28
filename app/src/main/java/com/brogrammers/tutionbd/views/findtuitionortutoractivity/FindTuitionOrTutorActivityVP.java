package com.brogrammers.tutionbd.views.findtuitionortutoractivity;

import android.content.Intent;

import com.brogrammers.tutionbd.BasePresenter;
import com.brogrammers.tutionbd.BaseView;

public interface FindTuitionOrTutorActivityVP {
    interface View extends BaseView<Presenter>{
        void onSetupProfileUi();
        void showSnackBarMessage(String message);
        void navigateToActivity(Intent intent);
    }

    interface Presenter extends BasePresenter{
        void onPostButtonClick();
    }
}
