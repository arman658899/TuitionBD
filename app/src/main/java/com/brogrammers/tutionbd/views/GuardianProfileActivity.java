package com.brogrammers.tutionbd.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.listeners.OnUploadListener;
import com.brogrammers.tutionbd.managers.ProfileManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.security.Guard;

public class GuardianProfileActivity extends AppCompatActivity {
    CircularImageView circularImageView;
    ImageView ivNID;
    TextView tvName,tvMobile;
    private AlertDialog dialogBoxForUpdate;
    private ProfileManager profileManager;
    private Dialog loadingDialog;
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