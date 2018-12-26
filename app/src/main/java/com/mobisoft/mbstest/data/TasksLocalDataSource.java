package com.mobisoft.mbstest.data;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mobisoft.mbswebplugin.dao.db.WebViewDao;

/**
 * Author：Created by fan.xd on 2017/5/9.
 * Email：fang.xd@mobisoft.com.cn
 * Description：数据加载，
 */

public class TasksLocalDataSource implements TasksDataSource {
    private static TasksLocalDataSource INSTANCE;
    private WebViewDao webViewDao;
    private Context applicationContext;

    public TasksLocalDataSource(Context context) {
        this.applicationContext = context.getApplicationContext();
        this.webViewDao = new WebViewDao(applicationContext);
    }

    public static TasksLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TasksLocalDataSource(context);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void isFirstInstall(@NonNull LoadCallback callback) {
        PackageManager packageManager = applicationContext.getPackageManager();
        String currentVersion = null;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(applicationContext.getPackageName(), 0);
            currentVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String str = webViewDao.getWebviewValuejson(currentVersion);
        if (TextUtils.isEmpty(str)) {
            callback.onDataNotAvailable();
            saveFirstInstall(currentVersion);

        } else {
            callback.onTasksLoaded(str);
//            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveFirstInstall(@NonNull String currentVersion) {
        webViewDao.saveWebviewJson("Version", currentVersion, "已经安装");
    }

    @Override
    public void getSplashImage(@NonNull LoadCallback callback) {
        //ToDo  网络接口
//        callback.onTasksLoaded("http://bpic.588ku.com/back_pic/03/87/27/6057d15507685a7.jpg");
        callback.onTasksLoaded("http://bpic.588ku.com/back_pic/03/87/27/6057d15507685a9.jpg");
    }

    @Override
    public void getGuideImages(@NonNull LoadCallback callback) {
        //ToDo  网络接口 获取引导页
    }

    @Override
    public void getCurrentAccount(@NonNull LoadCallback callback) {
        String cur_account = webViewDao.getWebviewValuejson("cur_account");
        if (TextUtils.isEmpty(cur_account)) {
            callback.onDataNotAvailable();
        } else {
            callback.onTasksLoaded(cur_account);
        }
    }

    @Override
    public void login(@NonNull LoadCallback callback, String info) {
        webViewDao.saveWebviewJson("cur_account","cur_account",info);
    }

    @Override
    public void removeCurrentAccount(@NonNull LoadCallback callback) {
        int n = webViewDao.deleteWebviewList(null, "cur_account");
//        int accountVo = webViewDao.deleteWebviewList(null, "accountVo");

        if (n > 0) {
            callback.onTasksLoaded("退出登录成功！");
        } else {
            callback.onDataNotAvailable();

        }
    }

    @Override
    public void changerLanguage(@NonNull LoadCallback callback) {

    }
}
