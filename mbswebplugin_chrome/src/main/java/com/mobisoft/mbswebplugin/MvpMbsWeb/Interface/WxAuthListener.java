package com.mobisoft.mbswebplugin.MvpMbsWeb.Interface;

/**
 * Author：Created by fan.xd on 2018/6/26.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */
public interface WxAuthListener {

	void onComplete(int var2, String code);

	void onError(int var2, Throwable var3);
}
