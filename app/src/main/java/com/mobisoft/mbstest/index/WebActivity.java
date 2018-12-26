package com.mobisoft.mbstest.index;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mobisoft.mbstest.Base.BaseUrlConfig;
import com.mobisoft.mbstest.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;

public class WebActivity extends AppCompatActivity {

    private WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        web_view  = (WebView) findViewById(R.id.web_view);
        WebSettings settings = web_view.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        web_view.setWebChromeClient(new WebChromeClient());
        web_view.setWebViewClient(new WebViewClient());
        web_view.loadUrl(BaseUrlConfig.URL_INDEX);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WebActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //手动统计页面("SplashScreen"为页面名称，可自定义)

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WebActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onPause(this); //手动统计页面("SplashScreen"为页面名称，可自定义)

    }
}
