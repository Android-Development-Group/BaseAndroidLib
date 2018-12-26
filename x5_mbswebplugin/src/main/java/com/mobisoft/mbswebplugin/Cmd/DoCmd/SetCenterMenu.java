package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.mobisoft.mbswebplugin.Cmd.CMD;
import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Entity.CoustomeMenu;
import com.mobisoft.mbswebplugin.Entity.Item;
import com.mobisoft.mbswebplugin.Entity.TabEntity;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;
import com.mobisoft.mbswebplugin.utils.ColorUtils;
import com.mobisoft.mbswebplugin.utils.UrlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Created by fan.xd on 2018/6/21.
 * Email：fang.xd@mobisoft.com.cn
 * Description：设置头布局table选项卡
 */
public class SetCenterMenu extends DoCmdMethod {

	@Override
	public String doMethod(HybridWebView webView, Context context, final MbsWebPluginContract.View view, final MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
		CoustomeMenu menu = JSON.parseObject(params, CoustomeMenu.class);
		final List<Item> items = menu.getItem();
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			mTabEntities.add(new TabEntity(item.getName(), item.getIcon(), item.getUrl(), item.getCallback()));
		}
		CommonTabLayout commonTabLayout = view.setCenterMenu();
		commonTabLayout.setTabData(mTabEntities);
		String selectColor = menu.getSelectedColor();
		if (!TextUtils.isEmpty(selectColor))
			commonTabLayout.setTextSelectColor(ColorUtils.getArgb(selectColor));
		String unSelectColor = menu.getUnSelectedColor();
		if (!TextUtils.isEmpty(unSelectColor))
			commonTabLayout.setTextUnselectColor(ColorUtils.getArgb(unSelectColor));


		commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
			@Override
			public void onTabSelect(int position) {
//                switchFragment(position);
				if (!TextUtils.isEmpty(items.get(position).getCallback())) {
					view.loadUrl(UrlUtil.getFormatJs(items.get(position).getCallback(), position + ""));

				} else if (!TextUtils.isEmpty(items.get(position).getUrl())) {
					if (items.get(position).getUrl().startsWith("http")) {
						presenter.nextPage(items.get(position).getUrl(), CMD.action_nextPage);
					} else {
						presenter.nextPage(ProxyConfig.getConfig().getBaseUrl() + items.get(position).getUrl(), CMD.action_nextPage);
					}
				}
			}

			@Override
			public void onTabReselect(int position) {

			}
		});

		return null;
	}


}
