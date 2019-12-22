package com.example.talentplusapplication.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.talentplusapplication.Proxy.MyVideoItem;
import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.video.adapter.holder.VideoViewHolder;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;

import java.util.List;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    private final VideoPlayerManager mVideoPlayerManager;
    private final List<MyVideoItem> mList;
    private final Context mContext;
    private final OnLoadFabClickListener mOnLoadFabListener;
    private Integer count = 0;
    private String TAG = "VRVA";

    public VideoRecyclerViewAdapter(VideoPlayerManager videoPlayerManager, Context context, List<MyVideoItem> list, OnLoadFabClickListener onLoadFabClickListener) {
        mVideoPlayerManager = videoPlayerManager;
        mContext = context;
        mOnLoadFabListener = onLoadFabClickListener;
        mList = list;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new VideoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder viewHolder, int position) {
        MyVideoItem videoItem = mList.get(position);
        viewHolder.init((PostDtoListProxy) videoItem);
        viewHolder.mFab_Comments.setOnClickListener(v -> mOnLoadFabListener.onLoadFabClick(position, viewHolder.txt_commentCount));
        viewHolder.mFab_Share.setOnClickListener(v -> mOnLoadFabListener.onLoadFabShareClick(position, viewHolder.mFab_Share, viewHolder.txt_shareCount));
        viewHolder.mFab_Like.setOnClickListener(v -> mOnLoadFabListener.onLoadFabLikeClick(position, viewHolder.mFab_Like, viewHolder.txt_likeCount));
        viewHolder.mFab_profile.setOnClickListener(v -> mOnLoadFabListener.onLoadFabProfileClick(position));
    }

    public interface OnLoadFabClickListener {
        void onLoadFabClick(int position, TextView txt_comment_count);

        void onLoadFabLikeClick(int position, ImageButton mFab_Like, TextView txt_count);

        void onLoadFabShareClick(int position, ImageButton mFab_Share, TextView txt_shareCount);

        void onLoadFabProfileClick(int position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
