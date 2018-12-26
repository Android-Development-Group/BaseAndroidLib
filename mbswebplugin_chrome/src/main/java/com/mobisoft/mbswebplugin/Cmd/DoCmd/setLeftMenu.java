package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Entity.Item;
import com.mobisoft.mbswebplugin.Entity.LeftMenu;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.MvpMbsWeb.MbsWebFragment;

import java.util.List;

/**
 * Author：Created by fan.xd on 2018/6/14.
 * Email：fang.xd@mobisoft.com.cn
 * Description：设置左上角菜单
 */
public class setLeftMenu extends DoCmdMethod {
	@Override
	public String doMethod(HybridWebView webView, Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		LeftMenu leftMenu = JSON.parseObject(params, LeftMenu.class);
		List<Item> item = leftMenu.getItem();
		if (item.size() == 1) {
//			if(!TextUtils.isEmpty(leftMenu.getType())){//默认为finsh当前页面，返回，
//				view.setLeftMenu(params,item,leftMenu.getType(),leftMenu.getData());
//			}else {
//
//			}
		} else if (item.size() > 1) {

		}
		if (TextUtils.isEmpty(leftMenu.getType())) {//默认为finsh当前页面，返回，
			view.setLeftMenu(params, item, leftMenu.getType(), leftMenu.getData());
			view.showLeftMenu(MbsWebFragment.BACK_IMAGE_BTN, 0, item.get(0).getIcon(), null);

		} else if (TextUtils.equals(leftMenu.getType(), "vertical")) {//垂直布局

		} else if (TextUtils.equals(leftMenu.getType(), "slideBar")) {//侧边栏
			view.setLeftMenu(params, item, leftMenu.getType(), leftMenu.getData());
			view.showLeftMenu(MbsWebFragment.BACK_IMAGE_BTN, 0, item.get(0).getIcon(), null);
		} else if (TextUtils.equals(leftMenu.getType(), "horizontal")) {//水平布局

		} else {
			view.setLeftMenu(params, item, leftMenu.getType(), leftMenu.getData());
			view.showLeftMenu(MbsWebFragment.BACK_IMAGE_BTN, 0, item.get(0).getIcon(), null);
		}

		return null;
	}
}
