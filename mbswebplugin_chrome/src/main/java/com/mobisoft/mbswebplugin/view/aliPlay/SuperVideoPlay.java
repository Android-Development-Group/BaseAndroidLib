package com.mobisoft.mbswebplugin.view.aliPlay;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunVidSts;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.widget.AliyunScreenMode;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.mobisoft.mbswebplugin.Entity.CallBackResult;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVo;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVoDao;
import com.mobisoft.mbswebplugin.Entity.Videos;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.ActivityManager;
import com.mobisoft.mbswebplugin.base.Recycler;
import com.mobisoft.mbswebplugin.dao.greenDao.GreenDBManager;
import com.mobisoft.mbswebplugin.refresh.BGARefreshLayout;
import com.mobisoft.mbswebplugin.utils.FileUtils;

import java.util.List;

/**
 * Author：Created by fan.xd on 2018/6/20.
 * Email：fang.xd@mobisoft.com.cn
 * Description：播放器单例
 */
public class SuperVideoPlay implements Recycler.Recycleable {

	private AliyunVodPlayerView mAliyunVodPlayerView;
	private RelativeLayout ll_mbs_fragmnet;

	private static SuperVideoPlay superVideoPlay;
	private int orientation = Configuration.ORIENTATION_PORTRAIT;
	private boolean isAdd;

	public static SuperVideoPlay getInstance() {
		if (superVideoPlay == null) {
			superVideoPlay = new SuperVideoPlay();
		}
		return superVideoPlay;
	}


