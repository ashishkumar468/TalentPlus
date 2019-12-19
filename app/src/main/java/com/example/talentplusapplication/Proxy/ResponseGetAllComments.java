package com.example.talentplusapplication.Proxy;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetAllComments extends BaseResponse {


    @Expose
    @SerializedName("commentsDtoList")
    @Nullable
    private List<CommentsDtoProxy> commentsDtoList;

    @Expose
    @SerializedName("commentsDto")
    @Nullable
    private CommentsDtoProxy commentsDto;

    @Nullable
    public List<CommentsDtoProxy> getCommentsDtoList() {
        return commentsDtoList;
    }

    public void setCommentsDtoList(@Nullable List<CommentsDtoProxy> commentsDtoList) {
        this.commentsDtoList = commentsDtoList;
    }

    @Nullable
    public CommentsDtoProxy getCommentsDto() {
        return commentsDto;
    }

    public void setCommentsDto(@Nullable CommentsDtoProxy commentsDto) {
        this.commentsDto = commentsDto;
    }
}
