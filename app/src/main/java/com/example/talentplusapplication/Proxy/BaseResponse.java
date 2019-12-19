package com.example.talentplusapplication.Proxy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {


    @Expose
    @SerializedName("code")
    private  String code;

    @Expose
    @SerializedName("description")
    private  String description;

    @Expose
    @SerializedName("data")
    private  boolean data;

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
}
