package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.OrientationEventListener;

import com.alibaba.fastjson.annotation.JSONField;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Entity.CallBackResult;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2018/10/18.
 * Email：fang.xd@mobisoft.com.cn
 * Description：允许屏幕横竖操作
 */
public class AllowChangeScreen extends DoCmdMethod {

	private String TAG = "AllowChangeScreen";
	private Context mContext;
	private String loadCallback;
	private boolean screenPortrait = true;
	private MbsWebPluginContract.View mView;

	@Override
	public String doMethod(HybridWebView webView, Context context,
						   MbsWebPluginContract.View view,
						   MbsWebPluginContract.Presenter presenter,
						   String cmd, String params, String callBack) {
		mContext = context;
		mView = view;
		loadCallback = callBack;
		mView.AllowScreenMode(new MyOrientoinListener(mContext));
		return null;
	}

	class CallBackResultSc extends CallBackResult<String> {
		@JSONField(name="AllowChangeScreen")
		String AllowChangeScreen;

		public String getAllowChangeScreen() {
			return this.AllowChangeScreen;
		}

		public void setAllowChangeScreen(String a) {
			this.AllowChangeScreen = a;
		}
	}

	class MyOrientoinListener extends OrientationEventListener {

		public MyOrientoinListener(Context context) {
			super(context);
		}

		public MyOrientoinListener(Context context, int rate) {
			super(context, rate);
		}

		@Override
		public void onOrientationChanged(int orientation) {
			Log.i(TAG, "orention" + orientation);
			final CallBackResultSc resultSc = new CallBackResultSc();
			int screenOrientation = mContext.getResources().getConfiguration().orientation;
			if (((orientation >= 0) && (orientation < 45)) || (orientation > 315)) {//设置竖屏
				if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && orientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
					Log.i(TAG, "设置竖屏");
					if(screenPortrait){
						return;
					}
					screenPortrait=true;
					((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					resultSc.setAllowChangeScreen("portrait");
					final JSONObject object = new JSONObject();
					try {
						object.put("AllowChangeScreen","portrait");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					((Activity) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mView.loadCallback(loadCallback, object.toString());

						}
					});
				}
			} else if (orientation > 225 && orientation < 315) { //设置横屏
				Log.i(TAG, "设置横屏");
				if(!screenPortrait){
					return;
				}
				screenPortrait=false;

				if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
					((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					resultSc.setAllowChangeScreen("landscape");
					final JSONObject object = new JSONObject();
					try {
						object.put("AllowChangeScreen","landscape");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					((Activity) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mView.loadCallback(loadCallback, object.toString());

						}
					});
				}
			}
//			else if (orientation > 45 && orientation < 135) {// 设置反向横屏
//				Log.i(TAG, "反向横屏");
//				if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
//					if(screenPortrait){
//						return;
//					}
//					screenPortrait=true;
//					((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//					resultSc.setAllowChangeScreen("landscape");
//					((Activity) mContext).runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							mView.loadCallback(loadCallback, resultSc);
//						}
//					});
//				}
//			}
//			else if (orientation > 135 && orientation < 225) {
//				Log.i(TAG, "反向竖屏");
//				if(!screenPortrait){
//					return;
//				}
//				screenPortrait=false;
//				if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
//					((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
//					resultSc.setAllowChangeScreen("portrait");
//					((Activity) mContext).runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							mView.loadCallback(loadCallback, resultSc);
//						}
//					});
//				}
//			}
		}
	}

}
