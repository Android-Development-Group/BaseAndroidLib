package com.mobisoft.mbstest.data;

import android.support.annotation.NonNull;

/**
 * Author：Created by fan.xd on 2017/5/9.
 * Email：fang.xd@mobisoft.com.cn
 * Description：获取得数据接口
 */

public interface TasksDataSource {


    interface LoadCallback {

        void onTasksLoaded(String data);

        void onDataNotAvailable();
    }

    void isFirstInstall(@NonNull LoadCallback callback);

    void saveFirstInstall(@NonNull String currentVersion);

    void getSplashImage(@NonNull LoadCallback callback);

    void getGuideImages(@NonNull LoadCallback callback);

    /**
     * 获取当前工号
     *
     * @param callback
     */
    void getCurrentAccount(@NonNull LoadCallback callback);

    /**
     * 移除当前 登录的工号
     * @param callback
     */
    void removeCurrentAccount(@NonNull LoadCallback callback);

    /**
     *登录
     * @param callback
     */
    void login(@NonNull LoadCallback callback,String info);


    /**
     * 改变当前语言
     * @param callback
     */
    void changerLanguage(@NonNull LoadCallback callback);

}
