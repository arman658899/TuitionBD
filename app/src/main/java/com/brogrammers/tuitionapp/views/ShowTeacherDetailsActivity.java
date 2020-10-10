package com.brogrammers.tuitionapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brogrammers.tuitionapp.AppPreferences;
import com.brogrammers.tuitionapp.ApplicationHelper;
import com.brogrammers.tuitionapp.Constants;
import com.brogrammers.tuitionapp.R;
import com.brogrammers.tuitionapp.beans.AdInfo;
import com.brogrammers.tuitionapp.beans.User;
import com.brogrammers.tuitionapp.listeners.OnDataDownloadListener;
import com.brogrammers.tuitionapp.managers.ProfileManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Calendar;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.YELLOW;

public class ShowTeacherDetailsActivity extends AppCompatActivity {
    private AdInfo adInfo;
    private TextView tvToolbarTittle;
    private Button buttonNid;
    public TextView tvTittle,tvPostedDate,tvSalary,tvSubject,tvLocation,tvClass,tvSchedule,tvLanguage,tvPostType,tvYear;

    //user ui
    private CircularImageView circularImageView;
    private TextView tvMobile,tvUserName,tvUniversity,tvDept;

    ProfileManager profileManager;
    Dialog loadingDialog;
    private String ID_CARD_LINK = "";

    //private String USER_UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_find_teacher_post_details);

        //adInfo = (AdInfo) getIntent().getExtras().getSerializable("ad");

        adInfo = (AdInfo) getIntent().getSerializableExtra("ad");

        /**Bundle bundle = new Bundle();
         bundle.putString("tittle",adInfo.getTittle());
         bundle.putString("salary",adInfo.getSalary());
         bundle.putString("location",adInfo.getLocation());
         bundle.putString("subject",adInfo.getSubject());
         bundle.putString("class",adInfo.getStudentClass());
         bundle.putString("language",adInfo.getLanguage());
         bundle.putString("schedule",adInfo.getSchedule());
         bundle.putString("documentId",adInfo.getDocumentId());
         bundle.putString("userUid",adInfo.getUserUid());
         bundle.putLong("time",adInfo.getCreatedTime());

         intent.putExtras(bundle);*/

        loadingDialog = ApplicationHelper.getUtilsHelper().getLoadingDialog(this);
        loadingDialog.setCancelable(false);

        profileManager = ProfileManager.getInstance(this);

        tvToolbarTittle = findViewById(R.id.textview_toolbar_tittle);

        //user view
        circularImageView = findViewById(R.id.circularImageView);
        tvMobile = findViewById(R.id.contact_number_tv);
        tvUserName = findViewById(R.id.name_tv);
        tvUniversity = findViewById(R.id.uni_clg_name_tv);
        tvDept = findViewById(R.id.department_name_tv);
        tvYear = findViewById(R.id.tv_year);
        buttonNid = findViewById(R.id.nid_card);

        buttonNid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowTeacherDetailsActivity.this,ShowNidOrSIdCartActivity.class);
                intent.putExtra("link",ID_CARD_LINK);
                startActivity(intent);
            }
        });

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

            tvLocation.setText(adInfo.getLocation());
            tvTittle.setText(adInfo.getTittle());
            tvSalary.setText(adInfo.getSalary());
            tvSubject.setText(adInfo.getSubject());
            tvClass.setText(adInfo.getStudentClass());
            tvLanguage.setText(adInfo.getLanguage());
            tvSchedule.setText(adInfo.getSchedule());
            tvPostedDate.setText(getFormatedDate(adInfo.getCreatedTime()));


            /*tvLocation.setText(getIntent().getStringExtra("location"));
            tvTittle.setText(getIntent().getStringExtra("tittle"));
            tvSalary.setText(getIntent().getStringExtra("salary"));
            tvSubject.setText(getIntent().getStringExtra("subject"));
            tvClass.setText(getIntent().getStringExtra("class"));
            tvLanguage.setText(getIntent().getStringExtra("language"));
            tvSchedule.setText(getIntent().getStringExtra("schedule"));
            tvPostedDate.setText(getFormatedDate(getIntent().getLongExtra("time",Calendar.getInstance().getTimeInMillis())));
*/
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


        if (adInfo!=null && adInfo.getUserUid()!=null){
            loadingDialog.show();
            profileManager.getSingleUserDataSnapshot(adInfo.getUserUid(), new OnDataDownloadListener<User>() {
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

        try{
            tvUserName.setText(user.getUserName());

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            tvMobile.setText(user.getUserMobile());
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            if (user.getSubject().isEmpty()){
                tvDept.setText("not available");
            }else tvDept.setText(user.getSubject());
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if (user.getCollege().isEmpty() && user.getYear().isEmpty()){
                tvUniversity.setText("not available");
            }else tvUniversity.setText(user.getCollege());
        }catch (Exception e){
            e.printStackTrace();
        }

       try{
           if (!user.getIdCardLink().isEmpty()) {
               ID_CARD_LINK = ""+user.getIdCardLink();
               buttonNid.setVisibility(View.VISIBLE);
           }
       }catch (Exception e){
           e.printStackTrace();
       }

       try{
           if (user.getYear().isEmpty()){
               tvYear.setText("not available");
           }else tvYear.setText(user.getYear());
       }catch (Exception e){
           e.printStackTrace();
       }


    }

    private void showSnackbarMessage(String message){
        Snackbar.make(tvTittle,message, BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(YELLOW)
                .setTextColor(BLACK)
                .show();
    }

}