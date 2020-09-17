package com.brogrammers.tutionbd.views.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.R;

public class LoginActivity extends AppCompatActivity implements LoginActivityVP.View{
    private EditText etNumber;
    private LoginActivityVP.Presenter presenter;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginActivityPresenter(this,this);

        etNumber = findViewById(R.id.editTextTextPersonName2);

        dialog = ApplicationHelper.getUtilsHelper().getNoNetworkDialog(this);
        dialog.findViewById(R.id.button_retry).setOnClickListener(v -> presenter.onStart());

        findViewById(R.id.go_button).setOnClickListener(v -> presenter.onGoButtonClicked(""+etNumber.getText().toString()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.onStart();
    }

    @Override
    public void onNavigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onNotifyEdittextMobile(String message) {
        etNumber.setError(message);
        etNumber.requestFocus();
    }

    @Override
    public void onShowNoNetworkDialog() {
        dialog.show();
    }

    @Override
    public void onDismissNoNetworkDialog() {
        dialog.dismiss();
    }

    @Override
    public void setPresenter(LoginActivityVP.Presenter presenter) {
        this.presenter = presenter;
    }
}
