package com.example.talentplusapplication.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talentplusapplication.R;
import com.example.talentplusapplication.ui.profile.Config;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;


public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeVideoAdapter.YouTubeViewHolder> {
    Context mContext;
    List<VideoItem> videoItems;
   public  YouTubePlayerView mYouTubePlayerView;

    /*public YoutubeVideoAdapter(Context mContext, List<VideoItem> videoItems) {
    }*/

    public YoutubeVideoAdapter(Context mContext, List<VideoItem> videoItems, YouTubePlayerView youTubePlayerView) {
        this.mContext = mContext;
        this.videoItems = videoItems;
        this.mYouTubePlayerView = youTubePlayerView;
    }

    @NonNull
    @Override
    public YouTubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yutube_video_row, parent, false);
        YouTubeViewHolder mYouTubeViewHolder = new YouTubeViewHolder(view);
        return mYouTubeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull YouTubeViewHolder holder, int position) {
//        holder.mFrameLayout.removeAllViews();
//        holder.mFrameLayout.addView(mYouTubePlayerView);
        mYouTubePlayerView.initialize(Config.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    youTubePlayer.loadVideo("eQxk4T65J3c");
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });


        if (videoItems.size() > 0) {
//            Glide.with(mContext).load(videoItems.get(position).getVideoThumbnail()).into(holder.mCover);
//            holder.mPlayer.initialize(Config.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
//                @Override
//                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                    youTubePlayer.loadVideo(videoItems.get(position).videoId);
//                }
//
//                @Override
//                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//                }
//            });


            /*final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                }

                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                    youTubeThumbnailView.setVisibility(View.VISIBLE);
//                    holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
                }
            };*/

//            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext, Config.DEVELOPER_KEY,"eQxk4T65J3c");
//            mContext.startActivity(intent);

            /*holder.youTubeThumbnailView.initialize(Config.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                    youTubeThumbnailLoader.setVideo(videoItems.get(position).getVideoId());

                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    //write something for failure
                }
            });*/
        }

    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }


    public class YouTubeViewHolder extends RecyclerView.ViewHolder {

        //        public final YouTubePlayerSupportFragment mPlayer;
        public  YouTubePlayerView mPlayer = null;
        public FrameLayout mFrameLayout;
        YouTubeThumbnailView youTubeThumbnailView;
        public final TextView mTitle;
        public final ImageView mCover;

        public YouTubeViewHolder(@NonNull View view) {
            super(view);
            mFrameLayout = view.findViewById(R.id.frame_layout);
            mFrameLayout.removeAllViews();
            mFrameLayout.addView(mYouTubePlayerView);
//            mPlayer = view.findViewById(R.id.youtube_player);
//            mPlayer =  view.findViewById(R.id.youtube_player_fragment);
            mTitle = (TextView) view.findViewById(R.id.youtube_title);
            mCover = (ImageView) view.findViewById(R.id.youtube_cover);
//            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);

        }


    }
}
