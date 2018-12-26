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


package com.cnc.mediaplayer.sdk.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.cnc.mediaplayer.sdk.lib.settings.CNCSDKSettings;
import com.cnc.mediaplayer.sdk.utils.UIUtils;
import com.cnc.mediaplayer.sdk.utils.CommonUtils;
import com.cnc.mediaplayer.sdk.widget.gesture.GestureController;
import com.cnc.mediaplayer.sdk.widget.quality.MediaPlayerVideoModule;
import com.cnc.mediaplayer.sdk.widget.quality.VideoQualityPopupView;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;

/**
 * @包名 ：com.cnc.mediaplayer.sdk.controller
 * @类名 ：CNCMediaController
 * @时间 ：2017/11/30 16:08
 * @作者 ：linby （注释/修订）
 * @版本 ：1.4.1
 * @描述 ：媒体播放控制器自定义界面。主要包括标题栏、播放进度条、播放时间、清晰度、全屏等控制
 */
/**
 * A view containing controls for a MediaPlayer. Typically contains the buttons
 * like "Play/Pause" and a progress slider. It takes care of synchronizing the
 * controls with the state of the MediaPlayer.
 * <p>
 * The way to use this class is to a) instantiate it programatically or b)
 * create it in your xml layout.
 *
 * a) The MediaController will create a default set of controls and put them in
 * a window floating above your application. Specifically, the controls will
 * float above the view specified with setAnchorView(). By default, the window
 * will disappear if left idle for three seconds and reappear when the user
 * touches the anchor view. To customize the MediaController's style, layout and
 * controls you should extend MediaController and override the {#link
 * {@link #makeControllerView()} method.
 *
 * b) The MediaController is a FrameLayout, you can put it in your layout xml
 * and get it through {@link #findViewById(int)}.
 *
 * NOTES: In each way, if you want customize the MediaController, the SeekBar's
 * id must be mediacontroller_progress, the Play/Pause's must be
 * mediacontroller_pause, current time's must be mediacontroller_time_current,
 * total time's must be mediacontroller_time_total, file name's must be
 * mediacontroller_file_name. And your resources must have a pause_button
 * drawable and a play_button drawable.
 * <p>
 * Functions like show() and hide() have no effect when MediaController is
 * created in an xml layout.
 */
public class CNCMediaController extends FrameLayout implements GestureController.IPlayerGestureControl {

    private final String TAG = this.getClass().getSimpleName();

    private ICNCPlayerControlEx mPlayer;
    private Context mContext;
    private ProgressBar mProgress;
    private TextView mEndTime, mCurrentTime;
    private TextView mTitle;
    private boolean mShowing = false;
    private boolean mDragging;

    private boolean mEnableChangeFullScreen = true;
    private boolean mIsFullScreen = false;

    private static final int sDefaultTimeout = 3000;

    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSE = 2;
    private static final int STATE_LOADING = 3;
    private static final int STATE_ERROR = 4;
    private static final int STATE_COMPLETE = 5;

    private int mState = STATE_LOADING;

    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int SHOW_LOADING = 3;
    private static final int HIDE_LOADING = 4;
    private static final int SHOW_ERROR = 5;
    private static final int HIDE_ERROR = 6;
    private static final int SHOW_COMPLETE = 7;
    private static final int HIDE_COMPLETE = 8;
    private static final int UPDATE_PAUSE_PLAY = 21;

    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private ImageButton mPlayBtn;// 开启暂停按钮
    private ImageButton mChangeFullScreenBtn;
    private View mBackBtn;// 返回按钮
    private ViewGroup mLoadingLayout;
    private ViewGroup mErrorLayout;
    private TextView mErrorText;
    private View mTopLayout;
    private View mBottomLayout;
    private View mGestureLayout;
    private View mPlayCenterBtn;
    private CNCSDKSettings mSDKSettings;
    private GestureController mGestureController;

    // 视频清晰度切换
    private LinearLayout mQualityLayout;
    private TextView mQualityTextView;
    private VideoQualityPopupView mQualityPopup;
    protected MediaPlayerVideoModule mCurrentQuality;
    List<MediaPlayerVideoModule> mVideoQualityList;

    private OnControllerMsgListener mOnControllerMsgListener;

    public CNCMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    public CNCMediaController(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mSDKSettings = CNCSDKSettings.getInstance();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewRoot = inflater.inflate(UIUtils.getLayoutResIDByName(mContext, "cnc_player_controller"), this);
        viewRoot.setOnTouchListener(mTouchListener);
        initControllerView(viewRoot);
        if (mSDKSettings != null && mSDKSettings.isUsingGestureController()) {
            mGestureController = new GestureController(mGestureLayout, this);
        }
    }

