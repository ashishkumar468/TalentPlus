package com.example.talentplusapplication.video.adapter.items;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.Proxy.ResponseGetAllPost;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.VideosDownloader;
import com.example.talentplusapplication.camera.BaseCameraActivity;
import com.example.talentplusapplication.video.adapter.holder.VideoViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;
import com.volokh.danylo.video_player_manager.Config;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.MediaPlayerWrapper;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;
import com.volokh.danylo.video_player_manager.utils.Logger;

import java.io.File;
import java.io.IOException;

public class AssetVideoItem extends BaseVideoItem {

    private static final String TAG = AssetVideoItem.class.getSimpleName();
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;

    //    private final AssetFileDescriptor mAssetFileDescriptor;
    private String mTitle = null;

    private final Picasso mImageLoader;
    private final int mImageResource;
    private PostDtoListProxy mPostList = null;

    public AssetVideoItem(String title, AssetFileDescriptor assetFileDescriptor, VideoPlayerManager<MetaData> videoPlayerManager, Picasso imageLoader, int imageResource) {
        super(videoPlayerManager);
        mTitle = title;
        mImageLoader = imageLoader;
        mImageResource = imageResource;
    }

    public AssetVideoItem(String title, VideoPlayerManager<MetaData> videoPlayerManager, Picasso imageLoader, int imageResource) {
        super(videoPlayerManager);
        mTitle = title;
        mImageLoader = imageLoader;
        mImageResource = imageResource;
    }

    public AssetVideoItem(PostDtoListProxy postDtoList, VideoPlayerManager<MetaData> videoPlayerManager, Picasso imageLoader, int imageResource) {
        super(videoPlayerManager);
        mPostList = postDtoList;
        mImageLoader = imageLoader;
        mImageResource = imageResource;
    }


    @Override
    public void update(int position, final VideoViewHolder viewHolder, VideoPlayerManager videoPlayerManager) {
        if (SHOW_LOGS) Logger.v(TAG, "update, position " + position);

        if (mPostList.getUserName() != null) {
            viewHolder.mUser_Name.setText("@" + mPostList.getUserName());
        } else {
            viewHolder.mUser_Name.setText("talentPlusUser");
        }
        if (mPostList.getPostTitle() != null) {
            viewHolder.mUser_post_description.setText("" + mPostList.getPostTitle());
        } else {
            viewHolder.mUser_post_description.setText("");
        }
        if (mPostList.isLike()) {
            viewHolder.mFab_Like.setBackgroundTintList(MyApplication.getAppContext().getResources().getColorStateList(R.color.post_like));
        } else {
            viewHolder.mFab_Like.setBackgroundTintList(MyApplication.getAppContext().getResources().getColorStateList(R.color.white));

        }
        if (mPostList.getThumbsUpCount() != null) {
            viewHolder.txt_likeCount.setText("" + mPostList.getThumbsUpCount());
        } else {
            viewHolder.txt_likeCount.setText("0");
        }
        if (mPostList.getCommentsCount() != null) {
            viewHolder.txt_commentCount.setText("" + mPostList.getCommentsCount());
        } else {
            viewHolder.txt_commentCount.setText("0");
        }
        if (mPostList.getSharesCount() != null) {
            viewHolder.txt_shareCount.setText("" + mPostList.getSharesCount());
        } else {
            viewHolder.txt_shareCount.setText("0");
        }
        if (mPostList.getProfilePictureUrl() != null) {
            Glide.with(MyApplication.getAppContext()).load(mPostList.getProfilePictureUrl()).into(viewHolder.mFab_profile);
        }
        viewHolder.mCover.setVisibility(View.VISIBLE);
        Glide.with(MyApplication.getAppContext()).load(mPostList.getVideoUrl()).into(viewHolder.mCover);
    }

    public String currentItem() {
        return mPostList.getVideoUrl();
    }

    @Override
    public void playNewVideo(MetaData currentItemMetaData, VideoPlayerView player, VideoPlayerManager<MetaData> videoPlayerManager) {
        if (!TextUtils.isEmpty(mPostList.getVideoUrl())) {
            videoPlayerManager.playNewVideo(null, player, mPostList.getVideoUrl());
        }
    }

    @Override
    public void stopPlayback(VideoPlayerManager videoPlayerManager) {
        videoPlayerManager.stopAnyPlayback();
    }

    @Override
    public String toString() {
        return getClass() + ", mTitle[" + mPostList.getVideoUrl() + "]";
    }
}
