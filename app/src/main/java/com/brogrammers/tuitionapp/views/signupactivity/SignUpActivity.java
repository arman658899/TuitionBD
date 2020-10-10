package com.brogrammers.tuitionapp.views.signupactivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.brogrammers.tuitionapp.AppPreferences;
import com.brogrammers.tuitionapp.ApplicationHelper;
import com.brogrammers.tuitionapp.R;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.YELLOW;

public class SignUpActivity extends AppCompatActivity implements SignUpActivityVP.View {
    private SignUpActivityVP.Presenter presenter;
    private Dialog networkDialog,loadingDialog;
    private Button go_Button;
    private RadioGroup radioGroup;
    private EditText etUserName;
    private Bitmap selectedImageBitmap;
    private ImageView imageView;
    private String gender = "";

    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final int OTP_REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_signup);

        presenter = new SignUpActivityPresenter(this,this);

        loadingDialog = ApplicationHelper.getUtilsHelper().getLottieLoadingBeHappy(this);
        networkDialog = ApplicationHelper.getUtilsHelper().getNoNetworkDialog(this);
        networkDialog.findViewById(R.id.button_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRetryButtonClicked();
            }
        });

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = findViewById(checkedId);
                gender = button.getText().toString();
            }
        });

        imageView = findViewById(R.id.imageView6);
        etUserName = findViewById(R.id.editTextTextPersonName);

        go_Button=findViewById( R.id.go_button );
        go_Button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    presenter.onGoButtonClicked(
                            ""+etUserName.getText().toString(),
                            gender,
                            AppPreferences.UserInfo.getUserMobileNumber(SignUpActivity.this),
                            selectedImageBitmap
                    );
                } catch (Exception e) {
                    presenter.onExceptionHandle("Please check input values.");
                    e.printStackTrace();
                }
            }
        } );

        imageView.setOnClickListener(v -> presenter.onProfileImageClicked());

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            try{
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    try {
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                        File croppedImage = new File(resultUri.getPath());
                        if (croppedImage.length()>100*1024){
                            selectedImageBitmap = presenter.onCompressImage(resultUri.getPath());
                        }

                        Glide.with(this)
                                .load(selectedImageBitmap)
                                .into(imageView);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Image is not selected.", Toast.LENGTH_SHORT).show();
                    }

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(this, "ImageCropper Exception", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(this, "Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onNetworkDialog(boolean isConnected) {
        if (isConnected) networkDialog.dismiss();
        else networkDialog.show();
    }

    @Override
    public void onLoadingDialog(boolean isLoading) {
        if (isLoading) loadingDialog.show();
        else loadingDialog.dismiss();
    }

    @Override
    public void onNavigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onShowSnackbarMessage(String message) {
        Snackbar.make(etUserName,message, BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(YELLOW)
                .setTextColor(BLACK)
                .show();
    }

    @Override
    public void onNotifyEdittextName() {
        etUserName.setError("Invalid username.");
        etUserName.requestFocus();
    }

    @Override
    public void onNotifyEdittextPhone() {

    }

    @Override
    public void onRequestPermissionForImage() {
        requestPermission();
    }

    @Override
    public void setPresenter(SignUpActivityVP.Presenter presenter) {
        this.presenter = presenter;
    }

    private void requestPermission () {

        if (PackageManager.PERMISSION_GRANTED !=
                ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);
            } else {
                //Yeah! I want both block to do the same thing, you can write your own logic, but this works for me.
                ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);
            }
        } else {

            startImageChoosingOption();
        }

    }
    private void startImageChoosingOption() {
       /* ImagePicker.create(AddProductActivity.this)
                .showCamera(false)
                .limit(1)
                .start();*/

        CropImage.activity()
                .setAspectRatio(1,1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(SignUpActivity.this);
    }
}