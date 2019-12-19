package com.example.talentplusapplication.ui.home.adapter;

public class VideoItem {

    public  String videoId;
    public  String videoThumbnail;


    public VideoItem(String videoId, String videoThumbnail) {
        this.videoId = videoId;
        this.videoThumbnail = videoThumbnail;
    }

    public String getVideoId() {
        return videoId;
    }

   /* public void setVideoId(String videoId) {
        this.videoId = videoId;
    }*/

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

   /* public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }*/
}
