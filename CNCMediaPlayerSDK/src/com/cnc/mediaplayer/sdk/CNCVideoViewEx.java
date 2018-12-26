package com.cnc.mediaplayer.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.cnc.mediaplayer.sdk.controller.CNCMediaController;
import com.cnc.mediaplayer.sdk.controller.ICNCPlayerControlEx;
import com.cnc.mediaplayer.sdk.lib.event.GeneralEvent;
import com.cnc.mediaplayer.sdk.lib.settings.CNCSDKSettings;
import com.cnc.mediaplayer.sdk.lib.utils.log.ALog;
import com.cnc.mediaplayer.sdk.lib.videoview.CNCVideoView;
import com.cnc.mediaplayer.sdk.lib.videoview.IMediaEventsListener;
import com.cnc.mediaplayer.sdk.listeners.OnFullscreenChangeListener;
import com.cnc.mediaplayer.sdk.utils.UIUtils;
import com.cnc.mediaplayer.sdk.widget.orientation.OrientationController;
import com.cnc.mediaplayer.sdk.widget.playlist.MediaPlayerVideoEpisode;
import com.cnc.mediaplayer.sdk.widget.quality.MediaPlayerVideoModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by wuhp on 2016/8/26.
 */
public class CNCVideoViewEx extends CNCVideoView implements ICNCPlayerControlEx {

	private final String TAG = CNCVideoViewEx.class.getSimpleName();
	protected CNCMediaController mMediaController;
	protected OrientationController mOrientationController;
	protected int mSeekPosition;
	protected CNCSDKSettings mSDKSettings;
	private int mVideoViewLayoutWidth = 0;
	private int mVideoViewLayoutHeight = 0;
	private boolean mIsFullScreen;
	protected List<MediaPlayerVideoEpisode> mVideoEpisodeList;
	protected int mNextEpisodeIndex = 0;
	private final Object mLock = new Object();
	private OnFullscreenChangeListener mOnFullscreenChangeListener;
	private IMediaEventsListener mOnMediaEventsListener;

	public CNCVideoViewEx(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	public CNCVideoViewEx(Context context) {
		super(context);
		init(context);
	}

	public CNCVideoViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CNCVideoViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		mSDKSettings = CNCSDKSettings.getInstance();
		//自动翻转控制。此处仅是允许检测方向变化，是否自动翻转还需要判断mSDKSettings.isAutoRotate()
		mOrientationController = new OrientationController(context.getApplicationContext(), this);
		if (mSDKSettings != null && mSDKSettings.isAutoRotate()) {
			mOrientationController.enableOrientationDetect();
		} else {
			mOrientationController.disableOrientationDetect();
		}

		/**
		 * 设置回调，把CNCVideoView的一些事件通过回调传递给CNCVideoViewEx
		 */
		setMediaEventsListener(new IMediaEventsListener() {
			@Override
			public void onMediaPause() {
				if (mOnMediaEventsListener != null) {
					mOnMediaEventsListener.onMediaPause();
				}
			}

			@Override
			public void onMediaStart() {
				if (!isPrepared() && mMediaController != null) {
					mMediaController.showLoading();
				}
				if (mOnMediaEventsListener != null) {
					mOnMediaEventsListener.onMediaStart();
				}
			}

			@Override
			public void onBufferingStart() {
				if (mMediaController != null) {
					mMediaController.showLoading();
				}
				if (mOnMediaEventsListener != null) {
					mOnMediaEventsListener.onBufferingStart();
				}
			}

			@Override
			public void onBufferingEnd() {
				if (mMediaController != null) {
					mMediaController.hideLoading();
				}
				if (mOnMediaEventsListener != null) {
					mOnMediaEventsListener.onBufferingEnd();
				}
			}

			@Override
			public void onMediaCompletion() {
				if (mMediaController != null) {
					mMediaController.showComplete();
				}
				if (mOnMediaEventsListener != null) {
					mOnMediaEventsListener.onMediaCompletion();
				}
			}

			@Override
			public void onMediaPrepared() {
				if (mMediaController != null) {
					mMediaController.hideLoading();
					mMediaController.setEnabled(true);
				}
				if (mOnMediaEventsListener != null) {
					mOnMediaEventsListener.onMediaPrepared();
				}
			}

			@Override
			public void onMediaError(int what, int extra) {
				String errorText = "[" + extra + "] " + GeneralEvent.getEventString(extra);
				ALog.e(TAG, "onMediaError: " + what + "," + extra + "。错误说明：" + errorText);
				if (mMediaController != null) {
					mMediaController.showError(errorText);
				}
				if (mOnMediaEventsListener != null) {
					mOnMediaEventsListener.onMediaError(what, extra);
				}
			}

			@Override
			public void onMediaInfo(int what, int extra) {
				if (mOnMediaEventsListener != null) {
					mOnMediaEventsListener.onMediaInfo(what, extra);
				}
			}
		});
	}

