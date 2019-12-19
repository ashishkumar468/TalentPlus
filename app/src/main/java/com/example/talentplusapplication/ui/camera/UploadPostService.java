package com.example.talentplusapplication.ui.camera;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class UploadPostService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UploadPostService(String name) {
        super(name);
    }
    public UploadPostService() {
        super("");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String path = intent.getStringExtra("path");
        String description  = intent.getStringExtra("description");
        intent.setAction(UploadActivity.FILTER_ACTION_KEY);
//        String echoMessage = "IntentService after a pause of 3 seconds echoes " + message;
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent.putExtra("path", path).
                putExtra("description",description));


    }
}
