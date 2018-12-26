package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsResultListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.core.QrcodeActivity;
import com.mobisoft.mbswebplugin.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;
import static com.mobisoft.mbswebplugin.core.QrcodeActivity.RESULT_QR_CODE;

/**
 * Author：Created by fan.xd on 2017/8/8.
 * Email：fang.xd@mobisoft.com.cn
 * Description：二位吗识别
 */

public class QrCode extends DoCmdMethod implements MbsResultListener {
    private MbsWebPluginContract.View view;
    private String callback;

    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        this.view = view;
        this.callback = callBack;
        Intent intent = new Intent(context, QrcodeActivity.class);
        ((Activity) context).startActivityForResult(intent, RESULT_QR_CODE);
        presenter.setResultListener(QrCode.this);

        return null;
    }

    @Override
    public void onActivityResult(Context context, MbsWebPluginContract.View view, int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            Log.i("onScanQRCodeSuccess", "result：" + result);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("result",result);
                view.loadUrl(UrlUtil.getFormatJs(callback, jsonObject.toString()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
