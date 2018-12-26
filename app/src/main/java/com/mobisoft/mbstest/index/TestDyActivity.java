package com.mobisoft.mbstest.index;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mobisoft.mbstest.R;
import com.umeng.analytics.MobclickAgent;

public class TestDyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dy);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("testDy"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //手动统计页面("SplashScreen"为页面名称，可自定义)

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("testDy"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onPause(this); //手动统计页面("SplashScreen"为页面名称，可自定义)

    }
}
