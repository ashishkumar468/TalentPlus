package com.example.talentplusapplication.video.adapter.holder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.views.CircularImageView;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;
import com.volokh.danylo.video_player_manager.utils.Logger;


public class VideoViewHolder extends RecyclerView.ViewHolder {

    public final FrameLayout mPlayer;
    public final TextView mTitle;
    public final TextView mUser_Name;
    public final TextView mUser_post_description;
    public final TextView txt_likeCount;
    public final TextView txt_shareCount;
    public final TextView txt_dislikeCount;
    public final TextView txt_commentCount;
    public final ImageView mCover;
    public final ImageButton mFab_Comments;
    public final ImageButton mFab_Share;
    public final ImageButton mFab_Like;
    public final CircularImageView mFab_profile;

    public final TextView mVisibilityPercents;
    public final ProgressBar mProgressBar;

    public VideoViewHolder(View view) {
        super(view);
        txt_shareCount = view.findViewById(R.id.txt_shareCount);
        txt_commentCount = view.findViewById(R.id.txt_commentCount);
        txt_dislikeCount = view.findViewById(R.id.txt_dislikeCount);
        txt_likeCount = view.findViewById(R.id.txt_likeCount);
        mFab_Comments = view.findViewById(R.id.fab_comment);
        mFab_Like = view.findViewById(R.id.fab_like);
        mFab_Share = view.findViewById(R.id.fab_share);
        mFab_profile = view.findViewById(R.id.fab_profile);
        mPlayer = view.findViewById(R.id.media_container);
        mTitle = (TextView) view.findViewById(R.id.title);
        mUser_Name = (TextView) view.findViewById(R.id.user_name);
        mUser_post_description = (TextView) view.findViewById(R.id.user_post_description);
        mCover = (ImageView) view.findViewById(R.id.cover);
        mVisibilityPercents = (TextView) view.findViewById(R.id.visibility_percents);
        mProgressBar = view.findViewById(R.id.pb_video);
    }

    public void init(PostDtoListProxy item) {
        itemView.setTag(this);
        if (item.getUserName() != null) {
            mUser_Name.setText("@" + item.getUserName());
        } else {
            mUser_Name.setText("talentPlusUser");
        }
        if (item.getPostTitle() != null) {
            mUser_post_description.setText("" + item.getPostTitle());
        } else {
            mUser_post_description.setText("");
        }
        if (item.isLike()) {
            mFab_Like.setBackgroundTintList(MyApplication.getAppContext().getResources().getColorStateList(R.color.post_like));
        } else {
            mFab_Like.setBackgroundTintList(MyApplication.getAppContext().getResources().getColorStateList(R.color.white));

        }
        if (item.getThumbsUpCount() != null) {
            txt_likeCount.setText("" + item.getThumbsUpCount());
        } else {
            txt_likeCount.setText("0");
        }
        if (item.getCommentsCount() != null) {
            txt_commentCount.setText("" + item.getCommentsCount());
        } else {
            txt_commentCount.setText("0");
        }
        if (item.getSharesCount() != null) {
            txt_shareCount.setText("" + item.getSharesCount());
        } else {
            txt_shareCount.setText("0");
        }
        if (item.getProfilePictureUrl() != null) {
            Glide.with(MyApplication.getAppContext()).load(item.getProfilePictureUrl()).into(mFab_profile);
        }
        mCover.setVisibility(View.VISIBLE);
        Glide.with(MyApplication.getAppContext()).load(item.getVideoUrl()).into(mCover);
    }
}
