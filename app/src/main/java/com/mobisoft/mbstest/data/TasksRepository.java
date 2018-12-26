package com.mobisoft.mbstest.data;

import android.support.annotation.NonNull;

import com.mobisoft.mbswebplugin.MvpMbsWeb.Base.Preconditions;

/**
 * Author：Created by fan.xd on 2017/5/9.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class TasksRepository implements TasksDataSource {
    private static TasksRepository INSTANCE = null;
    private final TasksDataSource tasksDataSource;
    private final TasksDataSource localDataSource;


    private TasksRepository(TasksDataSource source, TasksLocalDataSource localDataSource) {
        tasksDataSource = source;
        this.localDataSource = localDataSource;
    }

    public static TasksRepository getInstance(TasksDataSource source, TasksLocalDataSource localDataSource) {

        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(source, localDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void isFirstInstall( @NonNull LoadCallback callback) {
        Preconditions.checkNotNull(callback);
        localDataSource.isFirstInstall(callback);
    }

    @Override
    public void saveFirstInstall(@NonNull String currentVersion) {
        localDataSource.saveFirstInstall(currentVersion);
    }

    @Override
    public void getSplashImage(@NonNull LoadCallback callback) {
        Preconditions.checkNotNull(callback);
        localDataSource.getSplashImage(callback);
    }

    @Override
    public void getGuideImages(@NonNull LoadCallback callback) {
        Preconditions.checkNotNull(callback);
        localDataSource.getGuideImages(callback);
    }

    @Override
    public void getCurrentAccount(@NonNull LoadCallback callback) {
        Preconditions.checkNotNull(callback);
        localDataSource.getCurrentAccount(callback);
    }

    @Override
    public void removeCurrentAccount(@NonNull LoadCallback callback) {
        Preconditions.checkNotNull(callback);
        localDataSource.removeCurrentAccount(callback);
    }

    @Override
    public void login(@NonNull LoadCallback callback, String info) {
        Preconditions.checkNotNull(callback);
        localDataSource.login(callback,info);
    }


    @Override
    public void changerLanguage(@NonNull LoadCallback callback) {
        Preconditions.checkNotNull(callback);
        localDataSource.changerLanguage(callback);
    }
}
