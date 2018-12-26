package com.mobisoft.mbswebplugin.Entity;

import java.util.List;

/**
 * Author：Created by fan.xd on 2018/6/13.
 * Email：fang.xd@mobisoft.com.cn
 * Description：抽屉菜单栏位的
 */
public class Data {
	private String title;
	private List<SubMenuVo> children;
	private String subLink;
	private String icon;
	private String type;
	private String callBack;

	public Data() {
	}




	public List<SubMenuVo> getChildren() {
		return children;
	}

	public void setChildren(List<SubMenuVo> children) {
		this.children = children;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCallBack() {
		return callBack;
	}

	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}

	public String getSubLink() {

		return subLink;
	}

	public void setSubLink(String subLink) {
		this.subLink = subLink;
	}
}
