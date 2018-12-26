package com.mobisoft.mbswebplugin.utils;

import android.graphics.Color;
import android.text.TextUtils;

/**
 * Author：Created by fan.xd on 2018/6/27.
 * Email：fang.xd@mobisoft.com.cn
 * Description： Parse the color string, and return the corresponding color-int.
 */
public class ColorUtils {

	/**
	 * 获取颜色
	 * Parse the color string, and return the corresponding color-int.
	 *
	 * @param colors argb颜色的数组{255,255,255,0.6}
	 * @return corresponding color-int
	 */
	public static int getArgb(String colors) {

		if (TextUtils.isEmpty(colors)) {
			return 0;
		} else {
			String[] c = colors.split(",");
			if (c.length == 1) {
				return Color.parseColor(c[0]);
			} else if (c.length == 3) {
				return getRgb(c);
			} else if (c.length == 4) {
				return getArgb(c);
			} else {
				return Color.parseColor(colors);
			}
		}
	}


	/**
	 * 获取Argb颜色
	 *
	 * @param uns
	 * @return
	 */
	private static int getArgb(String[] uns) {
		Float aFloat = Float.valueOf(uns[3]);
		if (aFloat <= 1)
			return Color.argb((int) (aFloat * 255), Integer.valueOf(uns[0]), Integer.valueOf(uns[1]), Integer.valueOf(uns[2]));
		else {
			int a = Integer.valueOf(uns[3]);
			return Color.argb(a, Integer.valueOf(uns[0]), Integer.valueOf(uns[1]), Integer.valueOf(uns[2]));
		}

	}

	/**
	 * 获取RGb颜色
	 *
	 * @param uns
	 * @return
	 */
	private static int getRgb(String[] uns) {
		return Color.rgb(Integer.valueOf(uns[0]), Integer.valueOf(uns[1]), Integer.valueOf(uns[2]));
	}
}
