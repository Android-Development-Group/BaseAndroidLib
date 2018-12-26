package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Author：Created by fan.xd on 2017/11/13.
 * Email：fang.xd@mobisoft.com.cn
 * Description：国泰copy文本的
 */

public class CutAndCopy extends DoCmdMethod {
    private ClipboardManager mClipboardManager;

    @Override
    public String doMethod(HybridWebView hybridWebView, final Context context, final MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, final String callBack) {
        JSONObject jsonObject = null;
        mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        try {
            jsonObject = new JSONObject(params);
            String cpy = jsonObject.optString("cpy");
            copyText("cpy",cpy);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将文本拷贝至剪贴板
     *
     * @param text
     */
    public void copyText(String label, String text) {
        ClipData clip = ClipData.newPlainText(label, text);

        mClipboardManager.setPrimaryClip(clip);
    }

}
