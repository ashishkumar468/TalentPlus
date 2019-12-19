package com.example.talentplusapplication.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talentplusapplication.R;

import java.util.ArrayList;

public class VideoPlayAdapter  extends RecyclerView.Adapter<VideoPlayAdapter.VideoViewHolder> {

    private  Context mContext;
    private ArrayList<String> videoUrlList;


    public VideoPlayAdapter(Context mContext, ArrayList<String> videoUrlList) {
        this.mContext = mContext;
        this.videoUrlList = videoUrlList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.video_row, parent, false);
        return new VideoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final VideoViewHolder holder, int position) {

        holder.video_one.setVideoPath(videoUrlList.get(position));
        holder.video_one.seekTo(1);


        holder.play_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.video_one.start();
                holder.play_one.setVisibility(View.GONE);

            }
        });


    }

    @Override
    public int getItemCount() {
        return videoUrlList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        VideoView video_one;
        ImageView play_one;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            video_one =itemView.findViewById(R.id.videoview_one);
            play_one=itemView.findViewById(R.id.playbutton_one);
        }
    }
}
