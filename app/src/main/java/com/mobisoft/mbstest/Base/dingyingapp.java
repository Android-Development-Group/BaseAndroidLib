package com.mobisoft.mbstest.Base;

import android.util.Log;

import com.mobisoft.mbstest.BuildConfig;
import com.mobisoft.mbstest.Cmd.GoHomePage;
import com.mobisoft.mbstest.Cmd.LogOut;
import com.mobisoft.mbswebplugin.Cmd.CMD;
import com.mobisoft.mbswebplugin.Cmd.ProxyCmd;
import com.mobisoft.mbswebplugin.base.ActivityManager;
import com.mobisoft.mbswebplugin.base.BaseApp;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebSettings;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.File;

/**
 * Author：Created by fan.xd on 2017/8/7.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class dingyingapp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        ActivityManager.get().registerSelf(this);
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.e("print","加载内核是否成功:onCoreInitFinished");

            }

            @Override
            public void onViewInitFinished(boolean b) {
                Log.e("print","加载内核是否成功:"+b);
            }
        });
        UMConfigure.init(this, "5c04ca4bf1f5569d41000223", "mobisoft",UMConfigure.DEVICE_TYPE_PHONE,"");
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        UMConfigure.setLogEnabled(true);
        MobclickAgent.openActivityDurationTrack(false);
        ProxyConfig.getConfig()
                .setCachePath(getCacheDir() + File.separator + "Ambs")
                .setCacheUrl(BaseUrlConfig.URL_MANIFEST)
                .setPORT(8188)
                .setCacheMode(WebSettings.LOAD_DEFAULT)
                .setChangeHttps(false)
                .setOpenProxy(false)
                .setImageBaseUrl(BaseUrlConfig.BASE_IMAGE_URL)
                .setBaseUrl(BuildConfig.BASE_URL);
        ProxyCmd.getInstance()
                .setHomePage(new GoHomePage())
                .putCmd(CMD.cmd_logout, LogOut.class.getName());

    }
}
