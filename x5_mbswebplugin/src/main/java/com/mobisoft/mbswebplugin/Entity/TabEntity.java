package com.mobisoft.mbswebplugin.Entity;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Author：Created by fan.xd on 2018/6/21.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */
public class TabEntity implements CustomTabEntity {

	public String title;
	public int selectedIcon;
	public int unSelectedIcon;
	private String name;

	private String icon;

	private String url;

	private String callback;

	public TabEntity(String title, int selectedIcon, int unSelectedIcon, String name, String icon, String url, String callback) {
		this.title = title;
		this.selectedIcon = selectedIcon;
		this.unSelectedIcon = unSelectedIcon;
		this.name = name;
		this.icon = icon;
		this.url = url;
		this.callback = callback;
	}

	public TabEntity(String title) {
		this.title = title;
	}

	public TabEntity(String title, String icon, String url, String callback) {
		this.title = title;
		this.icon = icon;
		this.url = url;
		this.callback = callback;
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return icon;
	}

	public String getUrl() {
		return url;
	}

	public String getCallback() {
		return callback;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTabTitle() {
		return title;
	}

	@Override
	public int getTabSelectedIcon() {
		return selectedIcon;
	}

	@Override
	public int getTabUnselectedIcon() {
		return unSelectedIcon;
	}
}
