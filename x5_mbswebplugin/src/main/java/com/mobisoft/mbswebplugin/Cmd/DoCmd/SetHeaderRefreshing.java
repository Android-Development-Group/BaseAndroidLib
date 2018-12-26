package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;

/**
 * Author：Created by fan.xd on 2018/7/13.
 * Email：fang.xd@mobisoft.com.cn
 * Description：设置刷新
 */
public class SetHeaderRefreshing extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

		view.forbiddenRefresh(false);
		return null;
	}
}
