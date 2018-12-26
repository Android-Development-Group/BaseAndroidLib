package com.mobisoft.mbswebplugin.dao.greenDao;

import android.content.Context;

import com.mobisoft.mbswebplugin.Entity.DaoMaster;
import com.mobisoft.mbswebplugin.Entity.DaoSession;
import com.mobisoft.mbswebplugin.Entity.DownloadVideoVoDao;

import org.greenrobot.greendao.database.Database;

/**
 * Author：Created by fan.xd on 2018/6/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description：GreenDbManger
 */
public class GreenDBManager {
	private static GreenDBManager dbManager;
	public DaoSession daoSession;

	public static synchronized GreenDBManager getInstance() {
		if (dbManager == null) {
			dbManager = new GreenDBManager();
		}
		return dbManager;
	}

	 public GreenDBManager onInit(Context context) {
		if (daoSession == null) {
			DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), "video_db", null);
			Database db = helper.getWritableDb();
			DaoMaster daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();
		}

		return dbManager;
	}

	/**
	 * 获取数据库的数据
	 *
	 * @param context
	 * @return
	 */
	public DownloadVideoVoDao getVideoDao(Context context) {
		return onInit(context).daoSession.getDownloadVideoVoDao();
	}
}
