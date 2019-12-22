package com.example.talentplusapplication.video;

import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.talentplusapplication.BaseActivity;
import com.example.talentplusapplication.Proxy.MyVideoItem;
import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.video.adapter.VideoRecyclerViewAdapter;
import com.example.talentplusapplication.video.adapter.holder.VideoViewHolder;
import com.example.talentplusapplication.video.adapter.items.BaseVideoItem;
import com.volokh.danylo.video_player_manager.Config;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.visibility_utils.calculator.DefaultSingleItemCalculatorCallback;
import com.volokh.danylo.visibility_utils.calculator.ListItemsVisibilityCalculator;
import com.volokh.danylo.visibility_utils.calculator.SingleListViewItemActiveCalculator;
import com.volokh.danylo.visibility_utils.scroll_utils.ItemsPositionGetter;
import com.volokh.danylo.visibility_utils.scroll_utils.RecyclerViewItemPositionGetter;

import java.util.ArrayList;

public class VideoListActivity extends BaseActivity  implements VideoRecyclerViewAdapter.OnLoadFabClickListener
{

    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
    private static final String TAG = VideoListActivity.class.getSimpleName();

    private final ArrayList<MyVideoItem> mList = new ArrayList<>();

    private VideoPlayerRecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    /**
     * ItemsPositionGetter is used by {@link ListItemsVisibilityCalculator} for getting information about
     * items position in the RecyclerView and LayoutManager
     */
    private ItemsPositionGetter mItemsPositionGetter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onHideStatusBarAndToolBar();
        setContentView(R.layout.activity_video_list);
        PostDtoListProxy postDtoListProxy = new PostDtoListProxy();
        postDtoListProxy.setVideoUrl("https://dl.dropboxusercontent.com/s/hfb4xe6cpce6303/videoplayback.mp4?dl=0");
        mList.add(postDtoListProxy);
        mRecyclerView = findViewById(R.id.recycler_view_video_list);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        String url= "";

        VideoRecyclerViewAdapter videoRecyclerViewAdapter = new VideoRecyclerViewAdapter(mVideoPlayerManager, this, mList,this);

        mRecyclerView.setAdapter(videoRecyclerViewAdapter);
        mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager, mRecyclerView);

    }


    /**
     * Here we use {@link SingleVideoPlayerManager}, which means that only one video playback is possible.
     */
    private final VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        // we have to stop any playback in onStop
        mVideoPlayerManager.resetMediaPlayer();
    }

    @Override
    public void onLoadFabClick(int position, TextView txt_comment_count) {

    }

    @Override
    public void onLoadFabLikeClick(int position, ImageButton mFab_Like, TextView txt_count) {

    }

    @Override
    public void onLoadFabShareClick(int position, ImageButton mFab_Share, TextView txt_shareCount) {

    }

    @Override
    public void onLoadFabProfileClick(int position) {

    }
}