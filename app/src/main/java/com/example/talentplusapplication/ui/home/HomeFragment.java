package com.example.talentplusapplication.ui.home;

import android.app.Activity;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
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
import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.Utility;
import com.example.talentplusapplication.VideoDownloadService;
import com.example.talentplusapplication.VideosDownloader;
import com.example.talentplusapplication.camera.BaseCameraActivity;
import com.example.talentplusapplication.ui.comments.BottomSheetFragment;
import com.example.talentplusapplication.ui.user.UserProfileActivity;
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
import com.volokh.danylo.video_player_manager.ui.MediaPlayerWrapper;
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
    private static List<PostDtoListProxy> listPost;

    private static final ArrayList<BaseVideoItem> mList = new ArrayList<>();

    /**
     * Only the one (most visible) view should be active (and playing).
     * To calculate visibility of views we use {@link SingleListViewItemActiveCalculator}
     */
    private final ListItemsVisibilityCalculator mVideoVisibilityCalculator =
            new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), mList);

    private RecyclerView mRecyclerView;
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
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_video_list);

//        String url="https://dl.dropboxusercontent.com/s/hfb4xe6cpce6303/videoplayback.mp4?dl=0";
        String url = "https://dl.dropboxusercontent.com/s/r2ntgqflhdjspuo/201910_15-162032cameraRecorder.mp4?dl=0";
