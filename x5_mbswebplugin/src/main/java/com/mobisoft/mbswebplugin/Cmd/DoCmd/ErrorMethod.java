package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;

/**
 * Author：Created by fan.xd on 2017/2/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description：错误时候的默认毁掉方法
 */

public class ErrorMethod extends DoCmdMethod {

    @Override
    public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
//        String msg = String.format(context.getString(R.string.cmd_error_message), cmd);
        String msg = String.format("当前 cmd 命令：%s 出现异常!", cmd);
//                    ToastUtil.showLongToast(context, msg);
        new Throwable(msg+"\n"+params).printStackTrace();
//        if (BuildConfig.DEBUG) {
//            return msg;
//        }
//        final AlertDialog.Builder builder = new AlertDialog.Builder(webView
//                .getContext());
//        builder.setTitle(R.string.cmd_error_title).setMessage(msg + "\n" + "params：" + params +
//                "\n" + "callBack：" + callBack)
//                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        //禁止响应按back键的事件
//        builder.setCancelable(false);
//        AlertDialog dialog = builder.create();
//        dialog.show();
        return msg;
    }
}
