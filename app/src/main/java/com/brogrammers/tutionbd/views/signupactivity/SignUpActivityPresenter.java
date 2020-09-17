package com.brogrammers.tutionbd.views.signupactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;


import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.beans.User;
import com.brogrammers.tutionbd.listeners.OnImageUploadListener;
import com.brogrammers.tutionbd.listeners.OnUploadListener;
import com.brogrammers.tutionbd.managers.ProfileManager;
import com.brogrammers.tutionbd.views.UserTypeActivity;
import com.brogrammers.tutionbd.views.loginactivity.LoginActivity;
import com.brogrammers.tutionbd.views.mainactivity.MainActivity;
import com.brogrammers.tutionbd.views.otpactivity.OtpVarificationActivity;

import java.io.File;
import java.io.IOException;

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
    public void onGoButtonClicked(String userName, String mobileNumber, Bitmap bitmap) {
        if (userName.isEmpty()){
            view.onNotifyEdittextName();
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

                    User user = new User(
                            userName,
                            mobileNumber,
                            ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid(),
                            downloadLink,
                            profileManager.getUserDocumentId()
                    );

                    profileManager.createUser(user, new OnUploadListener() {
                        @Override
                        public void onUploaded() {
                            view.onLoadingDialog(false);

                            AppPreferences.UserInfo.setUserInfo(context,user);

                            Intent intent = new Intent(context, UserTypeActivity.class);
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
