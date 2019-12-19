package com.example.talentplusapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;


import androidx.annotation.WorkerThread;

import com.example.talentplusapplication.ui.camera.videocompression.MediaController;
import com.example.talentplusapplication.ui.home.HomeFragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class VideosDownloader {


    private static String TAG = "VideosDownloader";
    //    String url="https://dl.dropboxusercontent.com/s/2gkzkk7rdrwttoa/VID-20191031-WA0004.mp4?dl=0";
//    String url="https://dl.dropboxusercontent.com/s/dzd5mqu4yisvtmc/VID-20191031-WA0003.mp4?dl=0";
    String url = "https://dl.dropbox.com/s/c54awtcwql71ao0/VID-20191026-WA0009.mp4?dl=0";

    Context context;

    FileCache fileCache;
    IVideoDownloadListener iVideoDownloadListener;

    public VideosDownloader(Context context) {
        this.context = context;
        fileCache = new FileCache(context);
    }

    /////////////////////////////////////////////////////////////////
// Start downloading all videos from given urls
    String downloadedPath;

    public String startVideosDownloading(String videoUrl) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                for(int i=0; i<videosList.size(); i++)
//                {
//                    final MediaStore.Video video = videosList.get(i);
//                    String id = video.getId();
//                    String url = video.getUrl();

                String isVideoDownloaded = FileCache.readPreferences(context, url, "false");
                boolean isVideoAvailable = Boolean.valueOf(isVideoDownloaded);
//                    if(isVideoAvailable)
//                    {
                //Download video from url
                downloadedPath = downloadVideo(videoUrl);
                context.sendBroadcast(new Intent(context, HomeFragment.VideoDownloadBroadCastReceiver.class).setAction("ChangeVideoList"));
                //Log.i(TAG, "Vides downloaded at: " + downloadedPath);
//                Activity activity = (Activity) context;
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        FileCache.savePreferences(context, url, "true");
////                                iVideoDownloadListener.onVideoDownloaded(video);
//                    }
//                });
//                    }

//                }
            }
        });
        thread.start();

        return downloadedPath;
    }



/////////////////////////////////////////////////////////////////

    private File tempFile;

    private String downloadVideo(String urlStr) {
        URL url = null;
        File file = null;
        try {

            file = fileCache.getFile(urlStr);
            url = new URL(urlStr);
            long startTime = System.currentTimeMillis();
            URLConnection ucon = null;
            ucon = url.openConnection();
            //this is the total size of the file which we are downloading
//            int totalSize = ucon.getContentLength();
             Integer length = 0;
//            if (file.exists()) {
//
//
////                 length = file.length();
//
//                length =       new AsyncTask<Void, Void, Integer>() {
//
//                    @Override
//                    protected Integer doInBackground(Void... voids) {
//                        int length1 = 0;
//                        try {
//                            URL url = new URL(urlStr);
//                            length1 = url.openConnection().getContentLength();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        return length1;
//                    }
//                }.get();
////                if (totalSize > length) {
////                    file.delete();
////                } else {
////                    return file.getAbsolutePath();
////                }
//            }

            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
            FileOutputStream outStream = new FileOutputStream(file);
            byte[] buff = new byte[5 * 1024];

            //Read bytes (and store them) until there is nothing more to read(-1)
            int len;
            while ((len = inStream.read(buff)) != -1) {
                outStream.write(buff, 0, len);
            }

            //clean up
            outStream.flush();
            outStream.close();
            inStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("TAG", "download  : "+e.getMessage());
            if (file.exists()){
                file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (file.exists()){
                file.delete();
            }
            Log.e("TAG", "download : "+e.getMessage());
        }
        tempFile = file;
//        new VideoCompressor().execute();
        return file.getAbsolutePath();
    }

    ///////////////////////////////////////////////////////////////////////////////////

    private class VideoCompressor extends AsyncTask<Void, Void, Boolean> {

        String compress_path;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return MediaController.getInstance().convertVideo(downloadVideo(url));
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            if (compressed) {
                compress_path = MediaController.cachedFile.getPath();
                Log.e("Compression", "Compression successfully!");
                Log.e("Compressed File Path", "" + MediaController.cachedFile.getPath());
                deleteTempFile();
//                uploadFile(compress_path);
            }
        }
    }


    public void setOnVideoDownloadListener(IVideoDownloadListener iVideoDownloadListener) {
        this.iVideoDownloadListener = iVideoDownloadListener;
    }


    private void deleteTempFile() {

        if (tempFile != null && tempFile.exists()) {

            tempFile.delete();

        }

    }


}