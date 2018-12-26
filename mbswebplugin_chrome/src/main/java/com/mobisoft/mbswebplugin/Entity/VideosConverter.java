package com.mobisoft.mbswebplugin.Entity;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * Author：Created by fan.xd on 2018/6/28.
 * Email：fang.xd@mobisoft.com.cn
 * Description： greenDao 转换 视频列表
 */
public class VideosConverter implements PropertyConverter<List<Videos>, String> {
	@Override
	public List<Videos> convertToEntityProperty(String databaseValue) {
		if (TextUtils.isEmpty(databaseValue)) {
			return null;
		} else {
			List<Videos> videos = JSON.parseArray(databaseValue, Videos.class);
			return videos;
		}
	}

	@Override
	public String convertToDatabaseValue(List<Videos> entityProperty) {
		if (entityProperty == null) {
			return null;
		} else {
			String json = JSON.toJSONString(entityProperty);
			return json;
		}
	}
}
