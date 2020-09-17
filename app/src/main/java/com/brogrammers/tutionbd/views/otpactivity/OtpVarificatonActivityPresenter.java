package com.brogrammers.tutionbd.views.otpactivity;

import android.content.Context;
import android.content.Intent;

import com.brogrammers.tutionbd.listeners.OnExistListener;
import com.brogrammers.tutionbd.managers.ProfileManager;
import com.brogrammers.tutionbd.views.UserTypeActivity;
import com.brogrammers.tutionbd.views.mainactivity.MainActivity;
import com.brogrammers.tutionbd.views.signupactivity.SignUpActivity;

public class OtpVarificatonActivityPresenter implements OtpVerificationActivityVP.Presenter {
    private Context context;
    private OtpVerificationActivityVP.View view;

    private ProfileManager profileManager;
    public OtpVarificatonActivityPresenter(Context context, OtpVerificationActivityVP.View view){
        this.context = context;
        this.view = view;
        profileManager = ProfileManager.getInstance(context);
        this.view.setPresenter(this);
    }

    @Override
    public void onVerifyButtonClicked(String otpCode) {
        if (otpCode.isEmpty()) return;
        view.onShowLoadingDialog();
        view.onVerifyOtpCode(otpCode);
    }

    @Override
    public void onResendButtonClicked() {
        view.onResendOtpCode();
        view.onMakeResendButtonGone();
    }

    @Override
    public void onBackButtonClicked() {
        view.onBackPressedClicked();
    }

    @Override
    public void onTimeOut() {
        view.onMakeResendButtonVisible();
    }

    @Override
    public void onCheckNewUserOrNot(String userUid) {
        profileManager.checkUserExitOnDbOrNot(userUid, new OnExistListener() {
            @Override
            public void onExist(boolean isExist) {
                view.onDismissLoadingDialog();
                if (isExist){
                    Intent intent = new Intent(context, UserTypeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    view.onNavigateToActivity(intent);
                }else{
                    Intent intent = new Intent(context, SignUpActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    view.onNavigateToActivity(intent);
                }
            }

            @Override
            public void onError() {
                view.onShowSnackbarMessage("Something error happened. Please try again.");
            }
        });
    }

    @Override
    public void onStart() {
        view.onSendOtpCode();
    }
}
