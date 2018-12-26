package com.cnc.mediaplayer.sdk.listeners;

/**
 * @包名 ：com.cnc.mediaplayer.sdk.listeners
 * @类名 ：OnFullscreenChangeListener
 * @时间 ：2018/02/05 10:08
 * @作者 ：linby （注释）
 * @版本 ：1.6.1
 * @描述 ：播放器全屏变化事件的监听
 */
public interface OnFullscreenChangeListener {
    /**
     * 全屏状态改变回调
     * @param fullscreen 是否为全屏
     */
    void OnFullscreenChange(boolean fullscreen);
}
