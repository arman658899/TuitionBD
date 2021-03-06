package com.brogrammers.tuitionapp.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.brogrammers.tuitionapp.ApplicationHelper;
import com.brogrammers.tuitionapp.Constants;
import com.brogrammers.tuitionapp.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class AddIDCardPhotoActivity extends AppCompatActivity {
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private Toolbar toolbar;
    ImageView imageView;
    Bitmap selectedImageBitmap;
    Dialog loadingDialog;
    //from intent
    private String CHILD_KEY_NID;
    private boolean isProfile = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_i_d_card_photo);

        CHILD_KEY_NID = getIntent().getStringExtra("child");
        isProfile = getIntent().getBooleanExtra("mode",false);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (isProfile){
            toolbar.setTitle("Update Profile Photo");
        }else toolbar.setTitle("Update Student ID/NID Photo");

        imageView = findViewById(R.id.imageView12);

        loadingDialog = ApplicationHelper.getUtilsHelper().getLoadingDialog(this);
        loadingDialog.setCancelable(false);

        //add image
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        //upload image
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageBitmap!=null){
                    loadingDialog.show();
                    uploadImage();
                }else Toast.makeText(AddIDCardPhotoActivity.this, "Please select photo of Student ID or NID", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadImage() {
        StorageReference storageIDs;
        if (isProfile){
            storageIDs = ApplicationHelper.getDatabaseHelper().getStorage().getReference(Constants.DB_USERS);
        }else storageIDs = ApplicationHelper.getDatabaseHelper().getStorage().getReference("ID_PHOTOS");
        final StorageReference imageName = storageIDs.child(ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] data = baos.toByteArray();

        imageName.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put(CHILD_KEY_NID,uri.toString());

                            CollectionReference collUsers = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_USERS);

                            collUsers.document(ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid())
                                    .set(hashMap, SetOptions.merge())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            loadingDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                selectedImageBitmap = null;
                                                Toast.makeText(AddIDCardPhotoActivity.this, "Photo uploaded.", Toast.LENGTH_SHORT).show();
                                            }else
                                                Toast.makeText(AddIDCardPhotoActivity.this, "Failed to upload photo.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                }else{
                    loadingDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.dismiss();
            }
        });
    }

    private void requestPermission () {

        if (PackageManager.PERMISSION_GRANTED !=
                ContextCompat.checkSelfPermission(AddIDCardPhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddIDCardPhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(AddIDCardPhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);
            } else {
                //Yeah! I want both block to do the same thing, you can write your own logic, but this works for me.
                ActivityCompat.requestPermissions(AddIDCardPhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                .start(AddIDCardPhotoActivity.this);
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
                            selectedImageBitmap = compressImage(resultUri.getPath());
                        }

                        if (selectedImageBitmap!=null){
                            imageView.setVisibility(View.VISIBLE);
                            findViewById(R.id.button4).setVisibility(View.VISIBLE);
                        }else return;

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

    private Bitmap compressImage(String directoryPath) {
        Bitmap compressedImage = null;
        if (directoryPath.isEmpty() || directoryPath==null) return compressedImage;
        try {
            String extr = Environment.getExternalStorageDirectory().toString();

            //File imageFile = new File(extr+uri);
            File imageFile = new File(directoryPath);
            compressedImage = new Compressor(AddIDCardPhotoActivity.this)
                    .compressToBitmap(imageFile);

        } catch (IOException e) {
            Log.d("CompressImage Error", e.getMessage());
        }
        return  compressedImage;
    }
}