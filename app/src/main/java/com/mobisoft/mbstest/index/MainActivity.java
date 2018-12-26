package com.mobisoft.mbstest.index;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mobisoft.mbstest.Base.BaseUrlConfig;
import com.mobisoft.mbstest.BuildConfig;
import com.mobisoft.mbstest.Injection;
import com.mobisoft.mbstest.R;
import com.mobisoft.mbstest.Service.NetworkStateService;
import com.mobisoft.mbstest.data.TasksDataSource;
import com.mobisoft.mbstest.data.TasksRepository;
import com.mobisoft.mbstest.splashScreen.SplashActivity;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebActivity;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebFragment;
import com.mobisoft.mbswebplugin.MvpMbsWeb.WebPluginPresenter;
import com.mobisoft.mbswebplugin.base.AppConfing;
import com.mobisoft.mbswebplugin.utils.ActivityCollector;
import com.mobisoft.mbswebplugin.utils.ActivityUtils;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Author：Created by fan.xd on 2017/8/7.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class MainActivity extends AppCompatActivity {

    private boolean isBackPressedClick;
    private WebPluginPresenter presenter;
    private MbsWebFragment fragment;
    private TasksRepository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_mbs_web_01);
        fragment = (MbsWebFragment) getSupportFragmentManager().findFragmentById(R.id.content_Frame_1);
        Bundle bundle2 = getIntent().getExtras();
        if (bundle2 == null) {
            bundle2 = new Bundle();
        }
        if (TextUtils.isEmpty(bundle2.getString("url"))) {
            bundle2.putString("url", BaseUrlConfig.URL_ME);
        }

        bundle2.putBoolean(AppConfing.IS_HIDENAVIGATION, true);

        if (fragment == null) {

            fragment = MbsWebFragment.newInstance(bundle2);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.content_Frame_1);
            fragment.setArguments(bundle2);

        }
        presenter = new WebPluginPresenter(fragment, MainActivity.this, MbsWebActivity.class, bundle2);
        repository = Injection.provideTasksRepository(this);
        checkPermission();
        startListenerNetWorkState();
    }

    /**
     * 启动监听网络状态
     */
    private void startListenerNetWorkState() {
        Intent intent = new Intent(this, NetworkStateService.class);
        intent.setAction(BuildConfig.APPLICATION_ID + ".NetworkStateService");
        startService(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();

            String url = bundle.getString("url");
            boolean isRefresh = bundle.getBoolean("isRefresh");

            if (!TextUtils.isEmpty(url)) {
                Log.i("oye", "onNewIntent: " + url);
                fragment.onHrefLocation(true);
                fragment.setMainUrl(url);
            }
        }
        super.onNewIntent(intent);
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            boolean hasPerm2 = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm && !hasPerm2) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 101);
            } else if (!hasPerm)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            else if (!hasPerm2)
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 101);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isBackPressedClick) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                isBackPressedClick = true;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isBackPressedClick = false;

                    }//延时两秒，如果超出则擦错第一次按键记录
                }, 2000);
            } else {//退出程序
                this.finish();
                System.exit(0);
            }
            return true;
        } else
            return presenter.onKeyDown(keyCode, event);
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_table, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                repository.removeCurrentAccount(new TasksDataSource.LoadCallback() {
                    @Override
                    public void onTasksLoaded(String data) {
                        ToastUtil.showShortToast(MainActivity.this, data);
                        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        MainActivity.this.startActivity(intent);
                        MainActivity.this.finish();

                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("main"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //手动统计页面("SplashScreen"为页面名称，可自定义)

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("main"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onPause(this); //手动统计页面("SplashScreen"为页面名称，可自定义)

    }
}
