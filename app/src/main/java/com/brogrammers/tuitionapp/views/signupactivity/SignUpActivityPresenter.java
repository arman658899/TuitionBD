package com.brogrammers.tuitionapp.views.signupactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;


import com.brogrammers.tuitionapp.AppPreferences;
import com.brogrammers.tuitionapp.ApplicationHelper;
import com.brogrammers.tuitionapp.beans.User;
import com.brogrammers.tuitionapp.listeners.OnImageUploadListener;
import com.brogrammers.tuitionapp.listeners.OnUploadListener;
import com.brogrammers.tuitionapp.managers.ProfileManager;
import com.brogrammers.tuitionapp.views.mainactivity.MainActivity;
import com.brogrammers.tuitionapp.views.loginactivity.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class SignUpActivityPresenter implements SignUpActivityVP.Presenter {
    private Context context;
    private SignUpActivityVP.View view;

    private ProfileManager profileManager;
    public SignUpActivityPresenter(Context context, SignUpActivityVP.View view){
        this.context = context;
        this.view = view;
        profileManager = ProfileManager.getInstance(context);
        this.view.setPresenter(this);
    }

    @Override
    public void onGoButtonClicked(String userName,String gender, String mobileNumber, Bitmap bitmap) {
        if (userName.isEmpty()){
            view.onNotifyEdittextName();
            return;
        }

        if (gender.isEmpty()){
            view.onShowSnackbarMessage("Please select your gender.");
            return;
        }

        if (bitmap==null){
            view.onShowSnackbarMessage("Please select profile image.");
            return;
        }

        if (ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser()!=null){
            view.onLoadingDialog(true);
            profileManager.uploadImage(bitmap, ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid(), new OnImageUploadListener() {
                @Override
                public void onUploaded(String downloadLink) {

                    //uid and document id same!
                    User user = new User(
                            userName,
                            gender,
                            "",
                            mobileNumber,
                            ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid(),
                            downloadLink,
                            ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid(),  //profileManager.getUserDocumentId(),
                            "",
                            "",
                            ""
                    );

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("userName",userName);
                    hashMap.put("gender",gender);
                    hashMap.put("year","");
                    hashMap.put("userMobile",mobileNumber);
                    hashMap.put("userUid",ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid());
                    hashMap.put("userImageLink",downloadLink);
                    hashMap.put("documentId",ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid());
                    hashMap.put("college","");
                    hashMap.put("subject","");
                    hashMap.put("idCardLink","");

                    profileManager.createUserHashMap(hashMap, new OnUploadListener() {
                        @Override
                        public void onUploaded() {
                            view.onLoadingDialog(false);

                            AppPreferences.UserInfo.setUserInfo(context,user);
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            view.onNavigateToActivity(intent);
                        }

                        @Override
                        public void onFailed() {
                            view.onLoadingDialog(false);
                            view.onShowSnackbarMessage("Failed to create user. Please try again.");
                        }
                    });
                }
            });

        }else{
            view.onShowSnackbarMessage("Your are not logged in.");

            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            view.onNavigateToActivity(intent);
        }

    }

    @Override
    public void onRetryButtonClicked() {
        view.onNetworkDialog(ApplicationHelper.getUtilsHelper().isConnected());
    }

    @Override
    public void onExceptionHandle(String message) {
        view.onShowSnackbarMessage(message);
    }

    @Override
    public void onProfileImageClicked() {
        view.onRequestPermissionForImage();
    }

    @Override
    public Bitmap onCompressImage(String path) {
        return compressImage(path);
    }

    @Override
    public void createUser(Bitmap bitmap, User user) {

    }

    @Override
    public void onStart() {
        view.onNetworkDialog(ApplicationHelper.getUtilsHelper().isConnected());
    }

    private boolean isValidPhoneNumber(String mobileNumber){
        if (mobileNumber.isEmpty()) return false;
        else if (mobileNumber.length()!=11) return false;
        else{
            switch (mobileNumber.substring(0,3)){
                case "014":
                case "015":
                case "016":
                case "017":
                case "018":
                case "019": return true;
                default: return false;
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
            compressedImage = new Compressor(context.getApplicationContext())
                    .compressToBitmap(imageFile);

        } catch (IOException e) {
            Log.d("CompressImage Error", e.getMessage());
        }
        return  compressedImage;
    }

}
