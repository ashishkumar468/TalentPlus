package com.example.talentplusapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.Proxy.ResponseGetAllPost;
import com.example.talentplusapplication.ui.home.HomeFragment;
import com.example.talentplusapplication.ui.test_home.TestHomeActivity;
import com.example.talentplusapplication.webservices.ApiClient;
import com.example.talentplusapplication.webservices.ApiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static int SPLASH_SCREEN_TIME_OUT = 2000;
    private static String TAG = "SplashScreen";
    private List<PostDtoListProxy> listPost;
    private String userId;
    VideosDownloader mVideosDownloader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.content_main);
         mVideosDownloader=new VideosDownloader(SplashScreen.this);

        if (new Utility(MyApplication.getAppContext()).isConnectingToInternet()) {
            if (checkAndRequestPermissions()) {
                onCallWebserviceForAllPost();
            }
        }

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mIntent = new Intent(SplashScreen.this,
//                        YouTubePlayActivity.class);
                        BottomNavigationActivity.class);
//                        CameraActivity.class);
//                        LoginActivity.class);
                startActivity(mIntent);
                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);*/
    }


    public void onCallWebserviceForAllPost() {

        userId = getSharedPreferences(Constants.Shared_Pref_Name, MODE_PRIVATE).getString(Constants.USER_ID, "");

        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);

        mApiInterface.getAllPost(userId).enqueue(new Callback<ResponseGetAllPost>() {
            @Override
            public void onResponse(Call<ResponseGetAllPost> call, Response<ResponseGetAllPost> response) {
                if (response.isSuccessful()) {

                    if (response.body().getCode().equals("EP0001")) {
                        listPost = new ArrayList<>();
                        SharedPreferences mPrefs = getSharedPreferences("name", MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(listPost);
                        prefsEditor.putString("MyObject", json);
                        prefsEditor.apply();

                        Intent mIntent = new Intent(SplashScreen.this,
//                        YouTubePlayActivity.class);
                                BottomNavigationActivity.class);
//                        CameraActivity.class);
//                        LoginActivity.class);
//                        Bundle data = new Bundle();
//                        data.putSerializable("data", (Serializable) listPost);
//                        mIntent.putExtra ("result.content", data);
                        startActivity(mIntent);
                        finish();

                    } else if (response.body().getCode().equals("NPF-002")) {
                        listPost = new ArrayList<>();
                        SharedPreferences mPrefs = getSharedPreferences("name", MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(listPost);
                        prefsEditor.putString("MyObject", json);
                        prefsEditor.apply();

                        Intent mIntent = new Intent(SplashScreen.this,
//                        YouTubePlayActivity.class);
                                BottomNavigationActivity.class);
//                        CameraActivity.class);
//                        LoginActivity.class);
//                        Bundle data = new Bundle();
//                        data.putSerializable("data", (Serializable) listPost);
//                        mIntent.putExtra ("result.content", data);
                        startActivity(mIntent);
                        finish();

                    } else if (response.body().getCode().equals("200")) {
//                        onShowToast(response.body().getDescription());
                        listPost = response.body().getPostsDtoList();

                        SharedPreferences mPrefs = getSharedPreferences("name", MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(listPost);
                        prefsEditor.putString("MyObject", json);
                        prefsEditor.apply();

//                        for (PostDtoListProxy postDtoListProxy:listPost){
//                            mVideosDownloader.startVideosDownloading(postDtoListProxy.getVideoUrl());
//
//                        }

                        Intent mIntent = new Intent(SplashScreen.this,
//                        YouTubePlayActivity.class);
                                BottomNavigationActivity.class);
//                        CameraActivity.class);
//                        LoginActivity.class);
//                        Bundle data = new Bundle();
//                        data.putSerializable("data", (Serializable) listPost);
//                        mIntent.putExtra ("result.content", data);
                        startActivity(mIntent);
                        finish();

                    } else {
                        listPost = new ArrayList<>();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGetAllPost> call, Throwable t) {

                listPost = new ArrayList<>();
            }
        });
    }


    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(MyApplication.getAppContext(),
                Manifest.permission.CAMERA);
        int record_audio = ContextCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.RECORD_AUDIO);
        int wtite = ContextCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (record_audio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("in fragment on request", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                        // process the normal flow
                        if (new Utility(MyApplication.getAppContext()).isConnectingToInternet()) {
                                onCallWebserviceForAllPost();
                        }
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("in fragment on request", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

}

