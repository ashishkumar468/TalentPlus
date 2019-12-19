package com.example.talentplusapplication.video.adapter.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.talentplusapplication.R;
import com.example.talentplusapplication.views.CircularImageView;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;


public class VideoViewHolder extends RecyclerView.ViewHolder{

    public final VideoPlayerView mPlayer;
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

    public VideoViewHolder(View view) {
        super(view);
        txt_shareCount = view.findViewById(R.id.txt_shareCount);
        txt_commentCount = view.findViewById(R.id.txt_commentCount);
        txt_dislikeCount = view.findViewById(R.id.txt_dislikeCount);
        txt_likeCount = view.findViewById(R.id.txt_likeCount);
        mFab_Comments = view.findViewById(R.id.fab_comment);
        mFab_Like = view.findViewById(R.id.fab_like);
        mFab_Share= view.findViewById(R.id.fab_share);
        mFab_profile = view.findViewById(R.id.fab_profile);
        mPlayer = (VideoPlayerView) view.findViewById(R.id.player);
        mTitle = (TextView) view.findViewById(R.id.title);
        mUser_Name = (TextView) view.findViewById(R.id.user_name);
        mUser_post_description = (TextView) view.findViewById(R.id.user_post_description);
        mCover = (ImageView) view.findViewById(R.id.cover);
        mVisibilityPercents = (TextView) view.findViewById(R.id.visibility_percents);
    }
}
