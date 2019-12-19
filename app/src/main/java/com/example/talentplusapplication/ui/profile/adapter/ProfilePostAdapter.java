package com.example.talentplusapplication.ui.profile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.PostViewHolder> {

    private List<String> urlList=new ArrayList<>();
    private Context mContext;
    private Glide mGlide;
    private OnLoadThumbnailListener onLoadThumbnailListener;


    public ProfilePostAdapter( Context mContext, List<String> urlList,OnLoadThumbnailListener listener) {
        this.urlList = urlList;
        this.mContext = mContext;
        this.onLoadThumbnailListener=listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_post_row, parent, false);
        PostViewHolder postViewHolder = new PostViewHolder(view);
        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        mGlide.with(MyApplication.getAppContext()).load(urlList.get(position)).centerCrop().into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onLoadThumbnailListener.onLoadThumbnailClick(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }
    public interface  OnLoadThumbnailListener{
        void onLoadThumbnailClick(int position);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{

        private final ImageView thumbnail;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail=itemView.findViewById(R.id.image_thumbnail);
        }
    }
}
