package com.example.talentplusapplication;

import android.content.Context;

import com.example.talentplusapplication.Proxy.PostDtoListProxy;
import com.example.talentplusapplication.camera.BaseCameraActivity;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class FileCache {

    private static List<String> listPost;
    private File cacheDir;

    public FileCache(Context context){
        cacheDir=BaseCameraActivity.getAndroidMoviesFolderInternalStorage();
    }

    public File getFile(String url){
        // Split path into segments
        String segments[] = url.split("/");
        String s ="\\?dl";
        String segment[] = segments[segments.length - 1].split(s);
// Grab the last segment
        String filename = segment[0];
//        String filename = segments[segments.length - 1];
//        String filename_[] = filename.split("/?");

        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);


        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

    public static String readPreferences(Context mContext,String url,String defValue){
        Log.e("TAG","This is read ");
        String value=defValue;
        SharedPreferences mSharedPreferences=mContext.getSharedPreferences(Constants.Shared_Pref_Name,Context.MODE_PRIVATE);
        String json = mSharedPreferences.getString("DownloadUrl", "");
        Type type = new TypeToken<List<String>>() {}.getType();
        listPost = new Gson().fromJson(json, type);
        if (listPost!=null){
            for (String dUrl : listPost){

                if (dUrl.equals(url)){
                    return "true";
                }else {
                    value= "false";
                }
            }
        }


//        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(listPost);
//        prefsEditor.putString("MyObject", json);
//        prefsEditor.apply();
        return value;
    }
    public static String savePreferences(Context mContext,String url,String defValue){

        Log.e("TAG","This is save ");
        String value=defValue;
        SharedPreferences mSharedPreferences=mContext.getSharedPreferences(Constants.Shared_Pref_Name,Context.MODE_PRIVATE);
        String json = mSharedPreferences.getString("DownloadUrl", "");
        Type type = new TypeToken<List<String>>() {}.getType();
        listPost = new Gson().fromJson(json, type);
        if (listPost!=null){
            listPost.add(url);
        }else {
            listPost=new ArrayList<>();
            listPost.add(url);
        }


        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String jsonUpdated = gson.toJson(listPost);
        prefsEditor.putString("DownloadUrl", jsonUpdated);
        prefsEditor.apply();

        return value;
    }

}