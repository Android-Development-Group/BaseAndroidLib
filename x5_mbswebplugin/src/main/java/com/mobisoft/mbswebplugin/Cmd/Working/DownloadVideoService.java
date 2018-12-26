package com.mobisoft.mbswebplugin.Cmd.Working;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoCallback;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVo;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVoDao;
import com.mobisoft.mbswebplugin.Entity.Videos;
import com.mobisoft.mbswebplugin.base.ActivityManager;
import com.mobisoft.mbswebplugin.base.AppConfing;
import com.mobisoft.mbswebplugin.dao.db.WebViewDao;
import com.mobisoft.mbswebplugin.dao.greenDao.GreenDBManager;
import com.mobisoft.mbswebplugin.utils.FileUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author：Created by fan.xd on 2018/6/18.
 * Email：fang.xd@mobisoft.com.cn
 * Description：下载文件
 */
public class DownloadVideoService extends Service {
	private DownloadManager downloadManager;
	private long Id;
	private Timer timer;
	private DownloadVideoVo downloadVideoVo;
	private String course_no;
	private int index = -1;
	private SuperHashMap map = new SuperHashMap();
	private HashMap<String, Long> idMap = new HashMap<>();
	private DownloadReceiver receiver;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			downloadVideo();
		}
	};


	@Override
	public void onCreate() {
		super.onCreate();
		receiver = new DownloadReceiver();

		IntentFilter intentFilter = new IntentFilter();

		//设置接收广播的类型
		intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(receiver, intentFilter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle.getBoolean("isMain", false)) {
				queryAllDownloadIng();
			} else {
				DownloadVideoVo videoVo = (DownloadVideoVo) bundle.getSerializable("videoInfo");
				if (videoVo != null && !videoVo.equals(downloadVideoVo)) {
					map.put(videoVo.getCourse_no(), videoVo);
					if (map.size() == 1) {
						preparationDownLoad(videoVo);
					}
				}
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 获取全部的 正在下载中的的课程
	 * 并且开启下载
	 */
	private void queryAllDownloadIng() {
		if (map.size() == 0) {
			DownloadVideoVoDao dao = GreenDBManager.getInstance().getVideoDao(this);
			QueryBuilder qb = dao.queryBuilder();
			List<DownloadVideoVo> back = (List<DownloadVideoVo>) qb.where(
					DownloadVideoVoDao.Properties.Status.eq(AppConfing.DOWNLOADING))
					.list();
			if (back != null && back.size() >= 1) {
				for (int i = 0; i < back.size(); i++) {
					DownloadVideoVo videoVo = back.get(i);
					map.put(videoVo.getCourse_no(), videoVo);
				}
				index = -1;
				preparationDownLoad(map.getIndex(0));
			}
		}

	}

	/**
	 * 预备下载
	 *
	 * @param videoVo
	 */
	private synchronized void preparationDownLoad(DownloadVideoVo videoVo) {
		if (videoVo == null) {
			return;
		}
		course_no = videoVo.getCourse_no();
		this.downloadVideoVo = videoVo;
		downloadVideo();
	}

	/**
	 * 下载文件
	 */
	private synchronized void downloadVideo() {
		index++;
		if (downloadVideoVo != null) {
			List<Videos> videos = downloadVideoVo.getVideos();
			if (videos != null) {
				if (index >= videos.size()) {
					map.remove(downloadVideoVo.getCourse_no());
					index = -1;
					if (map.size() >= 1)
						preparationDownLoad(map.getIndex(0));

				} else {
					final Videos videoInfo = videos.get(index);
					downloadVideoVo.setStatus(AppConfing.DOWNLOADING);
					videoInfo.setDownload(false);
					DownloadVideoVoDao dao = GreenDBManager.getInstance().getVideoDao(this);


					dao.insertOrReplace(downloadVideoVo);
					if (TextUtils.isEmpty(videoInfo.getPlayUrl())) {
						videoInfo.setDownload(false);
						dao.insertOrReplace(downloadVideoVo);
						handler.sendEmptyMessageDelayed(0, 2 * 1000);
					} else if (!FileUtils.isFileExists(FileUtils.getVideoDirectory(downloadVideoVo, videoInfo))) {
						downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
						// 下载一个大文件。
						final DownloadManager.Request request = new DownloadManager.Request(
								Uri.parse(videoInfo.getPlayUrl()));
						doDownLoad(videoInfo, request);

//						if (!NetWorkUtils.isWifiConnected(DownloadVideoService.this)) {
//							AlertDialog.Builder dialog = new AlertDialog.Builder(DownloadVideoService.this);
//							dialog.setTitle(R.string.wen_xin_tips);
//							dialog.setMessage(R.string.down_msg);
//							dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialogInterface, int i) {
//									dialogInterface.dismiss();
//									request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
//									doDownLoad(videoInfo, request);
//								}
//							});
//							dialog.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialogInterface, int i) {
//									dialogInterface.dismiss();
//									doDownLoad(videoInfo, request);
//									// 仅允许在WIFI连接情况下下载
////									request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
//								}
//							});
//							dialog.create().show();
//						} else {
//							doDownLoad(videoInfo, request);
//						}


					} else {
						downloadVideoVo.setStatus(AppConfing.EXIST);
						videoInfo.setDownload(true);
						dao.insertOrReplace(downloadVideoVo);
						handler.sendEmptyMessageDelayed(0, 2 * 1000);

					}
				}
			}
		}

	}

	private void doDownLoad(Videos videoInfo, DownloadManager.Request request) {
		// 通知栏中将出现的内容
		request.setTitle(downloadVideoVo.getTitle() + "" + videoInfo.getCourseItem_no());
		request.setDescription(downloadVideoVo.getVideo_description());
		// 下载过程和下载完成后通知栏有通知消息。
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

		// 此处可以由开发者自己指定一个文件存放下载文件。
		// 如果不指定则Android将使用系统默认的
		request.setDestinationUri(Uri.fromFile(FileUtils.getLocalFile()));
		String dirType = FileUtils.getDownLaodVideoPath(downloadVideoVo);


		File file = new File(dirType);
		if (!file.exists()) file.mkdirs();
		request.setDestinationInExternalPublicDir(dirType, videoInfo.getCourseItem_no());
		// enqueue 开始启动下载...
		Id = downloadManager.enqueue(request);
	}


	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 进度查询
	 */
	private void queryTask(final String callBack) {
		timer = new java.util.Timer(true);

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				DownloadVideoCallback callback1 = query();
				if (callback1.getProgress() == -1 || callback1.getProgress() >= 100) {
					cancelQuery();

					int i = moveNext(course_no);
					if (i == -1) timer.cancel();
				}
				String params = JSON.toJSONString(callback1);
				Intent tent = new Intent("loadProgressVideo");// 广播的标签，一定要和需要接受的一致。
				tent.putExtra("data", params);
				tent.putExtra("function", callBack);
				DownloadVideoService.this.sendBroadcast(tent);// 发送广播
			}
		};
		timer.schedule(timerTask, 1000, 2000);
	}

	/**
	 * 停止定时器
	 */
	private void cancelQuery() {
		if (timer != null)
			timer.cancel();
	}

	/**
	 * 查询下载进度，文件总大小多少，已经下载多少？
	 */
	private DownloadVideoCallback query() {

		DownloadVideoCallback callback = new DownloadVideoCallback();
		callback.setCourse_no(downloadVideoVo.getCourse_no());
		callback.setCourseItem_no(downloadVideoVo.getVideos().get(index).getCourseItem_no());
		DownloadManager.Query downloadQuery = new DownloadManager.Query();
		downloadQuery.setFilterById(Id);
		Cursor cursor = downloadManager.query(downloadQuery);
		if (cursor != null && cursor.moveToFirst()) {

			int fileName;

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				//android 7.0以后使用COLUMN_LOCAL_URI字段
				fileName = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
			} else {
				fileName = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
			}
			String fn = cursor.getString(fileName);
//			String fu = cursor.getString(fileUri);

			int totalSizeBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
			int bytesDownloadSoFarIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);

			// 下载的文件总大小
			int totalSizeBytes = cursor.getInt(totalSizeBytesIndex);

			// 截止目前已经下载的文件总大小
			int bytesDownloadSoFar = cursor.getInt(bytesDownloadSoFarIndex);

			int pase = (bytesDownloadSoFar / totalSizeBytes) * 100;
			callback.setCurrentSize(bytesDownloadSoFar);
			callback.setTotalSize(totalSizeBytes);
			callback.setProgress(pase);

			Log.i(this.getClass().getName(),
					" 下载到本地 " + fn +
							"\n 文件总大小:" + totalSizeBytes +
							"\n 已经下载:" + bytesDownloadSoFar +
							"\n 进度:" + pase + "%");

			cursor.close();

			return callback;
		}
		callback.setProgress(-1);
		return callback;
	}


	/**
	 * 移动到下一个视频下载
	 *
	 * @param course_no 课件代码
	 */
	private int moveNext(String course_no) {
		DownloadVideoVo downloadVideoVo = map.get(course_no);
		if (downloadVideoVo == null)
			return -1;
		this.downloadVideoVo = downloadVideoVo;
		List<Videos> videos = downloadVideoVo.getVideos();
		if (index >= videos.size() - 1) {
			downloadVideoVo.setStatus("exist");
			DownloadVideoVoDao dao = GreenDBManager.getInstance().getVideoDao(this);
			dao.insertOrReplace(downloadVideoVo);
//			String jsonString = JSON.toJSONString(downloadVideoVo);
//			long n = setKeyToDB(this, downloadVideoVo.getColumn(), course_no, jsonString);
			map.remove(course_no);
			if (map.size() > 0)
				moveNext(map.getIndex(0).getCourse_no());
			return 1;
		}
		this.index++;
		if (FileUtils.isFileExists(FileUtils.getDownLoadVideoAbsoluteath(downloadVideoVo))) {
			moveNext(downloadVideoVo.getCourse_no());
		}
		downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

		// 假设从这一个链接下载一个大文件。
		Videos videoInfo = videos.get(index);
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(videoInfo.getPlayUrl()));

		// 仅允许在WIFI连接情况下下载
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

		// 通知栏中将出现的内容
		request.setTitle(downloadVideoVo.getTitle() + "" + videoInfo.getCourseItem_no());
		request.setDescription(downloadVideoVo.getVideo_description());
		// 下载过程和下载完成后通知栏有通知消息。
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

		// 此处可以由开发者自己指定一个文件存放下载文件。
		// 如果不指定则Android将使用系统默认的
		request.setDestinationUri(Uri.fromFile(FileUtils.getLocalFile()));
		String dirType = FileUtils.getDownLaodVideoPath(downloadVideoVo);


		File file = new File(dirType);
		if (!file.exists()) file.mkdirs();
		request.setDestinationInExternalPublicDir(dirType, videoInfo.getCourseItem_no());

		// enqueue 开始启动下载...
		Id = downloadManager.enqueue(request);
		queryTask(this.downloadVideoVo.getCallBack());
		return 1;
	}


	/**
	 * 存储到数据库
	 *
	 * @param context
	 * @param account 工号
	 * @param key     key
	 * @param value   json字符串
	 */
	private long setKeyToDB(Context context, String account, String key, String value) {
		WebViewDao mWebViewDao = new WebViewDao(context.getApplicationContext());

		return mWebViewDao.saveWebviewJson(account, key, value);

	}


	/**
	 *
	 */
	class SuperHashMap {
		HashMap<String, DownloadVideoVo> map;
		List<String> list;

		public SuperHashMap() {
			map = new HashMap<>();
			list = new ArrayList<>();
		}

		public int size() {
			return map.size();
		}

		public void put(String key, DownloadVideoVo videoVo) {
			map.put(key, videoVo);
			list.add(key);
		}

		public void remove(String key) {
			map.remove(key);
			list.remove(key);
		}

		public DownloadVideoVo getIndex(int index) {
			if (index >= map.size()) return null;
			return map.get(list.get(index));
		}

		public DownloadVideoVo get(String key) {

			return map.get(key);
		}

		public HashMap<String, DownloadVideoVo> getMap() {
			return map;
		}

		public void setMap(HashMap<String, DownloadVideoVo> map) {
			this.map = map;
		}

	}


	/**
	 * 广播接收器，接受ACTION_DOWNLOAD_COMPLETE和ACTION_NOTICATION_CLICKED
	 */
	class DownloadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				Log.e("测试", index + "--->" + intent.getData());
				if (index == -1)
					return;
				if (downloadVideoVo.getVideos().size() - 1 == index) {
					downloadVideoVo.setStatus(AppConfing.EXIST);

				} else {
					downloadVideoVo.setStatus(AppConfing.DOWNLOADING);
				}
				downloadVideoVo.getVideos().get(index).setDownload(true);
				DownloadVideoVoDao dao = GreenDBManager.getInstance().getVideoDao(context);
				dao.insertOrReplace(downloadVideoVo);

				handler.sendEmptyMessageDelayed(0, 2 * 1000);


			} else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {

//				Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();

			}

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null)
			unregisterReceiver(receiver);
	}

	/**
	 * 该视频是否已经下载
	 *
	 * @param courseNo
	 * @param courseItem_no
	 * @return
	 */
	private boolean getIsDownload(String courseNo, String courseItem_no) {
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
}
