package com.example.talentplusapplication.Proxy;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDTOProxy {



    @Expose
    @SerializedName("createdBy")
    private  int createdBy;

    @Expose
    @SerializedName("createdOn")
    private  String createdOn;

    @Expose
    @SerializedName("updatedBy")
    private  int updatedBy;

    @Expose
    @SerializedName("updatedOn")
    private  String updatedOn ;

    @Expose
    @SerializedName("delFlag")
    private  String delFlag ;
    @Expose
    @SerializedName("userId")
    private  int userId ;
    @Expose
    @SerializedName("userName")
    private  String userName ;

    @Expose
    @SerializedName("role")
    private  String role ;

    @Expose
    @SerializedName("fName")
    private  String fName ;

    @Expose
    @SerializedName("lName")
    private  String lName ;

    @Expose
    @SerializedName("profilePictureUrl")
    private  String profilePictureUrl ;

    @Expose
    @SerializedName("contactNo")
    private  String contactNo ;

    @Expose
    @SerializedName("emailId")
    private  String emailId ;

    @Expose
    @SerializedName("oldPassword")
    private  String oldPassword ;

    @Expose
    @SerializedName("newPassword")
    private  String newPassword ;

    @Expose
    @SerializedName("followingCount")
    private  String followingCount ;

    @Expose
    @SerializedName("followersCount")
    private  String followersCount ;

    @Expose
    @SerializedName("likeCount")
    private  Integer likeCount ;

    @Expose
    @Nullable
    @SerializedName("isFollowing")
    private  boolean isFollowing ;

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    @Expose
    @SerializedName("postsDtoList")
    @Nullable
    private List<PostDtoListProxy> postsDtoList ;


    @Nullable
    public List<PostDtoListProxy> getPostsDtoList() {
        return postsDtoList;
    }

    public void setPostsDtoList(@Nullable List<PostDtoListProxy> postsDtoList) {
        this.postsDtoList = postsDtoList;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
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

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
