package com.example.talentplusapplication.Proxy;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseIsUserExist {

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
    @SerializedName("userDto")
    @Nullable
    private  UserDTOProxy userDto;

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
    public UserDTOProxy getUserDto() {
        return userDto;
    }

    @Nullable
    public void setUserDto(UserDTOProxy userDto) {
        this.userDto = userDto;
    }
}
