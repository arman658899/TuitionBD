package com.brogrammers.tutionbd.views.otpactivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OtpVarificationActivity extends AppCompatActivity {
    private EditText etOtpCode;
    private Button resendButton,btnVerify;
    private TextView tvOtpMobileNo,tvTimer;


    //temp variable
    private  int sec;

    //timer
    private Timer timerForSecond,timerForLate;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken OTP_RESENDING_TOKEN;
    private String OTP_VERIFICATION_CODE;
    private String formatedPhoneNumber,userName;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_varification);

        formatedPhoneNumber = "+88"+getIntent().getStringExtra("mobile_number");
        userName = ""+getIntent().getStringExtra("user_name");


        tvTimer = findViewById(R.id.textView11);
        etOtpCode = findViewById(R.id.otp_view);
        etOtpCode.requestFocus();
        btnVerify = findViewById(R.id.btn_verify);
        resendButton = findViewById(R.id.button_resend_otp);
        tvOtpMobileNo = findViewById(R.id.textView42);

        tvOtpMobileNo.setText("Please type the verification code sent to \n"+formatedPhoneNumber);


       /* otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otpCode) {

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP_VERIFICATION_CODE,otpCode);
                signInWithPhoneAuthCredential(credential);

            }
        });*/

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpCode = ""+etOtpCode.getText().toString().trim();
                if (otpCode.isEmpty()) return;
                try {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP_VERIFICATION_CODE,otpCode);
                    signInWithPhoneAuthCredential(credential);
                }catch (Exception e){
                    Toast.makeText(OtpVarificationActivity.this, R.string.invalid_otp, Toast.LENGTH_SHORT).show();
                }
            }
        });



        //back button image
        findViewById(R.id.imageview_otp_activity_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendCode(formatedPhoneNumber);
            }
        });

        //checkInternetStatus();


    }

    private void checkInternetStatus() {
        if (ApplicationHelper.getUtilsHelper().isConnected()){
            sendOtpCodeToPhone(formatedPhoneNumber);
        }else{
            final Dialog myDialog = new Dialog(this);
            myDialog.setContentView(R.layout.sampleview_no_internet);
            myDialog.findViewById(R.id.button_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkInternetStatus();
                    myDialog.dismiss();
                }
            });
            myDialog.setCancelable(false);
            //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        }
    }

    private void sendOtpCodeToPhone(String phoneNumber) {
        if (phoneNumber.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Invalid Number")
                    .setMessage("You have provided invalid phone number. Please try again...")
                    .setCancelable(false)
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendOtpCodeToPhone(formatedPhoneNumber);
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

            return;
        }
        Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show();
        Log.d("PHONE_NUMBER", phoneNumber);

       setUpVerificationCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
        Toast.makeText(this, "Verification code is sent", Toast.LENGTH_SHORT).show();

        startTimer();

    }

    private void startTimer() {
        sec = 0;
        tvTimer.setVisibility(View.VISIBLE);

        timerForSecond = new Timer();
        timerForSecond.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTimer.setText(convertToString(sec));
                        sec++;
                        if (sec==60){
                            resendButton.setVisibility(View.VISIBLE);
                            tvTimer.setVisibility(View.INVISIBLE);
                            timerForSecond.cancel();
                        }
                    };
                });
            }
        }, 0, 1000);


    }

    private String convertToString(int i) {
        String text = String.valueOf(i);
        if (text.length()==1) text = "0"+text;
        return text;
    }

    private void setUpVerificationCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull final PhoneAuthCredential phoneAuthCredential) {

                /*try{
                    timerForLate = new Timer();
                    timerForLate.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    signInWithPhoneAuthCredential(phoneAuthCredential);
                                    timerForLate.cancel();
                                };
                            });
                        }
                    }, 7000);
                }catch (Exception e){
                    e.printStackTrace();
                }*/

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(OtpVarificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCodeSent(String s, @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                OTP_VERIFICATION_CODE = s;
                OTP_RESENDING_TOKEN = forceResendingToken;
               //otpView.setText(s);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        showProgressDialog();
        ApplicationHelper.getDatabaseHelper().getAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userUid = ""+ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid();
                           /* User user = new User("username",formatedPhoneNumber,"address",userUid);
                            collRefUsers.document(userUid)
                                    .set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(OtpVarificationActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        }
                                    });*/

                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                pd.dismiss();
                                Toast.makeText(OtpVarificationActivity.this, R.string.invalid_otp,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(OtpVarificationActivity.this,R.string.invalid_otp, Toast.LENGTH_SHORT).show();
                //constraintLayoutOtp.setVisibility(View.VISIBLE);
            }
        });
    }

    public void resendCode(String phoneNumber) {
        if (phoneNumber.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Invalid Number")
                    .setMessage("You have provided invalid phone number. Please try again...")
                    .setCancelable(false)
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resendCode(formatedPhoneNumber);
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

            return;
        }
        resendButton.setVisibility(View.GONE);
        setUpVerificationCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                OTP_RESENDING_TOKEN);

        Toast.makeText(this, "Verfication Code Resending...", Toast.LENGTH_SHORT).show();
        startTimer();
    }

    private void showProgressDialog(){
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timerForSecond!=null) timerForSecond.cancel();
        if (timerForLate!=null) timerForLate.cancel();
    }
}
