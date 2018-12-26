package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.text.TextUtils;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/8/30.
 * Email：fang.xd@mobisoft.com.cn
 * Description：设置是否需要关闭当前页面
 * 默认 true & 0 :允许关闭
 * fasle  & 1  ：点击返回不能关闭页面
 */

public class GoBack extends DoCmdMethod {

    /**
     * 关闭页面
     */
    public static final String FINISH = "0";

    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        try {
            JSONObject jsonObject = new JSONObject(params);
            /**
             * 关闭页面
             *  类型 0
             *
             * */
            String isNeedClose = jsonObject.optString("closeType", FINISH);
            view.setNeedClose(TextUtils.equals(FINISH,isNeedClose) ?true:false);
//            webView.loadUrl(UrlUtil.getFormatJs(callBack, valueFromDB));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
