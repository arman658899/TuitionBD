package com.brogrammers.tutionbd.views.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.adapters.TabAdapter;
import com.brogrammers.tutionbd.views.PostForTuitionActivity;
import com.brogrammers.tutionbd.views.introslideractivity.IntroSliderActivity;
import com.brogrammers.tutionbd.views.loginactivity.LoginActivity;
import com.brogrammers.tutionbd.views.signupactivity.SignUpActivity;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements MainActivityVP.View{
    private MainActivityVP.Presenter presenter;

    private ViewPager viewPager;
    private TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainActivityPresenter(this,this);

        viewPager = findViewById(R.id.viewpager);
        tab = findViewById(R.id.tablayout);

        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        tab.setupWithViewPager(viewPager);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        findViewById(R.id.textview_post_here).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostForTuitionActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

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
