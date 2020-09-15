package com.brogrammers.tutionbd.views.loginactivity;

import android.content.Context;
import android.content.Intent;

import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.views.otpactivity.OtpVarificationActivity;

public class LoginActivityPresenter implements LoginActivityVP.Presenter {
    private Context context;
    private LoginActivityVP.View view;

    public LoginActivityPresenter(Context context, LoginActivityVP.View view){
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }
    @Override
    public void onGoButtonClicked(String userName,final String mobileNumber) {
        if (userName.isEmpty()){
            view.onNotifyEdittextName();
            return;
        }
        if (!isValidPhoneNumber(mobileNumber)){
            view.onNotifyEdittextPhone();
            return;
        }

        Intent intent = new Intent(context, OtpVarificationActivity.class);
        intent.putExtra("user_name",userName);
        intent.putExtra("mobile_number",mobileNumber);
        view.onNavigateToActivity(intent);
    }

    @Override
    public void onRetryButtonClicked() {
        if (ApplicationHelper.getUtilsHelper().isConnected()){
            view.onDismissNoNetworkDialog();
        }else view.onShowNoNetworkDialog();
    }

    @Override
    public void onExceptionHandle(String message) {
        view.onShowSnackbarMessage(message);
    }

    @Override
    public void onStart() {
        this.view.onShowNoNetworkDialog();
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
}
