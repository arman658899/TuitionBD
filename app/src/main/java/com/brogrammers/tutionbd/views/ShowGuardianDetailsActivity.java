package com.brogrammers.tutionbd.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.beans.AdInfo;
import com.brogrammers.tutionbd.beans.User;
import com.brogrammers.tutionbd.listeners.OnDataDownloadListener;
import com.brogrammers.tutionbd.managers.PostManager;
import com.brogrammers.tutionbd.managers.ProfileManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Calendar;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.YELLOW;

public class ShowGuardianDetailsActivity extends AppCompatActivity {
    private AdInfo adInfo;
    private PostManager postManager;
    private TextView tvToolbarTittle;
    public TextView tvTittle,tvPostedDate,tvSalary,tvSubject,tvLocation,tvClass,tvSchedule,tvLanguage,tvPostType;

    //user ui
    private CircularImageView circularImageView;
    private TextView tvMobile,tvUserName;

    ProfileManager profileManager;
    Dialog loadingDialog;

    //post details
    private String USER_UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_find_tuition_post_details);

        //adInfo = (AdInfo) getIntent().getExtras().getSerializable("ad");

        USER_UID = getIntent().getStringExtra("userUid");



        loadingDialog = ApplicationHelper.getUtilsHelper().getLoadingDialog(this);
        loadingDialog.setCancelable(false);

        profileManager = ProfileManager.getInstance(this);

        tvToolbarTittle = findViewById(R.id.textview_toolbar_tittle);

        //user view
        circularImageView = findViewById(R.id.circularImageView);
        tvMobile = findViewById(R.id.contact_number_tv);
        tvUserName = findViewById(R.id.name_tv);

        //adview
        tvTittle = findViewById(R.id.tv_tittle);
        tvPostedDate = findViewById(R.id.tv_posted_time);
        tvSalary = findViewById(R.id.tuition_fee_range);
        tvSubject = findViewById(R.id.tv_subject);
        tvLocation = findViewById(R.id.tv_location);
        tvLanguage = findViewById(R.id.textview_language);
        tvClass = findViewById(R.id.textview_class);
        tvSchedule = findViewById(R.id.textview_weekly_schedule);

        tvPostType = findViewById(R.id.post_type);

        findViewById(R.id.imageview_back).setOnClickListener(v -> onBackPressed());

        updateAdUi();

    }

    private void updateAdUi() {
        try{
            if (AppPreferences.getProfileType(this)== Constants.PROFILE_FIND_TUITION_TEACHER){
                //post for tuition
                tvPostType.setText("Need Tutor");
            }else if (AppPreferences.getProfileType(this)==Constants.PROFILE_FIND_TUTOR_GUARDIAN){
                //post for tutor
                tvPostType.setText("Need Tuition");
            }
            tvLocation.setText(getIntent().getStringExtra("location"));
            tvTittle.setText(getIntent().getStringExtra("tittle"));
            tvSalary.setText(getIntent().getStringExtra("salary"));
            tvSubject.setText(getIntent().getStringExtra("subject"));
            tvClass.setText(getIntent().getStringExtra("class"));
            tvLanguage.setText(getIntent().getStringExtra("language"));
            tvSchedule.setText(getIntent().getStringExtra("schedule"));
            tvPostedDate.setText(getFormatedDate(getIntent().getLongExtra("time",Calendar.getInstance().getTimeInMillis())));


        }catch (Exception e){
            e.printStackTrace();
            showSnackbarMessage("Something error happened.");
        }
    }


    private String getFormatedDate(long createdTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createdTime);

        return DateFormat.format("dd-MM-yyyy",calendar).toString();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (USER_UID!=null){
            loadingDialog.show();
            profileManager.getSingleUserDataSnapshot(USER_UID, new OnDataDownloadListener<User>() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onFinished() {
                    loadingDialog.dismiss();
                }

                @Override
                public void onDownloading(User user) {
                    updateUserUI(user);
                }

                @Override
                public void onError() {
                    loadingDialog.dismiss();
                    showSnackbarMessage("Please check your internet and try again.");
                }
            });
        }else showSnackbarMessage("Something error happened.");

    }

    private void updateUserUI(User user) {
        Glide.with(this)
                .load(user.getUserImageLink())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(circularImageView);

        tvUserName.setText(user.getUserName());
        tvMobile.setText(user.getUserMobile());
    }

    private void showSnackbarMessage(String message){
        Snackbar.make(tvTittle,message, BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(YELLOW)
                .setTextColor(BLACK)
                .show();
    }
}