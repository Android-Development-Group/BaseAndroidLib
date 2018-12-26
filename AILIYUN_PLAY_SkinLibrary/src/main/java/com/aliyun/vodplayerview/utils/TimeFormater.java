package com.aliyun.vodplayerview.utils;
/*
 * Copyright (C) 2010-2018 Alibaba Group Holding Limited.
 */

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 时间格式化工具类
 */
public class TimeFormater {

	/**
	 * 格式化毫秒数为 xx:xx:xx这样的时间格式。
	 *
	 * @param ms 毫秒数
	 * @return 格式化后的字符串
	 */
	public static String formatMs(long ms) {
		int seconds = (int) (ms / 1000);
		int finalSec = seconds % 60;
		int finalMin = seconds / 60 % 60;
		int finalHour = seconds / 3600;

		StringBuilder msBuilder = new StringBuilder("");
		if (finalHour > 9) {
			msBuilder.append(finalHour).append(":");
		} else if (finalHour > 0) {
			msBuilder.append("0").append(finalHour).append(":");
		}

		if (finalMin > 9) {
			msBuilder.append(finalMin).append(":");
		} else if (finalMin > 0) {
			msBuilder.append("0").append(finalMin).append(":");
		} else {
			msBuilder.append("00").append(":");
		}

		if (finalSec > 9) {
			msBuilder.append(finalSec);
		} else if (finalSec > 0) {
			msBuilder.append("0").append(finalSec);
		} else {
			msBuilder.append("00");
		}

		return msBuilder.toString();
	}

	public static String formatFloat(double value) {

		try {
			Log.e("value",value+" :  value");
			if(TextUtils.equals("NaN",value+"")
					||TextUtils.equals("Infinity",value+"")){
				return "0.000";
			}
			BigDecimal decimal = new BigDecimal(String.valueOf(value));
			DecimalFormat format = new DecimalFormat("0.000");
			format.setRoundingMode(RoundingMode.HALF_UP);
			return format.format(decimal.doubleValue());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("NaN",value+" :  NaN");
			return "0.000";
		}
	}
}
