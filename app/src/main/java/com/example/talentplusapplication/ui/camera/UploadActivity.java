package com.example.talentplusapplication.ui.camera;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.SharedLink;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.example.talentplusapplication.BottomNavigationActivity;
import com.example.talentplusapplication.Constants;
import com.example.talentplusapplication.ForgotPassword;
import com.example.talentplusapplication.LoginActivity;
import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.Proxy.LoginInput;
import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.Proxy.ResponseGetAllPost;
import com.example.talentplusapplication.Proxy.ResponseLogin;
import com.example.talentplusapplication.Proxy.ResponseRegistration;
import com.example.talentplusapplication.Proxy.SavePostInput;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.SplashScreen;
import com.example.talentplusapplication.Utility;
import com.example.talentplusapplication.ui.camera.videocompression.MediaController;
import com.example.talentplusapplication.webservices.ApiClient;
import com.example.talentplusapplication.webservices.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;

    private static final String TAG = "UploadActivity";
    private static final String ACCESS_TOKEN = Constants.DROPBOX_ACCESS_TOKEN;
    public static final String FILTER_ACTION_KEY = "Service_key";
    private List<PostDtoListProxy> listPost;
    ImageView img_thumbnail;
    EditText edt_description;
    AppCompatButton btn_cancel;
    AppCompatButton btn_done;
    private boolean is_recorded;

    private String path;

    private String upload_post_path;
    private String user_id;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        path = getIntent().getStringExtra("video_path");
        is_recorded = getIntent().getBooleanExtra("is_record",false);
        Log.e(TAG, "path   " + path);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mSharedPreferences =getSharedPreferences(Constants.Shared_Pref_Name,MODE_PRIVATE);


        initialization();


    }

    private void initialization() {

        img_thumbnail = findViewById(R.id.image_thumbnail);
        edt_description = findViewById(R.id.edt_description);
        btn_done = findViewById(R.id.btn_done);
        btn_cancel = findViewById(R.id.btn_cancel);

        Glide.with(this).load(path).centerCrop().into(img_thumbnail);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_id=mSharedPreferences.getString(Constants.USER_ID,"");
                if (user_id.equals("")){

                    Intent mIntent=new Intent(UploadActivity.this,LoginActivity.class);
                    startActivity(mIntent);

                }else {
                    if (checkAndRequestPermissions()) {
                        if (new Utility(UploadActivity.this).isConnectingToInternet()) {

                            btn_done.setEnabled(false);

                            File file = new File(path);
                            // Get length of file in bytes
                            long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                            long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                            long fileSizeInMB = fileSizeInKB / 1024;

                            long fileMinSize = 10;

                            if(fileSizeInMB>fileMinSize){
                                Log.e(TAG,"Video Size  " +fileSizeInMB);
                                new VideoCompressor().execute();
                            }else {
                                Log.e(TAG,"Video Size  " +fileSizeInMB);
                                uploadFile(path);
                            }
//                           Log.e(TAG,"Is redcorde " +is_recorded);
//                            if(is_recorded){
//                                new VideoCompressor().execute();
//                                Log.e(TAG,"Is redcorde if  " +is_recorded);
//                            }else {
//                                Log.e(TAG,"Is redcorde  else " +is_recorded);
////                                uploadFile(path);
//                            }

//                            uploadFile(path);


                        } else {
                            onShowToast("Internet connection is not available");
                        }
                    }
                }
            }
        });
    }

    private class VideoCompressor extends AsyncTask<Void, Void, Boolean> {

        String compress_path;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return MediaController.getInstance().convertVideo(path);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            if (compressed) {
                compress_path = MediaController.cachedFile.getPath();
                Log.e("Compression", "Compression successfully!");
                Log.e("Compressed File Path", "" + MediaController.cachedFile.getPath());
                File file = new File(compress_path);
                // Get length of file in bytes
                long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;
                Log.e("Compression", "Compression successfully! file size : "+fileSizeInMB);
                uploadFile(compress_path);
            }
        }
    }

    private void onShowToast(String message) {

        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();

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
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("in fragment on request", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
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

    private boolean checkAndRequestPermissions() {
        int wtite = ContextCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    DbxClientV2 sDbxClient;
    private void uploadFile(String fileUri) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.setMessage("Uploading");
        dialog.show();



        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("dropbox/Apps/Talent Plus")
                .withHttpRequestor(new OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
                .build();

        sDbxClient = new DbxClientV2(requestConfig, ACCESS_TOKEN);

        new UploadFileTask(this, true, sDbxClient, new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete(FileMetadata result) {
//                dialog.dismiss();
                Log.e(TAG, "Upload File name." + result.getId() + " " +
                        result.getName() + "  " + result.getPathDisplay() + "  " +
                        result.getParentSharedFolderId());

                upload_post_path = result.getPathDisplay();

                if (new Utility(UploadActivity.this).isConnectingToInternet()) {
                    onCallWebserviceForSavePost(dialog);
//                    Intent intent = new Intent(UploadActivity.this, UploadPostService.class);
//                    intent.putExtra("path", upload_post_path);
//                    intent.putExtra("description", edt_description.getText().toString());
//                    startService(intent);
//
//                    Intent mIntent=new Intent(UploadActivity.this, BottomNavigationActivity.class);
//                        startActivity(mIntent);
//                        finish();

                } else {
                    onShowToast("Internet connection is not available");
                }

//                String message = result.getName() + " size " + result.getSize() + " modified " +
//                        DateFormat.getDateTimeInstance().format(result.getClientModified());

                String message = "Your video has been upload";

                onShowToast(message);

            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();

                Log.e(TAG, "Failed to upload file.", e);
//                e.printStackTrace();
                Toast.makeText(UploadActivity.this,
                        "An error has occurred",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(fileUri);
    }

    private void onCallWebserviceForSavePost(ProgressDialog dialog) {

        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);
        SavePostInput input = new SavePostInput(user_id, edt_description.getText().toString(), UploadFileTask.getSharedUrl());

        Log.e(TAG,"Title "+input.getPostTitle());

        JsonObject mJsonObject = new JsonObject();
        mJsonObject.addProperty("userId", input.getUserId());
        mJsonObject.addProperty("postTitle", input.getPostTitle());
        mJsonObject.addProperty("videoUrl", input.getVideoUrl());
//                Log.e("LoginActivity","String json " +mJsonObject.toString());

        mApiInterface.savePost(mJsonObject).enqueue(new Callback<ResponseRegistration>() {
            @Override
            public void onResponse(Call<ResponseRegistration> call, Response<ResponseRegistration> response) {
//                dialog.cancel();
                if (response.isSuccessful()) {

                    if (response.body().getCode().equals("200")) {

//                        onShowToast(response.body().getDescription());

                        onCallAllPostApi(dialog);

//                        Intent mIntent=new Intent(UploadActivity.this, BottomNavigationActivity.class);
//                        startActivity(mIntent);
//                        finish();

                    } else {
                        dialog.cancel();
                       onShowToast("Please upload video again");
//                        Intent mIntent=new Intent(UploadActivity.this, BottomNavigationActivity.class);
//                        startActivity(mIntent);
//                        finish();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseRegistration> call, Throwable t) {
//                        Toast.makeText(MyApplication.getAppContext(),"Something is wrong",Toast.LENGTH_SHORT).show();

//                button.setEnabled(true);
                onShowToast("Something is wrong");


                dialog.cancel();
            }
        });

    }

    private void onCallAllPostApi(ProgressDialog dialog) {


        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);

        mApiInterface.getAllPost(user_id).enqueue(new Callback<ResponseGetAllPost>() {
            @Override
            public void onResponse(Call<ResponseGetAllPost> call, Response<ResponseGetAllPost> response) {
                if (dialog!=null){
                    dialog.dismiss();
                }

                if (response.isSuccessful()) {

                    if (response.body().getCode().equals("EP0001")) {
//                        onShowToast("Please upload video again");

                    } else if (response.body().getCode().equals("200")) {
//                        onShowToast(response.body().getDescription());
                        listPost = response.body().getPostsDtoList();

                        SharedPreferences mPrefs = getSharedPreferences("name", MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        prefsEditor.remove("MyObject");
                        Gson gson = new Gson();
                        String json = gson.toJson(listPost);
                        prefsEditor.putString("MyObject", json);
                        prefsEditor.apply();

                        Intent mIntent = new Intent(UploadActivity.this,
                                BottomNavigationActivity.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mIntent);
                        finish();

                    } else {
                        listPost = new ArrayList<>();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGetAllPost> call, Throwable t) {
                dialog.dismiss();
                listPost = new ArrayList<>();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        this.finishAffinity();
        finish();
    }

}
