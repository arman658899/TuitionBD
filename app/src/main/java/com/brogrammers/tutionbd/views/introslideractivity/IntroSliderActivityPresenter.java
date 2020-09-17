package com.brogrammers.tutionbd.views.introslideractivity;

import android.content.Context;
import android.content.Intent;

import com.brogrammers.tutionbd.views.loginactivity.LoginActivity;
import com.brogrammers.tutionbd.views.signupactivity.SignUpActivity;

public class IntroSliderActivityPresenter implements IntroSliderActivityVP.Presenter {
    private Context context;
    private IntroSliderActivityVP.View view;

    public IntroSliderActivityPresenter(Context context, IntroSliderActivityVP.View view){
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onSkipButtonClicked() {
        Intent intent = new Intent( context, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        view.navigateToActivity(intent);
    }

    @Override
    public void onGettingStartedClicked() {
        Intent intent = new Intent( context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        view.navigateToActivity(intent);
    }

    @Override
    public void onNextButtonClicked(int position) {
        view.changeSliderPointer(position+1);
    }

    @Override
    public void onSlideChange(int position) {

    }

    @Override
    public void onStart() {
        view.startIntroSlider();
    }
}
