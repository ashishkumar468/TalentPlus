package com.example.talentplusapplication.Proxy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowingDtoProxy {

    @Expose
    @SerializedName("followingId")
    private  int followingId;

    @Expose
    @SerializedName("userId")
    private  int userId;

    @Expose
    @SerializedName("followingUserId")
    private  int followingUserId;

    @Expose
    @SerializedName("createdOn")
    private  String createdOn;

    @Expose
    @SerializedName("delFlag")
    private  Boolean delFlag;

    @Expose
    @SerializedName("userDto")
    private  UserDTOProxy userDto;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getFollowingId() {
        return followingId;
    }

    public void setFollowingId(int followingId) {
        this.followingId = followingId;
    }

    public int getFollowingUserId() {
        return followingUserId;
    }

    public void setFollowingUserId(int followingUserId) {
        this.followingUserId = followingUserId;
    }

    public UserDTOProxy getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDTOProxy userDto) {
        this.userDto = userDto;
    }
}


