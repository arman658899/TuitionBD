package com.brogrammers.tutionbd.views.findtuitionortutoractivity;

import android.content.Context;
import android.content.Intent;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.managers.PostManager;
import com.brogrammers.tutionbd.views.PostForTuitionActivity;
import com.brogrammers.tutionbd.views.loginactivity.LoginActivity;

public class FindTuitionOrTutorActivityPresenter implements FindTuitionOrTutorActivityVP.Presenter {
    private Context context;
    private PostManager postManager;
    private FindTuitionOrTutorActivityVP.View view;
    public FindTuitionOrTutorActivityPresenter(Context context, FindTuitionOrTutorActivityVP.View view){
        this.view = view;
        this.context = context;
        postManager = PostManager.getInstance(context);
        //post manager should add before setPresenter.
        this.view.setPresenter(this);
    }

    @Override
    public void onStart() {
        //show all item
        if (ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser()==null){
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            view.navigateToActivity(intent);
        }else{
            view.onSetupProfileUi();
        }

    }

    @Override
    public void onPostButtonClick() {
        Intent intent = new Intent(context, PostForTuitionActivity.class);
        view.navigateToActivity(intent);
    }
}
