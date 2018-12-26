package com.mobisoft.mbstest.Cmd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mobisoft.mbstest.Base.BaseUrlConfig;
import com.mobisoft.mbstest.Injection;
import com.mobisoft.mbstest.data.TasksDataSource;
import com.mobisoft.mbstest.data.TasksRepository;
import com.mobisoft.mbstest.index.MainActivity;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebApp;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Base.Preconditions;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.helper.CoreConfig;
import com.mobisoft.mbswebplugin.helper.FunctionConfig;
import com.mobisoft.mbswebplugin.helper.ThemeConfig;

;


/**
 * Author：Created by fan.xd on 2017/6/13.
 * Email：fang.xd@mobisoft.com.cn
 * Description：退出登录操作
 */

public class LogOut extends DoCmdMethod {
    private TasksRepository repository;

    @Override
    public String doMethod(HybridWebView hybridWebView, final Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        repository = Injection.provideTasksRepository(context);
        repository.removeCurrentAccount(new TasksDataSource.LoadCallback() {
            @Override
            public void onTasksLoaded(String data) {
                CoreConfig coreConfig = (new CoreConfig.Builder(context, ThemeConfig.DEFAULT,
                        FunctionConfig.DEFAULT_ACTIVITY))
                        .setURL(Preconditions.checkNotNull(BaseUrlConfig.URL_INDEX))
                        .setNoAnimcation(false)
                        .setHideNavigation(true)
                        .build();
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("isRefresh", true);
                bundle1.putString("url", BaseUrlConfig.URL_INDEX);
                Activity activity = (Activity) context;
                if (activity instanceof MainActivity) {
                    HybridWebApp.init(coreConfig).startHomeActivity(context, MainActivity.class, bundle1, Intent.FLAG_ACTIVITY_SINGLE_TOP);
                } else {
                    HybridWebApp.init(coreConfig).startHomeActivity(context, MainActivity.class, bundle1);

                    activity.finish();
                    Log.i("oye","activity no MainActivity");

                }
            }

            @Override
            public void onDataNotAvailable() {
                Log.i("LogOut", "onDataNotAvailable + 退出失败");
            }
        });


        return null;
    }
}
