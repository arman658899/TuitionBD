package com.brogrammers.tuitionapp;

import android.app.Application;


public class ApplicationHelper {
    private static DatabaseHelper databaseHelper;
    private static UtilsHelper utilsHelper;
    static void initialize(Application application){
        databaseHelper = DatabaseHelper.getInstance(application);
        databaseHelper.init();
        utilsHelper = UtilsHelper.getInstance(application);
    }

    public static DatabaseHelper getDatabaseHelper(){
        return databaseHelper;
    }

    public static UtilsHelper getUtilsHelper(){
        return utilsHelper;
    }
}
