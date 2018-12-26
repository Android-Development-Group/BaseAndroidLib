package com.cnc.mediaplayer.sdk.widget.gesture;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnc.mediaplayer.sdk.lib.settings.CNCSDKSettings;
import com.cnc.mediaplayer.sdk.lib.utils.log.ALog;
import com.cnc.mediaplayer.sdk.utils.UIUtils;

import java.util.Locale;


/**
 * @包名 ：com.cnc.mediaplayer.sdk.widget.gesture
 * @类名 ：GestureController
 * @时间 ：2017/11/30 15:55
 * @作者 ：linby （注释/修订）
 * @版本 ：1.4.1
 * @描述 ：手势控制器
 */
public class GestureController extends GestureDetector.SimpleOnGestureListener {

    private final String TAG = this.getClass().getSimpleName();

    private View mLayout;
    private Context mContext;
    IPlayerGestureControl mPlayerGestureControl;
    private View mGestureVolumeLayout;
    private View mGestureBrightnessLayout;
    private View mGestureFastForwardLayout;

    TextView mVolumeText;
    ImageView mVolumeImg;
    TextView mFastForwardDeltaText;
    TextView mFastForwardDurationText;
    TextView mFastForwardTargetText;
    TextView mBrightnessText;

    private CNCSDKSettings mSDKSettings;
    private final int mMaxVolume;
    private float mBrightness = -1;
    private int mVolume = -1;
    private long mNewPosition = -1;
    private final AudioManager mAudioManager;
    private final GestureDetector mGestureDetector;

    public GestureController(View gestureLayout, IPlayerGestureControl playerGestureControl) {
        mLayout = gestureLayout;
        mContext = gestureLayout.getContext();
        mPlayerGestureControl = playerGestureControl;
        mSDKSettings = CNCSDKSettings.getInstance();

        mGestureVolumeLayout = mLayout.findViewById(UIUtils.getIdResIDByName(mContext, "layout_gesture_volume"));
        mGestureBrightnessLayout = mLayout.findViewById(UIUtils.getIdResIDByName(mContext, "layout_gesture_brightness"));
        mGestureFastForwardLayout = mLayout.findViewById(UIUtils.getIdResIDByName(mContext, "layout_gesture_fastforward"));

        mVolumeText = (TextView) mGestureVolumeLayout.findViewById(UIUtils.getIdResIDByName(mContext, "tv_gesture_volume"));
        mVolumeImg = (ImageView) mGestureVolumeLayout.findViewById(UIUtils.getIdResIDByName(mContext, "iv_gesture_volume"));
        mFastForwardDeltaText = (TextView) mGestureFastForwardLayout.findViewById(UIUtils.getIdResIDByName(mContext, "tv_gesture_fastforward_delta"));
        mFastForwardDurationText = (TextView) mGestureFastForwardLayout.findViewById(UIUtils.getIdResIDByName(mContext, "tv_gesture_fastforward_all"));
        mFastForwardTargetText = (TextView) mGestureFastForwardLayout.findViewById(UIUtils.getIdResIDByName(mContext, "tv_gesture_fastforward_target"));

        Activity activity = (Activity) gestureLayout.getContext();
        mAudioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mGestureDetector = new GestureDetector(gestureLayout.getContext(), new GestureListener());
    }

    /**
     * 当触摸事件发生时回调
     * @param event 事件类型
     * @return 是否处理这个touch事件
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }
        return false;
    }

    /**
     * 手势监听器
     */
    public class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private boolean mFirstTouch;
        private boolean mVolumeControl;
        private boolean mToSeek;

