package com.cnc.mediaplayer.sdk.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * @包名 ：com.cnc.mediaplayer.sdk.utils
 * @类名 ：UIUtils
 * @时间 ：2018/02/05 10:15
 * @作者 ：linby （注释）
 * @版本 ：1.6.1
 * @描述 ：UI工具类
 */
public class UIUtils {
    private final static String TAG = UIUtils.class.getSimpleName();

    /**
     * 通过资源名字获取布局id
     * @param context 上下问
     * @param name 资源的名字字符串
     * @return 布局id号
     */
    public static int getLayoutResIDByName(Context context, String name) {
        int resId = context.getResources().getIdentifier(name, "layout", context.getPackageName());
        if (resId == 0) {
            Log.e(TAG, "get layout failed, name = " + name + ", package = " + context.getPackageName());
        }
        return resId;
    }

    /**
     * 通过资源名字获取资源id
     * @param context 上下问
     * @param name 资源的名字字符串
     * @return 资源id号
     */
    public static int getIdResIDByName(Context context, String name) {
        int resId = context.getResources().getIdentifier(name, "id", context.getPackageName());
        if (resId == 0) {
            Log.e(TAG, "get id failed, name = " + name + ", package = " + context.getPackageName());
        }
        return resId;
    }

    /**
     * 通过资源名字获取字符串id
     * @param context 上下问
     * @param name 资源的名字字符串
     * @return 资源字符串id号
     */
    public static int getStringResIDByName(Context context, String name) {
        int resId = context.getResources().getIdentifier(name, "string", context.getPackageName());
        if (resId == 0) {
            Log.e(TAG, "get string failed, name = " + name + ", package = " + context.getPackageName());
        }
        return resId;
    }

    /**
     * 通过资源名字获取int类型id
     * @param context 上下问
     * @param name 资源的名字字符串
     * @return int类型id号
     */
    public static int getIntegerResIDByName(Context context, String name) {
        int resId = context.getResources().getIdentifier(name, "integer", context.getPackageName());
        if (resId == 0) {
            Log.e(TAG, "get integer failed, name = " + name + ", package = " + context.getPackageName());
        }
        return resId;
    }

    /**
     * 通过资源名字获取drawable类型id
     * @param context 上下问
     * @param name 资源的名字字符串
     * @return drawable类型id号
     */
    public static int getDrawableResIDByName(Context context, String name) {
        int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        if (resId == 0) {
            Log.e(TAG, "get drawable failed, name = " + name + ", package = " + context.getPackageName());
        }
        return resId;
    }

    /**
     * 通过资源名字获取drawable类型id
     * @param context 上下问
     * @param name 资源的名字字符串
     * @return drawable类型id号
     */
    public static int getRawResIDByName(Context context, String name) {
        int resId = context.getResources().getIdentifier(name, "raw", context.getPackageName());
        if (resId == 0) {
            Log.e(TAG, "get raw failed, name = " + name + ", package = " + context.getPackageName());
        }
        return resId;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }
    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();

    }
}
