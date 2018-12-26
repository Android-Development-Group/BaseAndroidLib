package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.utils.UrlUtil;
import com.mobisoft.mbswebplugin.view.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author：Created by fan.xd on 2017/2/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description： AlertDialog 弹出控件
 * 可以根据命令的不同显示不同的 样式的AlertDialog
 * {"title":"提示","content":"是否退出登录？","yes_action":"logout","no_action":"close"}入参描述：
 * title：提示框，头部标题 content: 提示框标语yes_action: 确认按钮，回掉方法 no_ation：取消按钮，回掉方法
 */

public class ConfirmMethod extends DoCmdMethod {
    @Override
    public String doMethod(final HybridWebView webView, final Context context, final MbsWebPluginContract.View view, final MbsWebPluginContract.Presenter presenter, String cmd, String params, final String callBack) {


        AlertDialog mAlertDialog = new AlertDialog(context).builder();

        try {
            JSONObject object = new JSONObject(params);
            final String title = object.optString("title");
            final String content = object.optString("content");
            String confirm = object.optString("confirm");
            final String yes_action = object.optString("yes_action");
            String cancel = object.optString("cancel");
            final String no_action = object.optString("no_action");
//            if(TextUtils.isEmpty(confirm)){
//                confirm=yes_action;
//            }if(TextUtils.isEmpty(cancel)){
//                confirm=no_action;
//            }

            if (TextUtils.isEmpty(title)) {
                mAlertDialog.setTitle(context.getString(R.string.wen_xin_tips));
            } else {
                mAlertDialog.setTitle(title);
            }

            if (TextUtils.isEmpty(content)) {
                mAlertDialog.setTitle(context.getString(R.string.jing_gao));
            } else {
                mAlertDialog.setMsg(content);
            }
            final JSONObject jsonObject = new JSONObject();
            if(TextUtils.isEmpty(confirm)&& TextUtils.isEmpty(cancel)){
                mAlertDialog.setPositiveButton(context.getString(R.string.que_ren), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject.put("yes","yes");
                            jsonObject.put("result",true);
                            view.loadUrl(UrlUtil.getFormatJs(callBack,jsonObject.toString()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                mAlertDialog.setNegativeButton(context.getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject.put("no","cancel");
                            jsonObject.put("result",false);
                            view.loadUrl(UrlUtil.getFormatJs(callBack,jsonObject.toString()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else if (!TextUtils.isEmpty(confirm)){
                mAlertDialog.setPositiveButton(confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject.put("yes","yes");
                            jsonObject.put("result",true);
                            view.loadUrl(UrlUtil.getFormatJs(callBack,jsonObject.toString()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            if (!TextUtils.isEmpty(cancel)) {
                mAlertDialog.setNegativeButton(cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject.put("no","cancel");
                            jsonObject.put("result",false);
                            view.loadUrl(UrlUtil.getFormatJs(callBack,jsonObject.toString()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }                    }
                });
            }

            mAlertDialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
