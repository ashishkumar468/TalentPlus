package com.example.talentplusapplication.Proxy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchProxy {


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
    @SerializedName("delFlag")
    private  String delFlag;

    @Expose
    @SerializedName("userId")
    private  int userId;

    @Expose
    @SerializedName("userName")
    private  String userName;

    @Expose
    @SerializedName("role")
    private  String role;

    @Expose
    @SerializedName("fName")
    private  String fName;

    @Expose
    @SerializedName("lName")
    private  String lName;

    @Expose
    @SerializedName("profilePictureUrl")
    private  String profilePictureUrl;

    @Expose
    @SerializedName("followingCount")
    private  Integer followingCount;

    @Expose
    @SerializedName("followersCount")
    private  Integer followersCount;

    @Expose
    @SerializedName("likeCount")
    private  Integer likeCount;

    @Expose
    @SerializedName("isFollowing")
    private  Integer isFollowing;

    @Expose
    @SerializedName("postsDtoList")
    private  Integer postsDtoList;

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

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(Integer isFollowing) {
        this.isFollowing = isFollowing;
    }

    public Integer getPostsDtoList() {
        return postsDtoList;
    }

    public void setPostsDtoList(Integer postsDtoList) {
        this.postsDtoList = postsDtoList;
    }
}

