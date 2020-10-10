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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class PostForTuitionActivity extends AppCompatActivity {
    private EditText etTittle,etSalary,etClass,etSubject;
    private Dialog loadingDialog;
    private TextView tvLocation,tvTittle;
    private Spinner spinnerLanguage,spinnerSchedule;
    private CollectionReference collRef,locationRef;

    private static final int SELECT_LOCATION_REQUEST = 110;
    private double mLat=-1,mLon=-1;
    private String mAddress="",schedule = "",language = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_for_tuition);

        tvTittle = findViewById(R.id.textView5);

        switch (AppPreferences.getProfileType(this)){
            case Constants.PROFILE_FIND_TUITION_TEACHER:{
                collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION_TO_GUARDIAN);
                locationRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION_LOCATION);
                tvTittle.setText("Post for Tuition");
                break;
            }
            case Constants.PROFILE_FIND_TUTOR_GUARDIAN:{
                collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUTOR_TO_TEACHER);
                locationRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUTOR_LOCATION);
                tvTittle.setText("Post for Tutor");
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

        loadingDialog = ApplicationHelper.getUtilsHelper().getLottieLoadingBeHappy(this);
        loadingDialog.setCancelable(false);

        etTittle = findViewById(R.id.title_et);
        etSubject = findViewById(R.id.fullName_Et);
        etSalary = findViewById(R.id.editTextTextPersonName5);
        etClass = findViewById(R.id.editTextTextPersonName6);
        tvLocation = findViewById(R.id.editTextTextPersonName4);

        spinnerLanguage = findViewById(R.id.spinner_language);
        spinnerSchedule = findViewById(R.id.spinner_weekly_scedule);

        spinnerLanguage.setAdapter(new ArrayAdapter<String>(PostForTuitionActivity.this,R.layout.sampleview_only_textview,R.id.textview_item,Constants.LANGUAGE_MEDIUM));
        spinnerSchedule.setAdapter(new ArrayAdapter<String>(PostForTuitionActivity.this,R.layout.sampleview_only_textview,R.id.textview_item,Constants.WEEKLY_SCHEDULE));
        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusCheck();
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
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
                String documentId = collRef.document().getId();

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
                        .set(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingDialog.dismiss();
                                if (task.isSuccessful()){
                                    updateUi();
                                    Toast.makeText(PostForTuitionActivity.this, "Post uploaded.", Toast.LENGTH_SHORT).show();
                                    if (mLat ==-1 || mLon == -1) return;

                                    GeoFirestore geoFirestore = new GeoFirestore(locationRef);
                                    GeoPoint point = new GeoPoint(mLat,mLon);
                                    geoFirestore.setLocation(documentId,point);

                                }else Toast.makeText(PostForTuitionActivity.this, "Failed to upload post.", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });

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
}
