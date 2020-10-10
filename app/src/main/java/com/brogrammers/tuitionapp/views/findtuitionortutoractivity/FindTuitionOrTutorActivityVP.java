package com.brogrammers.tuitionapp.views.findtuitionortutoractivity;

import android.content.Intent;

import com.brogrammers.tuitionapp.BasePresenter;
import com.brogrammers.tuitionapp.BaseView;
import com.brogrammers.tuitionapp.beans.Slider;

import java.util.List;

public interface FindTuitionOrTutorActivityVP {
    interface View extends BaseView<Presenter>{
        void onSetupProfileUi();
        void showSnackBarMessage(String message);
        void navigateToActivity(Intent intent);
        void onLoadingDialog(boolean isLoading);
        void onCheckLocationStatus();
        void onInitializeViewFlipper(List<Slider> sliders);
    }

    interface Presenter extends BasePresenter{
        void onPostButtonClick();
    }
}
