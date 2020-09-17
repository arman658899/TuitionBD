package com.brogrammers.tutionbd.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.views.introslideractivity.IntroSliderActivity;
import com.brogrammers.tutionbd.views.loginactivity.LoginActivity;
import com.brogrammers.tutionbd.views.mainactivity.MainActivity;
import com.brogrammers.tutionbd.views.signupactivity.SignUpActivity;

public class UserTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        findViewById(R.id.constraint1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //find a tutor
                Intent intent = new Intent(UserTypeActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        findViewById(R.id.constraint2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //find a tuition
                Toast.makeText(UserTypeActivity.this, "On developing state.", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.constraintLayout2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //explore
                Toast.makeText(UserTypeActivity.this, "On developing state.", Toast.LENGTH_SHORT).show();
            }
        });

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
    protected void onStop() {
        super.onStop();
    }
}
