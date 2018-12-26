package com.mobisoft.mbswebplugin.utils;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * activity 集合
 *
 * @author Li Yong
 * 2016年6月16日 上午22:15:03
 * @version V1.0
 * activity控制器
 */
public class ActivityCollector {

	public static List<Activity> activities = new ArrayList<Activity>();
	/**
	 * 存放activity的列表
	 */
	public static HashMap<Class<?>, Activity> hashMapAc = new LinkedHashMap<>();
	public static HashMap<Integer, Activity> hashMapAc2 = new LinkedHashMap<>();
	private static boolean isFinish = false;

	/**
	 * 添加Activity
	 *
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		activities.add(activity);
		hashMapAc.put(activity.getClass(), activity);
		hashMapAc2.put(activities.indexOf(activity),activity);
		Log.i("LLL", "activities= size = " + activities.size());
	}

	/**
	 * 移除Activity
	 *
	 * @param activity
	 */
	public static void removeActivity(Activity activity) {
		if (activities.contains(activity)) {
			hashMapAc2.remove(activity);
			activities.remove(activity);
			hashMapAc.remove(activity.getClass());
		}

	}

	/**
	 * isActivityExist 是否存在
	 *
	 * @param activity
	 */
	public static boolean isActivityExist(Class activity) {
		return hashMapAc.containsKey(activity);

	}

	/**
	 * 移除Activity
	 *
	 * @param index
	 */
//    public static void clearTask(int index){
//        for (int i = 0; i <activities.size() ; i++) {
//            for (int j = 1; j <= index; j++) {
//                if(i==j){
//                    activities.get(activities.size()-1-i).finish();
//                    activities.remove(activities.size()-1-i);
//                }
//            }
//        }
//    }
	public static void clearTask(int index) {
		List<Activity> activities = ActivityCollector.activities;
		if (index > activities.size() && activities.size() > 1) {
			for (int i = 1; i < activities.size() - 1; i++) {
				Class aClass = activities.get(i).getClass();
				((Activity) activities.get(i)).finish();
				activities.remove(i);
				hashMapAc.remove(aClass);
				hashMapAc2.remove(i);

				i--;
			}
		} else {
			for (int i = 0; i < activities.size(); ++i) {
				for (int j = 1; j <= index; ++j) {
					int size = activities.size();
					if (i == j) {
						Class aClass = activities.get(i).getClass();
						((Activity) activities.get(size - 1 - i)).finish();
						activities.remove(size - 1 - i);
						hashMapAc.remove(aClass);
						hashMapAc2.remove(i);
						break;
					}
				}
			}
		}
	}

	/**
	 * 销毁全被Activity
	 */
	public static void finishAll() {

		if (!isFinish) {
			isFinish = true;
			for (Activity activity : activities) {
				if (!activity.isFinishing()) {
					hashMapAc.remove(activity.getClass());
					hashMapAc2.remove(activity);
					activity.finish();
				}
			}
			activities.clear();
			isFinish = false;
		}


		Log.e("finishAll", "控制 销毁@！");
//        System.exit(0);
	}

	/**
	 * 销毁全被Activity
	 */
	public static void finishOther() {
		if (!isFinish) {
			isFinish = true;
			for (Activity activity : activities) {
				if (!activity.isFinishing()) {
					if (hashMapAc2.size() == 1) {
						activities.clear();
						activities.add(0,activity);
						isFinish = false;
						return;
					} else {
						activity.finish();
					}

				}
			}

			isFinish = false;
		}
	}

	/**
	 * 销毁Activity,除了第一个
	 */
	public static void goHomePage() {
		Activity homeActivity = activities.get(0);
		Log.i("LLL", "activities= size = " + activities.size());
		activities.remove(0);
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
		activities.clear();
		activities.add(homeActivity);
//        System.exit(0);
	}


}  