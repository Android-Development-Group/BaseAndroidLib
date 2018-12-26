package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Entity.CallBackResult;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;

/**
 * Author：Created by fan.xd on 2018/7/5.
 * Email：fang.xd@mobisoft.com.cn
 * Description：开始播放视频
 */
public class StartBGVideo extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

		view.startBGVideo();
		CallBackResult<String> jsResult =new CallBackResult<>();
		view.loadCallback(callBack, JSON.toJSONString(jsResult));
		return null;
	}
}
