package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Entity.CallBackResult;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVo;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVoDao;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.base.AppConfing;
import com.mobisoft.mbswebplugin.dao.greenDao.GreenDBManager;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONObject;

import java.util.List;

/**
 * Author：Created by fan.xd on 2018/6/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description：获取已经下载的本地视频
 */
public class GetCourseVideo extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		try {
			JSONObject jsonObject = new JSONObject(params);
//			String course_no = jsonObject.optString("course_no");
//			String courseItem_no = jsonObject.optString("courseItem_no");
			String column = jsonObject.optString("column");
			List<DownloadVideoVo> videoVo = getValueFromDB(context, column);
			CallBackResult<List<DownloadVideoVo>> backResult = new CallBackResult<>();
			backResult.setData(videoVo);
			String jsonString = JSON.toJSONString(backResult);
			view.loadCallback(callBack, jsonString);
//			if (videoVo != null) {
//				String jsonString = JSON.toJSONString(videoVo);
//				view.loadCallback(callBack, jsonString);
//			} else {
//				String jsonString = JSON.toJSONString(new ArrayList<DownloadVideoVo>());
//				view.loadCallback(callBack, jsonString);
//			}

		} catch (Exception e) {
			e.printStackTrace();
			CallBackResult<List<DownloadVideoVo>> backResult = new CallBackResult<>();
			backResult.setData(null);
			backResult.setMsg(e.getMessage());
			String jsonString = JSON.toJSONString(backResult);
			view.loadCallback(callBack, jsonString);
		}
		return null;
	}


	/**
	 * 根据key 从数据库得到value
	 *
	 * @param key 关键字
	 * @return 根据acoutn 和 key查询据库的数据
	 */
	/**
	 * 根据key 从数据库得到value
	 *
	 * @param column 栏目
	 * @return 根据acoutn 和 key查询据库的数据
	 */
	protected List<DownloadVideoVo> getValueFromDB(Context context, String column) {

		DownloadVideoVoDao dao = GreenDBManager.getInstance().getVideoDao(context);
		QueryBuilder qb = dao.queryBuilder();
		List<DownloadVideoVo> back;
		if (TextUtils.isEmpty(column)) {
			back = (List<DownloadVideoVo>) qb.where(
					DownloadVideoVoDao.Properties.Status.notEq(AppConfing.NOTEXIST))
					.list();
		} else {
			back = (List<DownloadVideoVo>) qb.where(
					DownloadVideoVoDao.Properties.Status.notEq(AppConfing.NOTEXIST),
					DownloadVideoVoDao.Properties.Column.eq(column))
					.list();

		}
		return back;
	}


}
