package com.mobisoft.mbswebplugin.MvpMbsWeb.Interface;

import com.mobisoft.mbswebplugin.Entity.Data;

import java.util.List;

/**
 * Author：Created by fan.xd on 2018/6/14.
 * Email：fang.xd@mobisoft.com.cn
 * Description：设置策划菜单的监听
 */
public interface DrawerLayoutListener {

	void setDrawerLayoutData(List<Data> layoutData);

	void setDrawerLayoutMenuOnClick();
}
