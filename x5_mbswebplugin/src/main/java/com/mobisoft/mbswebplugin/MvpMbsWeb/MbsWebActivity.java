package com.mobisoft.mbswebplugin.MvpMbsWeb;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.AppConfing;
import com.mobisoft.mbswebplugin.base.BaseWebActivity;
import com.mobisoft.mbswebplugin.utils.ActivityCollector;
import com.mobisoft.mbswebplugin.utils.ActivityUtils;
import com.umeng.analytics.MobclickAgent;

/***
 *  webView native 混合式开发核心组件
 */
public class MbsWebActivity extends BaseWebActivity {

	private WebPluginPresenter mbsPersenter;
	private MbsWebFragment mbsWebFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mbs_web_01);
		ActivityCollector.addActivity(this);

		mbsWebFragment =
				(MbsWebFragment) getSupportFragmentManager().findFragmentById(R.id.content_Frame_1);

		if (mbsWebFragment == null) {
			mbsWebFragment = MbsWebFragment.newInstance(getIntent().getExtras());
			ActivityUtils.addFragmentToActivity(
					getSupportFragmentManager(), mbsWebFragment, R.id.content_Frame_1);
		}
		mbsPersenter = new WebPluginPresenter(mbsWebFragment, this, MbsWebActivity.class, getIntent().getExtras());
		mbsPersenter.onCreate();
		boolean isRestart = getIntent().getExtras().getBoolean(AppConfing.IS_RESTART, false);
//
//		if(isRestart){
//			ActivityCollector.finishOther();
//		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		mbsPersenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mbsPersenter.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return mbsPersenter.onKeyDown(keyCode, event);
	}

	@Override
	public void finish() {
		super.finish();
		mbsPersenter.finish();
	}

	@Override
	protected void onDestroy() {
		mbsPersenter = null;
		ActivityCollector.removeActivity(this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this); //统计时长

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this); //统计时长
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		//解决某些手机上锁屏之后会出现标题栏的问题。
		if (mbsWebFragment != null)
			mbsWebFragment.updatePlayerViewMode();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (newConfig.fontScale != 1)//非默认值
			getResources();
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		if (res.getConfiguration().fontScale != 1) {//非默认值
			Configuration newConfig = new Configuration();
			newConfig.setToDefaults();//设置默认
			res.updateConfiguration(newConfig, res.getDisplayMetrics());
		}
		return res;
	}


}
