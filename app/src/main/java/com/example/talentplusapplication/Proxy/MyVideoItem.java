package com.example.talentplusapplication.Proxy;

public interface MyVideoItem {
    String getVideoUrl();
    int getPostsId();
    boolean isLike();
    void setLike(boolean isLiked);
    int getUserId();
}
