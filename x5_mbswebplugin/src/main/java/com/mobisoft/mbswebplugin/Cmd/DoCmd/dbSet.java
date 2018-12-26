package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Entity.JsResult;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.dao.db.WebViewDao;
import com.mobisoft.mbswebplugin.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/3/17.
 * Email：fang.xd@mobisoft.com.cn
 * Description：Sql数据存储
 */

public class dbSet extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

        try {
            JSONObject jsonObject = new JSONObject(params);
            /** 存储key*/
            String getkey = jsonObject.optString("key");
            /*工号*/
            String acount = jsonObject.optString("account");
            String value = jsonObject.optString("value");
            long flag = setKeyToDB(context,acount, getkey, value);
            JsResult jsResult = new JsResult();
            jsResult.setAccount(acount);
            jsResult.setKey(getkey);
            jsResult.setResult(flag==-1?false:true);
            String json= JSON.toJSONString(jsResult);
            webView.loadUrl(UrlUtil.getFormatJs(callBack,json));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 存储到数据库
     * @param context
     * @param account 工号
     * @param key   key
     * @param value json字符串
     */
    private long setKeyToDB(Context context, String account, String key, String value) {
        WebViewDao mWebViewDao = new WebViewDao(context.getApplicationContext());

        return mWebViewDao.saveWebviewJson(account,key,value);

    }
}
