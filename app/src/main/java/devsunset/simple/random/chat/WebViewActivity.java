/*
 * @(#)WebViewActivity.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * <PRE>
 * SimpleRandomChat WebViewActivity
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */


public class WebViewActivity extends Activity{

    private WebView mWebView;
    private WebSettings mWebSettings;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);


        mWebView = (WebView)findViewById(R.id.webview_area);
        mWebView.setWebViewClient(new WebViewClient());
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        Intent intent = getIntent();
        mWebView.loadUrl(intent.getStringExtra("URL_ADDRESS"));
    }
}
