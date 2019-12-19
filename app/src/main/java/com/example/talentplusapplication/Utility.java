package com.example.talentplusapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ashish
 */
public class Utility {


    private Context mContext;

    public Utility(Context context) {
        mContext = context;
    }

    public boolean isConnectingToInternet() {
        if (mContext != null) {

            ConnectivityManager connectivity = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
            }
        }
        return false;
    }


    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void printLog(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }


    public String getFormatedDate(String epochTime) {

       /* //Start time of that day
        Calendar yesterdaysDateStart = Calendar.getInstance();
       *//* yesterdaysDateStart.set(Calendar.DAY_OF_MONTH, 0);
        yesterdaysDateStart.set(Calendar.HOUR_OF_DAY, 0);
        yesterdaysDateStart.set(Calendar.MILLISECOND, -1);*//*
        yesterdaysDateStart.add(Calendar.DAY_OF_YEAR,-1);

        //Start time of that day
        Calendar todaysDateStart = Calendar.getInstance();
        todaysDateStart.set(Calendar.HOUR_OF_DAY, 0);
        todaysDateStart.set(Calendar.MINUTE, 0);
        todaysDateStart.set(Calendar.SECOND, 0);
        todaysDateStart.set(Calendar.MILLISECOND, 0);

        //End time of that day
        Calendar todaysDateEnd = Calendar.getInstance();
        todaysDateEnd.set(Calendar.HOUR_OF_DAY, 24);
        todaysDateEnd.set(Calendar.MINUTE, 0);
        todaysDateEnd.set(Calendar.SECOND, 0);
        todaysDateEnd.set(Calendar.MILLISECOND, 0);


        if (Long.parseLong(epochTime) >= todaysDateStart.getTimeInMillis() && Long.parseLong(epochTime) < todaysDateEnd.getTimeInMillis()) {
            return "today";
        }
        if (Long.parseLong(epochTime) >= yesterdaysDateStart.getTimeInMillis() && Long.parseLong(epochTime) < todaysDateStart.getTimeInMillis()) {
            return "yesterday";
        }*/
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm");
        return sdf.format(new Date(Long.parseLong(epochTime)));
    }

    public void showOneButtonDialog(String message, String btnName) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(mContext);
        aBuilder.setTitle(mContext.getResources().getString(R.string.app_name));
        aBuilder.setMessage("" + message);
        aBuilder.setPositiveButton(btnName, null);
        aBuilder.show();
    }

    public boolean isCurrentDate(String date) {
        //Start time of that day
        Calendar dateFromStart = Calendar.getInstance();
        dateFromStart.setTimeInMillis(Long.parseLong(date));
        dateFromStart.set(Calendar.HOUR_OF_DAY, 0);
        dateFromStart.set(Calendar.MINUTE, 0);
        dateFromStart.set(Calendar.SECOND, 0);
        dateFromStart.set(Calendar.MILLISECOND, 0);

        //End time of that day
        Calendar dateToEnd = Calendar.getInstance();
        dateToEnd.setTimeInMillis(Long.parseLong(date));
        dateToEnd.set(Calendar.HOUR_OF_DAY, 24);
        dateToEnd.set(Calendar.MINUTE, 0);
        dateToEnd.set(Calendar.SECOND, 0);
        dateToEnd.set(Calendar.MILLISECOND, 0);

        return Calendar.getInstance().getTimeInMillis() >= dateFromStart.getTimeInMillis() && dateToEnd.getTimeInMillis() > Calendar.getInstance().getTimeInMillis();
    }

    public boolean isFutureDate(String date) {
        //Start time of that day
        Calendar currentDateFromStart = Calendar.getInstance();
        currentDateFromStart.set(Calendar.HOUR_OF_DAY, 0);
        currentDateFromStart.set(Calendar.MINUTE, 0);
        currentDateFromStart.set(Calendar.SECOND, 0);

        //End time of that day
        Calendar formattedDate = Calendar.getInstance();
        formattedDate.setTimeInMillis(Long.parseLong(date));

        printLog("isFutureDate", "" + currentDateFromStart.getTimeInMillis());
        printLog("isFutureDate", "" + formattedDate.getTimeInMillis());
        return formattedDate.getTimeInMillis() >= currentDateFromStart.getTimeInMillis();
    }


}
