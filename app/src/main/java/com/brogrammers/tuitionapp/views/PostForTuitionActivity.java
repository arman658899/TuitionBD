package com.brogrammers.tuitionapp.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brogrammers.tuitionapp.AppPreferences;
import com.brogrammers.tuitionapp.ApplicationHelper;
import com.brogrammers.tuitionapp.Constants;
import com.brogrammers.tuitionapp.R;
import com.brogrammers.tuitionapp.beans.AdInfo;
import com.brogrammers.tuitionapp.views.locationactivity.SelectLocationActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class PostForTuitionActivity extends AppCompatActivity {
    private EditText etTittle,etSalary,etClass,etSubject;
    private Button buttonUpload;
    private Dialog loadingDialog;
    private TextView tvLocation,tvTittle;
    private Spinner spinnerLanguage,spinnerSchedule;
    private CollectionReference collRef,locationRef;

    private static final int SELECT_LOCATION_REQUEST = 110;
    private double mLat=-1,mLon=-1;
    private String mAddress="",schedule = "",language = "";
    private String documentId = "";

    private AdInfo mAdInfo;
    private boolean isUpdate = false;

    //admob
    private RewardedAd rewardedAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_for_tuition);

        mAdInfo = (AdInfo) getIntent().getSerializableExtra("ad");
        isUpdate = getIntent().getBooleanExtra("update",false);

        tvTittle = findViewById(R.id.textView5);
        buttonUpload = findViewById(R.id.button);

        loadingDialog = ApplicationHelper.getUtilsHelper().getLottieLoadingBeHappy(this);
        loadingDialog.setCancelable(false);

        etTittle = findViewById(R.id.title_et);
        etSubject = findViewById(R.id.fullName_Et);
        etSalary = findViewById(R.id.editTextTextPersonName5);
        etClass = findViewById(R.id.editTextTextPersonName6);
        tvLocation = findViewById(R.id.editTextTextPersonName4);

        spinnerLanguage = findViewById(R.id.spinner_language);
        spinnerSchedule = findViewById(R.id.spinner_weekly_scedule);

        if (isUpdate) buttonUpload.setText("Update");
        else buttonUpload.setText("Post");

        switch (AppPreferences.getProfileType(this)){
            case Constants.PROFILE_FIND_TUITION_TEACHER:{
                collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION_TO_GUARDIAN);
                locationRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION_LOCATION);
                tvTittle.setText("Post for Tuition");
                etTittle.setHint("Need a tuition");
                break;
            }
            case Constants.PROFILE_FIND_TUTOR_GUARDIAN:{
                collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUTOR_TO_TEACHER);
                locationRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUTOR_LOCATION);
                tvTittle.setText("Post for Tutor");
                etTittle.setHint("Need a female tutor from DU");
                break;

            }
            default:
        }

        findViewById(R.id.imageView9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        spinnerLanguage.setAdapter(new ArrayAdapter<String>(PostForTuitionActivity.this,R.layout.sampleview_only_textview,R.id.textview_item,Constants.LANGUAGE_MEDIUM));
        spinnerSchedule.setAdapter(new ArrayAdapter<String>(PostForTuitionActivity.this,R.layout.sampleview_only_textview,R.id.textview_item,Constants.WEEKLY_SCHEDULE));
        tvLocation.setOnClickListener(v -> statusCheck());

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //post for tuition

                String tittle,subject,salary,studentClass;
                tittle = ""+etTittle.getText().toString();
                subject = ""+etSubject.getText().toString();
                salary = ""+etSalary.getText().toString();
                studentClass = ""+etClass.getText().toString();

                schedule = Constants.WEEKLY_SCHEDULE[spinnerSchedule.getSelectedItemPosition()];
                language = Constants.LANGUAGE_MEDIUM[spinnerLanguage.getSelectedItemPosition()];

                if (tittle.isEmpty()){
                    etTittle.setError("Please add proper tittle.");
                    etTittle.requestFocus();
                    return;
                }
                if (salary.isEmpty()){
                    etSalary.setError("Please add proper salary.");
                    etSalary.requestFocus();
                    return;
                }
                if (subject.isEmpty()){
                    etSubject.setError("Please add proper subject.");
                    etSubject.requestFocus();
                    return;
                }
                if (studentClass.isEmpty()){
                    etClass.setError("Please add proper class.");
                    etClass.requestFocus();
                    return;
                }

                if (schedule.isEmpty()){
                    Toast.makeText(PostForTuitionActivity.this, "Please select weekly schedule.", Toast.LENGTH_SHORT).show();
                    return;
               }

                if (language.isEmpty()){
                    Toast.makeText(PostForTuitionActivity.this, "Please select preferred language.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mAddress.isEmpty()){
                    mAddress = "";
                }

                if (ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser()==null){
                    return;
                }

                loadingDialog.show();

                if (isUpdate){
                    try{
                        documentId = mAdInfo.getDocumentId();
                    }catch (Exception e){
                        Log.d(Constants.TAG, "onClick: documentId null");
                        e.printStackTrace();
                    }
                }else documentId = collRef.document().getId();

                if (documentId.isEmpty()){
                    Toast.makeText(PostForTuitionActivity.this, "Something error happened. Please try again later.", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("tittle",tittle);
                hashMap.put("salary",salary);
                hashMap.put("location",mAddress);
                hashMap.put("subject",subject);
                hashMap.put("studentClass",studentClass);
                hashMap.put("language",language);
                hashMap.put("schedule",schedule);
                hashMap.put("documentId",documentId);
                hashMap.put("userUid",ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid());
                hashMap.put("createdTime",Calendar.getInstance().getTimeInMillis());
                hashMap.put("approve",false);

                /*AdInfo adInfo = new AdInfo(
                        tittle,
                        salary,
                        mAddress,
                        subject,
                        studentClass,
                        language,
                        schedule,
                        documentId,
                        ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid(),
                        Calendar.getInstance().getTimeInMillis());*/

                collRef.document(documentId)
                        .set(hashMap, SetOptions.merge())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingDialog.dismiss();
                                if (task.isSuccessful()){

                                    if (rewardedAd.isLoaded()) showAd();

                                    updateUi();
                                    Toast.makeText(PostForTuitionActivity.this, "Post uploaded.", Toast.LENGTH_SHORT).show();
                                    if (mLat ==-1 || mLon == -1) return;
                                    if (isUpdate) return;

                                    GeoFirestore geoFirestore = new GeoFirestore(locationRef);
                                    GeoPoint point = new GeoPoint(mLat,mLon);
                                    geoFirestore.setLocation(documentId,point);

                                }else Toast.makeText(PostForTuitionActivity.this, "Failed to upload post.", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });

        //loading reward ad
        loadAd();

    }

    private void loadAd(){
        rewardedAd = new RewardedAd(this,getString(R.string.reward_ad));
        RewardedAdLoadCallback callback = new RewardedAdLoadCallback(){
            @Override
            public void onRewardedAdLoaded() {
                super.onRewardedAdLoaded();

            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                super.onRewardedAdFailedToLoad(loadAdError);
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(),callback);
    }

    private void showAd(){
        RewardedAdCallback adCallback = new RewardedAdCallback() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

            }

            @Override
            public void onRewardedAdClosed() {
                super.onRewardedAdClosed();
                loadAd();
            }

            @Override
            public void onRewardedAdFailedToShow(AdError adError) {
                super.onRewardedAdFailedToShow(adError);
            }

            @Override
            public void onRewardedAdOpened() {
                super.onRewardedAdOpened();

            }
        };

        rewardedAd.show(this,adCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_LOCATION_REQUEST){
            if (resultCode == RESULT_OK){

                mLat = data.getDoubleExtra("lat",23.7104);
                mLon = data.getDoubleExtra("lon",90.40744);
                mAddress = ""+data.getStringExtra("address");

                Log.d(Constants.TAG, "onActivityResult: lat: "+mLat);
                Log.d(Constants.TAG, "onActivityResult: lon: "+mLon);
                Log.d(Constants.TAG, "onActivityResult: address: "+mAddress);

                if (mAddress.isEmpty()){
                    Toast.makeText(this, "Location is not selected. Please try again.", Toast.LENGTH_LONG).show();
                }else{
                    tvLocation.setText(mAddress);
                }

            }else{
                Toast.makeText(this, "Location is not selected. Please try again.", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try{
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showOnLocationAlert();
            }else{
                Intent intent = new Intent(PostForTuitionActivity.this, SelectLocationActivity.class);
                startActivityForResult(intent,SELECT_LOCATION_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showOnLocationAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please turn on your location.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateUi() {
        etClass.setText("");
        etSalary.setText("");
        tvLocation.setText("");
        etSubject.setText("");
        etTittle.setText("");
    }

    private String getPostId(String documentId) {
        char[] arrays = documentId.toCharArray();
        int sum = 0;
        for (int i=0; i<arrays.length; i++){
            sum += (i+1)*arrays[i];
        }
        return String.valueOf(sum);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isUpdate){
            //private EditText etTittle,etSalary,etClass,etSubject;
            //    private Dialog loadingDialog;
            //    private TextView tvLocation,tvTittle;
            try{
                etTittle.setText(mAdInfo.getTittle());
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                etSalary.setText(mAdInfo.getSalary());
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                etClass.setText(mAdInfo.getStudentClass());
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                etSubject.setText(mAdInfo.getSubject());
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                tvLocation.setText(mAdInfo.getLocation());
                mAddress = mAdInfo.getLocation();
            }catch (Exception e){
                e.printStackTrace();
            }

            for (int i=0; i<Constants.LANGUAGE_MEDIUM.length;i++){
                try{
                    if (mAdInfo.getLanguage().equals(Constants.LANGUAGE_MEDIUM[i])){
                        spinnerLanguage.setSelection(i);
                        break;
                    }
                }catch (Exception e){
                    Log.d(Constants.TAG, "onResume: getLanguage is null");
                    e.printStackTrace();
                }
            }
            for (int i=0; i<Constants.WEEKLY_SCHEDULE.length;i++){
                try{
                    if (mAdInfo.getSchedule().equals(Constants.WEEKLY_SCHEDULE[i])){
                        spinnerSchedule.setSelection(i);
                        break;
                    }
                }catch (Exception e){
                    Log.d(Constants.TAG, "onResume: getSchedule is null");
                    e.printStackTrace();
                }
            }

        }
    }
}
