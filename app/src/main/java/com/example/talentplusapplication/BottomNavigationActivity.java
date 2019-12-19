package com.example.talentplusapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.Proxy.ResponseGetAllPost;
import com.example.talentplusapplication.Proxy.ResponseRegistration;
import com.example.talentplusapplication.Proxy.SavePostInput;
import com.example.talentplusapplication.main.profile.ProfileActivity;
import com.example.talentplusapplication.ui.camera.CameraActivity;
import com.example.talentplusapplication.ui.camera.UploadActivity;
import com.example.talentplusapplication.webservices.ApiClient;
import com.example.talentplusapplication.webservices.ApiInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomNavigationActivity extends BaseActivity {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    public static List<PostDtoListProxy> listPost = new ArrayList<>();

    private SharedPreferences mSharedPreferences;
    private String userId;
    private static NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onHideStatusBarAndToolBar();

        setContentView(R.layout.activity_bottom_navigation);
        mSharedPreferences = getSharedPreferences(Constants.Shared_Pref_Name, MODE_PRIVATE);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_me)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (checkAndRequestPermissions()) {
                            navController.navigate(R.id.navigation_home);
                        }

                        break;
                    case R.id.navigation_dashboard:
                        navController.navigate(R.id.navigation_dashboard);
                        break;
                    case R.id.navigation_camera:

                        if (checkAndRequestPermissions()) {
                            Intent a = new Intent(BottomNavigationActivity.this, CameraActivity.class);
                            startActivity(a);
                        }
                        break;
                    case R.id.navigation_me:
                       /* Intent b = new Intent(BottomNavigationActivity.this, ProfileActivity.class);
                        startActivity(b);*/
                        onOpenDialogueForWait("please wait");

                        userId = mSharedPreferences.getString(Constants.USER_ID, "");
                        if (userId.equals("")) {
                            onShowToast("Please login ");
                            Intent a = new Intent(BottomNavigationActivity.this, LoginActivity.class);
                            startActivity(a);

                        } else {
                            navController.navigate(R.id.navigation_me);
                        }
                        onCloseDialogueForWait();
                        break;
                }
                return false;
            }
        });
    }

    private ProgressDialog dialog;

    private void onOpenDialogueForWait(String message) {
        dialog = new ProgressDialog(this);
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


    public static void onNavigateToTab() {

            navController.navigate(R.id.navigation_home);
    }

    public void onLoadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
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
                            showDialogOK("Camera and Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
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

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    public void onCallWebserviceForAllPost() {

        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);

        mApiInterface.getAllPost("").enqueue(new Callback<ResponseGetAllPost>() {
            @Override
            public void onResponse(Call<ResponseGetAllPost> call, Response<ResponseGetAllPost> response) {
                if (response.isSuccessful()) {

                    if (response.body().getCode().equals("EP0001")) {
                        onShowToast(response.body().getDescription());

                    } else if (response.body().getCode().equals("200")) {
//                        onShowToast(response.body().getDescription());
//                        listPost=response.body().getPostsDtoList();

//                        setDataToUI(response.body().getPostsDtoList());


                    } else {
//                        listPost=new ArrayList<>();
                        onShowToast("Please try again");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGetAllPost> call, Throwable t) {

//                listPost=new ArrayList<>();
                onShowToast("Something is wrong");
            }
        });
    }

  /* public static List<ResponseGetAllPost.PostDtoList> listPost =new ArrayList<>();
    private void setDataToUI(List<ResponseGetAllPost.PostDtoList> postsDtoList) {


        listPost=postsDtoList;


    }
    public static List<ResponseGetAllPost.PostDtoList> getPostList(){

        return listPost;
    }*/

    private void onShowToast(String message) {
        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String path = intent.getStringExtra("path");
            String description = intent.getStringExtra("description");

//            onCallWebserviceForSavePost(path,description);

        }
    }

    public void onCallWebserviceForSavePost(String path, String description) {


        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);
        SavePostInput input = new SavePostInput("2", description, path);

        JsonObject mJsonObject = new JsonObject();
        mJsonObject.addProperty("userId", input.getUserId());
        mJsonObject.addProperty("postTitle", input.getPostTitle());
        mJsonObject.addProperty("videoUrl", input.getVideoUrl());
//                Log.e("LoginActivity","String json " +mJsonObject.toString());

        mApiInterface.savePost(mJsonObject).enqueue(new Callback<ResponseRegistration>() {
            @Override
            public void onResponse(Call<ResponseRegistration> call, Response<ResponseRegistration> response) {
                if (response.isSuccessful()) {

                    if (response.body().getCode().equals("200")) {

//                        onShowToast(response.body().getDescription());

//                        Intent mIntent=new Intent(UploadActivity.this, BottomNavigationActivity.class);
//                        startActivity(mIntent);
//                        finish();

                    } else {

//                        onShowToast(response.body().getDescription());
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseRegistration> call, Throwable t) {
//                        Toast.makeText(MyApplication.getAppContext(),"Something is wrong",Toast.LENGTH_SHORT).show();

//                button.setEnabled(true);
//                onShowToast("Something is wrong");


            }
        });
    }

    MyReceiver myReceiver;

    private void setReceiver() {

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UploadActivity.FILTER_ACTION_KEY);

        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onStart() {

        super.onStart();

       /* try{
            setReceiver();
        } catch (Exception e){
            // already registered
        }*/
    }

    @Override
    protected void onStop() {
//        unregisterReceiver(myReceiver);
        super.onStop();

      /*  try{
            unregisterReceiver(myReceiver);
        } catch (Exception e){
            // already unregistered
        }*/
    }

}
