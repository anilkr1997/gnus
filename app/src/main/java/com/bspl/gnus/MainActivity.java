package com.bspl.gnus;

import androidx.annotation.RequiresApi;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.net.URISyntaxException;
import java.util.Objects;


public class MainActivity extends Activity {

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private WebView webView;
    String url;

    CustomProgress customProgress;
    ProgressBar progress_bar;
    int FILECHOOSER_RESULTCODE=1;
    Uri mCapturedImageURI;
    ValueCallback<Uri>  mUploadMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature( Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_main);
        progress_bar=findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        if (customProgress == null) {

            customProgress = CustomProgress.getInstance();



            progress_bar.setVisibility(View.VISIBLE);
            customProgress.showProgress(MainActivity.this, "please wait....", false);
        }


        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAppCacheEnabled(true);

        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);


        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);






        webView.getSettings().setJavaScriptEnabled(true);
        url="https://www.gondanewsupdates.in/";

        if(haveNetworkConnection()){
            startWebView(url);
        }
        else {
            customProgress.hideProgress();

            progress_bar.setVisibility(View.GONE);
            customProgress=null;
        }
    }



    private void startWebView(String url) {






        webView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                //location Setting
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);


            }



            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {

                //   mUploadMessage = filePathCallback;

                Intent chooserIntent = fileChooserParams.createIntent();

                MainActivity.this.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                return true;

            }
            String TAG="";




        });



        webView.setWebViewClient(new WebViewClient() {



            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                Uri uri = request.getUrl();




                return super.shouldOverrideUrlLoading(view, request);
            }




            public void onLoadResource (WebView view, String url) {

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (customProgress == null) {
                    // in standard case YourActivity.this

                    //  progressDialog.show();
                    customProgress = CustomProgress.getInstance();



                    progress_bar.setVisibility(View.VISIBLE);
                    customProgress.showProgress(MainActivity.this, "please wait....", false);
                }
            }

            public void onPageFinished(WebView view, String url) {
                try{

                    customProgress.hideProgress();
                    customProgress=null;

                    progress_bar.setVisibility(View.GONE);
                }catch(Exception exception){
                    exception.printStackTrace();
                }
                super.onPageFinished(view, url);
            }

        });

        webView.getSettings().setJavaScriptEnabled(true);


        webView.loadUrl(url);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FILECHOOSER_RESULTCODE)
        {

            if (null == this.mUploadMessage) {
                return;

            }

            Uri result=null;

            try{
                if (resultCode != RESULT_OK) {

                    result = null;

                } else {

                    // retrieve from the private variable if the intent is null
                    result = data == null ? mCapturedImageURI : data.getData();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), "activity :"+e,
                        Toast.LENGTH_LONG).show();
            }

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        }

    }


    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}


