package com.example.talentplusapplication.ui.camera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.camera.BaseCameraActivity;
import com.example.talentplusapplication.camera.widget.AspectFrameLayout;
import com.example.talentplusapplication.camera.widget.EglCore;
import com.example.talentplusapplication.camera.widget.MiscUtils;
import com.example.talentplusapplication.camera.widget.MoviePlayer;
import com.example.talentplusapplication.camera.widget.SpeedControlCallback;
import com.example.talentplusapplication.camera.widget.WindowSurface;
import com.example.talentplusapplication.ui.camera.videocompression.MediaController;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;

public class RecordPlayActivity  extends DropboxActivity implements
        SurfaceHolder.Callback, MoviePlayer.PlayerFeedback {
    private static final String TAG = RecordPlayActivity.class.getSimpleName();

    private AspectFrameLayout frameLayout;
    private ImageView img_thumbnail;
    private SurfaceView mSurfaceView;
    private String[] mMovieFiles;
//    private int mSelectedMovie;
    private boolean mShowStopLabel;
    private MoviePlayer.PlayTask mPlayTask;
    private boolean mSurfaceHolderReady = false;
    String path;;
    String compress_path;;
    private ImageButton btn_selected;
    private  Button play;
    private  boolean is_recorded;
    private  ImageButton cancel;
    private VideoPlayerView video_player;

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        video_player=findViewById(R.id.player_video);
        cancel=findViewById(R.id.btn_cancel);
        frameLayout = (AspectFrameLayout) findViewById(R.id.playMovie_afl);
        img_thumbnail = (ImageView) findViewById(R.id.img_video_thumbnail);
        btn_selected = (ImageButton) findViewById(R.id.btn_selected);
        mSurfaceView = (SurfaceView) findViewById(R.id.playMovie_surface);
         play = (Button) findViewById(R.id.btn_play);
        mSurfaceView.getHolder().addCallback(this);
         path = getIntent().getStringExtra("video_path");
         is_recorded = getIntent().getBooleanExtra("is_record",false);
        mMovieFiles = new String[]{path};

//        new VideoCompressor().execute();

//        updateControls();
        Glide.with(this)
                .load(Uri.fromFile( new File( path ) ))
                .centerCrop()
                .into(img_thumbnail);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent=new Intent(RecordPlayActivity.this,UploadActivity.class);
                mIntent.putExtra("video_path",path);
                mIntent.putExtra("is_record",is_recorded);
                startActivity(mIntent);
                finish();

//                uploadFile(path);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mVideoPlayerManager!=null){
                    mVideoPlayerManager.stopAnyPlayback();
                }

                finish();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img_thumbnail.setVisibility(View.GONE);
                video_player.setVisibility(View.VISIBLE);

                mVideoPlayerManager.playNewVideo(null,video_player,path);


            }
        });
    }


    /*private class VideoCompressor extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return MediaController.getInstance().convertVideo(path);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            if (compressed) {
                compress_path=MediaController.cachedFile.getPath();
                Log.e("Compression", "Compression successfully!");
                Log.e("Compressed File Path", "" + MediaController.cachedFile.getPath());

            }

        }
    }*/

    private final VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });

    @Override
    protected void onResume() {
        Log.d(TAG, "PlayMovieSurfaceActivity onResume");
        super.onResume();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "PlayMovieSurfaceActivity onPause");
        super.onPause();
        // We're not keeping track of the state in static fields, so we need to shut the
        // playback down.  Ideally we'd preserve the state so that the player would continue
        // after a device rotation.
        //
        // We want to be sure that the player won't continue to send frames after we pause,
        // because we're tearing the view down.  So we wait for it to stop here.
        if (mPlayTask != null) {
            stopPlayback();
            mPlayTask.waitForStop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPlayTask != null) {
            mPlayTask.requestStop();
        }
    }

    private void uploadFile(String fileUri) {
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setCancelable(false);
//        dialog.setMessage("Uploading");
//        dialog.show();


        DbxClientV2 sDbxClient;
        String accessToken="YGfCnQIDO_AAAAAAAAAAK1pjAkU4y85tvL2Z4FjQnWnzpS3xpD9NqGEl5N5095ZK";

        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("examples-v2-demo")
                .withHttpRequestor(new OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
                .build();

        sDbxClient = new DbxClientV2(requestConfig, accessToken);

        new UploadFileTask(this,true, sDbxClient, new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete(FileMetadata result) {
//                dialog.dismiss();

                String message = result.getName() + " size " + result.getSize() + " modified " +
                        DateFormat.getDateTimeInstance().format(result.getClientModified());
                Toast.makeText(RecordPlayActivity.this, message, Toast.LENGTH_SHORT)
                        .show();

            }

            @Override
            public void onError(Exception e) {
//                dialog.dismiss();

                Log.e(TAG, "Failed to upload file.",e );
//                e.printStackTrace();
                Toast.makeText(RecordPlayActivity.this,
                        "An error has occurred",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(fileUri);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // There's a short delay between the start of the activity and the initialization
        // of the SurfaceHolder that backs the SurfaceView.  We don't want to try to
        // send a video stream to the SurfaceView before it has initialized, so we disable
        // the "play" button until this callback fires.
        Log.d(TAG, "surfaceCreated");
        mSurfaceHolderReady = true;
        updateControls();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // ignore
        Log.d(TAG, "surfaceChanged fmt=" + format + " size=" + width + "x" + height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // ignore
        Log.d(TAG, "Surface destroyed");
    }


    /**
     * onClick handler for "play"/"stop" button.
     */
    public void clickPlayStop(@SuppressWarnings("unused") View unused) {
        img_thumbnail.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);

        if (mShowStopLabel) {
            Log.d(TAG, "stopping movie");
            stopPlayback();
            // Don't update the controls here -- let the task thread do it after the movie has
            // actually stopped.
            //mShowStopLabel = false;
            //updateControls();
        } else {
            if (mPlayTask != null) {
                Log.w(TAG, "movie already playing");
                return;
            }

            Log.d(TAG, "starting movie");
            SpeedControlCallback callback = new SpeedControlCallback();
            SurfaceHolder holder = mSurfaceView.getHolder();
            Surface surface = holder.getSurface();

            // Don't leave the last frame of the previous video hanging on the screen.
            // Looks weird if the aspect ratio changes.
            clearSurface(surface);

            MoviePlayer player = null;
            try {
                Log.e(TAG,"Path  in base     " +  path);
                player = new MoviePlayer(
//                        new File(Environment.getExternalStorageDirectory(), mMovieFiles[0]), surface, callback);

                        new File(path), surface, callback);
            } catch (IOException ioe) {
                Log.e(TAG, "Unable to play movie", ioe);
                surface.release();
                return;
            }

            AspectFrameLayout layout = (AspectFrameLayout) findViewById(R.id.playMovie_afl);
//            int width = player.getVideoWidth();
//            int height = player.getVideoHeight();

            int width = layout.getWidth();
            int height = layout.getHeight();

            layout.setAspectRatio((double) width / height);
            //holder.setFixedSize(width, height);

            mPlayTask = new MoviePlayer.PlayTask(player, this);

            mShowStopLabel = true;
            updateControls();
            mPlayTask.execute();
        }
    }

    /**
     * Requests stoppage if a movie is currently playing.
     */
    private void stopPlayback() {
        if (mPlayTask != null) {
            mPlayTask.requestStop();
        }
    }

    @Override   // MoviePlayer.PlayerFeedback
    public void playbackStopped() {
        Log.d(TAG, "playback stopped");
        mShowStopLabel = false;
        mPlayTask = null;
        updateControls();
    }

    /**
     * Updates the on-screen controls to reflect the current state of the app.
     */
    private void updateControls() {

        if (mShowStopLabel) {
            play.setText("Stop");
        } else {
            play.setText("Play");
        }

//        play.setEnabled(mSurfaceHolderReady);
    }

    /**
     * Clears the playback surface to black.
     */
    private void clearSurface(Surface surface) {
        // We need to do this with OpenGL ES (*not* Canvas -- the "software render" bits
        // are sticky).  We can't stay connected to the Surface after we're done because
        // that'd prevent the video encoder from attaching.
        //
        // If the Surface is resized to be larger, the new portions will be black, so
        // clearing to something other than black may look weird unless we do the clear
        // post-resize.
        EglCore eglCore = new EglCore();
        WindowSurface win = new WindowSurface(eglCore, surface, false);
        win.makeCurrent();
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        win.swapBuffers();
        win.release();
        eglCore.release();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        this.finishAffinity();
    }
}