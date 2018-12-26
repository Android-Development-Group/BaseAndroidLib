package com.mobisoft.mbswebplugin.view.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.mobisoft.mbswebplugin.R;

/**
 * Author：Created by fan.xd on 2018/1/31.
 * Email：fang.xd@mobisoft.com.cn
 * Description：进度条
 */

public class CustomDialog extends ProgressDialog implements CustomProgress{
    private TextView tv_msg;

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private void init(Context context) {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.customdialog_layout);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        tv_msg = (TextView) findViewById(R.id.tv_load_dialog);
    }

    @Override
    public void setMessage(String msg) {
        if (tv_msg == null)
            tv_msg = (TextView) findViewById(R.id.tv_load_dialog);
    }

    @Override
    public void show() {
        super.show();
    }
    @Override
    public void showHud() {
        show();
    }

    @Override
    public void dismissHud() {
        dismiss();
    }
    @Override
    public ProgressDialog getDialog() {
        return this;
    }
}
