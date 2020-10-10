package com.brogrammers.tuitionapp.views.otpactivity;

import android.content.Intent;

import com.brogrammers.tuitionapp.BasePresenter;
import com.brogrammers.tuitionapp.BaseView;

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
