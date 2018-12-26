package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Author：Created by fan.xd on 2017/6/19.
 * Email：fang.xd@mobisoft.com.cn
 * Description：国际化语言切换
 */

public class ChangerLanguage extends DoCmdMethod {
	private String languange;

	@Override
	public String doMethod(HybridWebView hybridWebView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
//		repository = Injection.provideTasksRepository(context);
		try {
			JSONObject jsonObject = new JSONObject(params);
			languange = jsonObject.optString("lang");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		Configuration config = resources.getConfiguration();
		// 应用用户选择语言
		config.locale = Locale.ENGLISH;
		resources.updateConfiguration(config, dm);
		ProxyConfig.getConfig().setBaseUrl(ProxyConfig.getConfig().getBaseUrl() + "/en");
		reStartApp(context);
		return null;
	}

	/***
	 * 重启APP
	 * @param context
	 */
	private void reStartApp(Context context) {
//		repository.saveLanguage(languange);
//
//		context.startActivity(new Intent(context, SplashActivity.class));
//		ActivityCollector.finishAll();
//		((Activity) context).finish();
	}
}
