package com.example.talentplusapplication.ui.user.follow_unfollow.FollowUnfollowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.Proxy.FollowersDtoProxy;
import com.example.talentplusapplication.Proxy.FollowingDtoProxy;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.ui.user.follow_unfollow.FollowUnfollowActivity;

import java.util.ArrayList;
import java.util.List;

public class FollowUnFollowAdapter extends RecyclerView.Adapter<FollowUnFollowAdapter.FollowUnFollowViewHolder> {

    private final boolean isFollow;
    private List<FollowingDtoProxy> followingDtoList = new ArrayList<>();
    private List<FollowersDtoProxy> userList = new ArrayList<>();
    private Context mContext;
    private Glide mGlide;
    private OnLoadThumbnailListener onLoadThumbnailListener;


   /* public FollowUnFollowAdapter(Context mContext, List<FollowersDtoProxy> urlList, OnLoadDiscoverInterface listener, boolean is_follow) {
        this.userList = urlList;
        this.mContext = mContext;
        this.isFollow = is_follow;
        this.onLoadThumbnailListener = listener;
    }*/

    public FollowUnFollowAdapter(FollowUnfollowActivity mContext, List<FollowersDtoProxy> urlList,List<FollowingDtoProxy> followingDtoList, FollowUnfollowActivity listener, boolean is_follow) {
        this.followingDtoList = followingDtoList;
        this.mContext = mContext;
        this.isFollow = is_follow;
        this.userList = urlList;
        this.onLoadThumbnailListener = listener;
    }

    @NonNull
    @Override
    public FollowUnFollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_following_row, parent, false);
        FollowUnFollowViewHolder postViewHolder = new FollowUnFollowViewHolder(view);
        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FollowUnFollowViewHolder holder, int position) {

        if (isFollow) {
            holder.btn_followUnfollow.setText("Following");

            FollowingDtoProxy followingDtoProxy = followingDtoList.get(position);
            if (followingDtoProxy.getUserDto().getUserName() != null) {
                holder.txt_userId.setText("@" + followingDtoProxy.getUserDto().getUserName());
            }

            if (followingDtoProxy.getUserDto().getfName() != null && followingDtoProxy.getUserDto().getlName() != null) {
                holder.txt_userName.setText(followingDtoProxy.getUserDto().getfName() + " " + followingDtoProxy.getUserDto().getlName());
            } else if (followingDtoProxy.getUserDto().getfName() != null) {
                holder.txt_userName.setText(followingDtoProxy.getUserDto().getfName());
            } else if (followingDtoProxy.getUserDto().getlName() != null) {
                holder.txt_userName.setText(followingDtoProxy.getUserDto().getlName());
            } else {
                holder.txt_userName.setText("Talent Plus");
            }

            if (followingDtoProxy.getUserDto().getProfilePictureUrl() != null) {
                mGlide.with(MyApplication.getAppContext()).load(followingDtoProxy.getUserDto().getProfilePictureUrl()).centerCrop().into(holder.thumbnail);
                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onLoadThumbnailListener.onLoadThumbnailClick(position);

                    }
                });
            }


        } else {
            holder.btn_followUnfollow.setText("Followers");

            FollowingDtoProxy followingDtoProxy = followingDtoList.get(position);
            if (followingDtoProxy.getUserDto().getUserName() != null) {
                holder.txt_userId.setText("@" + followingDtoProxy.getUserDto().getUserName());
            }

            if (followingDtoProxy.getUserDto().getfName() != null && followingDtoProxy.getUserDto().getlName() != null) {
                holder.txt_userName.setText(followingDtoProxy.getUserDto().getfName() + " " + followingDtoProxy.getUserDto().getlName());
            } else if (followingDtoProxy.getUserDto().getfName() != null) {
                holder.txt_userName.setText(followingDtoProxy.getUserDto().getfName());
            } else if (followingDtoProxy.getUserDto().getlName() != null) {
                holder.txt_userName.setText(followingDtoProxy.getUserDto().getlName());
            } else {
                holder.txt_userName.setText("Talent Plus");
            }

            if (followingDtoProxy.getUserDto().getProfilePictureUrl() != null) {
                mGlide.with(MyApplication.getAppContext()).load(followingDtoProxy.getUserDto().getProfilePictureUrl()).centerCrop().into(holder.thumbnail);
                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onLoadThumbnailListener.onLoadThumbnailClick(position);

                    }
                });
            }

        }




    }

    @Override
    public int getItemCount() {
//        if (isFollow){
//            return followingDtoList.size();
//        }else {
//            return userList.size();
//        }
        return followingDtoList.size();
    }

    public interface OnLoadThumbnailListener {
        void onLoadThumbnailClick(int position);
    }

    public class FollowUnFollowViewHolder extends RecyclerView.ViewHolder {

        private final ImageView thumbnail;
        private final TextView txt_userName;
        private final TextView txt_userId;
        private final AppCompatButton btn_followUnfollow;


        public FollowUnFollowViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.imageView_circular);
            txt_userName = itemView.findViewById(R.id.txt_userName);
            txt_userId = itemView.findViewById(R.id.txt_userId);
            btn_followUnfollow = itemView.findViewById(R.id.btn_follow_unfollow);
        }
    }
}