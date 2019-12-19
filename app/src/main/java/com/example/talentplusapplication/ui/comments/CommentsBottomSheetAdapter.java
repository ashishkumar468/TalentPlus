package com.example.talentplusapplication.ui.comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talentplusapplication.Proxy.CommentsDtoProxy;
import com.example.talentplusapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CommentsBottomSheetAdapter extends RecyclerView.Adapter<CommentsBottomSheetAdapter.CommentsViewHolder> {

    private List<CommentsDtoProxy> commentsDtoProxyArrayList = new ArrayList<>();
    private Context mContext;
    private Glide mGlide;
    private OnLoadCommentListener onLoadCommentListener;


    public CommentsBottomSheetAdapter(BottomSheetFragment mContext, List<CommentsDtoProxy> commentsDtoProxyList, OnLoadCommentListener listener) {
        this.mContext = mContext.getContext();
        this.commentsDtoProxyArrayList = commentsDtoProxyList;
        this.onLoadCommentListener = listener;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        CommentsViewHolder postViewHolder = new CommentsViewHolder(view);
        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {

        CommentsDtoProxy commentsDtoProxy = commentsDtoProxyArrayList.get(position);
        if (commentsDtoProxy.getUserName() != null) {
            holder.txt_userName.setText("@" + commentsDtoProxy.getUserName());
        }

         if (commentsDtoProxy.getCommentsDescription() != null) {
            holder.txt_comment.setText(commentsDtoProxy.getCommentsDescription());
        }
        if (commentsDtoProxy.getProfilePictureUrl() != null) {
//            mGlide.with(mContext).load("https://dl.dropbox.com/s/c6u1nny8890iow8/Screenshot_20190926-103052_WhatsApp.jpg?dl=0").centerCrop().into(holder.thumbnail);
            mGlide.with(mContext).load(commentsDtoProxy.getProfilePictureUrl()).centerCrop().into(holder.thumbnail);
            /*holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onLoadCommentListener.onLoadThumbnailClick(position);

                }
            });*/
        }

    }

    @Override
    public int getItemCount() {
        return commentsDtoProxyArrayList.size();

    }

    public interface OnLoadCommentListener {
        void onLoadThumbnailClick(int position);
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {

        private final ImageView thumbnail;
        private final TextView txt_userName;
        private final TextView txt_comment;


        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.imageView_circular);
            txt_userName = itemView.findViewById(R.id.txt_comment_userName);
            txt_comment = itemView.findViewById(R.id.txt_comments);
        }
    }
}