//        onCallWebserviceForAllPost();
        mPrefs = MyApplication.getAppContext().getSharedPreferences("name", MODE_PRIVATE);
        userId = MyApplication.getAppContext().getSharedPreferences(Constants.Shared_Pref_Name, MODE_PRIVATE).getString(Constants.USER_ID, "");
        String json = mPrefs.getString("MyObject", "");
        Type type = new TypeToken<List<PostDtoListProxy>>() {
        }.getType();
        listPost = new Gson().fromJson(json, type);
        mVideosDownloader = new VideosDownloader(this.getActivity());
        if (listPost != null && !listPost.isEmpty()) {
            setDataToUI();
        } else {
            onShowToast("There is no post! You can show your talent & be the first!");
        }


        return rootView;
    }


    private ProgressDialog dialog;

    private void onOpenDialogueForWait(String message) {
        dialog = new ProgressDialog(this.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.setMessage(message);
        dialog.show();
    }

    private void onCloseDialogueForWait() {
        if (dialog != null) {
            dialog.dismiss();
        }

    }

    public void setDataToUI() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
//        onOpenDialogueForWait("Please wait video is loading");

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        int count = 0;
        Log.e(TAG, " Video URL : " + listPost.size());

        for (PostDtoListProxy postDtoList : listPost) {
            try {
//                String segments[] = postDtoList.getVideoUrl().split("/");
//                String s = "\\?dl";
//                String segment[] = segments[segments.length - 1].split(s);
//                Log.d(TAG, "setDataToUI: " + segment[0]);


                returnUrl=returnVideoName(postDtoList.getVideoUrl());

                File file = new File(BaseCameraActivity.getAndroidMoviesFolderInternalStorage(), returnUrl);
                if (file.exists()) {
                    //Do action
                    String filename = BaseCameraActivity.getAndroidMoviesFolderInternalStorage() + "/" + returnUrl;
//                    postDtoList.setVideoUrl(filename);

//                    Log.e(TAG, "is Video is not corrupt :  " +  isVideoIsNotCorrupt(file));
                    Log.e(TAG, "set url  " + postDtoList.getVideoUrl());
                    mList.add(ItemFactory.createItemFromAsset(postDtoList, R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));

                } else {
                    count++;
                    if (count == listPost.size()) {
                        mList.add(ItemFactory.createItemFromAsset(listPost.get(0), R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));

                    }

                    Intent mIntent = new Intent(Intent.ACTION_SYNC, null, this.getActivity(), VideoDownloadService.class);
                    mIntent.putExtra("URL", postDtoList.getVideoUrl());
                    this.getContext().startService(mIntent);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        videoRecyclerViewAdapter = new VideoRecyclerViewAdapter(mVideoPlayerManager, getActivity(), mList, this);

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
        mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager, mRecyclerView);

    }

//    public boolean isVideoIsNotCorrupt(File file){
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(this.getContext(), Uri.fromFile(file));
//
//        String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
//
//        return "yes".equals(hasVideo);
//    }

    public String returnVideoName(String originalUrl) {

        String segments[] = originalUrl.split("/");
        String s = "\\?dl";
        String segment[] = segments[segments.length - 1].split(s);
        Log.d(TAG, "setDataToUI: " + segment[0]);
//        String returnUrl=segment[0];

        return segment[0];
    }


    public void setUIDataUpdated() {


        if (!listPost.isEmpty() && listPost != null) {
            Log.e(TAG, " Video URL : setUIDataUpdated " + listPost.size());
            if (countForLoad == 1) {
                onCloseDialogueForWait();
                countForLoad = 0;
            }
//       onCloseDialogueForWait();
            mList.clear();
            for (PostDtoListProxy postDtoList : listPost) {
                try {
//                    String segments[] = postDtoList.getVideoUrl().split("/");
//                    String s = "\\?dl";
//                    String segment[] = segments[segments.length - 1].split(s);
//                    Log.d(TAG, "setDataToUI: " + segment[0]);

                    returnUrl=returnVideoName(postDtoList.getVideoUrl());

//                    File file = new File(BaseCameraActivity.getAndroidMoviesFolderInternalStorage(), returnUrl);
//                    if (file.exists()) {
//                        //Do action
//                        String filename = BaseCameraActivity.getAndroidMoviesFolderInternalStorage() + "/" + returnUrl;
//                        postDtoList.setVideoUrl(filename);
                        Log.e(TAG, "set url  " + postDtoList.getVideoUrl());
//                    mList.add(ItemFactory.createItemFromAsset(postDtoList.getVideoUrl(), R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
                        mList.add(ItemFactory.createItemFromAsset(postDtoList, R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
//                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (videoRecyclerViewAdapter != null) {
                videoRecyclerViewAdapter.notifyDataSetChanged();
            }


        }
    }


    @Override
    public void onLoadBindMethod(int position, VideoViewHolder holder) {

        holder.mPlayer.addMediaPlayerListener(new MediaPlayerWrapper.MainThreadMediaPlayerListener() {
            @Override
            public void onVideoSizeChangedMainThread(int width, int height) {

            }

            @Override
            public void onVideoPreparedMainThread() {

            }

            @Override
            public void onVideoCompletionMainThread() {

            }

            @Override
            public void onErrorMainThread(int what, int extra) {
//                onRemoveElementFromList(position,holder);
            }

            @Override
            public void onBufferingUpdateMainThread(int percent) {

            }

            @Override
            public void onVideoStoppedMainThread() {

            }
        });

    }

        private void onRemoveElementFromList(int position, VideoViewHolder viewHolder) {
            int newPosition = viewHolder.getAdapterPosition();
            if (listPost!=null) {
                assert listPost.get(newPosition).getVideoUrl() != null;
                if (listPost.get(newPosition).getVideoUrl() != null) {
                    returnUrl = returnVideoName(listPost.get(newPosition).getVideoUrl());

                    File file = new File(BaseCameraActivity.getAndroidMoviesFolderInternalStorage(), returnUrl);
                    if (file.exists()) {
                        mList.remove(newPosition);
                        videoRecyclerViewAdapter.notifyItemRemoved(newPosition);
                        videoRecyclerViewAdapter.notifyItemRangeChanged(newPosition, mList.size());
                    }

                }
            }

    }

    private void onShowToast(String message) {

        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mList.isEmpty()) {
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

            mVideoPlayerManager.resetMediaPlayer();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        // we have to stop any playback in onStop
        mVideoPlayerManager.stopAnyPlayback();

//        new SimpleMainThreadMediaPlayerListener() {
//            @Override
//            public void onErrorMainThread(int what, int extra) {
//                Log.d(TAG, "onErrorMainThread");
//                mVideoPlayerManager.resetMediaPlayer();
//            }
//        };
//        mVideoPlayerManager.resetMediaPlayer();
    }


    @Override
    public void onLoadFabClick(int position, TextView txt_comment_count) {

//        View dialogView = getLayoutInflater().inflate(R.layout.comments_bottom_sheet_layout, null);
//        BottomSheetDialog dialog = new BottomSheetDialog(this.getActivity());
//        dialog.setContentView(dialogView);
//        dialog.show();

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

//            dialog.dismiss();

        }

        @Override
        protected Void doInBackground(String... strings) {




           /* try {
                URL u = new URL(strings[0]);
                URLConnection conn = u.openConnection();
                int contentLength = conn.getContentLength();

                DataInputStream stream = new DataInputStream(u.openStream());

                byte[] buffer = new byte[contentLength];
                stream.readFully(buffer);
                stream.close();

                DataOutputStream fos = new DataOutputStream(new FileOutputStream(BaseCameraActivity.getAndroidMoviesFolderInternalStorage()));
                fos.write(buffer);
                fos.flush();
                fos.close();
            } catch(FileNotFoundException e) {
                return null; // swallow a 404
            } catch (IOException e) {
                return null; // swallow a 404
            }
*/

               /* try
                {
                    URL url = new URL(strings[0]);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.setDoOutput(true);
                    c.connect();

                    String PATH = BaseCameraActivity.getAndroidMoviesFolderInternalStorage()
                            + "/load";
                    Log.v("LOG_TAG", "PATH: " + PATH);

                    File file = new File(PATH);
                    file.mkdirs();
                    File outputFile = new File(file, strings[0]);
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    InputStream is = c.getInputStream();

                    byte[] buffer = new byte[4096];
                    int len1 = 0;

                    while ((len1 = is.read(buffer)) != -1)
                    {
                        fos.write(buffer, 0, len1);
                    }

                    fos.close();
                    is.close();

                    Toast.makeText(MyApplication.getAppContext(), " A new file is downloaded successfully",
                            Toast.LENGTH_LONG).show();

                }
                catch (IOException e)
                {

                    dialog.dismiss();
                    e.printStackTrace();
                }*/


            return null;


        }
    }

    DbxClientV2 sDbxClient;

    private void onDonloadVideo(String fileUri) {
        final ProgressDialog dialog = new ProgressDialog(this.getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.setMessage("Downloading");
        dialog.show();


        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("dropbox/Apps/Talent Plus")
                .withHttpRequestor(new OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
                .build();

        sDbxClient = new DbxClientV2(requestConfig, Constants.DROPBOX_ACCESS_TOKEN);


        new DownloadFileTask(MyApplication.getAppContext(), sDbxClient, new DownloadFileTask.Callback() {

            @Override
            public void onDownloadComplete(File result) {

                dialog.dismiss();
                if (new Utility(MyApplication.getAppContext()).isConnectingToInternet()) {
//                    onCallWebserviceForSavePost(dialog);

                } else {
                    onShowToast("Internet connection is not available");
                }
            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();

                Log.e(TAG, "Failed to download file.", e);
//                e.printStackTrace();
                Toast.makeText(MyApplication.getAppContext(),
                        "An error has occurred",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(fileUri);
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

        Intent mIntent = new Intent(this.getContext(), UserProfileActivity.class);
        mIntent.putExtra("OtherUserId", listPost.get(position).getUserId());
        startActivity(mIntent);
    }
}