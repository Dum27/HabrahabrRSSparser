package com.example.work.rss;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Created by Work on 30.05.2015.
 */
public class WebVievActivity extends Activity
{
    private WebView mWebView;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        link = getIntent().getExtras().getString("link");
        mWebView = (WebView) findViewById(R.id.webview);
        //не будем тревожить стандартный браузер
        mWebView.setWebViewClient(new HelloWebViewClient());

        // включаем поддержку JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // указываем страницу загрузки
        mWebView.loadUrl(link);
    }

    private class HelloWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }
}
