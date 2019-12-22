package com.example.talentplusapplication.ui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.talentplusapplication.Constants;
import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.Proxy.BaseResponse;
import com.example.talentplusapplication.Proxy.MyVideoItem;
import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.Proxy.UserDTOProxy;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.Utility;
import com.example.talentplusapplication.VideoDownloadService;
import com.example.talentplusapplication.camera.BaseCameraActivity;
import com.example.talentplusapplication.ui.comments.BottomSheetFragment;
import com.example.talentplusapplication.video.VideoPlayerRecyclerView;
import com.example.talentplusapplication.video.adapter.VideoRecyclerViewAdapter;
import com.example.talentplusapplication.video.adapter.holder.VideoViewHolder;
import com.example.talentplusapplication.video.adapter.items.BaseVideoItem;
import com.example.talentplusapplication.video.adapter.items.ItemFactory;
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

public class UserVideoPlayActivity extends AppCompatActivity implements VideoRecyclerViewAdapter.OnLoadFabClickListener {


    private static final int SHARE_CODE = 2;
    private VideoPlayerRecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SharedPreferences mPrefs;
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
    private static final String TAG = UserVideoPlayActivity.class.getSimpleName();
    private List<MyVideoItem> listPost;
    private int selectedPosition;

    private final ArrayList<MyVideoItem> mList = new ArrayList<>();
    /**
     * ItemsPositionGetter is used by {@link ListItemsVisibilityCalculator} for getting information about
     * items position in the RecyclerView and LayoutManager
     */
    private ItemsPositionGetter mItemsPositionGetter;

    /**
     * Here we use {@link SingleVideoPlayerManager}, which means that only one video playback is possible.
     */
    private final VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });

    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    private String userName;
    private int postId;
    private String userId;
    SharedPreferences mSharedPreferences;
    private String userProfileUrl;
    private VideoRecyclerViewAdapter videoRecyclerViewAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userId = MyApplication.getAppContext().getSharedPreferences(Constants.Shared_Pref_Name, MODE_PRIVATE).getString(Constants.USER_ID, "");


        initialization();
    }

    private void initialization() {

        mRecyclerView = findViewById(R.id.recycler_view_video_list);

        mPrefs = MyApplication.getAppContext().getSharedPreferences(Constants.Shared_Pref_Name, MODE_PRIVATE);
        String json = mPrefs.getString("UserMyObject", "");
        Type type = new TypeToken<List<PostDtoListProxy>>() {
        }.getType();
        listPost = new Gson().fromJson(json, type);

        selectedPosition = getIntent().getIntExtra("position", 0);
        userName = getIntent().getStringExtra("user_name");
        userProfileUrl = getIntent().getStringExtra("user_profile_url");

        if (listPost != null && !listPost.isEmpty()) {
            setDataToUI();
        } else {
            onShowToast("There is no post! You can show your talent & be the first!");
        }
    }

    public void setDataToUI() {


        Log.e(TAG, " Video URL : " + listPost.size());

        UserDTOProxy userDTOProxy = new UserDTOProxy();
        userDTOProxy.setUserName(userName);
        userDTOProxy.setProfilePictureUrl(userProfileUrl);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
//        onOpenDialogueForWait("Please wait video is loading");

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setMediaObjects(listPost);
        int count = 0;
        Log.e(TAG, " Video URL : " + listPost.size());
        videoRecyclerViewAdapter = new VideoRecyclerViewAdapter(mVideoPlayerManager, this, listPost, this);

        mRecyclerView.setAdapter(videoRecyclerViewAdapter);
        mRecyclerView.scrollToPosition(selectedPosition);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.playVideo(false);
            }
        },5000);

    }

    private void onShowToast(String message) {

        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        // we have to stop any playback in onStop
        mRecyclerView.releasePlayer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    public void onLoadFabClick(int position, TextView txt_comment_count) {

        int postId = listPost.get(position).getPostsId();

        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(postId, txt_comment_count);
        assert this.getFragmentManager() != null;
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

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
//            dialog = new ProgressDialog(this.getActivity());
//            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            dialog.setCanceledOnTouchOutside(true);
//            dialog.setCancelable(false);
//            dialog.setMessage("Downloading");
//            dialog.show();
//            new onDownloadTask().execute(listPost.get(position).getVideoUrl());
//            onDonloadVideo(listPost.get(position).getVideoUrl());

            Log.e(TAG, listPost.get(position).getVideoUrl());

//            Intent myIntent = new Intent(Intent.ACTION_SEND);
//            myIntent.setType("text/plain");
//            String shareBody = listPost.get(position).getVideoUrl();
//            String shareSub = "Talent Plus";
//            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
//            myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
//            startActivityForResult(Intent.createChooser(myIntent, "Share using"), SHARE_CODE);

//            onCallIpdateShareCount(postId,txt_shareCount);

            File videoFile = new File(listPost.get(position).getVideoUrl());
            Uri videoURI = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                    ? FileProvider.getUriForFile(this, this.getPackageName(), videoFile)
                    : Uri.fromFile(videoFile);
            ShareCompat.IntentBuilder.from(this)
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
//                        onShowToast("Please upload video again");
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

    }

    @Override
    public void onItemClickListener(int position) {
        //TODO
    }
}
