package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.view.serach.SearchListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2018/7/13.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */
public class SetSearchBar extends DoCmdMethod {
	@Override
	public String doMethod(final HybridWebView webView, final Context context, final MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, final String callBack) {
		try {
			JSONObject jsonObject = new JSONObject(params);
			/** js返回的标题*/
			String placeholder = jsonObject.optString("placeholder");
			view.setSearchBar(placeholder, new SearchListener() {
				@Override
				public void onClick(View v) {

				}

				@Override
				public void onBack(View v) {
					if (context instanceof Activity) {
						((Activity) context).finish();
					}
				}

				@Override
				public void onCancel(View v) {

				}

				@Override
				public void onEditorAction(TextView v, int actionId, KeyEvent event, String editable) {
					if(actionId== EditorInfo.IME_ACTION_SEARCH
							||actionId==EditorInfo.IME_ACTION_GO
							||actionId ==EditorInfo.IME_ACTION_NEXT
							||event.getAction() == KeyEvent.KEYCODE_SEARCH
							||event.getAction() == KeyEvent.KEYCODE_MEDIA_NEXT){
						view.loadCallback("search",editable);
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
