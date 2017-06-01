package com.cloudhome.webview_intercept;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mAddressView = null;
    private WebView mWebView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddressView = (TextView) findViewById(R.id.addressView);
        mWebView = (WebView) findViewById(R.id.webView);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        settings.setDefaultTextEncodingName("utf-8");

        mWebView.setWebViewClient(new InterceptingWebViewClient(this, mWebView));

        mWebView.setWebChromeClient(new WebChromeClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


    }



    public void buttonClicked(View v) {
        load();
        mWebView.requestFocus();
    }

    private void load() {

        String postData = "";



//        byte[] postbyte = postData.getBytes();
//        mWebView.postUrl("http://192.168.1.52/web/post.html",postbyte);

        mWebView.loadUrl(HtmlUrls.Test2.geturl());
    }

    public enum HtmlUrls {
        baidu("http://www.baidu.com"),
        Jidai("http://m.haomoney.com/activity/reg/register.html?utm_source=wap_dukewangluo7"),
        Test2("https://www.xhqb.com/m/ffpp.html?appChannel=yingjqb01"),
        J2345("https://mdaikuan.2345.com/register3?channel=hj-qianmi_cpk_zyn"),
        XINGXING("http://www.m.starcredit.cn/h5regist/?s=dsp-hq14"),
        JSU("http://jsqb.kdqugou.com/newh5/web/page/app-reg?institution_id=124"),
        YongQianBao("http://www.yongqianbao.com/w/index?c=724"),
        XiaoHua("https://www.xhqb.com/m/ffpp.html?appChannel=yingjqb01");
        private String url;
        private HtmlUrls(String url) {
            this.url = url;
        }
        public String geturl(){
            return url;
        }
    }

}
