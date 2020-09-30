package com.brogrammers.tutionbd.views.findtuitionortutoractivity;

import android.content.Intent;

import com.brogrammers.tutionbd.BasePresenter;
import com.brogrammers.tutionbd.BaseView;
import com.brogrammers.tutionbd.beans.Slider;
import com.brogrammers.tutionbd.beans.HighlightItem;

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
