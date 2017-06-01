package com.cloudhome.webview_intercept;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PostInterceptJavascriptInterface {
    public static final String TAG = "PostInterceptJavascriptInterface";

    private static String mInterceptHeader = null;
    private InterceptingWebViewClient mWebViewClient = null;

    public PostInterceptJavascriptInterface(InterceptingWebViewClient webViewClient) {
        mWebViewClient = webViewClient;
    }

    public static String enableIntercept(Context context, byte[] data) throws IOException {
        if (mInterceptHeader == null) {
           mInterceptHeader = new String(IOUtils.InputStreamTOByte(context.getAssets().open("www/interceptheader.html")), "utf-8");
       }


        Log.d("4444", data.toString());
        org.jsoup.nodes.Document doc = Jsoup.parse(new String(data, "utf-8"));
        doc.outputSettings().prettyPrint(true);
        // Prefix every script to capture submits
        // Make sure our interception is the first element in the
        // header
        org.jsoup.select.Elements el = doc.getElementsByTag("head");

        if (el.size() > 0) {
            el.get(0).prepend(mInterceptHeader);
        }


        String pageContents = doc.toString();



        Log.d("777777------------", pageContents);

        return pageContents;
    }

    /**
     * 当POST请求时，修改url的函数位置
     */
    public static String injectIsParams(String url) throws UnsupportedEncodingException {


        //此处省略拼接函数

        return url;

    }

    @JavascriptInterface
    public void customAjax(final String method, final String body) throws UnsupportedEncodingException {
        Log.i(TAG, "Submit data: " + method + " " + body);

        //  Log.i("77777",url);
        // injectIsParams(body);

        mWebViewClient.nextMessageIsAjaxRequest(new AjaxRequestContents(method, injectIsParams(body + "")));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @JavascriptInterface
    public void customSubmit(String json, String method, String enctype) {
        Log.i(TAG, "Submit data: " + json + "\t" + method + "\t" + enctype);
        mWebViewClient.nextMessageIsFormRequest(
                new FormRequestContents(method, json, enctype));
    }

    public class FormRequestContents {
        public String method = null;
        public String json = null;
        public String enctype = null;

        public FormRequestContents(String method, String json, String enctype) {
            this.method = method;
            this.json = json;
            this.enctype = enctype;
        }
    }

    public class AjaxRequestContents {
        public String method = null;
        public String body = null;

        public AjaxRequestContents(String method, String body) {
            this.method = method;
            this.body = body;
        }
    }


}
