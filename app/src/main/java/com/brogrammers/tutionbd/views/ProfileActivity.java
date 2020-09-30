package com.brogrammers.tutionbd.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ProfileActivity extends AppCompatActivity {
    CircularImageView circularImageView;
    TextView tvName,tvMobile,tvUniversity,tvSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        circularImageView = findViewById(R.id.imageView7);
        tvName = findViewById(R.id.name_Tv);
        tvUniversity = findViewById(R.id.university_tv);
        tvMobile = findViewById(R.id.contact_number_et);
        tvSubject = findViewById(R.id.department_name_et);

        //update ui
        Glide.with(this)
                .load(AppPreferences.UserInfo.getUserImage(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(circularImageView);

        tvName.setText(AppPreferences.UserInfo.getUserName(this));
        tvUniversity.setText(AppPreferences.UserInfo.getUserCollete(this));
        tvMobile.setText(AppPreferences.UserInfo.getUserMobileNumber(this));
        tvSubject.setText(AppPreferences.UserInfo.getUserSubject(this));


        findViewById(R.id.imageview_back).setOnClickListener(v -> onBackPressed());
    }
}