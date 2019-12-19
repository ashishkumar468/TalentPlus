package com.example.talentplusapplication.Proxy;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseFollowing extends BaseResponse{

    @Expose
    @SerializedName("followingDtoList")
    @Nullable
    private List<FollowingDtoProxy> followingDtoList;

    @Expose
    @SerializedName("followingDto")
    @Nullable
    private List<FollowingDtoProxy> followingDto;

    @Nullable
    public List<FollowingDtoProxy> getFollowingDtoList() {
        return followingDtoList;
    }

    public void setFollowingDtoList(@Nullable List<FollowingDtoProxy> followingDtoList) {
        this.followingDtoList = followingDtoList;
    }

    @Nullable
    public List<FollowingDtoProxy> getFollowingDto() {
        return followingDto;
    }

    public void setFollowingDto(@Nullable List<FollowingDtoProxy> followingDto) {
        this.followingDto = followingDto;
    }
}
