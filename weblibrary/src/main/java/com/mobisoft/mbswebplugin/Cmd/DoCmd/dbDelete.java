package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Entity.JsResult;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.dao.db.WebViewDao;
import com.mobisoft.mbswebplugin.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/6/30.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class dbDelete extends DoCmdMethod {
    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
        try {
            JSONObject jsonObject = new JSONObject(params);
            /** 存储key*/
            String getkey = jsonObject.optString("key");
            /*工号*/
            String account = jsonObject.optString("account");
            int index = deleteFromDB(context, account, getkey);

            JsResult jsResult = new JsResult();
            jsResult.setAccount(account);
            jsResult.setKey(getkey);
            jsResult.setResult(index>0?true:false);
            String json= JSON.toJSONString(jsResult);
            webView.loadUrl(UrlUtil.getFormatJs(callBack,json));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 根据key 从数据库得到value
     *
     * @param account 工号
     * @param key     关键字
     * @return 根据acoutn 和 key查询据库的数据
     */
    protected int deleteFromDB(Context context, String account, String key) {
        WebViewDao mWebViewDao = new WebViewDao(context.getApplicationContext());
        return mWebViewDao.deleteWebviewList(account, key);
    }
}
