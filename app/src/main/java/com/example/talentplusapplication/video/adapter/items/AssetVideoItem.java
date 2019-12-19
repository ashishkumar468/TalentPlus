package com.example.talentplusapplication.video.adapter.items;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
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
//        mAssetFileDescriptor = assetFileDescriptor;
        mImageLoader = imageLoader;
        mImageResource = imageResource;
    }

    public AssetVideoItem(String title, VideoPlayerManager<MetaData> videoPlayerManager, Picasso imageLoader, int imageResource) {
        super(videoPlayerManager);
        mTitle = title;
        mImageLoader = imageLoader;
        mImageResource = imageResource;

//        mAssetFileDescriptor = null;
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
//        mImageLoader.load(mPostList.getVideoUrl()).into(viewHolder.mCover);
        Glide.with(MyApplication.getAppContext()).load(mPostList.getVideoUrl()).into(viewHolder.mCover);
    }

   /* @Override
    public void updateCover(int position, VideoViewHolder viewHolder) {
//        mImageLoader.load(mTitle).into(viewHolder.mCover);
//        Glide.with(MyApplication.getAppContext()).load(mPostList.getVideoUrl()).centerCrop().into(viewHolder.mCover);

    }*/

    public String currentItem() {
        return mPostList.getVideoUrl();
    }

    @Override
    public void playNewVideo(MetaData currentItemMetaData, VideoPlayerView player, VideoPlayerManager<MetaData> videoPlayerManager) {
//        videoPlayerManager.playNewVideo(currentItemMetaData, player, mAssetFileDescriptor);

        assert mPostList.getVideoUrl() != null;
        String returnFileName=returnVideoName(mPostList.getVideoUrl());

        try {

            File file = new File(BaseCameraActivity.getAndroidMoviesFolderInternalStorage(), returnFileName);
            if (file.exists()) {

//               boolean isCorrupt= new VideosDownloader(MyApplication.getAppContext()).onCheckDownloadFileIsCorruptOrNot(mPostList.getVideoUrl());
//               if (!isCorrupt){
                Log.e(TAG,"file is exist "+getSDCardUrlPath(returnFileName));
                   videoPlayerManager.playNewVideo(null, player,
                           getSDCardUrlPath(returnFileName));
//               }else {
//                   videoPlayerManager.playNewVideo(null, player,
//                           mPostList.getVideoUrl());
//               }


            }else {
                Log.e(TAG,"file is not  exist "+ mPostList.getVideoUrl());
                videoPlayerManager.playNewVideo(null, player,
                        mPostList.getVideoUrl());
            }

            player.addMediaPlayerListener(new MediaPlayerWrapper.MainThreadMediaPlayerListener() {
                @Override
                public void onVideoSizeChangedMainThread(int width, int height) {
                }

                @Override
                public void onVideoPreparedMainThread() {
                    // When video is prepared it's about to start playback. So we hide the cover
                }

                @Override
                public void onVideoCompletionMainThread() {
                }

                @Override
                public void onErrorMainThread(int what, int extra) {
//                    Log.e(TAG,"Player what "+ what);
//                    Log.e(TAG,"Player state vvvv  "+ player.getCurrentState());

//                    File f = new File(getSDCardUrlPath(returnFileName));
//                    if (f.exists()){
//                        videoPlayerManager.playNewVideo(null, player,
//                                mPostList.getVideoUrl());
//                        Boolean deleted = f.delete();
//                        if (deleted){
//                            Log.e(TAG,"Player deleted "+ deleted);
//                        }
//                    }

                }

                @Override
                public void onBufferingUpdateMainThread(int percent) {
                }

                @Override
                public void onVideoStoppedMainThread() {
                    // Show the cover when video stopped
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"Player exception "+ e);
        }
    }

    private String getSDCardUrlPath(String returnFileName) {
       return BaseCameraActivity.getAndroidMoviesFolderInternalStorage() + "/" + returnFileName;
    }

    public String returnVideoName(String originalUrl) {

        String segments[] = originalUrl.split("/");
        String s = "\\?dl";
        String segment[] = segments[segments.length - 1].split(s);
        Log.d(TAG, "setDataToUI: " + segment[0]);
//        String returnUrl=segment[0];

        return segment[0];
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
