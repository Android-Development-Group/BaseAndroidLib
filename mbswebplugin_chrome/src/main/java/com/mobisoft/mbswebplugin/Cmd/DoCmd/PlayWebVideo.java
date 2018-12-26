package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.os.Bundle;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Entity.CallBackResult;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.helper.CoreConfig;
import com.mobisoft.mbswebplugin.helper.FunctionConfig;
import com.mobisoft.mbswebplugin.helper.ThemeConfig;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mobisoft.mbswebplugin.base.AppConfing.IS_HIDENAVIGATION;

/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description：启动播放视频页面
 */

public class PlayWebVideo extends DoCmdMethod {
	private MbsWebPluginContract.View view;
	private String mcallBack;

	@Override
	public String doMethod(HybridWebView webView, Context context, final MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, final String callBack) {
		this.view = view;
		mcallBack = callBack;
		try {
			JSONObject jsonObject = new JSONObject(params);
			/** 视频地址*/
			String videoUrl = jsonObject.optString("videoUrl");

			/**图片地址**/
			String placeholderurl = jsonObject.optString("placeholderurl");
			String course_no = jsonObject.optString("course_no");
			String courseItem_no = jsonObject.optString("courseItem_no");
			int videoPercentage = jsonObject.optInt("videoPercentage", 0);
			boolean liveFlag = jsonObject.optBoolean("liveFlag");
			String column = jsonObject.optString("column");

			String[] data = new String[7];
			data[0] = course_no;
			data[1] = courseItem_no;
			data[2] = placeholderurl;
			data[3] = videoUrl;
			data[4] = String.valueOf(liveFlag);
			data[5] = String.valueOf(videoPercentage);
			data[6] = column;

			CallBackResult sResult;
			sResult = view.setPalyVideoView(videoUrl, placeholderurl, "localSource",
					data);
			if (sResult.isResult()) {
				view.loadCallback(mcallBack, sResult);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void nextVoidPage(Context context, String params) {
		try {
			JSONObject jsonObject = new JSONObject(params);
			/** 存储key*/
			String videoUrl = jsonObject.optString("videoUrl");
			/*工号*/
			String bottomUrl = jsonObject.optString("bottomUrl");
			String placeholderurl = jsonObject.optString("placeholderurl");

			Bundle bundle = new Bundle();
			bundle.putString("placeholderurl", placeholderurl);
			bundle.putString("videoUrl", videoUrl);
			bundle.putBoolean(IS_HIDENAVIGATION, true);
			FunctionConfig.Builder builder = new FunctionConfig.Builder();
			builder.setIsLeftIconShow(false);
			FunctionConfig functionConfig = builder.build();
			CoreConfig coreConfig1 =
					new CoreConfig.Builder(
							context, ThemeConfig.DEFAULT, functionConfig)
							.setAccount("8100458")//
							.setNoAnimcation(false)
							.setURL(ProxyConfig.getConfig().getBaseUrl().replace("/aescula", "") + bottomUrl)
							.setHideNavigation(true)
							.build();
//			HybridWebApp.init(coreConfig1).startWebActivity(context, VideoWebActivity.class, bundle);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


}
