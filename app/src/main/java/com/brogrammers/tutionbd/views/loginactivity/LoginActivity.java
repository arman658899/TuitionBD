package com.brogrammers.tutionbd.views.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.views.mainactivity.MainActivity;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.YELLOW;

public class LoginActivity extends AppCompatActivity implements LoginActivityVP.View {
    private LoginActivityVP.Presenter presenter;
    private Dialog dialog;
    private Button go_Button;
    private EditText etUserName,etMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login);

        presenter = new LoginActivityPresenter(this,this);

        dialog = ApplicationHelper.getUtilsHelper().getNoNetworkDialog(this);
        dialog.findViewById(R.id.button_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRetryButtonClicked();
            }
        });

        etMobile = findViewById(R.id.editTextTextPersonName2);
        etUserName = findViewById(R.id.editTextTextPersonName);

        go_Button=findViewById( R.id.go_button );
        go_Button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    presenter.onGoButtonClicked(""+etUserName.getText().toString(),""+etMobile.getText().toString());
                } catch (Exception e) {
                    presenter.onExceptionHandle("Please check input values.");
                    e.printStackTrace();
                }
            }
        } );
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
    public void onNavigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onShowSnackbarMessage(String message) {
        Snackbar.make(etMobile,message, BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(YELLOW)
                .setTextColor(BLACK)
                .show();
    }

    @Override
    public void onNotifyEdittextName() {
        etUserName.setError("Invalid username.");
        etUserName.requestFocus();
    }

    @Override
    public void onNotifyEdittextPhone() {
        etMobile.setError("Invalid mobile number.");
        etMobile.requestFocus();
    }

    @Override
    public void setPresenter(LoginActivityVP.Presenter presenter) {
        this.presenter = presenter;
    }
}