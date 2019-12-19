package com.example.talentplusapplication.ui.camera;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.example.talentplusapplication.camera.BaseCameraActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Async task to upload a file to a directory
 */
public class UploadFileTask extends AsyncTask<String, Void, FileMetadata> {

    private final Context mContext;
    private final DbxClientV2 mDbxClient;
    private final Callback mCallback;
    private final boolean isVideo;
    private Exception mException;
    private String TAG = "UploadFileTask";
    static SharedLinkMetadata sharedLinkMetadata;

    public interface Callback {
        void onUploadComplete(FileMetadata result);

        void onError(Exception e);
    }

    public UploadFileTask(Context context,boolean is_video, DbxClientV2 dbxClient, Callback callback) {
        mContext = context;
        mDbxClient = dbxClient;
        mCallback = callback;
        isVideo=is_video;
        sharedLinkMetadata = null;
    }

    @Override
    protected void onPostExecute(FileMetadata result) {
        super.onPostExecute(result);
        if (mException != null) {
            mCallback.onError(mException);
        } else if (result == null) {
            mCallback.onError(null);
        } else {
            mCallback.onUploadComplete(result);
        }
    }

    @Override
    protected FileMetadata doInBackground(String... params) {
        String localUri = params[0];
        FileMetadata mFileMetadata = null;
        Log.e(TAG, "Local Url " + localUri);
        File localFile = UriHelpers.getFileForUri(mContext, Uri.parse(localUri));

        File directory = BaseCameraActivity.getAndroidMoviesFolderInternalStorage();
        if (directory != null) {

            String lastString = null;
            try (InputStream inputStream = new FileInputStream(localUri)) {
//                Log.e("UploadFileTask",""+remoteFolderPath + "/" + remoteFileName);

                int index = localUri.lastIndexOf('/');
                lastString = localUri.substring(index + 1);

                if (isVideo){
                    mFileMetadata = mDbxClient.files().uploadBuilder("/sunUser/" + lastString)
                            .withMode(WriteMode.OVERWRITE)
                            .uploadAndFinish(inputStream);
                    sharedLinkMetadata = mDbxClient.sharing().createSharedLinkWithSettings("/sunUser/" + lastString);
                }else {
                    mFileMetadata = mDbxClient.files().uploadBuilder("/sunUserPic/" + lastString)
                            .withMode(WriteMode.OVERWRITE)
                            .uploadAndFinish(inputStream);
                    sharedLinkMetadata = mDbxClient.sharing().createSharedLinkWithSettings("/sunUserPic/" + lastString);
                }


//                 SharedLinkMetadata str=   sDbxClient.sharing().createSharedLinkWithSettings(result.getPathDisplay());
//                    str.getUrl();
//                Log.e(TAG,"Shared Link URL : "+str.getUrl()+"   "+
//                        str.toString());

            } catch (DbxException | IOException e) {
                mException = e;
            }

        }

        return mFileMetadata;
    }

    public static String getSharedUrl() {

        if (sharedLinkMetadata != null) {

            return sharedLinkMetadata.getUrl().replace("www", "dl");
        } else {
            return "";
        }

    }
}
