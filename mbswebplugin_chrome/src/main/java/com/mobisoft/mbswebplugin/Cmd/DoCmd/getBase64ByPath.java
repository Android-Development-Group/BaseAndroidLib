package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.Base64Util;
import com.mobisoft.mbswebplugin.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/10/15.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 * 根据绝对路径 获取base64位信息
 */

public class getBase64ByPath extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        try {
            JSONObject jsonObject = new JSONObject(params);
            String imagePath = jsonObject.optString("path");
            String base64File = Base64Util.encodeBase64File(imagePath);
            JSONObject object = new JSONObject();
            object.put("base64", base64File);
            object.put("path", imagePath);
            final String json = object.toString();
            view.loadUrl(UrlUtil.getFormatJs(callBack, json));

        } catch (JSONException e) {
            e.printStackTrace();
            view.loadUrl(UrlUtil.getFormatJs(callBack, "{}"));
        } catch (Exception e) {
            e.printStackTrace();
            view.loadUrl(UrlUtil.getFormatJs(callBack, "{}"));
        }
        return null;
    }
}
