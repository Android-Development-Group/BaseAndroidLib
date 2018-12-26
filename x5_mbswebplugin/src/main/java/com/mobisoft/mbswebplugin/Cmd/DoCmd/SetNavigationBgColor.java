package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.text.TextUtils;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description：设置toolbar的颜色
 */

public class SetNavigationBgColor extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		try {
			JSONObject jsonObject = new JSONObject(params);
			/** js返回的颜色*/
			String color = jsonObject.optString("color");
			String titleColor = jsonObject.optString("titleColor");
			String status = jsonObject.optString("status");// Hied 隐藏、Transparent 透明、Show 默认显示
			if (TextUtils.isEmpty(titleColor) && TextUtils.equals(color, "#FFFFFF")) {
				view.setTitleColor("#000000");
				view.setTitleBg(color);
			} else {
				view.setTitleBg(color);
				view.setTitleColor(titleColor);
			}


//            ((WebAppActivity)context).ll_head.setBackgroundColor(Color.parseColor(TextUtils.isEmpty(color) ? "#0089F6" : color));
//            ((WebAppActivity)context).mTv_head_title.setTextColor(Color.parseColor(TextUtils.isEmpty(color) ? "#FFFFFF" : "#000000"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
