package com.example.talentplusapplication.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.talentplusapplication.R;
import com.example.talentplusapplication.ui.home.adapter.VideoItem;
import com.example.talentplusapplication.ui.home.adapter.YoutubeVideoAdapter;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YouTubePlayActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

    private static final String VIDEO_ID = "t2ziNohlPz4";
    private static final String PLAYLIST_ID = "7E952A67F31C58A3";
    private static final ArrayList<String> VIDEO_IDS = new ArrayList<String>(Arrays.asList(
            new String[]{"t2ziNohlPz4", "8aCYZ3gXfy8", "zMabEyrtPRg"}));


    YouTubePlayerView mYouTubePlayerView;
    YouTubePlayer mPlayer;
    boolean isClick = true;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_video_list);

        setDataToUI();

        /*mYouTubePlayerView = findViewById(R.id.youtube_view);

        // Initializing video player with developer key
        mYouTubePlayerView.initialize(Config.DEVELOPER_KEY, this);

        mYouTubePlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick) {
                    mPlayer.pause();
                    isClick=false;
                }else {
                    isClick=true;
                    mPlayer.play();
                }

//                mPlayer.pause();

            }
        });*/

    }

    public void setDataToUI(){






        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);


//        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
//        linearSnapHelper.attachToRecyclerView(mRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        VideoItem videoItem=new VideoItem("","cwYGzxmUhME");

        List<VideoItem>  video_list=   new ArrayList<>();
        video_list.add(new VideoItem("","cwYGzxmUhME"));
        video_list.add(new VideoItem("","JEySoHbW-Y8"));
        video_list.add(new VideoItem("","cwYGzxmUhME"));
        video_list.add(new VideoItem("","HGNhZG8-BF0"));


        String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY", "6JYIGclVQdw", "LvetJ9U_tVY", "6JYIGclVQdw", "LvetJ9U_tVY", "6JYIGclVQdw", "LvetJ9U_tVY", "6JYIGclVQdw", "LvetJ9U_tVY", "6JYIGclVQdw", "LvetJ9U_tVY"};

//        RecyclerView.Adapter recyclerViewAdapter = new RecyclerViewAdapter(videoIds, this.getLifecycle());
//        mRecyclerView.setAdapter(recyclerViewAdapter);


/*
        YoutubeVideoAdapter mYoutubeVideoAdapter=new YoutubeVideoAdapter(this,video_list);
//
        mRecyclerView.setAdapter(mYoutubeVideoAdapter);*/



//        YouTubePlayerView  mYouTubePlayerView= YoutubeVideoAdapter.YouTubeViewHolder.mPlayer;
        YouTubePlayerView  mYouTubePlayerView=new YouTubePlayerView(this);

       /* YouTubePlayer.OnInitializedListener mListener =new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("cwYGzxmUhME");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        mYouTubePlayerView.initialize(com.example.talentplusapplication.ui.notifications.Config.DEVELOPER_KEY, mListener);*/

        YoutubeVideoAdapter mYoutubeVideoAdapter=new YoutubeVideoAdapter(this,video_list,mYouTubePlayerView);
//
        mRecyclerView.setAdapter(mYoutubeVideoAdapter);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            mPlayer = youTubePlayer;
            mPlayer.loadVideo(Config.YOUTUBE_VIDEO_CODE);

            // Hiding player controls
//            mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
            mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = "Something is wrong";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }
}
