package com.brogrammers.tuitionapp.views.signupactivity;

import android.content.Intent;
import android.graphics.Bitmap;

import com.brogrammers.tuitionapp.BasePresenter;
import com.brogrammers.tuitionapp.BaseView;
import com.brogrammers.tuitionapp.beans.User;

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
        void onGoButtonClicked(String userName, String gender,String mobileNumber,Bitmap bitmap) throws Exception;
        void onRetryButtonClicked();
        void onExceptionHandle(String message);
        void onProfileImageClicked();
        Bitmap onCompressImage(String path);
        void createUser(Bitmap bitmap, User user);
    }
}
