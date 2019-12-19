package com.example.talentplusapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.talentplusapplication.ui.home.HomeFragment;

public class VideoDownloadBroadCastReceiver_ extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("VDBROADCAST" ,"");
        if (intent.getAction()=="ChangeVideoList"){

            HomeFragment.VideoDownloadListener.onVideoDownloaded("");
        }

    }
}
