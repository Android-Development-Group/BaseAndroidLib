package com.mobisoft.mbswebplugin.view.progress;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mobisoft.mbswebplugin.R;

/**
 * Author：Created by fan.xd on 2018/11/8.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */
public class ProgressDialogShepai extends ProgressDialog implements CustomProgress {
	private ImageView iv_show;
	private AnimationDrawable anim;

	public ProgressDialogShepai(Context context) {
		super(context);
	}

	public ProgressDialogShepai(Context context, int theme) {
		super(context, theme);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init(getContext());
	}

	private void init(Context context) {
		//设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
		setCancelable(true);
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.view_refresh_header_yaowan);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		getWindow().setAttributes(params);
		iv_show = (ImageView) findViewById(R.id.iv_normal_refresh_header_yaowan);
		iv_show.setImageResource(R.drawable.bga_loading_yaowan_refreshing);
		anim = (AnimationDrawable) iv_show.getDrawable();
	}
	@Override
	public void setMessage(String msg) {
		if (iv_show == null)
			iv_show = (ImageView) findViewById(R.id.iv_normal_refresh_header_yaowan);
	}

	@Override
	public void show() {
		super.show();
		if (anim != null && !anim.isRunning())
			anim.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (anim != null && anim.isRunning())
			anim.stop();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		if (anim != null && anim.isRunning())
			anim.stop();
	}


	@Override
	public void showHud() {
		show();
	}

	@Override
	public void dismissHud() {
		dismiss();
	}

	@Override
	public ProgressDialog getDialog() {
		return this;
	}
}

