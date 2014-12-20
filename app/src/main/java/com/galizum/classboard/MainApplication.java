package com.galizum.classboard;

import android.app.Application;

public class MainApplication extends Application {
    public static String TAG;
    public static final boolean DEVELOPMENT_MODE = true;

    @Override
    public void onCreate() {
        TAG = getResources().getString(R.string.app_name);

        super.onCreate();
    }
}