package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.app.Activity;
import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.utils.ActivityCollector;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 清理activity
 */

public class ClearTask extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

//        if (((WebAppActivity)context).isClearTask) { // 不能二次清除
        if (view.getIsClearTask()) { // 不能二次清除
            return null;
        }
        try {
            JSONObject object = new JSONObject(params);
            final int index = object.optInt("clearTask");
            if(index==0){
                if(context instanceof Activity)
                    ((Activity) context).finish();
            }else {
                view.setIsClearTask(true);
                ActivityCollector.clearTask(index);
            }
//            ((WebAppActivity)context).isClearTask = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
