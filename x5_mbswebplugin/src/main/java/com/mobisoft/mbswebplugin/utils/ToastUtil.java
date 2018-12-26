package com.mobisoft.mbswebplugin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobisoft.mbswebplugin.R;

/**
 * 全局的Taost
 *
 * @author Fan xuedong
 *         2016年5月12日 下午9:15:03
 * @version V1.0
 *          弹窗操作
 */
public class ToastUtil {
    private static final String TAG = "ToastUtil";
    private static Toast toast;
    private static View view;

    private ToastUtil() {
    }

    @SuppressLint("ShowToast")
    private static void getToast(Context context) {
        if (toast == null) {
            toast = new Toast(context);
        }
        if (view == null) {
            view = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
        }
        toast.setView(view);
    }

    private static void getNewToast(Context context,CharSequence msg) {
        //自定义Toast控件
        if (toast == null) {
            toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.toast_clear_layout, null);
            LinearLayout relativeLayout = (LinearLayout) view.findViewById(R.id.toast_linear);
            //动态设置toast控件的宽高度，宽高分别是130dp
            //这里用了一个将dp转换为px的工具类PxUtil//(int) DisplayUtil.dip2px(context, 130)
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,  ViewGroup.LayoutParams.WRAP_CONTENT);
            relativeLayout.setLayoutParams(layoutParams);
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_toast_clear);
        textView.setText(msg);

        toast.setView(view);
        toast.show();
    }
    private static void getNewToast(Context context,int msgId) {
        //自定义Toast控件
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_clear_layout, null);
        LinearLayout relativeLayout = (LinearLayout) toastView.findViewById(R.id.toast_linear);
        //动态设置toast控件的宽高度，宽高分别是130dp
        //这里用了一个将dp转换为px的工具类PxUtil
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) DisplayUtil.dip2px(context, 130), ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(layoutParams);
        TextView textView = (TextView) toastView.findViewById(R.id.tv_toast_clear);
        textView.setText(msgId);
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(toastView);
        toast.show();
    }
    public static void showShortToast(Context context, CharSequence msg) {
        showToast(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context context, int resId) {
        showToast(context.getApplicationContext(), resId, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, CharSequence msg) {
        showToast(context.getApplicationContext(), msg, Toast.LENGTH_LONG);
    }

    public static void showLongToast(Context context, int resId) {
        showToast(context.getApplicationContext(), resId, Toast.LENGTH_LONG);
    }

    private static void showToast(Context context, CharSequence msg,
                                  int duration) {
        try {
            getToast(context);
            toast.setText(msg);
//            getNewToast(context,msg);
            toast.setText(msg);
            toast.setDuration(duration);
            toast.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static void showToast(Context context, int resId, int duration) {
        try {
            if (resId == 0) {
                return;
            }
            getToast(context);
            toast.setText(resId);

//            getNewToast(context,resId);
            toast.setDuration(duration);
            toast.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


}
