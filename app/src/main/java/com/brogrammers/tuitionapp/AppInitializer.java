package com.brogrammers.tuitionapp;

import android.app.Application;

public class AppInitializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationHelper.initialize(this);

    }
}
