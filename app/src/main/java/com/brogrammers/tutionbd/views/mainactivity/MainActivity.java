package com.brogrammers.tutionbd.views.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.views.introslideractivity.IntroSliderActivity;
import com.brogrammers.tutionbd.views.loginactivity.LoginActivity;
import com.brogrammers.tutionbd.views.findtuitionortutoractivity.FindTuitionOrTutorActivity;
import com.brogrammers.tutionbd.views.signupactivity.SignUpActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }

    private void startFindTuitionOrTutorActivity(){
        Intent intent = new Intent(MainActivity.this, FindTuitionOrTutorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        }else if (ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser()!=null){
            if (AppPreferences.UserInfo.getUserName(this).isEmpty()
                    || AppPreferences.UserInfo.getUserMobileNumber(this).isEmpty()){
                Intent intent = new Intent(this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_in);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