	public void setMediaController(CNCMediaController controller) {
		mMediaController = controller;
		controller.setEnabled(false);
		controller.setMediaPlayer(this);
	}

	@Override
	synchronized public void setFullscreen(boolean fullscreen) {
		int screenOrientation = fullscreen ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
				: ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		setFullscreen(fullscreen, screenOrientation);
	}

	@Override
	synchronized public void setFullscreen(boolean fullscreen, int screenOrientation) {
		// Activity需要设置为: android:configChanges="keyboardHidden|orientation|screenSize"
		Activity activity = (Activity) getContext();
		mIsFullScreen = fullscreen;
		if (fullscreen) {
			if (mVideoViewLayoutWidth == 0 && mVideoViewLayoutHeight == 0) {
				ViewGroup.LayoutParams params = getLayoutParams();
				mVideoViewLayoutWidth = params.width;//保存全屏之前的参数
				mVideoViewLayoutHeight = params.height;
			}
			ViewGroup.LayoutParams params = getLayoutParams();
			int width = UIUtils.getWidth(getContext());
			int h = UIUtils.getHeight(getContext());
//			params.height = h>width?width:h;
//			params.width =  h>width?h:width;
			params.height = width;
			params.width=h;
			setLayoutParams(params);

			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			activity.setRequestedOrientation(screenOrientation);
		} else {
			ViewGroup.LayoutParams params = getLayoutParams();
            params.width = mVideoViewLayoutWidth;//使用全屏之前的参数
            params.height = mVideoViewLayoutHeight;
            setLayoutParams(params);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			activity.setRequestedOrientation(screenOrientation);
//			int width = UIUtils.getScreenWidth(getContext());
//			int h = 9 * width / 16;
//			params.height = h;
//			params.width = width;
//			setLayoutParams(params);

		}
		if (mMediaController != null) {
			mMediaController.toggleButtons(fullscreen);
		}
		if (mOnFullscreenChangeListener != null) {
			mOnFullscreenChangeListener.OnFullscreenChange(fullscreen);
		}
	}

	@Override
	public void changeVideoQuality(String url) {
		if (url == null) {
			return;
		}
		int seekWhenPrepared = getLastSeekWhenPrepared();
		if (seekWhenPrepared <= 0) {
			seekWhenPrepared = getCurrentPosition();
		}
		release(true);
		play(url);
		if (mSDKSettings != null && !mSDKSettings.isLive()) {
			seekTo(seekWhenPrepared);
		}
	}

	@Override
	public void playNextEpisode() {
		if (mVideoEpisodeList == null || mVideoEpisodeList.size() <= 0) {
			ALog.e(TAG, "mVideoEpisodeList == null || mVideoEpisodeList.size() <= 0");

			return;
		}
		if (mNextEpisodeIndex < 0 || mNextEpisodeIndex >= mVideoEpisodeList.size()) {
			ALog.w(TAG, "episode index out of bound, index = " + mNextEpisodeIndex);
			mNextEpisodeIndex = 0;
		}
		if (mVideoEpisodeList != null && mVideoEpisodeList.size() > 0 && mNextEpisodeIndex >= 0) {
			MediaPlayerVideoEpisode nextEpisode = mVideoEpisodeList.get(mNextEpisodeIndex);
			if (nextEpisode != null) {
				String nextEpisodeUrl = nextEpisode.getUrl();
				MediaPlayerVideoModule nextEpisodeDefaultQuality = nextEpisode.getDefaultQuality();
				List<MediaPlayerVideoModule> qualityList = nextEpisode.getQualityList();
				String nextEpisodeTitle = nextEpisode.getTitle();

				if (nextEpisodeDefaultQuality != null) {
					nextEpisodeUrl = nextEpisodeDefaultQuality.getUrl();
				}
				if (nextEpisodeUrl != null && !nextEpisodeUrl.isEmpty()) {
					ALog.i(TAG, "播放下一集, url = " + nextEpisodeUrl);

					release(true);
					play(nextEpisodeUrl);
					if (mMediaController != null) {
						mMediaController.setVideoQualityList(qualityList);
						if (nextEpisodeDefaultQuality != null) {
							mMediaController.setVideoQuality(nextEpisodeDefaultQuality);
						}
						if (nextEpisodeTitle != null) {
							mMediaController.setTitle(nextEpisodeTitle);
						}
					}
				}
			}
			mNextEpisodeIndex++;
		}
	}

