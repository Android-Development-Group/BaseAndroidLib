/*
* Copyright (C) 2015 Author <dictfb#gmail.com>
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.cnc.mediaplayer.sdk.widget.orientation;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;

import com.cnc.mediaplayer.sdk.BuildConfig;
import com.cnc.mediaplayer.sdk.lib.utils.log.ALog;

/**
 * @包名 ：com.cnc.mediaplayer.sdk.widget.orientation
 * @类名 ：OrientationDetector
 * @时间 ：2018/02/05 10:55
 * @作者 ：linby （注释）
 * @版本 ：1.6.1
 * @描述 ：方向检测器
 */
public class OrientationDetector {

    private static final String TAG = OrientationDetector.class.getSimpleName();
    private static final int HOLDING_THRESHOLD = 0;
    private Context mContext;
    private OrientationEventListener mOrientationEventListener;

    private int mRotationThreshold = 30;
    private long mHoldingTime = 100;
    private long mLastCalcTime = 0;
    private Direction mLastDirection = Direction.PORTRAIT;

    private int mCurrentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;//初始为竖屏

    private OrientationChangeListener mOrientationChangeListener;

    public OrientationDetector(Context context) {
        this.mContext = context;
    }

    /**
     * 设置方向检测监听器
     * @param listener
     */
    public void setOrientationChangeListener(OrientationChangeListener listener) {
        this.mOrientationChangeListener = listener;
    }

    /**
     * 使能方向检测器
     */
    public void enable() {
        if (mOrientationEventListener == null) {
            mOrientationEventListener = new OrientationEventListener(mContext, SensorManager.SENSOR_DELAY_UI) {
                @Override
                public void onOrientationChanged(int orientation) {
                    processOrientationChange(orientation);
                }
            };
        }

        mOrientationEventListener.enable();
    }

    private void calcHoldingTime() {
        long current = System.currentTimeMillis();
        if (mLastCalcTime == 0) {
            mLastCalcTime = current;
        }
        mHoldingTime += current - mLastCalcTime;
//      ALog.d(TAG, "calcHoldingTime mHoldingTime=" + mHoldingTime);
        mLastCalcTime = current;
    }

    private void resetTime() {
        mHoldingTime = mLastCalcTime = 0;
    }

    private Direction calcDirection(int orientation) {
        if (orientation <= mRotationThreshold
                || orientation >= 360 - mRotationThreshold) {
            return Direction.PORTRAIT;
        } else if (Math.abs(orientation - 180) <= mRotationThreshold) {
            //不返回REVERSE_PORTRAIT，否则经常导致无故黑屏
            return Direction.PORTRAIT;
            //return Direction.REVERSE_PORTRAIT;
        } else if (Math.abs(orientation - 90) <= mRotationThreshold) {
            return Direction.REVERSE_LANDSCAPE;
        } else if (Math.abs(orientation - 270) <= mRotationThreshold) {
            return Direction.LANDSCAPE;
        }
        return null;
    }

    /**
     * 设置初始方向
     * @param direction 初始方向
     */
    public void setInitialDirection(Direction direction) {
        mLastDirection = direction;
    }

    /**
     * 失能方向检测
     */
    public void disable() {
        if (mOrientationEventListener != null) {
            mOrientationEventListener.disable();
        }
    }

    /**
     * 设置方向检测的阈值
     * @param degree 方向检测的阈值设置
     */
    public void setThresholdDegree(int degree) {
        mRotationThreshold = degree;
    }

    /**
     * 方向改变监听器
     */
    public interface OrientationChangeListener {
        /***
         * @param screenOrientation ActivityInfo.SCREEN_ORIENTATION_PORTRAIT or ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
         *                          or ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE or ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
         * @param direction         PORTRAIT or REVERSE_PORTRAIT when screenOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
         *                          LANDSCAPE or REVERSE_LANDSCAPE when screenOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE.
         */
        void onOrientationChanged(int screenOrientation, Direction direction);
    }

    /**
     * 方向值
     */
    public enum Direction {
        PORTRAIT, REVERSE_PORTRAIT, LANDSCAPE, REVERSE_LANDSCAPE
    }

    /**
     * 处理方向改变
     * @param orientation 方向值
     */
    synchronized public void processOrientationChange(int orientation) {
        Direction currDirection = calcDirection(orientation);
        if (currDirection == null) {
            return;
        }

        if (currDirection != mLastDirection) {
            resetTime();
            mLastDirection = currDirection;
            if (BuildConfig.DEBUG) {
                ALog.d(TAG, String.format("方向改变, 开始计时, 当前是方向为%s", currDirection));
            }
        } else {
            calcHoldingTime();
            if (mHoldingTime > HOLDING_THRESHOLD) {
                if (currDirection == Direction.LANDSCAPE) {
                    if (mCurrentOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        ALog.d(TAG, "mCurrentOrientation = " + mCurrentOrientation + ", switch to SCREEN_ORIENTATION_LANDSCAPE");
                        mCurrentOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                        if (mOrientationChangeListener != null) {
                            mOrientationChangeListener.onOrientationChanged(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, currDirection);
                        }
                    }

                } else if (currDirection == Direction.PORTRAIT) {
                    if (mCurrentOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                        ALog.d(TAG, "mCurrentOrientation = " + mCurrentOrientation + ", switch to SCREEN_ORIENTATION_PORTRAIT");
                        mCurrentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                        if (mOrientationChangeListener != null) {
                            mOrientationChangeListener.onOrientationChanged(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, currDirection);
                        }
                    }

                } else if (currDirection == Direction.REVERSE_PORTRAIT) {
                    if (mCurrentOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                        ALog.d(TAG, "mCurrentOrientation = " + mCurrentOrientation + ", switch to SCREEN_ORIENTATION_REVERSE_PORTRAIT");
                        mCurrentOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                        if (mOrientationChangeListener != null) {
                            mOrientationChangeListener.onOrientationChanged(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT, currDirection);
                        }
                    }

                } else if (currDirection == Direction.REVERSE_LANDSCAPE) {
                    if (mCurrentOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                        ALog.d(TAG, "mCurrentOrientation = " + mCurrentOrientation + ", switch to SCREEN_ORIENTATION_REVERSE_LANDSCAPE");
                        mCurrentOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                        if (mOrientationChangeListener != null) {
                            mOrientationChangeListener.onOrientationChanged(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE, currDirection);
                        }
                    }

                }

            }
        }

    }
}
