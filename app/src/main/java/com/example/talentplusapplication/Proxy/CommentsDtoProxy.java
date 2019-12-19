package com.example.talentplusapplication.Proxy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class CommentsDtoProxy {


    @Expose
    @SerializedName("createdBy")
    private  String createdBy;

    @Expose
    @SerializedName("createdOn")
    private  String createdOn;

    @Expose
    @SerializedName("updatedBy")
    private  String updatedBy;

    @Expose
    @SerializedName("updatedOn")
    private  String updatedOn;

    @Expose
    @SerializedName("delFlag")
    private  String delFlag;

    @Expose
    @SerializedName("commentsId")
    private  String commentsId;

    @Expose
    @SerializedName("postsId")
    private  String postsId;

    @Expose
    @SerializedName("userId")
    private  String userId;

    @Expose
    @SerializedName("commentsDescription")
    private  String commentsDescription;

    @Expose
    @SerializedName("profilePictureUrl")
    @Nullable
    private  String profilePictureUrl;

    @Expose
    @SerializedName("userName")
    @Nullable
    private  String userName;


    @Nullable
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(@Nullable String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    public void setUserName(@Nullable String userName) {
        this.userName = userName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(String commentsId) {
        this.commentsId = commentsId;
    }

    public String getPostsId() {
        return postsId;
    }

    public void setPostsId(String postsId) {
        this.postsId = postsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentsDescription() {
        return commentsDescription;
    }

    public void setCommentsDescription(String commentsDescription) {
        this.commentsDescription = commentsDescription;
    }
}
