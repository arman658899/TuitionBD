package com.brogrammers.tuitionapp.views.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.brogrammers.tuitionapp.AppPreferences;
import com.brogrammers.tuitionapp.ApplicationHelper;
import com.brogrammers.tuitionapp.Constants;
import com.brogrammers.tuitionapp.R;
import com.brogrammers.tuitionapp.views.introslideractivity.IntroSliderActivity;
import com.brogrammers.tuitionapp.views.loginactivity.LoginActivity;
import com.brogrammers.tuitionapp.views.findtuitionortutoractivity.FindTuitionOrTutorActivity;
import com.brogrammers.tuitionapp.views.signupactivity.SignUpActivity;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {
    private TemplateView templateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        findViewById(R.id.constraint1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //find a tutor
                AppPreferences.setProfileType(MainActivity.this, Constants.PROFILE_FIND_TUTOR_GUARDIAN);
                startFindTuitionOrTutorActivity();
            }
        });

        findViewById(R.id.constraint2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //find a tuition
                AppPreferences.setProfileType(MainActivity.this, Constants.PROFILE_FIND_TUITION_TEACHER);
                startFindTuitionOrTutorActivity();
            }
        });

        findViewById(R.id.constraintLayout2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //explore
                AppPreferences.setProfileType(MainActivity.this, Constants.PROFILE_EXPLORE);
                Toast.makeText(MainActivity.this, "On developing state.", Toast.LENGTH_SHORT).show();
            }
        });

        //initializing admob
        templateView = findViewById(R.id.my_template);

    }

    private void loadAd(){
        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.native_adv_ad))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                        templateView.setStyles(styles);
                        templateView.setNativeAd(unifiedNativeAd);

                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }


    private void startFindTuitionOrTutorActivity(){
        Intent intent = new Intent(MainActivity.this, FindTuitionOrTutorActivity.class);
        startActivity(intent);
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
        else if (ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser()==null){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_in);
        }/*else if (ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser()!=null){
            if (AppPreferences.UserInfo.getUserName(this).isEmpty()){
                Intent intent = new Intent(this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_in);
            }
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadAd();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
