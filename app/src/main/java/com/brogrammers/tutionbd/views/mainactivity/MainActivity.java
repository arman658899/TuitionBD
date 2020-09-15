package com.brogrammers.tutionbd.views.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.views.introslideractivity.IntroSliderActivity;
import com.brogrammers.tutionbd.views.loginactivity.LoginActivity;

public class MainActivity extends AppCompatActivity implements MainActivityVP.View{
    private MainActivityVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainActivityPresenter(this,this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!AppPreferences.Login.isFirstTimeLogin(this)){
            Intent intent = new Intent(this, IntroSliderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_in);
        }
        else if (!AppPreferences.Login.isLogin(this)){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_in);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onStart();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showSnackBarMessage(String message) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void setPresenter(MainActivityVP.Presenter presenter) {
        //presenter is actually initialize here
        this.presenter = presenter;
    }
}
