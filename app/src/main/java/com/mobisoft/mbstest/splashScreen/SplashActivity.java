package com.mobisoft.mbstest.splashScreen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.mobisoft.mbstest.Injection;
import com.mobisoft.mbstest.R;
import com.mobisoft.mbswebplugin.base.BaseWebActivity;
import com.mobisoft.mbswebplugin.utils.ActivityUtils;




/**
 * 闪屏页面
 */
public class SplashActivity extends BaseWebActivity {

    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        SplashFragment splashFragment = (SplashFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (splashFragment == null) {
            splashFragment = SplashFragment.getInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), splashFragment, R.id.contentFrame);

        }
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
//            String s  = extras.getString(JPushInterface.EXTRA_EXTRA);
//            Log.i("oye","JPushInterface  测试:"+s );
        }

        splashFragment.setArguments(extras);
        presenter = new SplashPresenter(Injection.provideTasksRepository(SplashActivity.this), splashFragment, null);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            presenter.startDownManifest();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE}, 101);
            else
                presenter.startDownManifest();
        } else {
            presenter.startDownManifest();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
