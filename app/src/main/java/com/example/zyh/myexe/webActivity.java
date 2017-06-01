package com.example.zyh.myexe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webActivity extends AppCompatActivity {

    protected WebView mwebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_web);
        initView();


        WebSettings webSetting = mwebview.getSettings();
        webSetting.setJavaScriptEnabled(true);
         mwebview.setWebViewClient(new WebViewClient());
        mwebview.loadUrl("http://91jinlin.com/");
    }



    private void initView() {
        mwebview = (WebView) findViewById(R.id.webview);
    }
}
