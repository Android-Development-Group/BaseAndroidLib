package com.cnc.mediaplayer.sdk.controller;

import com.cnc.mediaplayer.sdk.lib.videoview.ICNCPlayerControl;

/**
 * @包名 ：com.cnc.mediaplayer.sdk.controller
 * @类名 ：ICNCPlayerControlEx
 * @时间 ：2017/11/30 16:08
 * @作者 ：linby （注释/修订）
 * @版本 ：1.4.1
 * @描述 ：提供给用户用于控制视频播放的接口
 */
public interface ICNCPlayerControlEx extends ICNCPlayerControl {

    /**
     * 设置全屏或退出全屏
     * @param fullscreen 是否设置全屏
     */
    void setFullscreen(boolean fullscreen);

    /***
     * 设置全屏或退出全屏
     * @param fullscreen 是否设置全屏
     * @param screenOrientation valid only fullscreen=true.values should be one of
     *                          ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
     *                          ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
     *                          ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT,
     *                          ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
     */
    void setFullscreen(boolean fullscreen, int screenOrientation);

    /**
     * 切换分辨率
     * @param url 需要切换分辨率的url
     */
    void changeVideoQuality(String url);

    /**
     * 播放下一集
     */
    void playNextEpisode();

    /**
     * 截图带有水印
     * @param resourceId 水印的资源id
     * @param paddingleft 水印距离左边距离
     * @param paddingright 水印距离右边距离
     */
    void takeScreenShot(int resourceId,float paddingleft, float paddingright);
}
