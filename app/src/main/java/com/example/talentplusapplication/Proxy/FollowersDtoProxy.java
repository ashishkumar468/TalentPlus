package com.example.talentplusapplication.Proxy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowersDtoProxy {

    @Expose
    @SerializedName("followersId")
    private  int followersId;

    @Expose
    @SerializedName("userId")
    private  int userId;

    @Expose
    @SerializedName("followersUserId")
    private  int followersUserId;

    @Expose
    @SerializedName("createdOn")
    private  String createdOn;

    @Expose
    @SerializedName("delFlag")
    private  Boolean delFlag;

    @Expose
    @SerializedName("userDto")
    private  UserDTOProxy userDto;

    public int getFollowersId() {
        return followersId;
    }

    public void setFollowersId(int followersId) {
        this.followersId = followersId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFollowersUserId() {
        return followersUserId;
    }

    public void setFollowersUserId(int followersUserId) {
        this.followersUserId = followersUserId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public UserDTOProxy getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDTOProxy userDto) {
        this.userDto = userDto;
    }
}
