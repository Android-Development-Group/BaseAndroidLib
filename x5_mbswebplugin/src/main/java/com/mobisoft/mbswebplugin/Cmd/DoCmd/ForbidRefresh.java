package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;

import org.json.JSONException;

/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 禁止页面刷新
 * isForbidr : true 禁止刷新，false： 开启刷新啊
 */

public class ForbidRefresh extends DoCmdMethod {
	private boolean isForbidr = true;

	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
//        ((WebAppActivity)context).forbidRefresh();
		try {
			org.json.JSONObject object = new org.json.JSONObject(params);
			isForbidr = object.optBoolean("isForbidr", true);

		} catch (JSONException e) {
			e.printStackTrace();
			isForbidr = true;
		}

		view.forbiddenRefresh(isForbidr);
//        webView.excuteJSFunction(callBack,"item",params);
		return null;
	}
}
