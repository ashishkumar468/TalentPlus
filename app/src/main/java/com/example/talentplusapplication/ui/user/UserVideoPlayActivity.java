package com.example.talentplusapplication.ui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.Proxy.UserDTOProxy;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.Utility;
import com.example.talentplusapplication.VideoDownloadService;
import com.example.talentplusapplication.camera.BaseCameraActivity;
import com.example.talentplusapplication.ui.comments.BottomSheetFragment;
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

public class UserVideoPlayActivity extends AppCompatActivity  implements VideoRecyclerViewAdapter.OnLoadFabClickListener {


    private static final int SHARE_CODE = 2;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SharedPreferences mPrefs ;
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
    private static final String TAG = UserVideoPlayActivity.class.getSimpleName();
    private List<PostDtoListProxy> listPost;
    private int selectedPosition;

    private final ArrayList<BaseVideoItem> mList = new ArrayList<>();
    /**
     * Only the one (most visible) view should be active (and playing).
     * To calculate visibility of views we use {@link SingleListViewItemActiveCalculator}
     */
    private final ListItemsVisibilityCalculator mVideoVisibilityCalculator =
            new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), mList);

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


    public static class VideoDownloadBroadCastReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("VDBROADCAST", "");
            if (intent.getAction() == "ChangeVideoList") {

                new UserVideoPlayActivity().setUIDataUpdated();
            }

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        // enabling action bar app icon and behaving it as toggle button
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);

        Toolbar mToolbar=findViewById(R.id.toolbar);
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

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_video_list);

        mPrefs = MyApplication.getAppContext().getSharedPreferences(Constants.Shared_Pref_Name,MODE_PRIVATE);
        String json = mPrefs.getString("UserMyObject", "");
        Type type = new TypeToken<List<PostDtoListProxy>>() {}.getType();
        listPost = new Gson().fromJson(json, type);

        selectedPosition= getIntent().getIntExtra("position",0);
        userName=getIntent().getStringExtra("user_name");
        userProfileUrl=getIntent().getStringExtra("user_profile_url");

        if (listPost != null && !listPost.isEmpty()) {
            setDataToUI();
        } else {
            onShowToast("There is no post! You can show your talent & be the first!");
        }
    }

    public void setDataToUI(){


        Log.e(TAG," Video URL : "+listPost.size());

        UserDTOProxy userDTOProxy=new UserDTOProxy();
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

        int count = 0;
        Log.e(TAG, " Video URL : " + listPost.size());

        for (PostDtoListProxy postDtoList : listPost) {
            try {
                String segments[] = postDtoList.getVideoUrl().split("/");
                String s = "\\?dl";
                String segment[] = segments[segments.length - 1].split(s);
                Log.d(TAG, "setDataToUI: " + segment[0]);

                File file = new File(BaseCameraActivity.getAndroidMoviesFolderInternalStorage(), segment[0]);
                if (file.exists()) {
                    //Do action
                    String filename = BaseCameraActivity.getAndroidMoviesFolderInternalStorage() + "/" + segment[0];
                    postDtoList.setVideoUrl(filename);
                    Log.e(TAG, "set url  " + postDtoList.getVideoUrl());
                    mList.add(ItemFactory.createItemFromAsset(postDtoList, R.drawable.video_sample_1_pic, this, mVideoPlayerManager));

                } else {
                    count++;
                    if (count == listPost.size()) {
                        mList.add(ItemFactory.createItemFromAsset(listPost.get(0), R.drawable.video_sample_1_pic, this, mVideoPlayerManager));

                    }

                    Intent mIntent = new Intent(Intent.ACTION_SYNC, null, this, VideoDownloadService.class);
                    mIntent.putExtra("URL", postDtoList.getVideoUrl());
                    this.startService(mIntent);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        videoRecyclerViewAdapter = new VideoRecyclerViewAdapter(mVideoPlayerManager, this, mList, this);

        mRecyclerView.setAdapter(videoRecyclerViewAdapter);


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                mScrollState = scrollState;
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE && !mList.isEmpty()) {

                    mVideoVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!mList.isEmpty()) {
                    mVideoVisibilityCalculator.onScroll(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition() + 1,
                            mScrollState);
                }
            }
        });
        mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager, mRecyclerView); }


    public void setUIDataUpdated() {

        Log.e(TAG, " Video URL : setUIDataUpdated " + listPost.size());
        if (!listPost.isEmpty() && listPost != null) {
            mList.clear();
            for (PostDtoListProxy postDtoList : listPost) {
                try {
                    String segments[] = postDtoList.getVideoUrl().split("/");
                    String s = "\\?dl";
                    String segment[] = segments[segments.length - 1].split(s);
                    Log.d(TAG, "setDataToUI: " + segment[0]);

                    File file = new File(BaseCameraActivity.getAndroidMoviesFolderInternalStorage(), segment[0]);
                    if (file.exists()) {
                        //Do action
                        String filename = BaseCameraActivity.getAndroidMoviesFolderInternalStorage() + "/" + segment[0];
                        postDtoList.setVideoUrl(filename);
                        Log.e(TAG, "set url  " + postDtoList.getVideoUrl());
//                    mList.add(ItemFactory.createItemFromAsset(postDtoList.getVideoUrl(), R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
                        mList.add(ItemFactory.createItemFromAsset(postDtoList, R.drawable.video_sample_1_pic, this, mVideoPlayerManager));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (videoRecyclerViewAdapter != null) {
                videoRecyclerViewAdapter.notifyDataSetChanged();
            }


        }
    }


    private void onShowToast(String message) {

        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mList.isEmpty()){
            // need to call this method from list view handler in order to have filled list

            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {

                    mVideoVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());

                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // we have to stop any playback in onStop
        mVideoPlayerManager.resetMediaPlayer();
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

        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(postId,txt_comment_count);
        assert this.getFragmentManager() != null;
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

    }

    @Override
    public void onLoadBindMethod(int position, VideoViewHolder holder) {

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
        if (requestCode==SHARE_CODE){
            if (resultCode==RESULT_OK){
                onCallIpdateShareCount(postId,txt_Share_count);
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
}
