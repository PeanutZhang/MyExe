package com.cloudhome.webview_intercept;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.squareup.mimecraft.FormEncoding;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Set;

import okhttp3.OkHttpClient;

public class InterceptingWebViewClient extends WebViewClient {
    public static final String TAG = "InterceptingWebViewClient";

    private Context mContext = null;
    private WebView mWebView = null;
    private PostInterceptJavascriptInterface mJSSubmitIntercept = null;
    private OkHttpClient client = new OkHttpClient();


    public InterceptingWebViewClient(Context context, WebView webView) {
        mContext = context;
        mWebView = webView;
        mJSSubmitIntercept = new PostInterceptJavascriptInterface(this);
        mWebView.addJavascriptInterface(mJSSubmitIntercept, "interception");

    }


//    @Override
//    public void onPageStarted(WebView view, String url, Bitmap favicon){
//        if (url.startsWith("https://")) { //NON-NLS
//
//            mNextAjaxRequestContents = null;
//            mNextFormRequestContents = null;
//            view.stopLoading();
//            // DO SOMETHING
//        }
//    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        super.onReceivedSslError(webView, sslErrorHandler, sslError);

        sslErrorHandler.proceed();

    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        mNextAjaxRequestContents = null;
        mNextFormRequestContents = null;
        Log.d("77777post","---shouldOverrideUrlLoading---------");

        URL murl;
        URLConnection conexion;
        try {
            murl = new URL(url);
            conexion = murl.openConnection();
            conexion.setConnectTimeout(3000);
            conexion.connect();
            // get the size of the file which is in the header of the request
            int size = conexion.getContentLength();
            InputStream inputStream = conexion.getInputStream();
            String content = convertToString(inputStream);
            Log.d("77777post-content--","----------"+content);

        } catch (Exception e) {
            e.printStackTrace();
        }