    private void initControllerView(View viewRoot) {
        mTopLayout = viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "layout_controller_top"));
        mBottomLayout = viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "layout_controller_bottom"));
        mGestureLayout = viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "layout_controller_gesture"));

        mLoadingLayout = (ViewGroup) viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "layout_loading"));
        mErrorLayout = (ViewGroup) viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "layout_error"));
        mErrorText = (TextView) viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "tv_error"));
        mPlayBtn = (ImageButton) viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "btn_play"));
        mChangeFullScreenBtn = (ImageButton) viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "btn_change_fullscreen"));
        mPlayCenterBtn = viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "btn_play_center"));
        mBackBtn = viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "btn_back"));

        mQualityLayout = (LinearLayout) findViewById(UIUtils.getIdResIDByName(mContext, "layout_video_quality"));
        mQualityTextView = (TextView) findViewById(UIUtils.getIdResIDByName(mContext, "tv_quality"));
        mQualityPopup = new VideoQualityPopupView(getContext());

        if (mPlayBtn != null) {
            mPlayBtn.requestFocus();
            mPlayBtn.setOnClickListener(mClickListener);
        }

        if (mEnableChangeFullScreen) {
            if (mChangeFullScreenBtn != null) {
                mChangeFullScreenBtn.setVisibility(VISIBLE);
                mChangeFullScreenBtn.setOnClickListener(mClickListener);
            }
        } else {
            if (mChangeFullScreenBtn != null) {
                mChangeFullScreenBtn.setVisibility(GONE);
            }
        }

        if (mPlayCenterBtn != null) {//重新开始播放
            mPlayCenterBtn.setOnClickListener(mClickListener);
        }

        if (mBackBtn != null) {//返回按钮仅在全屏状态下可见
            mBackBtn.setOnClickListener(mClickListener);
        }

        mProgress = (ProgressBar) viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "seekbar"));
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
        }

        if (mSDKSettings != null && mSDKSettings.isUsingSelectVideoQuality()) {
            if (mQualityLayout != null) {
                mQualityLayout.setOnClickListener(mClickListener);
                mQualityLayout.setVisibility(VISIBLE);
            }
        }

        mEndTime = (TextView) viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "tv_duration"));
        mCurrentTime = (TextView) viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "tv_time_played"));
        mTitle = (TextView) viewRoot.findViewById(UIUtils.getIdResIDByName(mContext, "tv_title"));
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        mQualityPopup.setCallback(new VideoQualityPopupView.Callback() {
            @Override
            public void onQualitySelected(MediaPlayerVideoModule quality) {
                mQualityPopup.hide();
                setVideoQuality(quality);
                if (mPlayer != null && quality != null) {
                    mPlayer.changeVideoQuality(quality.getUrl());
                    // todo 重新播放
                }
            }

            @Override
            public void onPopupViewDismiss() {
                mQualityLayout.setSelected(false);
                if (isShowing())
                    show();
            }
        });

    }

    public void setMediaPlayer(ICNCPlayerControlEx player) {
        mPlayer = player;
        updatePausePlay();
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 3 seconds of inactivity.
     */
    public void show() {
        show(sDefaultTimeout);
    }

    private void disableUnsupportedButtons() {
        //直播时不允许拖拉
        if (mSDKSettings != null && mSDKSettings.isLive()) {
            mProgress.setEnabled(false);
        }
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 'timeout' milliseconds of inactivity.
     *
     * @param timeout The timeout in milliseconds. Use 0 to show
     *                the controller until hide() is called.
     */
    public void show(int timeout) {//只负责上下两条bar的显示,不负责中央loading,error,playBtn的显示.
        if (!mShowing) {
            setProgress();
            if (mPlayBtn != null) {
                mPlayBtn.requestFocus();
            }
            disableUnsupportedButtons();
            setShowing(true);
        }
        updatePausePlay();
        updateBackButton();

        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }
        if (mTopLayout.getVisibility() != VISIBLE) {
            mTopLayout.setVisibility(VISIBLE);
        }
        if (mBottomLayout.getVisibility() != VISIBLE) {
            mBottomLayout.setVisibility(VISIBLE);
        }

        // cause the progress bar to be updated even if mShowing
        // was already true. This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    /**
     * Get current show state
     * @return Is video show
     */
    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Set show state
     * @param show Is video show
     */
    private void setShowing(boolean show) {
        mShowing = show;
    }


    /**
     * Hide top and bottom bar and center loading view
     */
    public void hide() {//只负责上下两条bar的隐藏,不负责中央loading,error,playBtn的隐藏
        if (mQualityPopup.isShowing()) {
            mQualityPopup.hide();
        }
        if (mShowing) {
            mHandler.removeMessages(SHOW_PROGRESS);
            mTopLayout.setVisibility(GONE);
            mBottomLayout.setVisibility(GONE);

            setShowing(false);
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case FADE_OUT: //1
                    hide();
                    break;
                case SHOW_PROGRESS: //2
                    pos = setProgress();
                    if (!mDragging && mShowing && mPlayer != null && mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
                case SHOW_LOADING: //3
                    //show();
                    showCenterView(UIUtils.getIdResIDByName(mContext, "layout_loading"));
                    break;
                case SHOW_COMPLETE: //7
                    showCenterView(UIUtils.getIdResIDByName(mContext, "btn_play_center"));
                    break;
                case SHOW_ERROR: //5
                    if (msg.obj != null && mErrorText != null) {
                        mErrorText.setText((String) msg.obj);
                    }
                    show();
                    showCenterView(UIUtils.getIdResIDByName(mContext, "layout_error"));
                    break;
                case HIDE_LOADING: //4
                    //hide();
                    hideCenterView();
                    break;
                case HIDE_ERROR: //6
                case HIDE_COMPLETE: //8
                    hide();
                    hideCenterView();
                    break;
                case UPDATE_PAUSE_PLAY: //21
                    updatePausePlay();
                    break;
            }
        }
    };

    private void showCenterView(int resId) {
        if (resId == UIUtils.getIdResIDByName(mContext, "layout_loading")) {
            if (mLoadingLayout.getVisibility() != VISIBLE) {
                mLoadingLayout.setVisibility(VISIBLE);
            }
            if (mPlayCenterBtn.getVisibility() == VISIBLE) {
                mPlayCenterBtn.setVisibility(GONE);
            }
            if (mErrorLayout.getVisibility() == VISIBLE) {
                mErrorLayout.setVisibility(GONE);
            }
        } else if (resId == UIUtils.getIdResIDByName(mContext, "btn_play_center")) {
            if (mPlayCenterBtn.getVisibility() != VISIBLE) {
                mPlayCenterBtn.setVisibility(VISIBLE);
            }
            if (mLoadingLayout.getVisibility() == VISIBLE) {
                mLoadingLayout.setVisibility(GONE);
            }
            if (mErrorLayout.getVisibility() == VISIBLE) {
                mErrorLayout.setVisibility(GONE);
            }

        } else if (resId == UIUtils.getIdResIDByName(mContext, "layout_error")) {
            if (mErrorLayout.getVisibility() != VISIBLE) {
                mErrorLayout.setVisibility(VISIBLE);
            }
            if (mPlayCenterBtn.getVisibility() == VISIBLE) {
                mPlayCenterBtn.setVisibility(GONE);
            }
            if (mLoadingLayout.getVisibility() == VISIBLE) {
                mLoadingLayout.setVisibility(GONE);
            }
        }
    }


    private void hideCenterView() {
        if (mPlayCenterBtn.getVisibility() == VISIBLE) {
            mPlayCenterBtn.setVisibility(GONE);
        }
        if (mErrorLayout.getVisibility() == VISIBLE) {
            mErrorLayout.setVisibility(GONE);
        }
        if (mLoadingLayout.getVisibility() == VISIBLE) {
            mLoadingLayout.setVisibility(GONE);
        }
    }

    /**
     * Reset current display
     */
    public void reset() {
        mCurrentTime.setText("00:00");
        mEndTime.setText("00:00");
        mProgress.setProgress(0);
        mPlayBtn.setImageResource(UIUtils.getDrawableResIDByName(mContext, "cnc_player_start_btn"));
        setVisibility(View.VISIBLE);
        hideLoading();
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                long cachePos = 1000L * (position + mPlayer.getCachedDuration()) / duration;
                mProgress.setProgress((int) pos);
                mProgress.setSecondaryProgress((int) cachePos);
            }
        }

        if (mEndTime != null)
            mEndTime.setText(stringForTime(duration));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //show(0);
                break;
            case MotionEvent.ACTION_UP:
                if (mShowing) {
                    hide();
                } else {
                    show();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                hide();
                break;
            default:
                break;
        }
        return true;
    }

    //如果正在显示,则使之消失
    private OnTouchListener mTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (mGestureController != null) {
                mGestureController.onTouchEvent(event);
                return true;
            }
            return false;
        }
    };

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show();
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                doPauseResume();
                show();
                if (mPlayBtn != null) {
                    mPlayBtn.requestFocus();
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (mPlayer == null) {
                return false;
            }
            if (uniqueDown && !mPlayer.isPlaying()) {
                mPlayer.start();
                updatePausePlay();
                show();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (mPlayer == null) {
                return false;
            }
            if (uniqueDown && mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
                show();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
                || keyCode == KeyEvent.KEYCODE_CAMERA) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide();
            }
            return true;
        }
        show();
        return super.dispatchKeyEvent(event);
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPlayer == null) {
                return;
            }

            int id = v.getId();
            if (id == UIUtils.getIdResIDByName(mContext, "btn_play")) {
                doPauseResume();
                show();
            } else if (id == UIUtils.getIdResIDByName(mContext, "btn_change_fullscreen")) {
                mIsFullScreen = !mIsFullScreen;
                updateChangeFullScreenButton();
                updateBackButton();
                mPlayer.setFullscreen(mIsFullScreen);
            } else if (id == UIUtils.getIdResIDByName(mContext, "btn_play_center")) {
                hideCenterView();
                mPlayer.restart();
            } else if (id == UIUtils.getIdResIDByName(mContext, "btn_back")) { //backFullScreen
                backFullScreen();
            } else if (id == UIUtils.getIdResIDByName(mContext, "layout_video_quality")) {
                displayQualityPopupWindow();
            }
        }
    };

    /**
     * backFullScreen
     */
    public void backFullScreen() {
        if (mIsFullScreen) {
			mIsFullScreen = false;
			updateChangeFullScreenButton();
			updateBackButton();
			mPlayer.setFullscreen(false);
		}
    }

    private void updatePausePlay() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayBtn.setImageResource(UIUtils.getDrawableResIDByName(mContext, "cnc_player_pause_btn"));
            //mPlayCenterBtn.setVisibility(GONE);
        } else {
            mPlayBtn.setImageResource(UIUtils.getDrawableResIDByName(mContext, "cnc_player_start_btn"));
            //mPlayCenterBtn.setVisibility(VISIBLE);
        }
    }

    void updateChangeFullScreenButton() {
        if (mIsFullScreen) {
            mChangeFullScreenBtn.setImageResource(UIUtils.getDrawableResIDByName(mContext, "cnc_player_fullscreen_btn"));
        } else {
            mChangeFullScreenBtn.setImageResource(UIUtils.getDrawableResIDByName(mContext, "cnc_player_fullscreen_exit_btn"));
        }
    }

    /**
     * Toggle display mode : full screen or not
     * @param isFullScreen Set full screen or not
     */
    public void toggleButtons(boolean isFullScreen) {
        mIsFullScreen = isFullScreen;
        updateChangeFullScreenButton();
        updateBackButton();
    }

    void updateBackButton() {
        mBackBtn.setVisibility(mIsFullScreen ? View.VISIBLE : View.INVISIBLE);
    }

    public boolean isFullScreen() {
        return mIsFullScreen;
    }

    private void doPauseResume() {
        if (mPlayer == null) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        //updatePausePlay();
        //避免立刻调用updatePausePlay时，pause或start还未生效导致未能正确设置图标。
        mHandler.sendEmptyMessageDelayed(UPDATE_PAUSE_PLAY, 300);
    }

    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        int newPosition = 0;
        boolean change = false;

        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            if (mPlayer == null) {
                return;
            }
            show(3600000);

            mDragging = true;
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (mPlayer == null || !fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            newPosition = (int) newposition;
            change = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            if (mPlayer == null) {
                return;
            }
            if (change) {
                mPlayer.seekTo(newPosition);
                if (mCurrentTime != null) {
                    mCurrentTime.setText(stringForTime(newPosition));
                }
            }
            mDragging = false;
            setProgress();
            updatePausePlay();
            show();

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            setShowing(true);
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
//        super.setEnabled(enabled);
        if (mPlayBtn != null) {
            mPlayBtn.setEnabled(enabled);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        if (mEnableChangeFullScreen) {
            mChangeFullScreenBtn.setEnabled(enabled);
        }
        mBackBtn.setEnabled(true);// 全屏状态下右上角的返回键总是可用.
    }

    /**
     * Show current display view as loading
     */
    public void showLoading() {
        mHandler.sendEmptyMessage(SHOW_LOADING);
    }

    /**
     * Hide loading view
     */
    public void hideLoading() {
        mHandler.sendEmptyMessage(HIDE_LOADING);
    }

    /**
     * Show error text message
     * @param errorText Error text to show
     */
    public void showError(String errorText) {
        if (errorText == null) {
            return;
        }
        Message msg = mHandler.obtainMessage(SHOW_ERROR);
        msg.obj = errorText;
        mHandler.sendMessage(msg);
    }

    /**
     * Hide error message
     */
    public void hideError() {
        mHandler.sendEmptyMessage(HIDE_ERROR);
    }

    /**
     * Show complete message
     */
    public void showComplete() {
        mHandler.sendEmptyMessage(SHOW_COMPLETE);
    }

    /**
     * Hide complete message
     */
    public void hideComplete() {
        mHandler.sendEmptyMessage(HIDE_COMPLETE);
    }

    /**
     * Set url to title
     * @param title The url message
     */
    public void setTitle(String title) {
        if (title == null) {
            return;
        }
        mTitle.setText(title);
    }

    /**
     * Set error view resource id
     * @param resId Error view resource id
     */
    public void setOnErrorView(int resId) {
        mErrorLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(resId, mErrorLayout, true);
    }

    /**
     * Set error view resource id
     * @param onErrorView Error view
     */
    public void setOnErrorView(View onErrorView) {
        mErrorLayout.removeAllViews();
        mErrorLayout.addView(onErrorView);
    }

    /**
     * Set loading view resource id
     * @param resId Loading view resource id
     */
    public void setOnLoadingView(int resId) {
        mLoadingLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(resId, mLoadingLayout, true);
    }

    /**
     * Set loading view
     * @param onLoadingView loading view
     */
    public void setOnLoadingView(View onLoadingView) {
        mLoadingLayout.removeAllViews();
        mLoadingLayout.addView(onLoadingView);
    }

    /**
     * Set error view click listener
     * @param onClickListener Called when error happened
     */
    public void setOnErrorViewClick(OnClickListener onClickListener) {
        mErrorLayout.setOnClickListener(onClickListener);
    }

/*------------------------gesture相关 start--------------------------------------*/

    @Override
    public boolean onSingleTapUp() {
        if (isShowing()) {
            hide();
        } else {
            show();
        }
        return true;
    }

    /**
     * 双击时切换画面比例
     *
     * @return
     */
    @Override
    public boolean onDoubleTap() {
        if (mPlayer != null) {
            mPlayer.toggleAspectRatio();
        }
        return true;
    }

    @Override
    public int getMediaCurrentPosition() {
        if (mPlayer == null) {
            return 0;
        }
        return mPlayer.getCurrentPosition();
    }

    @Override
    public int getMediaDuration() {
        if (mPlayer == null) {
            return 0;
        }
        return mPlayer.getDuration();
    }

    @Override
    public void seekToNewPosition(long newPosition) {
        if (mPlayer == null) {
            return;
        }
        mPlayer.seekTo((int) newPosition);
    }
/*------------------------gesture相关 end--------------------------------------*/

    private void displayQualityPopupWindow() {
        // 弹出清晰度框
        if (mVideoQualityList == null || mVideoQualityList.size() <= 0) {
            return;
        }
        int widthExtra = CommonUtils.dip2px(getContext(), 5);
        int width = mQualityLayout.getMeasuredWidth() + widthExtra;
        int height = (CommonUtils.dip2px(getContext(), 30) + CommonUtils.dip2px(getContext(), 2)) * mVideoQualityList.size();

        int x = CommonUtils.getXLocationOnScreen(mQualityLayout) - widthExtra / 2;
        int y = CommonUtils.getYLocationOnScreen(mQualityLayout) - height;
        mQualityPopup.show(mQualityLayout, mVideoQualityList, this.mCurrentQuality, x, y, width, height);
        mQualityLayout.setSelected(true);
        show();
    }

    /**
     * 更新视频清晰度
     * @param quality 视频清晰度
     */
    public void updateVideoQualityState(MediaPlayerVideoModule quality) {
        mQualityTextView.setText(quality.getName());
    }

    /**
     * 设置视频清晰度
     * @param quality 视频清晰度
     */
    public void setVideoQuality(MediaPlayerVideoModule quality) {
        this.mCurrentQuality = quality;
        if (quality != null && quality.getName() != null) {
            mQualityTextView.setText(quality.getName());
        }
    }

    /**
     * 设置清晰度列表
     * @param list 视频清晰度列表
     */
    public void setVideoQualityList(List<MediaPlayerVideoModule> list) {
        this.mVideoQualityList = list;
    }

    /**
     * 当收到控制信息时回调
     */
    public interface OnControllerMsgListener {
        public void showMsg(String msg);
    }

    /**
     * 设置控制信息监听器
     * @param listener 控制信息监听器
     */
    public void setOnControllerMsgListener(OnControllerMsgListener listener) {
        mOnControllerMsgListener = listener;
    }

}
