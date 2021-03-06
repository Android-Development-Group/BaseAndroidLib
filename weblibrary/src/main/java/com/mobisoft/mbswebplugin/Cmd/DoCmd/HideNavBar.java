package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;

/**
 * Author：Created by fan.xd on 2018/6/6.
 * Email：fang.xd@mobisoft.com.cn
 * Description：隐藏标题栏
 */
public class HideNavBar extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		view.hideNavBar();
		return null;
	}
}
