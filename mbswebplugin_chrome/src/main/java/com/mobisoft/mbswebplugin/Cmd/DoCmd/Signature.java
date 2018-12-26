package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Cmd.Working.DefaultUploadCreator;
import com.mobisoft.mbswebplugin.Cmd.Working.UploadCB;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsResultListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.UpLoadUtile;
import com.mobisoft.mbswebplugin.view.SignatureView.SingActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/7/3.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 电子签名
 */

public class Signature extends DoCmdMethod implements MbsResultListener {
	public static final int REQUEST_CODE_SING = 0x89;
	private UploadCB uploadCB;
	private String callBack;
	private String params;

	@Override
	public String doMethod(HybridWebView hybridWebView, final Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, final String params, final String callBack) {
		uploadCB = new DefaultUploadCreator();
		uploadCB.create((Activity) context, view, callBack, 0);
		this.params = params;
		this.callBack = callBack;
		presenter.setResultListener(this);
		Intent intent = new Intent(context, SingActivity.class);

		try {
			JSONObject jsonObject = new JSONObject(params);
			String signature = jsonObject.optString("signature");
			String BGimageUrl = jsonObject.optString("BGimageUrl", "p");
			Bundle bundle = new Bundle();
			bundle.putString("signature", signature);
			bundle.putString("BGimageUrl", BGimageUrl);
			intent.putExtras(bundle);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		((Activity) context).startActivityForResult(intent, REQUEST_CODE_SING);
		return null;
	}

	@Override
	public void onActivityResult(final Context context, final MbsWebPluginContract.View view, final int requestCode, int resultCode, final Intent data) {

		switch (requestCode) {
			case REQUEST_CODE_SING:

				if (data != null) {

					String path = data.getStringExtra("path");
					uploadCB.onUploadStart(1);
					if (!TextUtils.isEmpty(path)) {
						UpLoadUtile.getInstance().postFile(context, path, params, callBack, uploadCB, 1);
						Toast.makeText(context, "已经签名成功", Toast.LENGTH_SHORT).show();
					}

				}

				break;
		}
	}


}
