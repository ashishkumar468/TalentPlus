package com.example.talentplusapplication.video.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.talentplusapplication.video.adapter.holder.VideoViewHolder;
import com.example.talentplusapplication.video.adapter.items.BaseVideoItem;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.ui.MediaPlayerWrapper;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

import java.util.List;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    private final VideoPlayerManager mVideoPlayerManager;
    private final List<BaseVideoItem> mList;
    private final Context mContext;
    private final OnLoadFabClickListener mOnLoadFabListener;
    private Integer count = 0;
    private String TAG="VRVA";

    public VideoRecyclerViewAdapter(VideoPlayerManager videoPlayerManager, Context context, List<BaseVideoItem> list, OnLoadFabClickListener onLoadFabClickListener) {
        mVideoPlayerManager = videoPlayerManager;
        mContext = context;
        mOnLoadFabListener = onLoadFabClickListener;
        mList = list;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        BaseVideoItem videoItem = mList.get(position);
        View resultView = videoItem.createView(viewGroup, mContext.getResources().getDisplayMetrics().widthPixels);
        return new VideoViewHolder(resultView);
    }
    @Override
    public void onBindViewHolder(VideoViewHolder viewHolder, int position) {
        BaseVideoItem videoItem = mList.get(position);
        videoItem.update(position, viewHolder, mVideoPlayerManager);

//        viewHolder.mPlayer.addMediaPlayerListener(new MediaPlayerWrapper.MainThreadMediaPlayerListener() {
//            @Override
//            public void onVideoSizeChangedMainThread(int width, int height) {
//
//            }
//
//            @Override
//            public void onVideoPreparedMainThread() {
//
//            }
//
//            @Override
//            public void onVideoCompletionMainThread() {
//
//            }
//
//            @Override
//            public void onErrorMainThread(int what, int extra) {
//
////                onRemoveElementFromList(position,viewHolder);
////                notifyItemMoved(position ,position+1);
////                VideoRecyclerViewAdapter.this.notify();
//
//            }
//
//            @Override
//            public void onBufferingUpdateMainThread(int percent) {
//
//            }
//
//            @Override
//            public void onVideoStoppedMainThread() {
//
//            }
//        });

        mOnLoadFabListener.onLoadBindMethod(position,viewHolder);

        viewHolder.mPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewHolder.mPlayer.getVisibility() == View.VISIBLE) {

                    if (count == 0) {

                        if (viewHolder.mPlayer.getIsPlaying()) {
                            viewHolder.mPlayer.pause();
                            count++;
                        }
                    } else {
                        if (!viewHolder.mPlayer.getIsPlaying()) {
                            viewHolder.mPlayer.start();
                            count--;
                        }
                    }

                }
            }
        });
        viewHolder.mFab_Comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnLoadFabListener.onLoadFabClick(position, viewHolder.txt_commentCount);
            }
        });
        viewHolder.mFab_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnLoadFabListener.onLoadFabShareClick(position, viewHolder.mFab_Share, viewHolder.txt_shareCount);
            }
        });
        viewHolder.mFab_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnLoadFabListener.onLoadFabLikeClick(position, viewHolder.mFab_Like, viewHolder.txt_likeCount);
            }
        });
        viewHolder.mFab_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnLoadFabListener.onLoadFabProfileClick(position);
            }
        });
    }

//    private void onRemoveElementFromList(int position, VideoViewHolder viewHolder) {
//            int newPosition = viewHolder.getAdapterPosition();
//            mList.remove(newPosition);
//            notifyItemRemoved(newPosition);
//            notifyItemRangeChanged(newPosition, mList.size());
//    }

    public interface OnLoadFabClickListener {
        void onLoadFabClick(int position, TextView txt_comment_count);
        void onLoadBindMethod(int position, VideoViewHolder holder);

        void onLoadFabLikeClick(int position, ImageButton mFab_Like, TextView txt_count);

        void onLoadFabShareClick(int position, ImageButton mFab_Share, TextView txt_shareCount);

        void onLoadFabProfileClick(int position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
