package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView= findViewById(R.id.webview);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                webView();
            }
        });

        webView();
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

  checkConnection();
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    public void checkConnection(){
        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork= manager.getActiveNetworkInfo();
        if (null!=activeNetwork){
            if (activeNetwork.getType()== ConnectivityManager.TYPE_WIFI){
                Toast.makeText(this, "Wifi is connected", Toast.LENGTH_SHORT).show();
            }else  if (activeNetwork.getType()== ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(this, "Mobile Data is On", Toast.LENGTH_SHORT).show();
            }

        }else {
            startActivity(new Intent(getApplicationContext(),ErrorScreen.class));
            finish();
            Toast.makeText(this, "Please check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void webView(){
        String url="http://lrbdigitalization.com/pizza";
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        //  webView.setWebViewClient(new CustomWebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        swipeContainer.setRefreshing(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        webView.setWebViewClient(new WebViewClient()
        {
            public  void  onPageFinished(WebView view, String url){
                swipeContainer.setRefreshing(false);
            }
        });
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }
}