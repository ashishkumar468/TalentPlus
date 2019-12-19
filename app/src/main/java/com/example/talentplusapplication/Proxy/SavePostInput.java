package com.example.talentplusapplication.Proxy;

public class SavePostInput {

    private  String userId ="";
    private  String postTitle="";
    private  String videoUrl="";


    public SavePostInput(String userId, String postTitle, String videoUrl) {
        this.userId = userId;
        this.postTitle = postTitle;
        this.videoUrl = videoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