       // view.loadUrl(url);
        return false;
    }

    public String convertToString(InputStream inputStream){
        StringBuffer string = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                string.append(line + "\n");
            }
        } catch (IOException e) {}
        return string.toString();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (request != null && request.getUrl() != null) {

            String scheme = request.getUrl().getScheme().trim();

            if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) {
                URL url;
                URLConnection connection;
                HttpURLConnection conn ;

                try {
                    if (request.getMethod().equals("POST")) {
                         url= new URL(request.getUrl().toString());
                         connection = url.openConnection();
                        conn = (HttpURLConnection) connection;
                        Log.d("7777776",request.getUrl().toString());
                        conn.setRequestMethod( "POST");
                        OutputStream os = conn.getOutputStream();
                        if (mNextAjaxRequestContents != null) {

                            Log.d("777777","44444");
                            writeBody(os);
                        } else {

                            Log.d("777777","5555");
                            writeForm(os);
                        }
                        os.close();



                        // Read input
                        String charset = conn.getContentEncoding() != null ? conn.getContentEncoding() : Charset.defaultCharset().displayName();
                        String mime = conn.getContentType();
                        //byte[] pageContents = IOUtils.readFully(connection.getInputStream());

//                        // Perform JS injection
//                        if (mime.equals("text/html")) {
//                            pageContents = PostInterceptJavascriptInterface
//                                    .enableIntercept(mContext, pageContents)
//                                    .getBytes(charset);
//                        }

                        // Convert the contents and return
                      //  InputStream isContents = new ByteArrayInputStream(pageContents);

                     //   return new WebResourceResponse(mime, charset,
                     //           isContents);
//

                    }else{
                         url = new URL(injectIsParams(request.getUrl().toString()));
                        connection = url.openConnection();
                        Log.d("7777779",request.getUrl().toString());
                    }





                    // Write body
                    if (request.getMethod().equals("GET")) {

                        String contentType = connection.getContentType();
                        // If got a contentType header
                        if(contentType != null) {

                            String mimeType = contentType;

                            // Parse mime type from contenttype string
                            if (contentType.contains(";")) {
                                mimeType = contentType.split(";")[0].trim();
                            }


                            return new WebResourceResponse(mimeType, connection.getContentEncoding(), connection.getInputStream());
                        }

                    }




                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public WebResourceResponse shouldInterceptRequest(final WebView view, final String urlstr) {
        try {
            // Our implementation just parses the response and visualizes it. It does not properly handle
            // redirects or HTTP errors at the moment. It only serves as a demo for intercepting POST requests
            // as a starting point for supporting multiple types of HTTP requests in a full fletched browser



            URL url = null;

            if (isPOST()) {
                url = new URL(urlstr);
                Log.d("77777post",urlstr);
            } else {
                Log.d("77777get", urlstr);
                url = new URL(injectIsParams(urlstr));

            }
            Log.d("77777get",url.toString());


            URLConnection rulConnection = url.openConnection();
            HttpURLConnection conn = (HttpURLConnection) rulConnection;
            Log.d("77777post-Method----","urlquestMethod-------->"+"--url-->"+urlstr+"----->\r\n"+((HttpURLConnection) rulConnection).getRequestMethod()+"----"+((HttpURLConnection) rulConnection).getResponseMessage()) ;

            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");

            conn.setRequestMethod(isPOST() ? "POST" : "GET");
            InputStream is = conn.getInputStream();



            // Write body
            if (isPOST()) {
                OutputStream os = conn.getOutputStream();
                if (mNextAjaxRequestContents != null) {
                    writeBody(os);
                } else {
                    writeForm(os);
                }
                os.close();
            }

//           else{
//                String contentType = rulConnection.getContentType();
//                // If got a contentType header
//                if(contentType != null) {
//
//                    String mimeType = contentType;
//
//                    // Parse mime type from contenttype string
//                    if (contentType.contains(";")) {
//                        mimeType = contentType.split(";")[0].trim();
//                    }
//
//
//                    return new WebResourceResponse(mimeType, rulConnection.getContentEncoding(), rulConnection.getInputStream());
//                }
//
//            }





            // Read input
            String charset = conn.getContentEncoding() != null ? conn.getContentEncoding() : Charset.defaultCharset().displayName();
            String mime = conn.getContentType();
            byte[] pageContents = IOUtils.InputStreamTOByte(conn.getInputStream());

            // Perform JS injection
            if (mime.equals("text/html")) {
                pageContents = PostInterceptJavascriptInterface
                        .enableIntercept(mContext, pageContents)
                        .getBytes(charset);
            }

            Log.d("888888",charset);
            // Convert the contents and return
            InputStream isContents = new ByteArrayInputStream(pageContents);

            mNextAjaxRequestContents=null;

            WebResourceResponse sresponse = new WebResourceResponse(mime,charset,isContents);
            Log.d("77777post","----------------->"+urlstr);
            Log.d("77777post","----sresponseAAA----"+url.toString());


            // Log.d("77777post---","-----sresponse------"+sresponse.);
//            if("https://jdappv3.haomoney.com/jd-app/cardniu/x/v1/getVerifyCode".equals(urlstr))
//            {
//                Log.d("77777post","------------------sresponse------------------------------");
//              //  Log.d("77777post---","-----sresponse------"+convertToString(sresponse.getData()));
//                // Log.d("77777post","-WebResourceResponse sresponse--"+sresponse.getReasonPhrase());
//            }

            return sresponse;



        } catch (FileNotFoundException e) {
            Log.w("Error 404","Error 404:" + e.getMessage());
            e.printStackTrace();

            return null;        // Let Android try handling things itself
        } catch (Exception e) {
            e.printStackTrace();

            return null;        // Let Android try handling things itself
        }
    }




    private boolean isPOST() {
        return (mNextAjaxRequestContents!=null&&mNextAjaxRequestContents.method.equals("POST"));
    }



    private void writeBody(OutputStream out) {
        try {
            Log.d("777773---->","--mNextFormRequestContents.json--"+mNextFormRequestContents.json);
            Log.d("777773", mNextAjaxRequestContents.body);
            out.write(mNextAjaxRequestContents.body.getBytes("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected void writeForm(OutputStream out) {
        try {


            JSONArray jsonPars = new JSONArray(mNextFormRequestContents.json);

            // We assume to be dealing with a very simple form here, so no file uploads or anything
            // are possible for reasons of clarity
            FormEncoding.Builder m = new FormEncoding.Builder();
            for (int i = 0; i < jsonPars.length(); i++) {
                JSONObject jsonPar = jsonPars.getJSONObject(i);

                m.add(jsonPar.getString("name"), jsonPar.getString("value"));
                // jsonPar.getString("type");
                // TODO TYPE?
            }
            m.build().writeBodyTo(out);



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getType(Uri uri) {
        String contentResolverUri = mContext.getContentResolver().getType(uri);
        if (contentResolverUri == null) {
            contentResolverUri = "*/*";
        }
        return contentResolverUri;
    }

    private PostInterceptJavascriptInterface.FormRequestContents mNextFormRequestContents = null;

    public void nextMessageIsFormRequest(PostInterceptJavascriptInterface.FormRequestContents formRequestContents) {
        mNextFormRequestContents = formRequestContents;
    }

    private PostInterceptJavascriptInterface.AjaxRequestContents mNextAjaxRequestContents = null;

    public void nextMessageIsAjaxRequest(PostInterceptJavascriptInterface.AjaxRequestContents ajaxRequestContents) {
        mNextAjaxRequestContents= ajaxRequestContents;
    }

    /**
     * 当GET请求时，修改url的函数位置
     */

    public static String injectIsParams(String url) throws UnsupportedEncodingException {

        //此处省略拼接函数
      return url;
    }


}
