package com.brogrammers.tutionbd.views.mainactivity;

import android.content.Context;

import com.brogrammers.tutionbd.managers.PostManager;

public class MainActivityPresenter implements MainActivityVP.Presenter {
    private Context context;
    private PostManager postManager;
    private MainActivityVP.View view;
    public MainActivityPresenter(Context context, MainActivityVP.View view){
        this.view = view;
        this.context = context;
        postManager = PostManager.getInstance(context);
        //post manager should add before setPresenter.
        this.view.setPresenter(this);
    }

    @Override
    public void onStart() {
        //show all item

    }
}
