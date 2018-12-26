package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.text.TextUtils;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2018/7/9.
 * Email：fang.xd@mobisoft.com.cn
 * Description：设置屏幕的横屏竖屏
 */
public class ChangeScreenMode extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		JSONObject mJSONObject = null;
		String orientation = null;
		try {
			mJSONObject = new JSONObject(params);
			orientation = mJSONObject.getString("Orientation");
		} catch (JSONException e) {
			e.printStackTrace();
			orientation = "LANDSCAPE";
		}

		if (context instanceof Activity) {
			if (TextUtils.equals("LANDSCAPE", orientation)) {
				view.hideTitle();
				((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else if (TextUtils.equals("PORTRAIT", orientation)) {
				((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		}
		return null;
	}
}
