package com.brogrammers.tutionbd.views.otpactivity;

import android.content.Intent;

import com.brogrammers.tutionbd.BasePresenter;
import com.brogrammers.tutionbd.BaseView;

public interface OtpVerificationActivityVP {
    interface View extends BaseView<Presenter>{
        void onNavigateToActivity(Intent intent);
        void onShowLoadingDialog();
        void onDismissLoadingDialog();
        void onShowSnackbarMessage(String message);
        void onShowTimer();
        void onSendOtpCode();
        void onResendOtpCode();
        void onMakeResendButtonVisible();
        void onMakeResendButtonGone();
        void onBackPressedClicked();
        void onVerifyOtpCode(String otpCode);
    }
    interface Presenter extends BasePresenter{
        void onVerifyButtonClicked(String otpCode);
        void onResendButtonClicked();
        void onBackButtonClicked();
        void onTimeOut();
        void onCheckNewUserOrNot(String userUid);

    }
}
