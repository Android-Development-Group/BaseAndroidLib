package com.mobisoft.mbswebplugin.view.progress;

import android.app.ProgressDialog;

/**
 * Author：Created by fan.xd on 2018/11/8.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */
public interface CustomProgress {
	void showHud();

	void dismissHud();

	ProgressDialog getDialog();

	void setMessage(String message);
}
