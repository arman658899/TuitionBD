package com.brogrammers.tutionbd.views.signupactivity;

import android.content.Intent;
import android.graphics.Bitmap;

import com.brogrammers.tutionbd.BasePresenter;
import com.brogrammers.tutionbd.BaseView;
import com.brogrammers.tutionbd.beans.User;

public interface SignUpActivityVP {
    interface View extends BaseView<Presenter>{
        void onNetworkDialog(boolean isConnected);
        void onLoadingDialog(boolean isLoading);
        void onNavigateToActivity(Intent intent);
        void onShowSnackbarMessage(String message);
        void onNotifyEdittextName();
        void onNotifyEdittextPhone();
        void onRequestPermissionForImage();
    }

    interface Presenter extends BasePresenter{
        void onGoButtonClicked(String userName, String mobileNumber,Bitmap bitmap) throws Exception;
        void onRetryButtonClicked();
        void onExceptionHandle(String message);
        void onProfileImageClicked();
        Bitmap onCompressImage(String path);
        void createUser(Bitmap bitmap, User user);
    }
}
