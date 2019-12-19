package com.example.talentplusapplication.ui.user.follow_unfollow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talentplusapplication.Constants;
import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.Proxy.FollowersDtoProxy;
import com.example.talentplusapplication.Proxy.FollowingDtoProxy;
import com.example.talentplusapplication.Proxy.ResponseFollowers;
import com.example.talentplusapplication.Proxy.ResponseFollowing;
import com.example.talentplusapplication.Proxy.ResponseGetUserProfile;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.Utility;
import com.example.talentplusapplication.ui.user.follow_unfollow.FollowUnfollowAdapter.FollowUnFollowAdapter;
import com.example.talentplusapplication.webservices.ApiClient;
import com.example.talentplusapplication.webservices.ApiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowUnfollowActivity extends AppCompatActivity implements FollowUnFollowAdapter.OnLoadThumbnailListener {


    private SharedPreferences mSharedPreferences;
    private RecyclerView mRecyclerView;
    private String userId;
    private boolean isFollow;
    private List<FollowersDtoProxy> followersDtoList;
    private List<FollowingDtoProxy> followingDtoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_unfollow);

        isFollow = getIntent().getBooleanExtra("isFollow", true);

        mSharedPreferences = MyApplication.getAppContext().getSharedPreferences(Constants.Shared_Pref_Name, Context.MODE_PRIVATE);
        mRecyclerView = findViewById(R.id.recyclerview_follow_unfollow);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (isFollow) {
            mToolbar.setTitle("Following");
        } else {
            mToolbar.setTitle("Followers");
        }


        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);

        mTitle.setText(mToolbar.getTitle());

        this.getSupportActionBar().setDisplayShowTitleEnabled(false);


        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setFollowersList();

    }

    private void setFollowersList() {

        userId = mSharedPreferences.getString(Constants.USER_ID, "");

        if (new Utility(this).isConnectingToInternet()) {
            onCallWebServicesGetProfileDetails();
        } else {
            onShowToast("Internet connection is not available");
        }
    }

    private void onCallWebServicesGetProfileDetails() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait");
        dialog.show();
        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);

        if (isFollow) {

            mApiInterface.getAllFollowing(userId).enqueue(new Callback<ResponseFollowing>() {
                @Override
                public void onResponse(Call<ResponseFollowing> call, Response<ResponseFollowing> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {

                        if (response.body().getCode().equals("EP0001")) {

                        } else if (response.body().getCode().equals("200")) {
//                        onShowToast(response.body().getDescription());
                            ResponseFollowing mResponseFollowers = response.body();
                            if (mResponseFollowers.getFollowingDto() != null) {
                                followingDtoList.addAll(mResponseFollowers.getFollowingDto());
                            } else {
                                followingDtoList = mResponseFollowers.getFollowingDtoList();
                            }


                            onSetData();


                        } else {
                            onShowToast(response.body().getDescription());
                            followingDtoList = new ArrayList<>();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseFollowing> call, Throwable t) {
                    onShowToast("Something is wrong");
                    followingDtoList = new ArrayList<>();

                }
            });

        } else {
            mApiInterface.getAllFollowers(userId).enqueue(new Callback<ResponseFollowing>() {
                @Override
                public void onResponse(Call<ResponseFollowing> call, Response<ResponseFollowing> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {

                        if (response.body().getCode().equals("EP0001")) {

                        } else if (response.body().getCode().equals("200")) {
//                        onShowToast(response.body().getDescription());
                            ResponseFollowing mResponseFollowers = response.body();
                            if (mResponseFollowers.getFollowingDto() != null) {
                                followingDtoList.addAll(mResponseFollowers.getFollowingDto());
                            } else {
                                followingDtoList = mResponseFollowers.getFollowingDtoList();
                            }


                            onSetData();


                        } else {
                            onShowToast(response.body().getDescription());
                            followingDtoList = new ArrayList<>();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseFollowing> call, Throwable t) {
                    onShowToast("Something is wrong");
                    followingDtoList = new ArrayList<>();

                }
            });
        }


    }

    private void onSetData() {

        FollowUnFollowAdapter followUnFollowAdapter = null;
//        if (isFollow){
//             followUnFollowAdapter = new FollowUnFollowAdapter(this, followersDtoList,followingDtoList, this, isFollow);
////            mRecyclerView.setAdapter(followUnFollowAdapter);
//
//        }else {
//            if (followersDtoList.size()>0){
//                 followUnFollowAdapter = new FollowUnFollowAdapter(this, followersDtoList, followingDtoList,this, isFollow);
////                mRecyclerView.setAdapter(followUnFollowAdapter);
//            }
//        }
        followUnFollowAdapter = new FollowUnFollowAdapter(this, followersDtoList, followingDtoList,this, isFollow);
        mRecyclerView.setAdapter(followUnFollowAdapter);
    }

    private void onShowToast(String message) {

        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoadThumbnailClick(int position) {


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
