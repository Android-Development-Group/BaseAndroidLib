package com.mobisoft.mbstest.Cmd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mobisoft.mbstest.index.MainActivity;
import com.mobisoft.mbswebplugin.Cmd.HomePage;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebApp;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Base.Preconditions;
import com.mobisoft.mbswebplugin.helper.CoreConfig;
import com.mobisoft.mbswebplugin.helper.FunctionConfig;
import com.mobisoft.mbswebplugin.helper.ThemeConfig;

/**
 * Author：Created by fan.xd on 2017/3/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class GoHomePage implements HomePage {

    @Override
    public void goHomePage(Context context, Bundle bundle, String url, String s1) {

        CoreConfig coreConfig = (new CoreConfig.Builder(context, ThemeConfig.DEFAULT,
                FunctionConfig.DEFAULT_ACTIVITY))
                .setURL(Preconditions.checkNotNull(url))
                .setNoAnimcation(false)
                .setHideNavigation(true)
                .build();
        Bundle bundle1 = new Bundle();
        bundle1.putBoolean("isRefresh", true);
        bundle1.putString("url", url);
        Activity activity = (Activity) context;
        if (activity instanceof MainActivity) {
            HybridWebApp.init(coreConfig).startHomeActivity(context, MainActivity.class, bundle1, Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else {
            HybridWebApp.init(coreConfig).startHomeActivity(context, MainActivity.class, bundle1);

            activity.finish();
            Log.i("oye","activity no MainActivity");
        }
    }
}
