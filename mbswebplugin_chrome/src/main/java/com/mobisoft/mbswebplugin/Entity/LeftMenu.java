package com.mobisoft.mbswebplugin.Entity;

import java.util.List;

/**
 * Author：Created by fan.xd on 2018/6/14.
 * Email：fang.xd@mobisoft.com.cn
 * Description：左上角菜单
 */
public class LeftMenu {
		private List<Item> item;

		private String type;

		private List<Data> data;

		public void setItem(List<Item> item){
			this.item = item;
		}
		public List<Item> getItem(){
			return this.item;
		}
		public void setType(String type){
			this.type = type;
		}
		public String getType(){
			return this.type;
		}
		public void setData(List<Data> data){
			this.data = data;
		}
		public List<Data> getData(){
			return this.data;
		}

}