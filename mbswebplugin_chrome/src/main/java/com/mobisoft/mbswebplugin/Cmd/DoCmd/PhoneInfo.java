package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.UUIDTools;
import com.mobisoft.mbswebplugin.utils.UrlUtil;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author：Created by fan.xd on 2018/6/19.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 获取APP、手机的信息
 * 1.21.手机信息(H5–->App)
 * 参数	名称	类型	备注
 * 命令	cmd	字符串	PhoneInfo
 * 返回参数
 * 版本号	version
 * 极光ID	jpushid
 * 设备id	uuid
 * bundleID	bundleid
 * 操作系统	osType
 * 操作系统型号	osModel
 * 应用名称	AppName
 */
public class PhoneInfo extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

		JSONObject object = new JSONObject();

		Class<?> threadClazz = null;
		Method method = null;
		String registrationid = null;
		try {
			threadClazz = Class.forName("cn.jpush.android.api.JPushInterface");
			method = threadClazz.getMethod("getRegistrationID", Context.class);
			registrationid = (String) method.invoke(null, context);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			registrationid = e.getMessage();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			registrationid = e.getMessage();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			registrationid = e.getMessage();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Throwable t = e.getTargetException();// 获取目标异常
			t.printStackTrace();
			registrationid = t.getMessage();
		}
		/**
		 * version  APP 版本号
		 * uuid     APP 唯一标识
		 * bundleid  包名
		 * osType    系统类型
		 * osModel   系统的版本
		 * AppName   APP的name
		 * jpushid   极光推送的注册ID
		 */
		try {
			object.put("version", getVersionName(context));
			object.put("uuid", UUIDTools.getInstance().getUuid(context));
			object.put("bundleid", context.getPackageName());
			object.put("osType", "android");
			object.put("osModel", getSdkVersion());
			object.put("AppName", getApplicationName(context));
			object.put("jpushid", registrationid);
		} catch (Exception e) {
			e.printStackTrace();
		}


		String json = UrlUtil.getFormatJs(callBack, object.toString());
		webView.loadUrl(json);
		return null;
	}

	/**
	 * 获取版本
	 *
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private String getVersionName(Context context) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

		String version = packInfo.versionName;
		return version;
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

	/**
	 * 手机系统版本
	 */
	public static String getSdkVersion() {

		return android.os.Build.VERSION.RELEASE;
	}
}
