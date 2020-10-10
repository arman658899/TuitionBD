package com.brogrammers.tuitionapp.views.loginactivity;

import android.content.Context;
import android.content.Intent;

import com.brogrammers.tuitionapp.ApplicationHelper;
import com.brogrammers.tuitionapp.views.otpactivity.OtpVarificationActivity;

public class LoginActivityPresenter implements LoginActivityVP.Presenter {
    private Context context;
    private LoginActivityVP.View view;

    public LoginActivityPresenter(Context context,LoginActivityVP.View view){
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }
    @Override
    public void onGoButtonClicked(String mobileNumber) {
        if (isValidPhoneNumber(mobileNumber)){
            Intent intent = new Intent(context, OtpVarificationActivity.class);
            intent.putExtra("mobile_number",mobileNumber);
            view.onNavigateToActivity(intent);
        }else view.onNotifyEdittextMobile("Invalid mobile number.");
    }

    @Override
    public void onStart() {
        if (ApplicationHelper.getUtilsHelper().isConnected()){
            view.onDismissNoNetworkDialog();
        }else view.onShowNoNetworkDialog();
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
