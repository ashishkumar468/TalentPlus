package com.example.talentplusapplication.Proxy;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseFollowers extends BaseResponse {



    @Expose
    @SerializedName("followersDtoList")
    @Nullable
    private List<FollowersDtoProxy> followersDtoList;

    @Expose
    @SerializedName("followersDto")
    @Nullable
    private List<FollowersDtoProxy> followersDto;

    @Nullable
    public List<FollowersDtoProxy> getFollowersDtoList() {
        return followersDtoList;
    }

    public void setFollowersDtoList(@Nullable List<FollowersDtoProxy> followersDtoList) {
        this.followersDtoList = followersDtoList;
    }

    @Nullable
    public List<FollowersDtoProxy> getFollowersDto() {
        return followersDto;
    }

    public void setFollowersDto(@Nullable List<FollowersDtoProxy> followersDto) {
        this.followersDto = followersDto;
    }
}
