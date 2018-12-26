package com.mobisoft.mbswebplugin.Entity;

import java.util.List;

/**
 * Author：Created by fan.xd on 2018/6/21.
 * Email：fang.xd@mobisoft.com.cn
 * Description：通用菜单
 */
public class CoustomeMenu {

	List<Item> item;
	/**
	 * 选中颜色
	 */
	String selectedColor;
	/**
	 * 未选中颜色
	 */
	String unSelectedColor;

	public List<Item> getItem() {
		return item;
	}

	public void setItem(List<Item> item) {
		this.item = item;
	}

	public String getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(String selectedColor) {
		this.selectedColor = selectedColor;
	}

	public String getUnSelectedColor() {
		return unSelectedColor;
	}

	public void setUnSelectedColor(String unSelectedColor) {
		this.unSelectedColor = unSelectedColor;
	}
}
