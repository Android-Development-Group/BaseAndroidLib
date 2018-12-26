package com.cnc.mediaplayer.sdk.widget.orientation;

import android.content.Context;
import android.content.pm.ActivityInfo;

import com.cnc.mediaplayer.sdk.controller.ICNCPlayerControlEx;

/**
 * @包名 ：com.cnc.mediaplayer.sdk.widget.orientation
 * @类名 ：OrientationController
 * @时间 ：2018/02/05 10:55
 * @作者 ：linby （注释）
 * @版本 ：1.6.1
 * @描述 ：手势方向控制
 */
public class OrientationController implements OrientationDetector.OrientationChangeListener {

    private ICNCPlayerControlEx mPlayerControl;
    private OrientationDetector mOrientationDetector;

    public OrientationController(Context context, ICNCPlayerControlEx playerControl) {
        mPlayerControl = playerControl;
        mOrientationDetector = new OrientationDetector(context);
        mOrientationDetector.setOrientationChangeListener(this);
    }

    @Override
    public void onOrientationChanged(int screenOrientation, OrientationDetector.Direction direction) {
        if (mPlayerControl == null) {
            return;
        }
        if (direction == OrientationDetector.Direction.PORTRAIT) {
            mPlayerControl.setFullscreen(false, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (direction == OrientationDetector.Direction.REVERSE_PORTRAIT) {
            mPlayerControl.setFullscreen(false, ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        } else if (direction == OrientationDetector.Direction.LANDSCAPE) {
            mPlayerControl.setFullscreen(true, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (direction == OrientationDetector.Direction.REVERSE_LANDSCAPE) {
            mPlayerControl.setFullscreen(true, ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
    }

    /**
     * 使能方向检测
     */
    public void enableOrientationDetect() {
        mOrientationDetector.enable();
    }

    /**
     * 失能方向检测
     */
    public void disableOrientationDetect() {
        mOrientationDetector.disable();
    }

    /**
     * 释放方向检测监听器
     */
    public void releaseListener(){
        mOrientationDetector.disable();
        mOrientationDetector.setOrientationChangeListener(null);
    }
}
