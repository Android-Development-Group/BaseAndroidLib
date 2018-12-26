package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.util.Log;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Author：Created by fan.xd on 2018/8/13.
 * Email：fang.xd@mobisoft.com.cn
 * Description：MD5 加密
 */
public class MD5 extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		try {

			JSONObject res = new JSONObject(params);
			String account = res.optString("account");
			String rescmd = res.optString("cmd");
			String payload = res.optString("payload");
			String ts = res.optString("ts");
			String opt = res.optString("opt", "guotaichanxian");
			StringBuilder sb = new StringBuilder();
			sb.append(rescmd);
			sb.append(payload);
			sb.append(ts);
			sb.append(account);
			sb.append(opt);
			JSONObject object = new JSONObject();
			String s = getMD5(sb.toString().trim().getBytes("UTF-8"));
			object.put("value", s);
			String json = object.toString();
			Log.i("kitapps", json);
			view.loadUrl(UrlUtil.getFormatJs(callBack, json));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * 16进制的字符串数组
	 */
	private final static String[] hexDigitsStrings = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f"};

	/**
	 * 16进制的字符集
	 */
	private final static char[] hexDigitsChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'A', 'B', 'C', 'D', 'E', 'F'};

	/**
	 * MD5加密以byte数组表示的字符串
	 *
	 * @param source 源字节数组
	 * @return 加密后的字符串
	 */
	public static String getMD5(byte[] source) {
		String s = null;

		final int temp = 0xf;
		final int arraySize = 32;
		final int strLen = 16;
		final int offset = 4;
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte[] tmp = md.digest();
			char[] str = new char[arraySize];
			int k = 0;
			for (int i = 0; i < strLen; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigitsChar[byte0 >>> offset & temp];
				str[k++] = hexDigitsChar[byte0 & temp];
			}
			s = new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

}
