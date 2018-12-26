package com.mobisoft.mbstest.Base;


import com.mobisoft.mbstest.BuildConfig;

/**
 * Author：Created by fan.xd on 2017/5/9.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class BaseUrlConfig {
    /**
     * 影像上传地址
     */
    public static final String BASE_IMAGE_URL = BuildConfig.URL_IMAGE;

    /**
     * 基本URLhttp://test.mobisoft.com.cn/cathay/index.html
     */
//    public static final String BASE_URL =BuildConfig.BASE_URL;
//    public static final String BASE_URL ="http://ut-zyxt.cathay-ins.com.cn/cathay";
    public static final String BASE_URL ="http://test.mobisoft.com.cn/sales/hytest/";

    /**
     * 登陆页面
     */
    public static final String URL_LOGIN = BASE_URL + "/mbsWebApp/index.html";
    /**
     * 我的页面
     * file:///android_asset/index.html
     */
    public static final String URL_ME = BASE_URL + "/mbsWebApp/index.html";
    /**
     * 首页
     */
//    public static final String URL_INDEX = BASE_URL + "index.html";
//    public static final String URL_INDEX = "http://nike.mobisoft.com.cn/VideoSync/";
//    public static final String URL_INDEX = "https://mp.weixin.qq.com/s?__biz=MjM5Mjk4NTY3MA==&mid=2650705990&idx=1&sn=4d47d575f245b9be8a91da00efffdc3e&chksm=be97d76f89e05e79488f6847228ae2447764cb0363d82c786a54a0d35084a442cd2a240d0c0e&scene=38#wechat_redirect&action=nextPage";
    public static final String URL_INDEX = "http://test.mobisoft.com.cn/sales/hytest/native.html";
     /* manifest
     */
    public static final String URL_MANIFEST = "http://test.mobisoft.com.cn/cathay/cache.manifest";


}
