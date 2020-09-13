package com.brogrammers.tuitionbd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OtpLogIn extends AppCompatActivity {

    Button go_Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_otp_log_in );

        go_Button=findViewById( R.id.go_button );

        go_Button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( OtpLogIn.this,MainActivity.class ) );
            }
        } );
    }
}