package com.mobisoft.mbswebplugin.Entity;

/**
 * Author：Created by fan.xd on 2018/6/14.
 * Email：fang.xd@mobisoft.com.cn
 * Description：左上角菜单
 * */
public class Item {
	private String name;

	private String icon;

	private String url;

	private String callback;

	/**是否显示红点**/
	private boolean redPoint;

	public boolean isRedPoint() {
		return redPoint;
	}

	public void setRedPoint(boolean redPoint) {
		this.redPoint = redPoint;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getCallback() {
		return this.callback;
	}
}
