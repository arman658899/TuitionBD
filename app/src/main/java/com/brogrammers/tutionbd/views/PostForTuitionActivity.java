package com.brogrammers.tutionbd.views;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.beans.AdInfo;
import com.brogrammers.tutionbd.views.locationactivity.SelectLocationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.util.Calendar;

public class PostForTuitionActivity extends AppCompatActivity {
    private EditText etTittle,etSalary,etClass,etSubject;
    private Dialog loadingDialog;
    private TextView tvLocation;

    private CollectionReference collRef,locationRef;

    private static final int SELECT_LOCATION_REQUEST = 110;
    private double mLat,mLon;
    private String mAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_for_tuition);


        switch (AppPreferences.getProfileType(this)){
            case Constants.PROFILE_FIND_TUITION_TEACHER:{
                collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION_TO_GUARDIAN);
                locationRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION_LOCATION);
                break;
            }
            case Constants.PROFILE_FIND_TUTOR_GUARDIAN:{
                collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUTOR_TO_TEACHER);
                locationRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUTOR_LOCATION);
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
                if (mAddress.isEmpty()){
                    tvLocation.setError("Please add proper location.");
                    tvLocation.requestFocus();
                    return;
                }

                if (ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser()==null){
                    return;
                }

                loadingDialog.show();
                String documentId = collRef.document().getId();

                AdInfo adInfo = new AdInfo(tittle,salary,mAddress,subject,studentClass,getPostId(documentId),documentId,ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid(), Calendar.getInstance().getTimeInMillis());

                collRef.document(documentId)
                        .set(adInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingDialog.dismiss();
                                if (task.isSuccessful()){

                                    GeoFirestore geoFirestore = new GeoFirestore(locationRef);
                                    GeoPoint point = new GeoPoint(mLat,mLon);
                                    geoFirestore.setLocation(documentId,point);

                                    updateUi();
                                    Toast.makeText(PostForTuitionActivity.this, "Post uploaded.", Toast.LENGTH_SHORT).show();
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
