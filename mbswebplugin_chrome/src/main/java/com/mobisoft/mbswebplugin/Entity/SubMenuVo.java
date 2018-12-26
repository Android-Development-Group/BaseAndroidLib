package com.mobisoft.mbswebplugin.Entity;

/**
 * Author：Created by fan.xd on 2018/6/13.
 * Email：fang.xd@mobisoft.com.cn
 * Description：二级菜单
 */
public class SubMenuVo {
	/**
	 * 副标题
	 */
	private String subTitle;
	/**
	 * 跳转连接地址
	 */
	private String subLink;
	/**
	 * icon
	 */
	private String icon;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 回掉方法
	 */
	private String callBack;



	public SubMenuVo() {
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
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
