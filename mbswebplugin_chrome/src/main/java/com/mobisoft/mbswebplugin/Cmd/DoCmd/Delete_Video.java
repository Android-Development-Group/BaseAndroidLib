package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Entity.CallBackResult;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVo;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVoDao;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.dao.greenDao.GreenDBManager;
import com.mobisoft.mbswebplugin.utils.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Author：Created by fan.xd on 2018/8/10.
 * Email：fang.xd@mobisoft.com.cn
 * Description：删除课程
 */
public class Delete_Video extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

		DownloadVideoVo parse = JSON.parseObject(params, DownloadVideoVo.class);
		parse.setCallBack(callBack);
		parse.setStatus("downloading");
		DownloadVideoVoDao dao = GreenDBManager.getInstance().getVideoDao(context);
		List<DownloadVideoVo> back = dao.queryBuilder().where(DownloadVideoVoDao.Properties.Course_no.eq(parse.getCourse_no())
		)
				.orderAsc(DownloadVideoVoDao.Properties.Id)
				.list();
		CallBackResult<DownloadVideoVo> backResult = new CallBackResult<>();

		if (back != null && back.size() >= 1) {
			DownloadVideoVo vo = back.get(0);
			String downLaodVideoPath = FileUtils.getDownLaodVideoPath(vo);
			FileUtils.deleteFile(new File(downLaodVideoPath));
			dao.delete(vo);
			backResult.setMsg(vo.getCourse_no() + " 删除成功！");
			backResult.setResult(true);
			backResult.setData(vo);
			view.loadCallback(callBack, backResult);
		} else {
			backResult.setResult(false);
			backResult.setData(parse);
			backResult.setMsg(parse.getCourse_no() + " 删除失败！本地没有此课程的相关数据");
			view.loadCallback(callBack, backResult);
		}

		return null;
	}
}
