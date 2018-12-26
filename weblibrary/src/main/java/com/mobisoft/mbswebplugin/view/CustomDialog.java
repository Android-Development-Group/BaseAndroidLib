package com.mobisoft.mbswebplugin.view;

import android.app.AlertDialog;
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

public class CustomDialog extends AlertDialog {
    private TextView tv_msg;
    private int progress;
    private int max;

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
        setContentView(R.layout.gif_customdialog_layout);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        tv_msg = (TextView) findViewById(R.id.tv_load_dialog);
    }

    public void setMessage(String msg) {
//        if (tv_msg == null)
//            tv_msg = (TextView) findViewById(R.id.tv_load_dialog);
        if (tv_msg != null)
            tv_msg.setText(msg);

    }

    @Override
    public void show() {
        super.show();
    }




    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
