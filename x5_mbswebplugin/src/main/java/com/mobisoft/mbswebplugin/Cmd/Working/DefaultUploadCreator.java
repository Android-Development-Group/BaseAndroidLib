package com.mobisoft.mbswebplugin.Cmd.Working;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.SafeDialogOper;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.UrlUtil;
import com.mobisoft.mbswebplugin.utils.Utils;
import com.mobisoft.mbswebplugin.view.progress.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/6/15.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class DefaultUploadCreator implements UploadCB {
	private CustomDialog dialog;
	private Activity activity;
	private MbsWebPluginContract.View view;
	private int maxSize = 1;
	private int index = 0;
	private JSONArray jsonArray = new JSONArray();
	private String picFunction;


	@Override
	public void create(Activity activity, MbsWebPluginContract.View view, String callBack, int themeId) {
		if (activity == null || activity.isFinishing()) {
			Log.e("UploadCBCreator--->", "show download dialog failed:activity was recycled or finished");
			return;
		}
		this.view = view;
		this.activity = activity;
		this.picFunction = callBack;
		if (themeId <= 0)
			dialog = new CustomDialog(activity, R.style.CustomDialog);
		else
			dialog = new CustomDialog(activity, themeId);
//		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setMax(100);
		dialog.setProgress(0);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
	}

	@Override
	public void onUploadComplete(final String json) {
		Utils.getMainHandler().post(new Runnable() {
			@Override
			public void run() {
				SafeDialogOper.safeDismissDialog(dialog);
				if (!TextUtils.isEmpty(json) && view != null) {
					view.loadUrl(json);
				}
			}
		});

		activity = null;
	}

	@Override
	public void onUploadStart(int total) {
		this.maxSize = total;
		dialog.setMax(100);
		SafeDialogOper.safeShowDialog(dialog);

	}

	@Override
	public void onUploadProgress(final long current, final long total) {

		Utils.getMainHandler().post(new Runnable() {
			@Override
			public void run() {
				dialog.setProgress((int) (current / total) * 100);
				dialog.setMessage("已上传:" + (int) (current / total) * 100);

			}
		});
	}

	@Override
	public void onUploadProgress(final int current, final int total) {
		Utils.getMainHandler().post(new Runnable() {
			@Override
			public void run() {
				dialog.setProgress((int) (current / total) * 100);
				dialog.setMessage("已上传:" + (current / total) * 100);

			}
		});
	}


	@Override
	public void onUploadError(Integer error) {
		if (error <= 0)
			ToastUtil.showShortToast(activity.getApplicationContext(), error);
		else
			ToastUtil.showShortToast(activity.getApplicationContext(), error);

	}

	@Override
	public void onUploadFinish(Integer message) {
		Utils.getMainHandler().post(new Runnable() {
			@Override
			public void run() {
				SafeDialogOper.safeDismissDialog(dialog);
			}
		});
		if (message > 0) {
			ToastUtil.showShortToast(activity.getApplicationContext(), message);
		}
	}

	@Override
	public void onUploadError(String error) {
		if (!TextUtils.isEmpty(error))
			ToastUtil.showShortToast(activity.getApplicationContext(), error);
		else
			ToastUtil.showShortToast(activity.getApplicationContext(), activity.getString(R.string.upload_farlure));
		index++;
		onUploadFinish(jsonArray);
	}

	@Override
	public void onUploadFinish(String message) {
		Utils.getMainHandler().post(new Runnable() {
			@Override
			public void run() {
				SafeDialogOper.safeDismissDialog(dialog);
			}
		});

		if (!TextUtils.isEmpty(message)) {
			ToastUtil.showShortToast(activity.getApplicationContext(), message);
		}


	}

	@Override
	public void onUpLoadCallBack(Object imageInfo) {

		jsonArray.put(imageInfo);
		index++;
		onUploadFinish(jsonArray);
	}

	private void onUploadFinish(JSONArray array) {
		if (index == maxSize) {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("images", array);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			onUploadComplete(UrlUtil.getFormatJs(picFunction, jsonObject.toString()));
		}
	}

}
