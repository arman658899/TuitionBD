package com.brogrammers.tutionbd.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.beans.AdInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;

import java.util.Calendar;

public class PostForTuitionActivity extends AppCompatActivity {
    private EditText etTittle,etSalary,etLocation,etClass,etSubject;
    private Dialog loadingDialog;

    private CollectionReference collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_for_tuition);

        loadingDialog = ApplicationHelper.getUtilsHelper().getLottieLoadingBeHappy(this);
        loadingDialog.setCancelable(false);

        etTittle = findViewById(R.id.title_et);
        etSubject = findViewById(R.id.fullName_Et);
        etLocation = findViewById(R.id.editTextTextPersonName4);
        etSalary = findViewById(R.id.editTextTextPersonName5);
        etClass = findViewById(R.id.editTextTextPersonName6);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //post for tuition

                String tittle,subject,location,salary,studentClass;
                tittle = ""+etTittle.getText().toString();
                subject = ""+etSubject.getText().toString();
                location = ""+etLocation.getText().toString();
                salary = ""+etSalary.getText().toString();
                studentClass = ""+etClass.getText().toString();

                if (tittle.isEmpty()){
                    etTittle.setError("Please add proper tittle.");
                    etTittle.requestFocus();
                    return;
                }
                if (salary.isEmpty()){
                    etSalary.setError("Please add proper salary.");
                    etSalary.requestFocus();
                    return;
                }
                if (subject.isEmpty()){
                    etSubject.setError("Please add proper subject.");
                    etSubject.requestFocus();
                    return;
                }
                if (studentClass.isEmpty()){
                    etClass.setError("Please add proper class.");
                    etClass.requestFocus();
                    return;
                }
                if (location.isEmpty()){
                    etLocation.setError("Please add proper location.");
                    etLocation.requestFocus();
                    return;
                }

                loadingDialog.show();
                String documentId = collRef.document().getId();

                AdInfo adInfo = new AdInfo(tittle,salary,location,subject,studentClass,getPostId(documentId),documentId,ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid(), Calendar.getInstance().getTimeInMillis());

                collRef.document(documentId)
                        .set(adInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingDialog.dismiss();
                                if (task.isSuccessful()){
                                    updateUi();
                                    Toast.makeText(PostForTuitionActivity.this, "Post uploaded.", Toast.LENGTH_SHORT).show();
                                }else Toast.makeText(PostForTuitionActivity.this, "Failed to upload post.", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });

    }

    private void updateUi() {
        etClass.setText("");
        etSalary.setText("");
        etLocation.setText("");
        etSubject.setText("");
        etTittle.setText("");
    }

    private String getPostId(String documentId) {
        char[] arrays = documentId.toCharArray();
        int sum = 0;
        for (int i=0; i<arrays.length; i++){
            sum += (i+1)*arrays[i];
        }
        return String.valueOf(sum);
    }
}
