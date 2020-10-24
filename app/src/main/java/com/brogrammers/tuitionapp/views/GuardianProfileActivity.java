package com.brogrammers.tuitionapp.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brogrammers.tuitionapp.AppPreferences;
import com.brogrammers.tuitionapp.ApplicationHelper;
import com.brogrammers.tuitionapp.R;
import com.brogrammers.tuitionapp.listeners.OnUploadListener;
import com.brogrammers.tuitionapp.managers.ProfileManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.mikhaellopez.circularimageview.CircularImageView;

public class GuardianProfileActivity extends AppCompatActivity {
    CircularImageView circularImageView;
    ImageView ivNID;
    TextView tvName,tvMobile;
    private AlertDialog dialogBoxForUpdate;
    private ProfileManager profileManager;
    private Dialog loadingDialog;

    //admob
    private AdView adView;
    private FrameLayout adContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_profile);

        profileManager = ProfileManager.getInstance(this);

        loadingDialog = ApplicationHelper.getUtilsHelper().getLoadingDialog(this);
        loadingDialog.setCancelable(false);

        ivNID = findViewById(R.id.imageview_id);
        circularImageView = findViewById(R.id.imageView7);
        tvName = findViewById(R.id.name_Tv);
        tvMobile = findViewById(R.id.contact_number_et);

        //update ui
        Glide.with(this)
                .load(AppPreferences.UserInfo.getUserImage(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(circularImageView);
        Glide.with(this)
                .load(AppPreferences.UserInfo.getUserStudentIDorNID(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivNID);

        tvName.setText(AppPreferences.UserInfo.getUserName(this));
        tvMobile.setText(AppPreferences.UserInfo.getUserMobileNumber(this));


        findViewById(R.id.imageview_back).setOnClickListener(v -> onBackPressed());

        //update name
        findViewById(R.id.textview_edit_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogForUpdate("Update Your Name","userName","UPDATE",tvName);
            }
        });

        //nid
        findViewById(R.id.textview_edit_nid_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuardianProfileActivity.this,AddIDCardPhotoActivity.class);
                intent.putExtra("child","idCardLink");
                intent.putExtra("mode",false);
                startActivity(intent);
            }
        });

        //profile
        findViewById(R.id.textview_edit_profile_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuardianProfileActivity.this,AddIDCardPhotoActivity.class);
                intent.putExtra("child","userImageLink");
                intent.putExtra("mode",true);
                startActivity(intent);
            }
        });

        //initializing admob
        adContainerView = findViewById(R.id.framelayout_adaptive_banner_ad_container);
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_ad));
        adContainerView.addView(adView);
        loadBanner();

    }

    private void loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        AdRequest adRequest = new AdRequest.Builder().build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);

        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }


    private void showAlertDialogForUpdate(String tittle, String childName, String buttonName, TextView root){
        AlertDialog.Builder builder = new AlertDialog.Builder(GuardianProfileActivity.this);
        View itemView = getLayoutInflater().inflate(R.layout.diaglogview_update_data,null,false);

        TextView tvTittle = itemView.findViewById(R.id.textview_tittle);
        EditText etInfo = itemView.findViewById(R.id.textview_details);
        Button buttonCancel = itemView.findViewById(R.id.button_cancel);
        Button buttonAddOrUpdate = itemView.findViewById(R.id.button_add);

        buttonAddOrUpdate.setText(buttonName);
        tvTittle.setText(tittle);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogBoxForUpdate!=null) dialogBoxForUpdate.cancel();
            }
        });

        buttonAddOrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = ""+etInfo.getText().toString();
                if (value.isEmpty()){
                    etInfo.setError("Please add info.");
                    etInfo.requestFocus();
                    return;
                }

                updateChildValue(value,childName,root);

                if (dialogBoxForUpdate!=null) dialogBoxForUpdate.cancel();
            }
        });

        builder.setView(itemView);

        dialogBoxForUpdate = builder.create();
        dialogBoxForUpdate.show();
    }

    private void updateChildValue(String value, String childName, TextView root) {
        loadingDialog.show();
        profileManager.updateUserChildValue(ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid(), childName, value, new OnUploadListener() {
            @Override
            public void onUploaded() {
                loadingDialog.dismiss();
                Toast.makeText(GuardianProfileActivity.this, "Successful.", Toast.LENGTH_SHORT).show();
                root.setText(value);
            }

            @Override
            public void onFailed() {
                loadingDialog.dismiss();
                Toast.makeText(GuardianProfileActivity.this, "Failed to update.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}