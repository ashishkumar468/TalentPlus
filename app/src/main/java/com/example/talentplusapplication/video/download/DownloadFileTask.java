package com.example.talentplusapplication.video.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.example.talentplusapplication.camera.BaseCameraActivity;
import com.example.talentplusapplication.ui.camera.UriHelpers;
import com.example.talentplusapplication.ui.home.HomeFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Task to download a file from Dropbox and put it in the Downloads folder
 */
public class DownloadFileTask extends AsyncTask<String, Void, File> {

    private static final String TAG = "DownloadFileTask";
    private final Context mContext;
    private final DbxClientV2 mDbxClient;
    private final Callback mCallback;
    private Exception mException;


    public interface Callback {
        void onDownloadComplete(File result);
        void onError(Exception e);
    }

    public DownloadFileTask(Context context, DbxClientV2 dbxClient, Callback callback) {
        mContext = context;
        mDbxClient = dbxClient;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);
        if (mException != null) {
            mCallback.onError(mException);
        } else {
            mCallback.onDownloadComplete(result);
        }
    }
    String lastString = null;
    @Override
    protected File doInBackground(String... params) {
        String localUri = params[0];
        FileMetadata mFileMetadata = null;
        Log.e(TAG, "Local Url " + localUri);
        File localFile = UriHelpers.getFileForUri(mContext, Uri.parse(localUri));

        File directory = BaseCameraActivity.getAndroidMoviesFolderInternalStorage();
        int index = localUri.lastIndexOf('/');
        lastString = localUri.substring(index + 1);
        File file = new File(directory, lastString);
        if (directory != null) {


            try (OutputStream outputStream = new FileOutputStream(file)){
//                Log.e("UploadFileTask",""+remoteFolderPath + "/" + remoteFileName);



                // Download the file.
                    mDbxClient.files().download("/s/cmelpfz5b9r7t1l/VIDEO_20191023_182046.mp4","0123456789")
                            .download(outputStream);


//                 SharedLinkMetadata str=   sDbxClient.sharing().createSharedLinkWithSettings(result.getPathDisplay());
//                    str.getUrl();
//                Log.e(TAG,"Shared Link URL : "+str.getUrl()+"   "+
//                        str.toString());

            } catch (DbxException | IOException e) {
                mException = e;
            }

        }

        return null;
    }
}
