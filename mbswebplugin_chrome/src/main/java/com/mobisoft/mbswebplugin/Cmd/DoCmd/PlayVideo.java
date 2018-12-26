package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.Voide.EduMediaPlayer;
import com.mobisoft.mbswebplugin.dao.db.WebViewDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description：打开播放视频界面
 */

public class PlayVideo extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		try {
			Intent i = new Intent(context, EduMediaPlayer.class);
			JSONObject json = new JSONObject(params);
			String url = json.optString("courseSrc");
			String progress = json.optString("progress");
			// 登陆用户的水印
			String waterMarkparams = getValueFromDB(context, "accountVo");
			if (!TextUtils.isEmpty(waterMarkparams)) {
				JSONObject accountInfo = new JSONObject(waterMarkparams);
				String fullName = accountInfo.getString("fullName");
				String department = accountInfo.getString("department");
				String waterMark = fullName + department;
				if (!TextUtils.isEmpty(waterMark))
					i.putExtra("waterMark", waterMark);
				else
					i.putExtra("waterMark", getApplicationName(context));

			} else {
				i.putExtra("waterMark", getApplicationName(context));
			}


//            Boolean studyState = json.optBoolean("studyState");
//            int currentTime = json.optInt("currentTime");
//            i.putExtra("studyState", studyState);
//            i.putExtra("currentTime", currentTime);

			i.putExtra("url", url);
			i.putExtra("progress", progress);
			context.startActivity(i);

		} catch (JSONException e) {
			e.printStackTrace();
		}


		return null;
	}

	/**
	 * 根据key 从数据库得到value
	 * * @param key     关键字
	 *
	 * @return 根据acoutn 和 key查询据库的数据
	 */
	protected String getValueFromDB(Context context, String key) {
		WebViewDao mWebViewDao = new WebViewDao(context.getApplicationContext());
		String pram;
		try {
			pram = URLDecoder.decode(key, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			pram = key;
		}

		return mWebViewDao.getWebviewValuejson(pram);
	}


	/**
	 * 获取APP name
	 *
	 * @param context
	 * @return
	 */
	public String getApplicationName(Context context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName =
				(String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}
}
