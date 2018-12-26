package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Entity.CallBackResult;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.FileUtils;

/**
 * Author：Created by fan.xd on 2018/9/29.
 * Email：fang.xd@mobisoft.com.cn
 * Description：获取缓存大小
 */
public class getCache extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
//		view.loadCallback(callBack, 1000+"");

		try {
			String size = FileUtils.getFileSize(context.getCacheDir());
			CallBackResult<String> callBackResult = new CallBackResult<>();
			callBackResult.setData(size);
			view.loadCallback(callBack, callBackResult);

		} catch (Exception e) {
			e.printStackTrace();
			view.loadCallback(callBack, 0 + "");

		}

		return null;
	}
}
