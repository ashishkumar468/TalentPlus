package com.example.talentplusapplication.video.adapter.items;

import android.app.Activity;

import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.Proxy.ResponseGetAllPost;
import com.squareup.picasso.Picasso;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

import java.io.IOException;
import java.util.List;

public class ItemFactory {

    public static BaseVideoItem createItemFromAsset(PostDtoListProxy postDtoList, int imageResource, Activity activity, VideoPlayerManager<MetaData> videoPlayerManager) throws IOException {
       // return new AssetVideoItem(assetName, activity.getAssets().openFd(assetName), videoPlayerManager, Picasso.with(activity), imageResource);

        return new AssetVideoItem(postDtoList,videoPlayerManager,Picasso.with(activity),imageResource);
    }

}
