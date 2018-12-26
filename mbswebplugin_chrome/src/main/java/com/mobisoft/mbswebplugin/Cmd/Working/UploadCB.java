package com.mobisoft.mbswebplugin.Cmd.Working;

import android.app.Activity;

import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;

/**
 * Author：Created by fan.xd on 2017/6/15.
 * Email：fang.xd@mobisoft.com.cn
 * Description：上传图片
 */

public interface UploadCB {

	void create(Activity activity, MbsWebPluginContract.View view, String callBack, int themeId);

	void onUploadComplete(String json);

	void onUploadStart(int total);

	void onUploadProgress(int current, int total);

	void onUploadProgress(final long current, final long total);


	void onUploadError(String error);


	void onUploadError(Integer error);

	void onUploadFinish(String message);

	void onUploadFinish(Integer message);

	void onUpLoadCallBack(Object imageInfo);

}
