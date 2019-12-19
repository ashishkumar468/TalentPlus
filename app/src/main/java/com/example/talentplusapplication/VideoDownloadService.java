package com.example.talentplusapplication;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.talentplusapplication.ui.home.HomeFragment;

public  class VideoDownloadService extends IntentService {

VideosDownloader mVideosDownloader;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     //         * @param name Used to name the worker thread, important only for debugging.
     */
    public VideoDownloadService() {
        super(VideoDownloadService.class.getName());
        mVideosDownloader=new VideosDownloader(MyApplication.getAppContext());
        Log.e("Service ","Constructor call");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String url = intent.getStringExtra("URL");
//            String path = intent.getStringExtra("path");

        Log.e("Service ","intent call"+" url " +url);

        mVideosDownloader.startVideosDownloading(url);
        this.stopSelf();



    }

//        @Override
//        protected void onHandleWork(@NonNull Intent intent) {
//            String url = intent.getStringExtra("url");
//            String path = intent.getStringExtra("path");
//
//            VideosDownloader mVideosDownloader=new VideosDownloader(this.getActivity());
//            mVideosDownloader.startVideosDownloading(postDtoList.getVideoUrl());
//            this.stopSelf();
//        }
}
