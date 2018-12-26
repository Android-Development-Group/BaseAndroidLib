package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.content.Intent;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebFragment;
import com.mobisoft.mbswebplugin.MvpMbsWeb.TbsReaderActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2018/8/22.
 * Email：fang.xd@mobisoft.com.cn
 * Description：支持打开 pdf、doc、xls、text、ppt、xlsx 等文件
 */
public class OpenFile extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		try {
			Intent i = new Intent(context, TbsReaderActivity.class);
			JSONObject json = new JSONObject(params);
			String url = json.optString("fileUrl");
			String type = json.optString("type");
			boolean isZoom = json.optBoolean("isZoom",true);
			i.putExtra(MbsWebFragment.URL, url);
			i.putExtra(TbsReaderActivity.ISZOOM, isZoom);
			i.putExtra(TbsReaderActivity.TYPE, type);
			context.startActivity(i);
//			presenter.nextPage("http://docs.google.com/gview?url="+url, CMD.action_nextPage);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
