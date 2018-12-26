package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2018/5/26.
 * Email：fang.xd@mobisoft.com.cn
 * Description：设置搜索标题
 */
public class setSearchBar extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        try {
            JSONObject jsonObject = new JSONObject(params);
            /** placeholder*/
            String hint = jsonObject.optString("placeholder");
            view.setSearchTitle(hint);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
