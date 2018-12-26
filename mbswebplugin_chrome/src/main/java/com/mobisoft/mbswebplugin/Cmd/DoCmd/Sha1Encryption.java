package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.text.TextUtils;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Entity.CallBackResult;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author：Created by fan.xd on 2018/11/20.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */
public class Sha1Encryption extends DoCmdMethod {
	//账号登录
	private String va1 = "b0ctXrxf0PugY0TAtMgVAz0hY88Tv1e1";
	//单点登录
	private String va2 = "4A7D1ED414474E4033AC29CC";

	@Override
	public String doMethod(HybridWebView webView, final Context context, com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract.View view, com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		try {
			JSONObject jsonObject = new JSONObject(params);
			String name = jsonObject.optString("name");
			String type = jsonObject.optString("type");

			if (TextUtils.equals("login", type)) {
				loadCallBack(view, callBack,name,va1);
			} else if (TextUtils.equals("singlelogin", type)) {
				loadCallBack(view, callBack,name,va2);
			}else {
				ToastUtil.showShortToast(context,"登录类型错误！"+type);
			}




		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 回调给H5
	 * @param view
	 * @param callBack
	 * @param name
	 * @param key
	 */
	private void loadCallBack(MbsWebPluginContract.View view, String callBack, String name, String key) {
		String shaEncrypt =shaEncrypt(appendName(name, key));
		CallBackResult<String> backResult = new CallBackResult<>();
		backResult.setData(shaEncrypt);
		view.loadCallback(callBack, backResult);
	}

	/**
	 * 获取加密
	 ** @param name
	 * @param key
	 */
	private String appendName(String name, String key) {
		StringBuilder sb = new StringBuilder();
		String str = name.replace("KEY",key);
		String[] names = str.split("&");
		for (int i = 0; i < names.length; i++) {
			sb.append(names[i]);
		}
		return sb.toString();
	}


	/**
	 * SHA加密
	 *
	 * @param strSrc 明文
	 * @return 加密之后的密文
	 */
	private String shaEncrypt(String strSrc) {
		MessageDigest md = null;
		String strDes = null;
		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance("SHA-1");// 将此换成SHA-1、SHA-512、SHA-384等参数
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}

	/**
	 * byte数组转换为16进制字符串
	 *
	 * @param bts 数据源
	 * @return 16进制字符串
	 */
	private String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

}
