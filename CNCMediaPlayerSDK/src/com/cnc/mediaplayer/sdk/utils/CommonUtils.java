package com.cnc.mediaplayer.sdk.utils;

import android.content.Context;
import android.view.View;

/**
 * @包名 ：com.cnc.mediaplayer.sdk.utils
 * @类名 ：CommonUtils
 * @时间 ：2018/02/05 10:11
 * @作者 ：linby （注释）
 * @版本 ：1.6.1
 * @描述 ：通用工具类
 */
public class CommonUtils {

    /**
     * 将dp值转换为对应的pixel值
     * @param context 上下文
     * @param dipValue dp值
     * @return 转换后的pixel值
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获取指定view在屏幕中的x坐标
     * @param view 需要获取位置的view
     * @return view在屏幕中的x坐标
     */
    public static int getXLocationOnScreen(View view) {
        if (view == null)
            throw new NullPointerException("view can't be null !!");
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[0];
    }

    /**
     * 获取指定view在屏幕中的y坐标
     * @param view 需要获取位置的view
     * @return view在屏幕中的y坐标
     */
    public static int getYLocationOnScreen(View view) {
        if (view == null)
            throw new NullPointerException("view can't be null !!");
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }
}
