package com.example.talentplusapplication.ui.home;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;
import com.example.talentplusapplication.Constants;
import com.example.talentplusapplication.IVideoDownloadListener;
import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.Proxy.BaseResponse;
import com.example.talentplusapplication.Proxy.MyVideoItem;
import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.Utility;
import com.example.talentplusapplication.VideoDownloadService;
import com.example.talentplusapplication.VideosDownloader;
import com.example.talentplusapplication.camera.BaseCameraActivity;
import com.example.talentplusapplication.ui.comments.BottomSheetFragment;
import com.example.talentplusapplication.ui.user.UserProfileActivity;
import com.example.talentplusapplication.video.VideoPlayerRecyclerView;
import com.example.talentplusapplication.video.adapter.VideoRecyclerViewAdapter;
import com.example.talentplusapplication.video.adapter.holder.VideoViewHolder;
import com.example.talentplusapplication.video.adapter.items.BaseVideoItem;
import com.example.talentplusapplication.video.adapter.items.ItemFactory;
import com.example.talentplusapplication.video.download.DownloadFileTask;
import com.example.talentplusapplication.webservices.ApiClient;
import com.example.talentplusapplication.webservices.ApiInterface;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.volokh.danylo.video_player_manager.Config;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.visibility_utils.calculator.DefaultSingleItemCalculatorCallback;
import com.volokh.danylo.visibility_utils.calculator.ListItemsVisibilityCalculator;
import com.volokh.danylo.visibility_utils.calculator.SingleListViewItemActiveCalculator;
import com.volokh.danylo.visibility_utils.scroll_utils.ItemsPositionGetter;
import com.volokh.danylo.visibility_utils.scroll_utils.RecyclerViewItemPositionGetter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements VideoRecyclerViewAdapter.OnLoadFabClickListener, IVideoDownloadListener {

    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int SHARE_CODE = 2;
    private static List<MyVideoItem> listPost;

    private static final ArrayList<BaseVideoItem> mList = new ArrayList<>();

    /**
     * Only the one (most visible) view should be active (and playing).
     * To calculate visibility of views we use {@link SingleListViewItemActiveCalculator}
     */
    private final ListItemsVisibilityCalculator mVideoVisibilityCalculator =
            new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), mList);

    private VideoPlayerRecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private static VideosDownloader mVideosDownloader;
    private VideoRecyclerViewAdapter videoRecyclerViewAdapter;
    SharedPreferences mPrefs;
    SharedPreferences mSharedPreferences;
    String userId;
    private String returnUrl;
    int postId;

    /**
     * ItemsPositionGetter is used by {@link ListItemsVisibilityCalculator} for getting information about
     * items position in the RecyclerView and LayoutManager
     */
    private ItemsPositionGetter mItemsPositionGetter;

    /**
     * Here we use {@link SingleVideoPlayerManager}, which means that only one video playback is possible.
     */
    private static final VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {


        }
    });
    private int countForLoad = 0;

    public abstract interface VideoDownloadListener {
        public static void onVideoDownloaded(String s) {


        }

    }

    public static class VideoDownloadBroadCastReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("VDBROADCAST", "");
            if (intent.getAction() == "ChangeVideoList") {
                new HomeFragment().onCloseDialogueForWait();

                new HomeFragment().setUIDataUpdated();
            }

        }
    }


    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_video_list, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view_video_list);
        String url = "https://dl.dropboxusercontent.com/s/r2ntgqflhdjspuo/201910_15-162032cameraRecorder.mp4?dl=0";
        mPrefs = MyApplication.getAppContext().getSharedPreferences("name", MODE_PRIVATE);
        userId = MyApplication.getAppContext().getSharedPreferences(Constants.Shared_Pref_Name, MODE_PRIVATE).getString(Constants.USER_ID, "");
        String json = mPrefs.getString("MyObject", "");
        Type type = new TypeToken<List<PostDtoListProxy>>() {
        }.getType();
        listPost = new Gson().fromJson(json, type);
        if (listPost != null && !listPost.isEmpty()) {
            setDataToUI();
        } else {
            onShowToast("There is no post! You can show your talent & be the first!");
        }


        return rootView;
    }


    private ProgressDialog dialog;

    private void onCloseDialogueForWait() {
        if (dialog != null) {
            dialog.dismiss();
        }

    }

    public void setDataToUI() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setMediaObjects(listPost);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        videoRecyclerViewAdapter = new VideoRecyclerViewAdapter(mVideoPlayerManager, getActivity(), listPost, this);
        mRecyclerView.setAdapter(videoRecyclerViewAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.playVideo(false);
            }
        },10000);
        mRecyclerView.playVideo(false);

    }


    public void setUIDataUpdated() {
        if (!listPost.isEmpty() && listPost != null) {
            Log.e(TAG, " Video URL : setUIDataUpdated " + listPost.size());
            if (countForLoad == 1) {
                onCloseDialogueForWait();
                countForLoad = 0;
            }
            mList.clear();
        }
    }

    private void onShowToast(String message) {
        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mList.isEmpty()) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {

                    mVideoVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());

                }
            });

            mVideoPlayerManager.resetMediaPlayer();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        mRecyclerView.releasePlayer();
    }


    @Override
    public void onLoadFabClick(int position, TextView txt_comment_count) {
        int postId = listPost.get(position).getPostsId();

        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(postId, txt_comment_count);
        assert this.getFragmentManager() != null;
        bottomSheetFragment.show(this.getFragmentManager(), bottomSheetFragment.getTag());

    }

    Integer count;

    @Override
    public void onLoadFabLikeClick(int position, ImageButton mFab_Like, TextView txt_count) {
        count = Integer.parseInt(txt_count.getText().toString());

        postId = listPost.get(position).getPostsId();
        if (listPost.get(position).isLike()) {
            mFab_Like.setBackgroundTintList(getResources().getColorStateList(R.color.post_like));
        } else {
            mFab_Like.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        }
        if (new Utility(MyApplication.getAppContext()).isConnectingToInternet()) {

            onCallSaveThumbsUp(position, mFab_Like, txt_count, listPost.get(position).isLike());
        }
    }

    private void onCallSaveThumbsUp(int position, ImageButton mFab_Like, TextView txt_count, boolean isLike) {

        userId = MyApplication.getAppContext().getSharedPreferences(Constants.Shared_Pref_Name, MODE_PRIVATE).getString(Constants.USER_ID, "");

        if (userId.equals("")) {
            onShowToast("Please login!");
        } else {

            ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);
            JsonObject mJsonObject = new JsonObject();
            mJsonObject.addProperty("postsId", "" + postId);
            mJsonObject.addProperty("userId", userId);

            mApiInterface.saveThumbsUp(mJsonObject).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                dialog.cancel();
                    if (response.isSuccessful()) {

                        if (response.body().getCode().equals("200")) {
                            if (!isLike) {
                                listPost.get(position).setLike(true);
                                mFab_Like.setBackgroundTintList(getResources().getColorStateList(R.color.post_like));
                                count++;
                                txt_count.setText("" + count);
                            } else {
                                listPost.get(position).setLike(false);
                                mFab_Like.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                                count--;
                                txt_count.setText("" + count);
                            }


                        } else {
//                        onShowToast("Please try again");
                        }

                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    onShowToast("Something is wrong");


                }
            });
        }
    }

    //    ProgressDialog dialog;
    TextView txt_Share_count;

    @Override
    public void onLoadFabShareClick(int position, ImageButton mFab_Share, TextView txt_shareCount) {
        postId = listPost.get(position).getPostsId();
        txt_Share_count = txt_shareCount;

        if (new Utility(MyApplication.getAppContext()).isConnectingToInternet()) {
            Log.e(TAG, listPost.get(position).getVideoUrl());
            File videoFile = new File(listPost.get(position).getVideoUrl());
            Uri videoURI = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                    ? FileProvider.getUriForFile(this.getContext(), this.getActivity().getPackageName(), videoFile)
                    : Uri.fromFile(videoFile);
            ShareCompat.IntentBuilder.from(this.getActivity())
                    .setStream(videoURI)
                    .setType("video/mp4")
                    .setChooserTitle("Share video...")
                    .startChooser();
            onCallIpdateShareCount(postId, txt_Share_count);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SHARE_CODE) {
            if (resultCode == RESULT_OK) {
                onCallIpdateShareCount(postId, txt_Share_count);
            }
        }
    }

    @Override
    public void onVideoDownloaded(MediaStore.Video video) {

    }

    @Override
    public void onVideoDownloadedComplete(String string) {
        setDataToUI();
    }

    public class onDownloadTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... strings) {
            return null;


        }
    }

    DbxClientV2 sDbxClient;

    private void onCallIpdateShareCount(int postId, TextView txt_shareCount) {

        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);

        mApiInterface.updateShareCount("" + postId).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getCode().equals("200")) {

                        Integer count = Integer.valueOf(txt_shareCount.getText().toString());
                        count++;
                        txt_shareCount.setText("" + count);


                    } else {
                    }

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onShowToast("Something is wrong");


            }
        });


    }

    @Override
    public void onLoadFabProfileClick(int position) {
        Intent mIntent = new Intent(this.getContext(), UserProfileActivity.class);
        mIntent.putExtra("OtherUserId", listPost.get(position).getUserId());
        startActivity(mIntent);
    }

    @Override
    public void onItemClickListener(int position) {
       //TODO
    }

}