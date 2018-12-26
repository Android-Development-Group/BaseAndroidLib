package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.NetworkUtils;
import com.mobisoft.mbswebplugin.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/9/15.
 * Email：fang.xd@mobisoft.com.cn
 * Description：获取网络状态
 */

public  class getNetStatus extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        boolean isNetworkConnected= NetworkUtils.isNetworkConnected(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("netConnected",isNetworkConnected);
            view.loadUrl(UrlUtil.getFormatJs(callBack,jsonObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
