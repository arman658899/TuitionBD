package com.brogrammers.tutionbd.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.views.introslideractivity.IntroSliderActivity;
import com.brogrammers.tutionbd.views.findtuitionortutoractivity.FindTuitionOrTutorActivity;

public class SplashScreen extends AppCompatActivity {

    private static int splashScreen=4000;
    //variable
    Animation topAnimation,bottomAnimation;
    ImageView image;
    TextView appTitle,slogan;
    SharedPreferences introSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView( R.layout.activity_splash_screen );

        topAnimation= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation= AnimationUtils.loadAnimation(this,R.anim.botom_animation);
        //hooks
        image=findViewById( R.id.imageView );
        appTitle=findViewById( R.id.appTitle );
        slogan=findViewById( R.id.slogan );

        image.setAnimation( topAnimation );
        appTitle.setAnimation( bottomAnimation );
        slogan.setAnimation( bottomAnimation );

        new Handler( ).postDelayed( new Runnable() {
            @Override
            public void run() {
                introSlider=getSharedPreferences( "OnBoardingScreen" ,MODE_PRIVATE);
                boolean isFirstTime=introSlider.getBoolean( "firstTime",true );
                if (isFirstTime){
                    SharedPreferences.Editor editor=introSlider.edit();
                    editor.putBoolean( "firstTime" ,true);
                    editor.apply();

                    Intent intent=new Intent (SplashScreen.this, IntroSliderActivity.class);
                    startActivity( intent );
                    finish();
                }
                else {
                    Intent intent=new Intent (SplashScreen.this, FindTuitionOrTutorActivity.class);
                    startActivity( intent );
                    finish();
                }

            }
        },splashScreen );
    }
}