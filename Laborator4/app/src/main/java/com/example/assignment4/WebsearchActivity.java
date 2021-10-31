package com.example.assignment4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.JobIntentService;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WebsearchActivity extends AppCompatActivity {


    public static final String EXTRA_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_websearch);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new MyCustomWebViewClient());
        myWebView.loadUrl("https://www.google.com/search?q=cat&sxsrf=AOaemvLS5ge4tDBMJRrBSLTBmhyQ4BDV_A:1635178324020&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjqtpb5-eXzAhXOgf0HHeRpBnEQ_AUoAXoECAIQAw&biw=1140&bih=959&dpr=1");

        ImageIntentService imageIntentService = new ImageIntentService() {
            @Override
            protected void onHandleIntent(@Nullable Intent intent) {
            ImageActivity imageActivity = new ImageActivity();

            }
        };
    }



    public void loadImage(View view) {

    }
}