package com.example.talentplusapplication.ui.test_home;

import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.VideosDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TestHomeActivity  extends AppCompatActivity {





//    @BindView(R.id.rv_home)

    AAH_CustomRecyclerView recyclerView;



    private final List<MyModel> modelList = new ArrayList<>();





    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_home_activity);

        recyclerView=findViewById(R.id.rv_home);


        VideosDownloader mVideosDownloader=new VideosDownloader(this);
        mVideosDownloader.startVideosDownloading("");


        Picasso p = Picasso.with(this);

//        modelList.add(new MyModel("http://www.betcoingaming.com/webdesigns/animatedslider/images/liveroulette2.mp4","http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg","name1"));

        modelList.add(new MyModel("https://dl.dropboxusercontent.com/s/2gkzkk7rdrwttoa/VID-20191031-WA0004.mp4?dl=0", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "video1"));
        modelList.add(new MyModel("https://dl.dropboxusercontent.com/s/dzd5mqu4yisvtmc/VID-20191031-WA0003.mp4?dl=0", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "video1"));
        modelList.add(new MyModel("https://dl.dropbox.com/s/h8jcp6iuabfvh65/demo%20sin.mp4?dl=0", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "video1"));
        modelList.add(new MyModel("https://dl.dropboxusercontent.com/s/9i64tsn596l88fq/VIDEO_20191031_122143.mp4?dl=0", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "video1"));
        modelList.add(new MyModel("https://dl.dropbox.com/s/gal662mj1qjpv9w/123.mp4?dl=0", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "video1"));
        modelList.add(new MyModel("https://dl.dropbox.com/s/dwt70oioxwj1qan/VIDEO_20191025_185307.mp4?dl=0", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "video1"));


        modelList.add(new MyModel("https://dl.dropboxusercontent.com/s/9ovzaov1e73odxi/VIDEO_20191024_171504.mp4?dl=0", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1491561340/hello_cuwgcb.jpg", "video2"));

//        modelList.add(new MyModel("http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/3_lfndfq.jpg", "image3"));


        modelList.add(new MyModel("https://dl.dropboxusercontent.com/s/iu9jcp6n84073gv/VIDEO_20191023_181744.mp4?dl=0", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795675/3_yqeudi.jpg", "video4"));


        modelList.add(new MyModel("https://dl.dropbox.com/s/k7fi4msavi3o6qn/VIDEO_20191025_175734.mp4?dl=0", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795675/1_pyn1fm.jpg", "video5"));


        modelList.add(new MyModel("https://dl.dropbox.com/s/pn0eolorpdm076u/VIDEO_20191025_175659.mp4?dl=0", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1491561340/hello_cuwgcb.jpg", "video6"));

//        modelList.add(new MyModel("http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/2_qwpgis.jpg", "image7"));
//
//        modelList.add(new MyModel("http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/1_ybonak.jpg", "image8"));

        modelList.add(new MyModel("https://firebasestorage.googleapis.com/v0/b/flickering-heat-5334.appspot.com/o/demo1.mp4?alt=media&token=f6d82bb0-f61f-45bc-ab13-16970c7432c4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "video9"));

//        modelList.add(new MyModel("http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/3_lfndfq.jpg", "name10"));

        modelList.add(new MyModel("http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70/v1481795676/4_nvnzry.mp4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795676/4_nvnzry.jpg", "video11"));

        modelList.add(new MyModel("https://firebasestorage.googleapis.com/v0/b/flickering-heat-5334.appspot.com/o/demo1.mp4?alt=media&token=f6d82bb0-f61f-45bc-ab13-16970c7432c4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "video12"));


//        modelList.add(new MyModel("http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/3_lfndfq.jpg", "image13"));
//
//        modelList.add(new MyModel("http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/2_qwpgis.jpg", "image14"));

        modelList.add(new MyModel("https://dl.dropbox.com/s/tc4bbyb0wckafez/VID-20191025-WA0001_001.mp4?dl=0", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795675/3_yqeudi.jpg", "video16"));



        modelList.add(new MyModel("http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70/v1481795675/1_pyn1fm.mp4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795675/1_pyn1fm.jpg", "video17"));

        modelList.add(new MyModel("https://firebasestorage.googleapis.com/v0/b/flickering-heat-5334.appspot.com/o/demo1.mp4?alt=media&token=f6d82bb0-f61f-45bc-ab13-16970c7432c4", "http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg", "video18"));

//        modelList.add(new MyModel("http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/2_qwpgis.jpg", "image19"));
//
//        modelList.add(new MyModel("http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/3_lfndfq.jpg", "image20"));
//
//        modelList.add(new MyModel("http://res.cloudinary.com/krupen/image/upload/q_70/v1481795690/1_ybonak.jpg", "image21"));



        //you can pass local file uri, but make sure it exists

//        modelList.add(new MyModel("/storage/emulated/0/VideoPlay/myvideo.mp4","http://res.cloudinary.com/krupen/video/upload/w_300,h_150,c_crop,q_70,so_0/v1481795681/2_rp0zyy.jpg","video18"));



        MyVideosAdapter mAdapter = new MyVideosAdapter(modelList, p);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);



        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());



        //todo before setAdapter

        recyclerView.setActivity(this);



        //optional - to play only first visible video

        recyclerView.setPlayOnlyFirstVideo(true); // false by default



        //optional - by default we check if url ends with ".mp4". If your urls do not end with mp4, you can set this param to false and implement your own check to see if video points to url

        recyclerView.setCheckForMp4(false); //true by default



        //optional - download videos to local storage (requires "android.permission.WRITE_EXTERNAL_STORAGE" in manifest or ask in runtime)

        recyclerView.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); // (Environment.getExternalStorageDirectory() + "/Video") by default



        recyclerView.setDownloadVideos(true); // false by default



        recyclerView.setVisiblePercent(50); // percentage of View that needs to be visible to start playing



        //extra - start downloading all videos in background before loading RecyclerView

        List<String> urls = new ArrayList<>();

        for (MyModel object : modelList) {

            if (object.getVideo_url() != null && object.getVideo_url().contains("http"))

                urls.add(object.getVideo_url());

        }

        recyclerView.preDownload(urls);



        recyclerView.setAdapter(mAdapter);

        //call this functions when u want to start autoplay on loading async lists (eg firebase)

        recyclerView.smoothScrollBy(0,1);

        recyclerView.smoothScrollBy(0,-1);



    }



    @Override

    protected void onStop() {

        super.onStop();

        //add this code to pause videos (when app is minimised or paused)

        recyclerView.stopVideos();

    }





}