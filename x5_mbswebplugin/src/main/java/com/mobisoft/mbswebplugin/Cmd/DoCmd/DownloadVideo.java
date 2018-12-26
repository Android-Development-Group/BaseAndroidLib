package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.mobisoft.mbswebplugin.BuildConfig;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Cmd.Working.DownloadVideoService;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVo;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVoDao;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.dao.db.WebViewDao;
import com.mobisoft.mbswebplugin.dao.greenDao.GreenDBManager;
import com.mobisoft.mbswebplugin.utils.FileUtils;
import com.mobisoft.mbswebplugin.utils.ToastUtil;

import java.io.File;

import cz.msebera.android.httpclient.Header;


/**
 * Author：Created by fan.xd on 2018/6/18.
 * Email：fang.xd@mobisoft.com.cn
 * Description：下载视频
 */
public class DownloadVideo extends DoCmdMethod {
	private Context activity;

	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		activity = context;

		DownloadVideoVo parse = JSON.parseObject(params, DownloadVideoVo.class);
		parse.setCallBack(callBack);
		parse.setStatus("downloading");
		DownloadVideoVoDao dao = GreenDBManager.getInstance().getVideoDao(context);
		dao.queryBuilder().where(DownloadVideoVoDao.Properties.Id.eq(parse.getId()));
		dao.insertOrReplace(parse);
//		setKeyToDB(context, parse.getColumn(), parse.getCourse_no(), params);
		Intent intent = new Intent(activity, DownloadVideoService.class);
		Bundle b = new Bundle();
		b.putSerializable("videoInfo", parse);
		intent.putExtras(b);
		intent.setAction(BuildConfig.APPLICATION_ID + "DownloadVideoService");
		activity.startService(intent);
//		try {
//			JSONObject jsonObject = new JSONObject(params);
//			String title = jsonObject.optString("title");//视频标题
//			String video_description = jsonObject.optString("video_description");//视频介绍
//			String coverForFeed = jsonObject.optString("coverForFeed");
//			String course_no = jsonObject.optString("course_no");//课程编号
//			JSONArray jsonArray = jsonObject.optJSONArray("videos");// 视频列表
////			for (int i = 0; i < jsonArray.length(); i++) {
////				JSONObject object = jsonArray.optJSONObject(0);
////				String playUrl = object.optString("playUrl");//视频播放地址
////				String courseItem_no = object.optString("courseItem_no");// 课程子编号
////
////				downLoadVideo(title, video_description, coverForFeed, course_no, playUrl, courseItem_no);
////
////			}
//			JSONObject object = jsonArray.optJSONObject(0);
//			String playUrl = object.optString("playUrl");//视频播放地址
//			String courseItem_no = object.optString("courseItem_no");// 课程子编号
//
//
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}


		return null;
	}

	/**
	 * 获取数据库的数据
	 *
	 * @param context
	 * @return
	 */
	private DownloadVideoVoDao getVideoDao(Context context) {
		return GreenDBManager.getInstance().onInit(context).daoSession.getDownloadVideoVoDao();
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
	 * @param title
	 * @param video_description
	 * @param coverForFeed
	 * @param course_no
	 * @param playUrl
	 * @param courseItem_no
	 */
	private void downLoadVideo(String title, String video_description, String coverForFeed, String course_no, String playUrl, final String courseItem_no) {

		AsyncHttpClient client = new AsyncHttpClient();
		client.setResponseTimeout(100 * 1000);
		File file = new File(FileUtils.getLocalFile(), courseItem_no);

		playUrl = "http://v1-dy.ixigua.com/fc988b931f7e97a42764d412cb282601/5b27656b/video/m/2209d5772f3792e4a578f4e8c4098722e3911583a6700004ea8d149cccc/";
		if (playUrl.contains("https")) {
			client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
		}
		client.get(playUrl, new FileAsyncHttpResponseHandler(file) {
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
				Log.e("DownloadZipTool", "statusCode: " + statusCode);
				Log.e("DownloadZipTool", "throwable: " + throwable.getMessage());
				// 下载成功后需要做的工作
				ToastUtil.showShortToast(activity, "文件下载失败:" + statusCode + " " + throwable.getMessage());
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, File file) {
				if (statusCode == 200) {
					Log.i("DownloadZipTool", "文件下载路径：" + file.getAbsolutePath() + "\n 名称：" + file.getName() + "\n 大小：" + file.length() / 1024 + " KB");
//					onUpdateComplete(file);
				} else {
					ToastUtil.showShortToast(activity, "文件下载失败:" + statusCode);
//					Log.i("DownloadZipTool", "文件下载失败：" + file.getAbsolutePath() + "\n 名称：" + file.getName() + "\n 大小：" + file.length() / 1024 + " KB");
				}
				// 下载成功后需要做的工作
			}

			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				super.onProgress(bytesWritten, totalSize);
				int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
				// 下载进度显示
				ToastUtil.showShortToast(activity, "文件下载中-->>:" + count);

				Log.e("DownloadZipTool", courseItem_no + " " + bytesWritten + " / " + totalSize + "=" + count + "%");
			}
		});
	}
}
