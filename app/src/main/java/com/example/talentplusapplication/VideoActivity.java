package com.example.talentplusapplication;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class VideoActivity extends AppCompatActivity {

    private String songUrl_one="https://www.youtube.com/watch?v=dxeU4OmlgBM";
    private String songUrl_two="https://www.youtube.com/watch?v=fHNBxpzQzkw";

    VideoView video_one;
    VideoView video_two;

    ImageView play_one;
    ImageView play_two;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intialization();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void intialization() {


        video_one=findViewById(R.id.videoview_one);
        video_two=findViewById(R.id.videoview_two);
        play_one=findViewById(R.id.playbutton_one);
        play_two=findViewById(R.id.playbutton_two);



        MediaController mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(video_one);
//        String uriPath = "https://www.demonuts.com/Demonuts/smallvideo.mp4"; //update package name
        String uriPath = "https://www.dropbox.com/s/hfb4xe6cpce6303/videoplayback.mp4?dl=0"; //update package name
       Uri uri = Uri.parse(uriPath);
//        video_one.setVideoPath("https://www.ebookfrenzy.com/android_book/movie.mp4");
        video_one.seekTo(1);
        video_one.setMediaController(mediacontroller);
        video_one.setVideoURI(uri);

        video_two.setVideoPath("https://www.ebookfrenzy.com/android_book/movie.mp4");
        video_two.seekTo(1);

        play_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                video_one.requestFocus();
                video_one.start();


//                video_one.start();
                play_one.setVisibility(View.GONE);

                // new DownloadTask(VideoPlayActivity.this, _btnPlayVideo, Utils.downloadVideoUrl);
            }
        });


        play_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                video_two.setVideoPath(songUrl_two);





                video_two.start();
                play_two.setVisibility(View.GONE);
                // new DownloadTask(VideoPlayActivity.this, _btnPlayVideo, Utils.downloadVideoUrl);
            }
        });



    }
}
