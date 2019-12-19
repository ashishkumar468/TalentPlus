package com.example.talentplusapplication.main.profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.OkHttp3Requestor;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.example.talentplusapplication.Constants;
import com.example.talentplusapplication.LoginActivity;
import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.Proxy.ResponseEditProfile;
import com.example.talentplusapplication.Proxy.ResponseRegistration;
import com.example.talentplusapplication.Proxy.SavePostInput;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.Utility;
import com.example.talentplusapplication.ui.camera.RecordPlayActivity;
import com.example.talentplusapplication.ui.camera.UploadActivity;
import com.example.talentplusapplication.ui.camera.UploadFileTask;
import com.example.talentplusapplication.views.CircularImageView;
import com.example.talentplusapplication.webservices.ApiClient;
import com.example.talentplusapplication.webservices.ApiInterface;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final String ACCESS_TOKEN = "DLQ-YalbVGAAAAAAAAAADgUkxTsoeDCIsyNTJ11N5hBi8ryEjLbq59zLdcKWrgXg";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;

    public ActionBar actionBar;
    private CircularImageView profile_image;
    private EditText editText;
    private AppCompatButton btn_save;
    private String selectedVideoPath;
    private String TAG = "EditProfActy";
    private String upload_post_path;
    private String user_id;
    private SharedPreferences mSharedPreferences;
    private String newImagePath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mSharedPreferences = getSharedPreferences(Constants.Shared_Pref_Name, MODE_PRIVATE);

        editText = findViewById(R.id.nameEditText);
        btn_save = findViewById(R.id.btn_save);
        profile_image = findViewById(R.id.imageView_circular);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkAndRequestPermissions()) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(photoPickerIntent, 1);
                } else {
                    onShowToast("Please set the permission");
                }

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                user_id = mSharedPreferences.getString(Constants.USER_ID, "");
                if (user_id.equals("")) {

                    Intent mIntent = new Intent(EditProfileActivity.this, LoginActivity.class);
                    startActivity(mIntent);

                } else {
                    if (new Utility(EditProfileActivity.this).isConnectingToInternet()) {
                        if (selectedVideoPath.equals("") || selectedVideoPath.isEmpty()){
                            onShowToast("Please select the Image!");
                        }else {
                            uploadFile(selectedVideoPath);
                        }

                    } else {
                        onShowToast("Internet connection is not available");
                    }
                }


//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("path", selectedVideoPath);
//                returnIntent.putExtra("name", editText.getText().toString());
//                setResult(Activity.RESULT_OK, returnIntent);
//                finish();

            }
        });
    }


    private void onShowToast(String message) {

        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();

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

        new UploadFileTask(this, false, sDbxClient, new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete(FileMetadata result) {
//                dialog.dismiss();
                Log.e(TAG, "Upload File name." + result.getId() + " " +
                        result.getName() + "  " + result.getPathDisplay() + "  " +
                        result.getParentSharedFolderId());

                upload_post_path = result.getPathDisplay();

                if (new Utility(EditProfileActivity.this).isConnectingToInternet()) {
//                    dialog.dismiss();
                    onCallWebserviceForSaveEditProfile(dialog);

                } else {
                    onShowToast("Internet connection is not available");
                }

//                String message = result.getName() + " size " + result.getSize() + " modified " +
//                        DateFormat.getDateTimeInstance().format(result.getClientModified());

                String message = "Your profile pic has been upload";

                onShowToast(message);

            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();

                Log.e(TAG, "Failed to upload file.", e);
//                e.printStackTrace();
                Toast.makeText(EditProfileActivity.this,
                        "An error has occurred",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(fileUri);
    }

    private void onCallWebserviceForSaveEditProfile(ProgressDialog dialog) {

        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);
        SavePostInput input = new SavePostInput(user_id, editText.getText().toString(), UploadFileTask.getSharedUrl());

        Log.e(TAG, "Title " + input.getPostTitle());

        JsonObject mJsonObject = new JsonObject();
        mJsonObject.addProperty("userId", input.getUserId());
        mJsonObject.addProperty("userName", input.getPostTitle());
        mJsonObject.addProperty("profilePictureUrl", input.getVideoUrl());
//                Log.e("LoginActivity","String json " +mJsonObject.toString());

        mApiInterface.saveEditProfile(mJsonObject).enqueue(new Callback<ResponseEditProfile>() {
            @Override
            public void onResponse(Call<ResponseEditProfile> call, Response<ResponseEditProfile> response) {
                dialog.cancel();
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals("200")) {

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("path", newImagePath);
                        returnIntent.putExtra("name", editText.getText().toString());
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    } else {

                        onShowToast("Please upload video again");
//                        Intent mIntent=new Intent(UploadActivity.this, BottomNavigationActivity.class);
//                        startActivity(mIntent);
//                        finish();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseEditProfile> call, Throwable t) {
//                        Toast.makeText(MyApplication.getAppContext(),"Something is wrong",Toast.LENGTH_SHORT).show();

//                button.setEnabled(true);
                onShowToast("Something is wrong");


                dialog.cancel();
            }
        });


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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            if (!newImagePath.equals("")) {


                Intent returnIntent = new Intent();
                returnIntent.putExtra("path", newImagePath);
                returnIntent.putExtra("name", editText.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }else {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("path", newImagePath);
                returnIntent.putExtra("name", editText.getText().toString());
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();

            if (videoUri != null) {
                try {
//                    selectedVideoPath = getPath(videoUri);
                    selectedVideoPath = getPath(videoUri);
                    newImagePath= compressImage(selectedVideoPath);
                    Glide.with(this).load(newImagePath).fitCenter().into(profile_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "gallery path = " + selectedVideoPath);


            }


        }

    }


   // Funtion for compress image
    public String compressImage(String imageUri) {

        String filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = selectedVideoPath;
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    //
}