	public AliyunVodPlayerView onCreatView() {
		if (mAliyunVodPlayerView == null) {
			mAliyunVodPlayerView = new AliyunVodPlayerView(ActivityManager.get().bottomActivity());
			mAliyunVodPlayerView.getContext();
			mAliyunVodPlayerView.setId(R.id.ly_dialog_picker);
			//设置RelativeLayout布局的宽高
			RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					(int) (ScreenUtils.getWidth(ActivityManager.get().topActivity()) * 9.0f / 16));
			relLayoutParams.addRule(RelativeLayout.ABOVE, R.id.web_tool_bar);
			RelativeLayout.LayoutParams relLayoutParams2 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			relLayoutParams2.addRule(RelativeLayout.BELOW, R.id.ly_dialog_picker);
			mAliyunVodPlayerView.setLayoutParams(relLayoutParams);
		}
		return mAliyunVodPlayerView;
	}

	public void addView(BGARefreshLayout bgaRefreshLayout, RelativeLayout ll_mbs_fragmnet) {

		if (bgaRefreshLayout == null || ll_mbs_fragmnet == null) {
			new RuntimeException("bgaRefreshLayout 或者 ll_mbs_fragmnet 不能为空");
		}

		View decorView = ActivityManager.get().topActivity().getWindow().getDecorView();
		int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
		decorView.setSystemUiVisibility(option);
		if (Build.VERSION.SDK_INT >= 21) {
			ActivityManager.get().topActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
			ll_mbs_fragmnet.setFitsSystemWindows(false);
		}

		RelativeLayout.LayoutParams relLayoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		relLayoutParams2.addRule(RelativeLayout.BELOW, R.id.ly_dialog_picker);
		bgaRefreshLayout.setLayoutParams(relLayoutParams2);
		this.ll_mbs_fragmnet = ll_mbs_fragmnet;
		if (mAliyunVodPlayerView != null) {
			ViewGroup parentViewGroup = (ViewGroup) mAliyunVodPlayerView.getParent();
			if (parentViewGroup != null ) {
				parentViewGroup.removeView(mAliyunVodPlayerView);
			}
		}
		ll_mbs_fragmnet.addView(mAliyunVodPlayerView);
		mAliyunVodPlayerView.setKeepScreenOn(true);//保持屏幕敞亮
// 去掉缓存功能
	//		String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save_cache";
	//		mAliyunVodPlayerView.setPlayingCache(true, sdDir, 60 * 60 /*时长, s */, 300 /*大小，MB*/);
		mAliyunVodPlayerView.setTheme(AliyunVodPlayerView.Theme.Blue);
		mAliyunVodPlayerView.setCirclePlay(false);

		mAliyunVodPlayerView.enableNativeLog();
		isAdd = true;
	}

	/**
	 * 设置视频的播放类型
	 *
	 * @param videoUrl
	 * @param type
	 * @param params
	 */
	public CallBackResult<String> setPlaySource(String videoUrl, String imageUrl, String type, String[] params, IAliyunVodPlayer.OnFirstFrameStartListener onFirstFrameStartListener) throws Exception {
		CallBackResult<String> backResult = new CallBackResult();

		if (TextUtils.isEmpty(type)) {
			backResult.setCode(400);
			backResult.setMsg("播放类型错误!");
			backResult.setResult(false);
			return backResult;
		}
//		if (TextUtils.isEmpty(params[1])) {
//			params[1] = "CourseItem_no";
//		}
		if (type.equals("localSource")) {
			if (mAliyunVodPlayerView.isPlaying() && !TextUtils.isEmpty(params[1])) {
				if (TextUtils.equals(params[1], mAliyunVodPlayerView.getVideoInfo().getCourseItem_no())) {
					backResult.setCode(200);
					backResult.setMsg("已经有同样的视频在播放了!");
					backResult.setResult(true);
					return backResult;
				} else {
					mAliyunVodPlayerView.stop();
				}
			} else if (mAliyunVodPlayerView.isPlaying() && TextUtils.isEmpty(params[3])) {
				backResult.setCode(404);
				backResult.setMsg("播放地址为空!");
				backResult.setResult(false);
				mAliyunVodPlayerView.stop();
				mAliyunVodPlayerView.setCoverUri(imageUrl);
				return backResult;
			} else if (TextUtils.equals(params[1], mAliyunVodPlayerView.getVideoInfo().getCourseItem_no())) {
				if (TextUtils.equals(params[3], mAliyunVodPlayerView.getVideoInfo().getPlayUrl())) {
					backResult.setCode(200);
					backResult.setMsg("已经设置过同样的视频课程了!");
					backResult.setResult(true);
					final IAliyunVodPlayer.PlayerState playerState = mAliyunVodPlayerView.getPlayerState();
					if(playerState == IAliyunVodPlayer.PlayerState.Stopped){
						mAliyunVodPlayerView.start();
					}
//					mAliyunVodPlayerView.setPlayInfo(params);
					return backResult;
				}
			}

			mAliyunVodPlayerView.setPlayInfo(params);

			AliyunVodPlayerView.VideoInfo videoInfo = mAliyunVodPlayerView.getVideoInfo();
			AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
			String videoDirectory = FileUtils.getVideoDirectory(videoInfo.getCourse_no(), videoInfo.getCourseItem_no());
			boolean isDownLoad = getIsDownload(videoInfo.getCourse_no(), videoInfo.getCourseItem_no());
			if (FileUtils.isVideoExists(videoDirectory) && isDownLoad) {
				alsb.setSource(videoDirectory);
			} else {
				alsb.setSource(videoInfo.getPlayUrl());
			}
			AliyunLocalSource localSource = alsb.build();
			if (TextUtils.isEmpty(videoInfo.getPlayUrl())) {
				mAliyunVodPlayerView.setCoverUri(videoInfo.getPlaceholderurl());
			} else {
				mAliyunVodPlayerView.setLocalSource(localSource);
				mAliyunVodPlayerView.setCoverUri(videoInfo.getPlaceholderurl());
			}
			Log.i("oye", videoInfo.toString());
			mAliyunVodPlayerView.setProgress(videoInfo.getVideoPercentage() * mAliyunVodPlayerView.getDuration());
			mAliyunVodPlayerView.setOnFirstFrameStartListener(onFirstFrameStartListener);
		} else if (type.equals("vidsts")) {

			String vid = params[0];
			String akId = params[1];
			String akSecret = params[2];
			String scuToken = params[3];

			AliyunVidSts vidSts = new AliyunVidSts();
			vidSts.setVid(vid);
			vidSts.setAcId(akId);
			vidSts.setAkSceret(akSecret);
			vidSts.setSecurityToken(scuToken);
			mAliyunVodPlayerView.setVidSts(vidSts);
		}
		backResult.setCode(200);
		backResult.setMsg("OK");
		backResult.setResult(true);
		return backResult;
	}

	/**
	 * 该视频是否已经下载
	 *
	 * @param courseNo
	 * @param courseItem_no
	 * @return
	 */
	private boolean getIsDownload(String courseNo, String courseItem_no) {
		if (TextUtils.isEmpty(courseItem_no)) return false;
		List<DownloadVideoVo> list = GreenDBManager.getInstance()
				.getVideoDao(ActivityManager.get().topActivity().getApplicationContext())
				.queryBuilder().where(DownloadVideoVoDao.Properties.Course_no.eq(courseNo)).list();
		if (list != null && list.size() >= 1) {
			List<Videos> videos = list.get(0).getVideos();
			if (videos != null) {
				for (int i = 0; i < videos.size(); i++) {
					Videos videos1 = videos.get(i);
					if (TextUtils.equals(videos1.getCourseItem_no(), courseItem_no)) {
						return videos1.isDownload();
					}
				}
			}
		}
		return false;
	}


	public void onDestroy() {
		orientation = Configuration.ORIENTATION_PORTRAIT;
		Recycler.release(this);
	}

	@Override
	public void release() {
		if (ll_mbs_fragmnet != null) {
			ll_mbs_fragmnet.removeAllViews();
			ll_mbs_fragmnet = null;
		}
	}


	public void setContext(Context context) {
//		if (context != null) {
//			if (mAliyunVodPlayerView != null) mAliyunVodPlayerView.setContext(context);
//		} else {
//			if (mAliyunVodPlayerView != null)
//				mAliyunVodPlayerView.setContext(ActivityManager.get().topActivity());
//		}
		if (mAliyunVodPlayerView != null)
			mAliyunVodPlayerView.setContext(ActivityManager.get().topActivity());
	}

	public void start() {
		if (mAliyunVodPlayerView != null) mAliyunVodPlayerView.onSart();

	}

	public void pause() {
		if (mAliyunVodPlayerView != null) mAliyunVodPlayerView.onPause();

	}

	public void stop() {
		if (mAliyunVodPlayerView != null) mAliyunVodPlayerView.stop();

	}

	public boolean onKeyDown() {
		if (mAliyunVodPlayerView != null) {
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				//设置为小屏状态
				mAliyunVodPlayerView.changeScreenMode(AliyunScreenMode.Small);
				return false;
			}else {
				mAliyunVodPlayerView.pause("close");
//				mAliyunVodPlayerView.onStop();
			}
		}
		return true;
	}

	/**
	 * 切换屏幕
	 */
	public void updatePlayerViewMode() {
		if (mAliyunVodPlayerView != null) {
			orientation = mAliyunVodPlayerView.getContext().getResources().getConfiguration().orientation;
			if (orientation == Configuration.ORIENTATION_PORTRAIT) {                //转为竖屏了。
				//显示状态栏
//                if (!isStrangePhone()) {
//                    getSupportActionBar().show();
//                }

				((Activity) mAliyunVodPlayerView.getmContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
				mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

				//设置view的布局，宽高之类
				RelativeLayout.LayoutParams aliVcVideoViewLayoutParams = (RelativeLayout.LayoutParams) mAliyunVodPlayerView.getLayoutParams();
				aliVcVideoViewLayoutParams.height = (int) (ScreenUtils.getWidth
						((Activity) mAliyunVodPlayerView.getmContext()) * 9.0f / 16);
				aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;


//                if (!isStrangePhone()) {
//                    aliVcVideoViewLayoutParams.topMargin = getSupportActionBar().getHeight();
//                }

			} else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {                //转到横屏了。
				//隐藏状态栏
				if (!isStrangePhone()) {
					((Activity) mAliyunVodPlayerView.getmContext()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
					mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  底部导航栏
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
				}

				//设置view的布局，宽高
				RelativeLayout.LayoutParams aliVcVideoViewLayoutParams = (RelativeLayout.LayoutParams) mAliyunVodPlayerView.getLayoutParams();
				aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
				aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                if (!isStrangePhone()) {
//                    aliVcVideoViewLayoutParams.topMargin = 0;
//                }

			}

		}
	}


	private boolean isStrangePhone() {
		boolean strangePhone = Build.DEVICE.equalsIgnoreCase("mx5")
				|| Build.DEVICE.equalsIgnoreCase("Redmi Note2")
				|| Build.DEVICE.equalsIgnoreCase("Z00A_1")
				|| Build.DEVICE.equalsIgnoreCase("hwH60-L02")
				|| Build.DEVICE.equalsIgnoreCase("hermes")
				|| (Build.DEVICE.equalsIgnoreCase("V4") && Build.MANUFACTURER.equalsIgnoreCase("Meitu"))
				|| (Build.DEVICE.equalsIgnoreCase("m1metal") && Build.MANUFACTURER.equalsIgnoreCase("Meizu"));

		VcPlayerLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
		return strangePhone;

	}
}
