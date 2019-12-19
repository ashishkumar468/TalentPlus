package com.example.talentplusapplication.ui.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talentplusapplication.BottomNavigationActivity;
import com.example.talentplusapplication.Constants;
import com.example.talentplusapplication.LoginActivity;
import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.Proxy.ResponseGetUserProfile;
import com.example.talentplusapplication.Proxy.UserDTOProxy;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.Utility;
import com.example.talentplusapplication.main.profile.EditProfileActivity;
import com.example.talentplusapplication.ui.profile.adapter.ProfilePostAdapter;
import com.example.talentplusapplication.ui.user.UserVideoPlayActivity;
import com.example.talentplusapplication.ui.user.follow_unfollow.FollowUnfollowActivity;
import com.example.talentplusapplication.views.CircularImageView;
import com.example.talentplusapplication.webservices.ApiClient;
import com.example.talentplusapplication.webservices.ApiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements ProfilePostAdapter.OnLoadThumbnailListener {

    private Context mContext;
    private NotificationsViewModel notificationsViewModel;
    private RecyclerView mRecyclerView;
    private ProfilePostAdapter profilePostAdapter;
    private SharedPreferences mSharedPreferences;
    private String userId;
    private UserDTOProxy userDTOProxy;
    private List<PostDtoListProxy> listPost;
    private ProfilePostAdapter.OnLoadThumbnailListener onLoadThumbnailListener;

    private CircularImageView profile_image;
    private AppCompatButton btn_edit_profile;
    private AppCompatButton btn_logout;
    private TextView txt_name;
    private TextView txt_post;
    private TextView txt_likes;
    private TextView txt_following;
    private TextView txt_followers;
    private String TAG = "ProfileFragment";
    private String userProfileUrl;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mSharedPreferences = MyApplication.getAppContext().getSharedPreferences(Constants.Shared_Pref_Name, Context.MODE_PRIVATE);
        mRecyclerView = root.findViewById(R.id.profile_recycler_view);
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 5, false));

        mContext = this.getContext();
        initialization(root);
        setDataToProfilePostAdapter();

        return root;
    }


    private void initialization(View root) {
        profile_image = root.findViewById(R.id.imageView_circular);
        btn_edit_profile = root.findViewById(R.id.edit_profile_btn);
        btn_logout = root.findViewById(R.id.logout_btn);
        btn_logout.setVisibility(View.VISIBLE);
        txt_name = root.findViewById(R.id.nameTextView);
        txt_post = root.findViewById(R.id.postsCounterTextView);
        txt_likes = root.findViewById(R.id.likesCountersTextView);
        txt_following = root.findViewById(R.id.followingsCounterTextView);
        txt_followers = root.findViewById(R.id.followersCounterTextView);

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, EditProfileActivity.class);
                startActivityForResult(intent, 1);

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSharedPreferences.edit().remove(Constants.USER_ID).apply();
                onShowToast("Logout successfully");
                BottomNavigationActivity.onNavigateToTab();


            }
        });


        txt_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, FollowUnfollowActivity.class);
                intent.putExtra("isFollow", false);
                startActivity(intent);

            }
        });

        txt_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, FollowUnfollowActivity.class);
                intent.putExtra("isFollow", true);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e(TAG,"This is ");
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            Log.e(TAG, data.getStringExtra("name"));
            txt_name.setText(data.getStringExtra("name"));
            Glide.with(this).load(data.getStringExtra("path")).fitCenter().into(profile_image);

        }
    }

    private void setDataToProfilePostAdapter() {

        userId = mSharedPreferences.getString(Constants.USER_ID, "");

        if (new Utility(this.getContext()).isConnectingToInternet()) {
            onCallWebServicesGetProfileDetails();
        } else {
            onShowToast("Internet connection is not available");
        }


    }

    private void onCallWebServicesGetProfileDetails() {
        final ProgressDialog dialog = new ProgressDialog(this.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait");
        dialog.show();
        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);

        mApiInterface.getUserProfile(userId).enqueue(new Callback<ResponseGetUserProfile>() {
            @Override
            public void onResponse(Call<ResponseGetUserProfile> call, Response<ResponseGetUserProfile> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getCode().equals("EP0001")) {

                    } else if (response.body().getCode().equals("UAE-005")) {

                        Intent mIntent=new Intent(MyApplication.getAppContext(), LoginActivity.class);
                        startActivity(mIntent);

                    } else if (response.body().getCode().equals("200")) {
//                        onShowToast(response.body().getDescription());
                        userDTOProxy = response.body().getUserDto();
                        userProfileUrl=userDTOProxy.getProfilePictureUrl();
                        listPost = response.body().getUserDto().getPostsDtoList();

                        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(listPost);
                        prefsEditor.putString("UserMyObject", json);
                        prefsEditor.apply();

                        onSetData();


                    } else {
                        onShowToast(response.body().getDescription());
                        listPost = new ArrayList<>();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGetUserProfile> call, Throwable t) {
                onShowToast("Something is wrong");
                listPost = new ArrayList<>();

            }
        });
    }

    private void onSetData() {
        if (userDTOProxy.getfName() != null && userDTOProxy.getlName() != null) {
            txt_name.setText(userDTOProxy.getfName() + " " + userDTOProxy.getlName());
        } else if (userDTOProxy.getfName() != null) {
            txt_name.setText(userDTOProxy.getfName());
        }else if(userDTOProxy.getlName() != null){
            txt_name.setText(userDTOProxy.getlName());
        }

        if (listPost != null) {
            txt_post.setText("" + listPost.size() + "\n" + "posts");
        } else {
            txt_post.setText("0" + "\n" + "posts");
        }

        if (userDTOProxy.getLikeCount() != null) {
            txt_likes.setText("" + userDTOProxy.getLikeCount() + "\n" + "likes");
        }
        if (userDTOProxy.getFollowersCount() != null) {
            txt_followers.setText("" + userDTOProxy.getFollowersCount() + "\n" + "followers");
        }
        if (userDTOProxy.getFollowingCount() != null) {
            txt_following.setText("" + userDTOProxy.getFollowingCount() + "\n" + "following");
        }
        if (userDTOProxy.getProfilePictureUrl() != null) {
            Glide.with(this).load(userDTOProxy.getProfilePictureUrl()).centerInside().into(profile_image);
        }


        List<String> urlList = new ArrayList<>();
        if(listPost!=null) {
            for (PostDtoListProxy postDtoListProxy : listPost) {
                urlList.add(postDtoListProxy.getVideoUrl());
            }

            profilePostAdapter = new ProfilePostAdapter(mContext, urlList, this);
            mRecyclerView.setAdapter(profilePostAdapter);
        }
    }

    private void onShowToast(String message) {

        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoadThumbnailClick(int position) {

        Log.e(TAG, "position " + position);


        Intent mIntent = new Intent(getContext(), UserVideoPlayActivity.class);
        mIntent.putExtra("position", position);
        mIntent.putExtra("user_name", txt_name.getText().toString());
        mIntent.putExtra("user_profile_url", userProfileUrl);
        startActivity(mIntent);

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}