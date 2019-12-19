package com.example.talentplusapplication.Proxy;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostDtoListProxy {

    @Expose
    @SerializedName("createdBy")
    @Nullable
    private  String createdBy;


    @Expose
    @SerializedName("createdOn")
    @Nullable
    private  String createdOn;

    @Expose
    @SerializedName("updatedBy")
    @Nullable
    private  String updatedBy;

    @Expose
    @SerializedName("updatedOn")
    @Nullable
    private  String updatedOn;

    @Expose
    @SerializedName("delFlag")
    @Nullable
    private  String delFlag;

    @Expose
    @SerializedName("postsId")
    @Nullable
    private  int postsId;

    @Expose
    @SerializedName("userId")
    @Nullable
    private  int userId;

    @Expose
    @SerializedName("postTitle")
    @Nullable
    private  String postTitle;

    @Expose
    @SerializedName("videoUrl")
    @Nullable
    private  String videoUrl;

    @Expose
    @SerializedName("sharesCount")
    @Nullable
    private  String sharesCount;

    @Expose
    @SerializedName("thumbsUpCount")
    @Nullable
    private  Integer thumbsUpCount;

    @Expose
    @SerializedName("thumbsDownCount")
    @Nullable
    private  int thumbsDownCount;

    @Expose
    @SerializedName("commentsCount")
    @Nullable
    private  Integer commentsCount;

    @Expose
    @SerializedName("isLike")
    @Nullable
    private  boolean isLike;

    @Expose
    @SerializedName("profilePictureUrl")
    @Nullable
    private  String profilePictureUrl;

    @Expose
    @SerializedName("userName")
    @Nullable
    private  String userName;

    @Expose
    @SerializedName("userDto")
    @Nullable
    private  UserDTOProxy userDto;

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

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

    @Nullable
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(@Nullable String createdBy) {
        this.createdBy = createdBy;
    }

    @Nullable
    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(@Nullable String createdOn) {
        this.createdOn = createdOn;
    }

    @Nullable
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(@Nullable String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Nullable
    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(@Nullable String updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Nullable
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(@Nullable String delFlag) {
        this.delFlag = delFlag;
    }

    public int getPostsId() {
        return postsId;
    }

    public void setPostsId(int postsId) {
        this.postsId = postsId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Nullable
    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(@Nullable String postTitle) {
        this.postTitle = postTitle;
    }

    @Nullable
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(@Nullable String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Nullable
    public String getSharesCount() {
        return sharesCount;
    }

    public void setSharesCount(@Nullable String sharesCount) {
        this.sharesCount = sharesCount;
    }

    public Integer getThumbsUpCount() {
        return thumbsUpCount;
    }

    public void setThumbsUpCount(Integer thumbsUpCount) {
        this.thumbsUpCount = thumbsUpCount;
    }

    public int getThumbsDownCount() {
        return thumbsDownCount;
    }

    public void setThumbsDownCount(int thumbsDownCount) {
        this.thumbsDownCount = thumbsDownCount;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    @Nullable
    public UserDTOProxy getUserDto() {
        return userDto;
    }

    public void setUserDto(@Nullable UserDTOProxy userDto) {
        this.userDto = userDto;
    }

}
