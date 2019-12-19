package com.example.talentplusapplication.ui.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.Proxy.SearchProxy;
import com.example.talentplusapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverAdapterViewHolder> {

    private List<SearchProxy> userList = new ArrayList<>();
    private Context mContext;
    private Glide mGlide;
    private OnLoadDiscoverInterface onLoadUserSearchClick;


    public DiscoverAdapter(Context mContext, List<SearchProxy> urlList, OnLoadDiscoverInterface listener) {
        this.mContext = mContext;
        this.userList = urlList;
        this.onLoadUserSearchClick = listener;
    }

    @NonNull
    @Override
    public DiscoverAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_row, parent, false);
        DiscoverAdapterViewHolder postViewHolder = new DiscoverAdapterViewHolder(view);
        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverAdapterViewHolder holder, int position) {

            SearchProxy mSearchProxy = userList.get(position);
            if (mSearchProxy.getUserName() != null) {
                holder.txt_userName.setText("@" + mSearchProxy.getUserName());
            }

            if (mSearchProxy.getFollowersCount() != null) {
                holder.txt_userFollowers
                        .setText("" + mSearchProxy.getFollowersCount());
            }

            if (mSearchProxy.getPostsDtoList() != null) {
                holder.txt_userFollowers
                        .setText("" + mSearchProxy.getPostsDtoList());
            }

            if (mSearchProxy.getfName() != null && mSearchProxy.getlName() != null) {
                holder.txt_userId.setText(mSearchProxy.getfName() + " " + mSearchProxy.getlName());
            } else if (mSearchProxy.getfName() != null) {
                holder.txt_userId.setText(mSearchProxy.getfName());
            } else if (mSearchProxy.getlName() != null) {
                holder.txt_userId.setText(mSearchProxy.getlName());
            } else {
                holder.txt_userId.setText("Talent Plus");
            }

            if (mSearchProxy.getProfilePictureUrl() != null) {
                mGlide.with(MyApplication.getAppContext()).load(mSearchProxy.getProfilePictureUrl()).centerCrop().into(holder.thumbnail);

            }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onLoadUserSearchClick.onLoadUserSearchClick(position);

            }
        });






    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface OnLoadDiscoverInterface {
        void onLoadUserSearchClick(int position);
    }

    public class DiscoverAdapterViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        private final ImageView thumbnail;
        private final TextView txt_userName;
        private final TextView txt_userId;
        private final TextView txt_userFollowers;
        private final TextView txt_userVideos;


        public DiscoverAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            view=itemView;
            thumbnail = itemView.findViewById(R.id.imageView_circular);
            txt_userName = itemView.findViewById(R.id.txt_userName);
            txt_userId = itemView.findViewById(R.id.txt_userFname);
            txt_userFollowers = itemView.findViewById(R.id.txt_userFollowers);
            txt_userVideos = itemView.findViewById(R.id.txt_userVideo);
        }
    }
}
