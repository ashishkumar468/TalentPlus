package com.example.talentplusapplication;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class MyApplication extends Application {

    private static Context context;

    /*public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }*/

    private static Application sApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
    public static Context getAppContext() {
        return  getApplication().getApplicationContext();
    }

    public static Application getApplication() {

        return sApplication;
    }

    public static int getHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getApplication().getApplicationContext().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        ((WindowManager) getAppContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displaymetrics);

        return displaymetrics.heightPixels;
    }

    public static int getWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getApplication().getApplicationContext().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        ((WindowManager) getAppContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displaymetrics);

        return displaymetrics.widthPixels;
    }

}
