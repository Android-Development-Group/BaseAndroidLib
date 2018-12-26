package com.mobisoft.mbswebplugin.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mobisoft.mbswebplugin.dao.db.WebViewDao;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class BaseWebActivity extends AppCompatActivity {

	public boolean isNeedClose = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (ProxyConfig.getConfig().isDark()) {
			// 系统 6.0 以上 状态栏白底黑字的实现方法
			this.getWindow()
					.getDecorView()
					.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			MIUISetStatusBarLightMode(this.getWindow(), true);
			FlymeSetStatusBarLightMode(this.getWindow(), true);
		}


	}

	/**
	 * 设置value 到数据库
	 *
	 * @param account
	 * @param key
	 * @param value
	 */
	protected void setKeyToDB(String account, String key, String value) {
		WebViewDao mWebViewDao = new WebViewDao(getApplicationContext());

		mWebViewDao.saveWebviewJson(account, key, value);
	}

	/**
	 * 根据key 从数据库得到value
	 *
	 * @param account 工号
	 * @param key     关键字
	 * @return 根据acoutn 和 key查询据库的数据
	 */
	protected String getValueFromDB(String account, String key) {
		WebViewDao mWebViewDao = new WebViewDao(getApplicationContext());
		return mWebViewDao.getWebviewValuejson(account, key);
	}

	protected void deleteValueFromDB(String account, String key) {
		WebViewDao mWebViewDao = new WebViewDao(getApplicationContext());

		mWebViewDao.deleteWebviewList(account, key);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
//        MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
//        MobclickAgent.onResume(this);

	}

	public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
		boolean result = false;
		if (window != null) {
			Class clazz = window.getClass();
			try {
				int darkModeFlag = 0;
				Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
				Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
				darkModeFlag = field.getInt(layoutParams);
				Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
				if (dark) {
					extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
				} else {
					extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
				}
				result = true;
			} catch (Exception e) {

			}
		}
		return result;
	}

	public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
		boolean result = false;
		if (window != null) {
			try {
				WindowManager.LayoutParams lp = window.getAttributes();
				Field darkFlag = WindowManager.LayoutParams.class
						.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
				Field meizuFlags = WindowManager.LayoutParams.class
						.getDeclaredField("meizuFlags");
				darkFlag.setAccessible(true);
				meizuFlags.setAccessible(true);
				int bit = darkFlag.getInt(null);
				int value = meizuFlags.getInt(lp);
				if (dark) {
					value |= bit;
				} else {
					value &= ~bit;
				}
				meizuFlags.setInt(lp, value);
				window.setAttributes(lp);
				result = true;
			} catch (Exception e) {

			}
		}
		return result;
	}
}
