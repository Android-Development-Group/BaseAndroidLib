package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.mobisoft.mbswebplugin.BuildConfig;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Cmd.Working.DownloadVideoService;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVo;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVoDao;
import com.mobisoft.mbswebplugin.Entity.Videos;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.base.AppConfing;
import com.mobisoft.mbswebplugin.dao.greenDao.GreenDBManager;
import com.mobisoft.mbswebplugin.utils.FileUtils;
import com.mobisoft.mbswebplugin.utils.UrlUtil;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static com.mobisoft.mbswebplugin.base.AppConfing.NOTEXIST;

/**
 * Author：Created by fan.xd on 2018/6/19.
 * Email：fang.xd@mobisoft.com.cn
 * Description：检查视频状态
 * 命令文档：1.29.检查视频状态
 * 参数	名称	类型	备注
 * 命令	cmd	字符串	checkVideo
 * 视频主键	course_no	字符串
 * 返回参数
 * status	字符串	notexist  不存在  downloading 正在下载 exist 已下载
 */
public class CheckVideo extends DoCmdMethod {


	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		try {
			JSONObject object = new JSONObject(params);
			String course_no = object.optString("course_no");
			String column = object.optString("column");

			DownloadVideoVo videoVo = getValueFromDB(context, TextUtils.isEmpty(column) ? "column" : column, course_no);
			if (videoVo == null) {
				videoVo = new DownloadVideoVo();
				videoVo.setStatus(NOTEXIST);
				videoVo.setCourse_no(course_no);
			} else {
				List<Videos> listVideo = videoVo.getVideos();

				for (int i = 0; i < listVideo.size(); i++) {
					String courseItem_no = listVideo.get(i).getCourseItem_no();
					String dirType = FileUtils.getDownLoadVideoAbsoluteath(videoVo);
					File file = new File(dirType);
					if (file.exists() && listVideo.get(i).isDownload()) {
//						listVideo.get(i).setIsDownload("true");
						if (TextUtils.equals(videoVo.getStatus(), NOTEXIST) || TextUtils.equals(videoVo.getStatus(), "downloading"))
							videoVo.setStatus(AppConfing.DOWNLOADING);
						else
							videoVo.setStatus(AppConfing.EXIST);
					} else {
//						listVideo.get(i).setIsDownload("false");
						if (i == 0) videoVo.setStatus(NOTEXIST);
						else if (TextUtils.equals(videoVo.getStatus(), AppConfing.EXIST) || TextUtils.equals(videoVo.getStatus(), "downloading"))
							videoVo.setStatus("downloading");
						else if (TextUtils.equals(videoVo.getStatus(), NOTEXIST))
							videoVo.setStatus(NOTEXIST);
					}

				}
				if (!TextUtils.equals(videoVo.getStatus(), AppConfing.EXIST)) {
					Intent intent = new Intent(context, DownloadVideoService.class);
					Bundle b = new Bundle();
					b.putSerializable("videoInfo", videoVo);
					intent.putExtras(b);
					intent.setAction(BuildConfig.APPLICATION_ID + "DownloadVideoService");
					context.startService(intent);
				}
				String url = UrlUtil.getFormatJs(callBack, JSON.toJSONString(videoVo));
				webView.loadUrl(url);
			}


		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 根据key 从数据库得到value
	 *
	 * @param column 栏目
	 * @param key    关键字
	 * @return 根据acoutn 和 key查询据库的数据
	 */
	protected DownloadVideoVo getValueFromDB(Context context, String column, String key) {
		DownloadVideoVoDao dao = GreenDBManager.getInstance().getVideoDao(context);
		QueryBuilder qb = dao.queryBuilder();
		List<DownloadVideoVo> back = (List<DownloadVideoVo>) qb.where(DownloadVideoVoDao.Properties.Course_no.eq(key)
		)
				.orderAsc(DownloadVideoVoDao.Properties.Id)
				.list();
		if (back != null && back.size() >= 1) {
			return back.get(0);
		}
		return null;
	}
}
