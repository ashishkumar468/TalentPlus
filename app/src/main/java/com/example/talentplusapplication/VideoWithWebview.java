package com.example.talentplusapplication;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoWithWebview extends AppCompatActivity {

    WebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_webview);

        initialization();

    }

    private void initialization() {
        webview =findViewById(R.id.webview_video);
//        webview.loadUrl("https://www.dropbox.com/home/Apps/Talent%20Plus?preview=VID_20190930_111258.mp4");

        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSaveFormData(true);

        webview.getSettings().setBuiltInZoomControls(true);
        this.webview.getSettings().setLoadWithOverviewMode(false);
        this.webview.getSettings().setUseWideViewPort(false);
//        webview.loadUrl("https://www.dropbox.com/home/Apps/Talent%20Plus?preview=VID_20190930_111258.mp4");
        webview.loadUrl("https://dl.dropboxusercontent.com/s/hfb4xe6cpce6303/videoplayback.mp4?dl=0");


    }
}
