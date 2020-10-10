package com.brogrammers.tuitionapp.views.introslideractivity;

import android.content.Intent;

import com.brogrammers.tuitionapp.BasePresenter;
import com.brogrammers.tuitionapp.BaseView;

public interface IntroSliderActivityVP {
    interface View extends BaseView<Presenter>{
        void startIntroSlider();
        void navigateToActivity(Intent intent);
        void changeSliderPointer(int position);
        void showGettingStartedButton();
    }
    interface Presenter extends BasePresenter{
        void onSkipButtonClicked();
        void onGettingStartedClicked();
        void onNextButtonClicked(int position);
        void onSlideChange(int position);
    }
}
