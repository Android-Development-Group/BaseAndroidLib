package com.mobisoft.mbswebplugin.base;

import android.app.Application;



public class BaseApp extends Application {
	public static BaseApp mInstance;




	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
//		ActivityManager.get().registerSelf(getApplicationContext());
//		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
//
//			@Override
//			public void onViewInitFinished(boolean arg0) {
//				// TODO Auto-generated method stub
//				//x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//				Log.i("app", " onViewInitFinished is " + arg0);
//			}
//
//			@Override
//			public void onCoreInitFinished() {
//				// TODO Auto-generated method stub
//			}
//		};
//		//x5内核初始化接口
//		QbSdk.initX5Environment(getApplicationContext(),  cb);
//
//

	}
	public static BaseApp getApplication() {
		return mInstance;
	}
}