        @Override
        public boolean onDown(MotionEvent e) {
            mFirstTouch = true;
            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return mPlayerGestureControl.onSingleTapUp();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return mPlayerGestureControl.onDoubleTap();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            float deltaY = mOldY - e2.getY();
            float deltaX = mOldX - e2.getX();
            if (mFirstTouch) {
                mToSeek = Math.abs(distanceX) >= Math.abs(distanceY);
                boolean rightAreaControl = mOldX > mLayout.getWidth() * 0.5f;
                if (mSDKSettings != null && mSDKSettings.getGestureBrightnessVolume() == CNCSDKSettings.GESTURE_LEFT_BRIGHTNESS_RIGHT_VOLUME) {
                    mVolumeControl = rightAreaControl;
                } else {
                    mVolumeControl = !rightAreaControl;
                }
                mFirstTouch = false;
            }

            if (mToSeek) {
                if (mSDKSettings != null && !mSDKSettings.isLive()) {
                    onProgressSlide(-deltaX / mLayout.getWidth());
                }
            } else {
                float percent = deltaY / mLayout.getHeight();
                if (mVolumeControl) {
                    onVolumeSlide(percent);
                } else {
                    onBrightnessSlide(percent);
                }
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

    }

    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        int per = (int) (index * 1.0 / mMaxVolume * 100);
        String text = per + "%";
        if (per == 0) {
            text = "off";
        }
        boolean mute = (per == 0);

        mVolumeText.setText(text);
        if (mute) {
            mVolumeImg.setImageResource(UIUtils.getDrawableResIDByName(mContext, "cnc_player_volume_off"));
        } else {
            mVolumeImg.setImageResource(UIUtils.getDrawableResIDByName(mContext, "cnc_player_volume_on"));
        }
        mGestureVolumeLayout.setVisibility(View.VISIBLE);
        mGestureBrightnessLayout.setVisibility(View.GONE);
        mGestureFastForwardLayout.setVisibility(View.GONE);
    }

    private void onProgressSlide(float percent) {
        long position = mPlayerGestureControl.getMediaCurrentPosition();
        long duration = mPlayerGestureControl.getMediaDuration();
        long deltaMax = Math.min(100 * 1000, duration - position);
        long delta = (long) (deltaMax * percent);


        mNewPosition = delta + position;
        if (mNewPosition > duration) {
            mNewPosition = duration;
        } else if (mNewPosition <= 0) {
            mNewPosition = 0;
            delta = -position;
        }
        int showDelta = (int) delta / 1000;
        if (showDelta != 0) {
            String deltaText = showDelta > 0 ? ("+" + showDelta) : "" + showDelta;
            String targetText = generateTime(mNewPosition) + "/";
            String durationText = generateTime(duration);

            mFastForwardDeltaText.setText(deltaText);
            mFastForwardTargetText.setText(targetText);
            mFastForwardDurationText.setText(durationText);
            mGestureVolumeLayout.setVisibility(View.GONE);
            mGestureBrightnessLayout.setVisibility(View.GONE);
            mGestureFastForwardLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 滑动改变亮度
     * @param percent 滑动改变的百分比
     */
    private void onBrightnessSlide(float percent) {
        Activity activity = (Activity) mLayout.getContext();
        if (mBrightness < 0) {
            mBrightness = activity.getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f) {
                mBrightness = 0.50f;
            } else if (mBrightness < 0.01f) {
                mBrightness = 0.01f;
            }
        }
        ALog.d(TAG, "mBrightness:" + mBrightness + ", percent:" + percent);
        WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        activity.getWindow().setAttributes(lpa);
        String text = ((int) (lpa.screenBrightness * 100)) + "%";

        mBrightnessText = (TextView) mGestureBrightnessLayout.findViewById(UIUtils.getIdResIDByName(mContext, "tv_gesture_brightness"));
        mBrightnessText.setText(text);
        mGestureVolumeLayout.setVisibility(View.GONE);
        mGestureBrightnessLayout.setVisibility(View.VISIBLE);
        mGestureFastForwardLayout.setVisibility(View.GONE);
    }

    /**
     * 手势结束
     */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;
        if (mNewPosition >= 0) {
            mPlayerGestureControl.seekToNewPosition(mNewPosition);
            mNewPosition = -1;
        }
        mGestureVolumeLayout.setVisibility(View.GONE);
        mGestureBrightnessLayout.setVisibility(View.GONE);
        mGestureFastForwardLayout.setVisibility(View.GONE);

    }

    private String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format(Locale.US,"%02d:%02d:%02d", hours, minutes, seconds) : String.format(Locale.US,"%02d:%02d", minutes, seconds);
    }

    /**
     * @包名 ：com.cnc.mediaplayer.sdk.widget.gesture
     * @接口名 ：IPlayerGestureControl
     * @时间 ：2017/11/30 15:54
     * @作者 ：linby （注释/修订）
     * @版本 ：1.4.1
     * @描述 ：用户手势控制
     */
    public interface IPlayerGestureControl {
        /**
         * 单手指触摸抬起
         */
        boolean onSingleTapUp();

        /**
         * 双击时回调
         */
        boolean onDoubleTap();

        /**
         * 获取当前媒体播放的位置
         */
        int getMediaCurrentPosition();

        /**
         * 获取媒体播放器时长
         */
        int getMediaDuration();

        /**
         * seek到新的位置
         */
        void seekToNewPosition(long newPosition);
    }
}
