package com.mobisoft.mbstest.splashScreen;

import android.os.Handler;

import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.BasePresenter;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.BaseView;


/**
 * Author：Created by fan.xd on 2017/3/2.
 * Email：fang.xd@mobisoft.com.cn
 * Description：SplashContract 闪屏页视图和持有者接口
 */

public interface SplashContract {



    interface Presenter extends BasePresenter {
        void getImageUrl();

        void setHandler(Handler handler);

        void startMainActivity();

        void startDownManifest();

        void onDestroy();
    }

    interface View extends BaseView<Presenter> {


        /**
         * 获取启动页的图片地址
         *
         * @param path
         */
        void lodeImage(String path);

        /**
         * 启动页动画
         */
        void startAnimation();

        /**
         * 显示消息
         * @param message
         */
        void showMessage(String message);

        /**
         * 显示消息
         * @param message
         */
        void showProgressDialog(String message, String title, boolean isShow);

        /**
         * 启动activity
         * @param what
         */
        void startActivity(int what);

    }
}
