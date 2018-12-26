package com.mobisoft.mbswebplugin.MvpMbsWeb.Interface;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.widget.ImageView;

import com.flyco.tablayout.CommonTabLayout;
import com.mobisoft.mbswebplugin.Entity.CallBackResult;
import com.mobisoft.mbswebplugin.Entity.Data;
import com.mobisoft.mbswebplugin.Entity.Item;
import com.mobisoft.mbswebplugin.Entity.MeunItem;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.view.serach.SearchListener;

import java.util.List;
import java.util.Map;

/**
 * Author：Created by fan.xd on 2017/3/2.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public interface MbsWebPluginContract {

	interface View extends BaseView<Presenter> {
		/**
		 * 禁止刷新
		 */
		void forbiddenRefresh(boolean i);

		/**
		 * 获取webview
		 */
		HybridWebView getWebView();

		/**
		 * 加载地址
		 *
		 * @param url 加载地址
		 */
		void loadUrl(String url);

		/**
		 * 重新加载页面
		 */
		void reload();

		/**
		 * 重新加载页面
		 */
		void reloadApp();

		/**
		 * 获取主页地址
		 *
		 * @return getUrl
		 */
		String getUrl();

		/**
		 * 下一页
		 *
		 * @param url    url
		 * @param action action
		 */
		void openNextWebActivity(String url, String action);

		/**
		 * 返回键
		 *
		 * @param keyCode 返回键
		 * @param event   返回键
		 * @return true or false
		 */
		boolean onKeyDown(int keyCode, KeyEvent event);

		/**
		 * 隐藏标题栏
		 */
		void hideTitle();

		/**
		 * 头部菜单点击事件
		 *
		 * @param list     菜单列表
		 * @param position 点击的item的下标
		 */
		void TopMenuClick(List<MeunItem> list, int position);

		/**
		 * 设置title菜单
		 */
		void setTitleMenu();

		/**
		 * 设置标题栏背景色
		 */
		void setTitleBg(String color);

		/**
		 * 设置标题字体颜色色
		 */
		void setTitleColor(String color);


		/**
		 * 状态栏 状态
		 *
		 * @param statusBar
		 */
		void setStatusBar(String statusBar);

		/**
		 * 获取 是否清除过任务栈
		 *
		 * @return getIsClearTask true：已经清理过
		 */
		boolean getIsClearTask();

		/**
		 * 设置是否清除过任务站
		 *
		 * @param b true ：清理过一次
		 * @return
		 */
		void setIsClearTask(boolean b);

		/**
		 * 设置右上角菜单
		 *
		 * @param json js返回的Menu数据
		 */
		void setTopAndRightMenu(String json);

		/**
		 * 设置顶部菜单
		 */
		void setTopRightMenuList();

		/**
		 * 是否显示 右上角菜单
		 *
		 * @param isShow
		 */
		void setRightMenuText(boolean isShow);

		/**
		 * 显示进度条
		 *
		 * @param action
		 * @param message
		 */
		void showHud(String action, String message,String type);

		/**
		 * 隐藏进度条
		 */
		void hideHud();

		/**
		 * 设置标题
		 *
		 * @param type  标题类相关 0：获取h5中的title
		 *              1：获取h5 命令中的title
		 *              default： 默认设置 h5中自带的title
		 * @param title
		 */
		void setTitle(int type, String title);

		/**
		 * 设置toolbar的返回图标
		 *
		 * @param resId
		 */
		void setNavigationIcon(int resId);

		/**
		 * 显示输入框
		 *
		 * @param param
		 * @param callBack
		 */
		void showInputWindow(String param, String callBack);

		/**
		 * 点击返回按钮  拦截 事件
		 *
		 * @param event
		 */
		void setBackEvent(String event);

		/**
		 * 局部刷新
		 *
		 * @param isRefreshInitPage
		 */
		void setRefreshStyle(boolean isRefreshInitPage);

		/**
		 * 设置是否需要关闭当前页面
		 * 默认 true 允许关闭
		 * fasle：点击返回不能关闭页面
		 *
		 * @param isNeedClose
		 */
		void setNeedClose(boolean isNeedClose);


		/**
		 * 设置又上角菜单
		 *
		 * @param param json
		 * @param item  是右上角菜单数据
		 * @param type  类型
		 * @param data
		 */
		void setLeftMenu(String param, List<Item> item, String type, List<Data> data);

		/**
		 * 隐藏返回按钮的布局
		 *
		 * @param type
		 */
		void hideLeftMenu(int type);

		/**
		 * 显示返回按钮的布局
		 *
		 * @param type
		 */
		void showLeftMenu(int type, int imageResource, String imageUrl, String text);

		/**
		 * 设置图片显示
		 *
		 * @param view
		 * @param url
		 * @param imageResource
		 */
		void setImageWithImageView(ImageView view, String url, int imageResource);

		/**
		 * 设置播放视频
		 *
		 * @param videoUrl 视频地址
		 * @param imageURL 视频的占位图地址
		 */
		CallBackResult setPalyVideoView(String videoUrl, String imageURL, String type, String[] params);

		/**
		 * 视频开始播放
		 */
		void startBGVideo();

		/**
		 * 视频停止播放
		 */
		void stopBGVideo();

		/**
		 * 视频暂停播放
		 */
		void pauseBGVideo();

		/**
		 * 设置视频进度
		 */
		void videoProgress();

		/**
		 * 视频设置全屏
		 *
		 * @param videoUrl
		 * @param coverUrl
		 */
		void setFullVideoBG(String videoUrl, String coverUrl);

		/**
		 * table_layout
		 */
		CommonTabLayout setCenterMenu();

		/**
		 * 加载地址
		 *
		 * @param param    参数
		 * @param callback 回掉方法
		 */
		void loadCallback(String callback, String param);

		/**
		 * 加载地址
		 *
		 * @param param    参数
		 * @param callback 回掉方法
		 */
		void loadCallback(String callback, CallBackResult param);

		/**
		 * 设置
		 *
		 * @param placeholder
		 */
		void setSearchBar(String placeholder, SearchListener searchListener);

		/**
		 * 更改屏幕
		 *
		 * @return
		 */
		void ChangeScreenMode(String action);

		/**
		 * 更改屏幕
		 *
		 * @return
		 */
		void AllowScreenMode(OrientationEventListener listener);
	}

	interface Presenter extends BasePresenter {
		/**
		 * onResume
		 */
		void onResume();


		/**
		 * 进入页面
		 */
		void onCreate();

		/**
		 * activity 结束调用
		 */
		void finish();

		/**
		 * 调用此方法结束activity
		 */
		void finishActivity();

		/**
		 * 页面结束
		 */
		void onDestroy();

		/**
		 * 申请权限 回掉
		 *
		 * @param requestCode  requestCode
		 * @param permissions  permissions
		 * @param grantResults grantResults
		 */
		void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

		/**
		 * onActivityResult 见名思意不多讲
		 *
		 * @param requestCode requestCode
		 * @param resultCode  resultCode
		 * @param data        传递数据intent
		 */
		void onActivityResult(int requestCode, int resultCode, Intent data);

		/**
		 * 返回键
		 *
		 * @param keyCode keyCode
		 * @param event   event
		 * @return
		 */
		boolean onKeyDown(int keyCode, KeyEvent event);

		/**
		 * 启动页面
		 *
		 * @param intent      intent
		 * @param requestCode requestCode
		 */
		void startActivityForResult(Intent intent, int requestCode);

		/**
		 * 启动页面
		 *
		 * @param intent
		 */
		void startActivity(Intent intent);

		/**
		 * 启动页面
		 *
		 * @param intent      携带数据
		 * @param cls         类名
		 * @param requestCode 请求码
		 */
		void startActivity(Intent intent, Class cls, int requestCode);

		/**
		 * 启动 系统组件，打电话，邮件等；
		 *
		 * @param intent
		 */
		void startIntent(Intent intent);

		void onPause();

		/**
		 * 甚至startActivityListener
		 *
		 * @param resultListener
		 */
		void setResultListener(MbsResultListener resultListener);

		/**
		 * 注册广播
		 *
		 * @param name     广播的actionName
		 * @param callback webView的回掉方法
		 */
		void registerBroadcastReceiver(String name, String callback);

		/**
		 * 设置代理
		 */
		void setProxy();

		/**
		 * @param context
		 * @param parameter 参数
		 */
		void setBottomMenu(Context context, String parameter);


		/**
		 * 关闭页面
		 *
		 * @param url    地址
		 * @param action action命令
		 */
		boolean onClosePage(String url, String action);

		/**
		 * 关闭页面并且返回主页
		 *
		 * @param url    url
		 * @param action action
		 * @return boolean 数据
		 */
		boolean onClosePageReturnMain(String url, String action);

		/**
		 * 返回并且局部刷新
		 *
		 * @param backParam
		 * @param url
		 * @return boolean 数据
		 */
		boolean onLocalRefresh(String backParam, String url);

		/**
		 * 隐藏头部导航栏
		 *
		 * @param url    地址
		 * @param action action命令
		 */
		boolean onLightweightPage(String url, String action);

		/**
		 * 隐藏头部导航栏
		 *
		 * @param url    地址
		 * @param action action命令
		 */
		void onHomePage(String url, String action);

		/**
		 * 设置标题
		 *
		 * @param type  标题类相关 0：获取h5中的title
		 *              1：获取h5 命令中的title
		 *              default： 默认设置 h5中自带的title
		 * @param title
		 */
		void setTitle(int type, String title);

		/**
		 * 设置toolbar的返回图标
		 *
		 * @param resId
		 */
		void setNavigationIcon(int resId);

		/**
		 * 命令回掉
		 *
		 * @param listener
		 */
		void setMbsRequestPermissionsResultListener(MbsRequestPermissionsListener listener);


		/**
		 * table_layout
		 * 设置数据设置
		 *
		 * @param items
		 */
		void setCenterMenu(List<Item> items);

		/**
		 * 下一页
		 *
		 * @param url    地址
		 * @param action action命令
		 * @param params action命令集合
		 */

		boolean nextPage(String url, String action);

		/**
		 * 执行Action 命令
		 *
		 * @param params
		 */
		void doAction(Map<String,String> params);
	}

}
