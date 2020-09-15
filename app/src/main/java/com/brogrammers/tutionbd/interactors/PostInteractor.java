package com.brogrammers.tutionbd.interactors;

import android.content.Context;

import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.DatabaseHelper;

public class PostInteractor {
    private static PostInteractor instance;
    private DatabaseHelper databaseHelper;
    private Context context;
    private PostInteractor(Context context){
        this.context = context;
        databaseHelper = ApplicationHelper.getDatabaseHelper();
    }

    public static PostInteractor getInstance(Context context){
        if (instance == null) instance = new PostInteractor(context);
        return instance;
    }

    //all firebase script will be add here.

}