	@Override
	public void takeScreenShot(int resourceId, float paddingLeft, float paddingTop) {
		if (mMediaController == null) {
			return;
		}
		synchronized (mLock) {
			View view = mMediaController;
			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache(true);
			Bitmap content = getVideoViewScreenShot();
			Bitmap layout = view.getDrawingCache(true);
			Bitmap screenshot = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(screenshot);
			if (content != null) {
				canvas.drawBitmap(content, getRenderViewToLeft(), getRenderViewTopHeight(), new Paint());
			}
			canvas.drawBitmap(layout, 0, 0, new Paint());
			//添加水印
			Bitmap waterMark = BitmapFactory.decodeResource(getResources(), resourceId);
			canvas.drawBitmap(waterMark, paddingLeft, paddingTop, new Paint());
			//canvas.save();
			//canvas.restore();
			File screenshotFile = null;
			File baseDir = mAppContext.getExternalFilesDir(null);
			if (baseDir != null) {
				screenshotFile = new File(baseDir, "screenshot");
			}
			if (!screenshotFile.exists()) {
				screenshotFile.mkdir();
			}
			File file = new File(screenshotFile.getPath(), getCurrentDataString() + ".jpg");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					ALog.e(TAG, "IOException", e);
				}
			}
			//经Bitmap保存为图片，保存在File下
			try {
				screenshot.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//销毁缓存，当再次点击按钮时建立新的缓存。
			view.setDrawingCacheEnabled(false);
			view.destroyDrawingCache();
		}
	}

	/**
	 * 启动播放器播放视频
	 *
	 * @param url 视频播放的地址
	 */
	public void play(String url) {
		if (mSeekPosition > 0) {
			seekTo(mSeekPosition);
		}
		setVideoPath(url);
		start();
	}

	/**
	 * 启动播放器播放视频，包含清晰度
	 *
	 * @param currentEpisode 当前集，包含清晰度列表
	 * @param episodeList    剧集列表
	 */
	public void play(MediaPlayerVideoEpisode currentEpisode, List<MediaPlayerVideoEpisode> episodeList) {
		if (currentEpisode == null || currentEpisode.getDefaultQuality() == null) {
			return;
		}
		mVideoEpisodeList = episodeList;
		if (episodeList != null && episodeList.size() > 0) {
			mNextEpisodeIndex = 1;
		}
		if (mMediaController != null) {
			mMediaController.setVideoQuality(currentEpisode.getDefaultQuality());
			mMediaController.setVideoQualityList(currentEpisode.getQualityList());
			mMediaController.setTitle(currentEpisode.getTitle());
		}
		play(currentEpisode.getDefaultQuality().getUrl());
	}

	public void setOnFullscreenChangeListener(OnFullscreenChangeListener listener) {
		mOnFullscreenChangeListener = listener;
	}

	public void setOnMediaEventsListener(IMediaEventsListener listener) {
		mOnMediaEventsListener = listener;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	/**
	 * 销毁播放器
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		//释放资源
		if (mOrientationController != null) {
			mOrientationController.releaseListener();
		}
	}

	/**
	 * 如果处于全屏状态，则退出全屏并返回true，否则返回false
	 *
	 * @return
	 */
	public boolean onBackPressed() {
		if (mIsFullScreen) {
			setFullscreen(false);
			return true;
		}
		return false;
	}
}
