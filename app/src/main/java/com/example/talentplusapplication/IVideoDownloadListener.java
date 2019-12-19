package com.example.talentplusapplication;

import android.provider.MediaStore;

public abstract interface IVideoDownloadListener {
    static void onVideoDownloaded(String s) {

    }

    public abstract void onVideoDownloaded(MediaStore.Video video);
    public abstract void onVideoDownloadedComplete(String string);
}
