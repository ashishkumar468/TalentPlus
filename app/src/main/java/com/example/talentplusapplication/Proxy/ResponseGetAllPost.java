package com.example.talentplusapplication.Proxy;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGetAllPost {

    @Expose
    @SerializedName("code")
    private  String code;

    @Expose
    @SerializedName("description")
    private  String description;

    @Expose
    @SerializedName("data")
    private  boolean data;


    @Expose
    @SerializedName("postsDtoList")
    @Nullable
    private List<PostDtoListProxy> postsDtoList;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    @Nullable
    public List<PostDtoListProxy> getPostsDtoList() {
        return postsDtoList;
    }

    public void setPostsDtoList(@Nullable List<PostDtoListProxy> postsDtoList) {
        this.postsDtoList = postsDtoList;
    }



}
