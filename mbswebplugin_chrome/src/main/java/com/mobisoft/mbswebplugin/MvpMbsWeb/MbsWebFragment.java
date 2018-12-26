package com.mobisoft.mbswebplugin.MvpMbsWeb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.tablayout.CommonTabLayout;
import com.mobisoft.mbswebplugin.Cmd.CMD;
import com.mobisoft.mbswebplugin.Cmd.CmdrBuilder;
import com.mobisoft.mbswebplugin.Cmd.Enum.StatusBar;
import com.mobisoft.mbswebplugin.Entity.CallBackResult;
import com.mobisoft.mbswebplugin.Entity.Data;
import com.mobisoft.mbswebplugin.Entity.Item;
import com.mobisoft.mbswebplugin.Entity.MeunItem;
import com.mobisoft.mbswebplugin.Entity.TopMenu;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebviewListener;
import com.mobisoft.mbswebplugin.MbsWeb.WebAppInterface;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Base.Preconditions;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.DrawerLayoutListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.ActivityManager;
import com.mobisoft.mbswebplugin.base.AppConfing;
import com.mobisoft.mbswebplugin.base.Recycler;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;
import com.mobisoft.mbswebplugin.refresh.BGARefreshLayout;
import com.mobisoft.mbswebplugin.refresh.BGAYaoWanRefreshViewHolder;
import com.mobisoft.mbswebplugin.utils.ActivityCollector;
import com.mobisoft.mbswebplugin.utils.NetworkUtils;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.UrlUtil;
import com.mobisoft.mbswebplugin.utils.Utils;
import com.mobisoft.mbswebplugin.view.MessageView.MBSMsgView;
import com.mobisoft.mbswebplugin.view.PopuMenu.PopupMenu;
import com.mobisoft.mbswebplugin.view.SingleSeletPopupWindow;
import com.mobisoft.mbswebplugin.view.TitleMenuPopupWindow;
import com.mobisoft.mbswebplugin.view.TopMenuPopupWindowActivity;
import com.mobisoft.mbswebplugin.view.aliPlay.SuperVideoPlay;
import com.mobisoft.mbswebplugin.view.progress.CustomDialog;
import com.mobisoft.mbswebplugin.view.progress.CustomProgress;
import com.mobisoft.mbswebplugin.view.progress.ProgressDialogShepai;
import com.mobisoft.mbswebplugin.view.serach.SearchBar;
import com.mobisoft.mbswebplugin.view.serach.SearchListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.GONE;
import static com.mobisoft.mbswebplugin.base.AppConfing.INTENT_REQUEST_CODE;
import static com.mobisoft.mbswebplugin.base.AppConfing.IS_HIDENAVIGATION;
import static com.mobisoft.mbswebplugin.base.AppConfing.IS_LEFT_ICON_SHOW;
import static com.mobisoft.mbswebplugin.base.AppConfing.IS_LEFT_TEXT_SHOW;
import static com.mobisoft.mbswebplugin.base.AppConfing.TITLECOLOR;
import static com.mobisoft.mbswebplugin.base.AppConfing.TYPE_ACTIVITY;
import static com.mobisoft.mbswebplugin.utils.UrlUtil.parseUrl;
import static java.lang.System.in;

/**
 *
 */
public class MbsWebFragment extends Fragment implements MbsWebPluginContract.View, View.OnClickListener, WebAppInterface,
		HybridWebviewListener, Recycler.Recycleable, BGARefreshLayout.BGARefreshLayoutDelegate {
	public static final String TAG = "MBS_WEBFRG";
	public static final int BACK_LAYOUT = 0;
	public static final int BACK_IMAGE_BTN = 1;
	public static final int BACK_TEXT_BTN = 2;
	private WebPluginPresenter presenter;

	/**
	 * 下拉刷新控件
	 */
	private BGARefreshLayout bgaRefreshLayout;
	/**
	 * 核心组件 webView
	 */
	private HybridWebView mWebViewExten;
	/**
	 * 上下文环境
	 */
	private Context mContext;
	/**
	 * 初次进来webview 的 url （必须要）
	 */
	public static final String URL = "url";
	/**
	 * 进度条
	 */
	public CustomProgress mProgressDialog;
	/**
	 * 用于记录第一次onPageFinished进来的
	 */
	private boolean firstComeIn = true;
	/**
	 * 获取网页标题
	 */
	private String urlTitle;
	/**
	 * 当前页面是否需要关闭
	 */
	public boolean isNeedClose = true;
	/**
	 * 初始化webview时用户account （必须要）
	 */
	public static final String ACCOUNT = "account";

	/**
	 * 初始化webview时avtivity进入动画 （非必须，有默认值）
	 */
	public static final String ANIMRES = "AnimRes";
	/**
	 * title左右两边菜单模式 （非必须，有默认值）
	 */
	public static final String SHOWMOUDLE = "showmodel";
	/**
	 * 搜索页面没有title模式 （非必须，有默认值）
	 */
	public static final String SHOWMOUDLESEARCHPAGE = "showModelSearchPage";
	/**
	 * 沉浸式菜单栏颜色 （非必须，有默认值）
	 */
	public static final String SYSTEM_BAR_COLOR = "SystemBarColor";
	/**
	 * 标题中间文字颜色颜色 （非必须，有默认值）
	 */
	public static final String TITLE_CENTER_TEXT_COLOR = "TitleCenterTextColor";
	/**
	 * 标题左边文字颜色颜色 （非必须，有默认值）
	 */
	public static final String TITLE_LEFT_TEXT_COLOR = "TitleLeftTextColor";
	/**
	 * 标题右边文字颜色颜色 （非必须，有默认值）
	 */
	public static final String TITLE_RIGHT_TEXT_COLOR = "TitleRightTextColor";
	/**
	 * 标题返回图片 （非必须，有默认值）
	 */
	public static final String ICON_BACK = "IconBack";
	/**
	 * 标题中间图片 （非必须，有默认值）
	 */
	public static final String ICON_TITLE_CENTER = "IconTitleCenter";
	/**
	 * 标题右边图片 （非必须，有默认值）
	 */
	public static final String ICON_TITLE_RIGHT = "IconTitleRight";


	/**
	 * 是否显示沉浸式菜单栏 （非必须，有默认值）
	 */
	public static final String IS_SYSTEM_BAR_SHOW = "IsSystemBarShow";
	/**
	 * 是否支持刷新 （非必须，有默认值）
	 */
	public static final String IS_REFRESH_ENABLE = "IsRefreshEnable";

	/**
	 * activity是否支持打开方式 （非必须，有默认值）
	 */
	public static final String IS_TRANSITION_MODE_ENABLE = "isTransitionModeEnable";
	/**
	 * activity打开方式 （非必须，有默认值）
	 */
	public static final String IS_TRANSITION_MODE = "isTransitionMode";
	/**
	 * 设置隐藏返回菜单栏位
	 */
	public static final String IS_HideBackLayout = "isHideBackLayout";
	/***
	 * 下拉刷新 延时取消刷新（等待initpage方法执行结束）
	 */
	public static final int DELAY_MILLIS = 400;
	/**
	 * TYPE_WEB 按返回键的类型 调用web View.goback()方法
	 */
	public static final String TYPE_WEB = "pageWeb";
	/**
	 * 缓存路径
	 */
	private static final String APP_CACAHE_DIRNAME = "/webcache";
	/**
	 * 返回上一页布局
	 */
	protected LinearLayout mLl_back;
	/**
	 * title布局
	 */
	public LinearLayout ll_head_title;
	/**
	 * 标题
	 */
	public TextView mTv_head_title;
	/**
	 * 右边 菜单 水平状态下  第二个 标题
	 */
	protected TextView tv_head_right_2;
	/**
	 * 右标题
	 */
	protected TextView tv_head_right;
	/**
	 * 左标题
	 */
	protected TextView tv_head_left;
	/**
	 * url
	 */
	protected String urlStr;
	/**
	 * 工号
	 */
	protected String accountStr;

	/**
	 * 单选标志
	 */
	protected int lv_single_Item = -1;
	/**
	 * 右标题
	 */
	protected RelativeLayout ll_right;
	/**
	 * 右上角菜单 水平状态  第二个 右标题
	 */
	protected RelativeLayout ll_right_2;

	/**
	 * 头布局
	 */
	public Toolbar toolbar;

	/***
	 * 单选菜单
	 */
	protected SingleSeletPopupWindow mSingleSeletPopupWindow;

	/***
	 * 右上角菜单
	 */
	protected TopMenuPopupWindowActivity mTopMenuPopWin;
	/***
	 * title菜单
	 */
	protected TitleMenuPopupWindow mTitleMenuPopWin; //
	/**
	 * 左边返回图片按钮
	 */
	protected ImageView iv_head_left;
	/**
	 * 菜单图标
	 */
	protected ImageView img_right;
	/**
	 * 右上角菜单 水平状态下  第二个  菜单图标
	 */
	protected ImageView img_right_2;
	/**
	 * title菜单图标
	 */
	protected ImageView iv_head_title_menu;
	/**
	 * 搜索头布局
	 */
	private LinearLayout ll_search;
	/**
	 * seeting头布局 相关
	 */
	private RadioGroup rg_all;
	private LinearLayout ll_center_normal;

	/***
	 * 右边菜单 选项
	 */
	protected List<MeunItem> listMenuItem = new ArrayList<>();
	/**
	 * 中间菜单选项
	 */
	protected List<MeunItem> listTitleMenuItem = new ArrayList<>();
	/**
	 * 右上角菜单标志位  默认true  移除第一个选项 将其显示再右边标题处
	 */
	protected boolean farstMune = true; //
	/**
	 * title菜单标志位  移除第一个选项 将其显示再标题处
	 */
	protected boolean farstTitleMune = true; // title菜单标志位
	/**
	 * 左右菜单 false 不显示 菜单
	 */
	protected boolean showModel = false; // 左右菜单
	/***
	 * 没有title 的搜索
	 */
	protected boolean showModelSearchPage = false; // 没有title 的搜索
	/**
	 * title 颜色
	 */
	protected int titleColor = 0; // title 颜色
	/**
	 * avtivity进入动画
	 */
	protected int animRes = 0; // avtivity进入动画
	/***
	 * 沉浸式菜单栏颜色
	 */
	protected int systemBarColor = 0; // 沉浸式菜单栏颜色
	/**
	 * 标题中间文字颜色颜色
	 */
	protected int titleCenterTextColor = 0; // 标题中间文字颜色颜色
	/***
	 * 标题左边文字颜色颜色
	 */
	protected int titleLeftTextColor = 0; // 标题左边文字颜色颜色
	/**************************
	 * 标题右边文字颜色颜色
	 ***************************/
	protected int titleRightTextColor = 0; // 标题右边文字颜色颜色
	/**
	 * 标题返回图片
	 */
	protected int iconBack = 0; // 标题返回图片
	/**************************
	 * 标题中间图片
	 ***************************/
	protected int iconTitleCenter = 0; // 标题中间图片
	/***
	 * 标题右边图片
	 */
	protected int iconTitleRight = 0; // 标题右边图片
	/**
	 * 是否显示左边“返回”文字
	 */
	protected boolean isLeftTextShow = false; // 是否显示左边“返回”文字
	/**
	 * 是否显示左边“返回”图片
	 */
	protected boolean isLeftIconShow = true; // 是否显示左边“返回”图片
	/**
	 * 代码控制  是否显示左边“返回”图片
	 */
	private boolean isLeftIconID = true;

	/**
	 * 是否显示沉浸式菜单栏
	 */
	protected boolean isSystemBarShow = true; // 是否显示沉浸式菜单栏
	/**
	 * 是否支持刷新
	 */
	protected boolean isRefreshEnable = false; // 是否支持刷新
	/***
	 * activity是否支持打开方式
	 */
	protected boolean isTransitionModeEnable = true; // activity是否支持打开方式
	/***
	 * activity打开方式
	 */
	protected String isTransitionMode = "RIGHT"; // activity打开方式
	/***
	 * 是否隐藏 导航栏 true :隐藏  false：显示
	 */
	protected boolean is_hidenavigation;
	/**
	 * 是否设置过 h5命令中标题 false:未设置命令标题
	 */
	private boolean issetTitle = false;
	/**
	 * 是否 清除过任务栈 false： 没有
	 ***/
	public boolean isClearTask = false;
	/**
	 * 消息控件
	 */
	private MBSMsgView tipView;
	/**
	 * 右上角菜单  水平状态下  第二个 消息控件
	 */
	private MBSMsgView tipView_2;
	/**
	 * 上拉刷新的线程
	 */
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i(ContentValues.TAG, " handler" + msg.what);
			switch (msg.what) {
				case 0:
				case 2: // 停止刷新
					if (bgaRefreshLayout != null) {
						bgaRefreshLayout.endRefreshing();
						bgaRefreshLayout.endLoadingMore();
					}
					break;
				case 1: //  开始刷新
					firstComeIn = true;
					bgaRefreshLayout.beginRefreshing();
					urlTitle = mWebViewExten.getTitle();
					if(mWebViewExten.getUrl().contains("file:///android_asset")){
						String str= getArguments().getString(URL);
						mWebViewExten.loadUrl(str);
					}else {
						mWebViewExten.reload();
					}

					break;
				case 3:// 关闭当前 页面
					boolean falg = (boolean) msg.obj;
					if (falg) presenter.finishActivity();
					break;
				case 4:// 返回WebView的上一页面
					boolean falg1 = (boolean) msg.obj;
					if (falg1) {//goBack()表示返回WebView的上一页面
						if (mWebViewExten.canGoBack()) {
							mWebViewExten.goBack();
							isNeedClose = true;
						} else // 结束当前页面
							presenter.finishActivity();
					}
					break;
				case 5:// webView Cmd
					String url = msg.obj.toString();
					Map<String, String> param = parseUrl(url);
					String parameter = param.get("para");
					String function = param.get("callback");
					Pattern p = Pattern.compile("\\//(.*?)\\?");//正则表达式，取=和|之间的字符串，不包括=和|
					Matcher m = p.matcher(url);
					String cmd = null;
					while (m.find()) {
						cmd = m.group();
					}
					if (cmd != null) {
						cmd = cmd.substring(2, cmd.length() - 1);
//                        onCommand(cmd, parameter, function);
						CmdrBuilder.getInstance()
								.setContext(mContext)
								.setWebView(mWebViewExten)
								.setPresenter(presenter)
								.setContractView(MbsWebFragment.this)
								.setCmd(cmd)
								.setParameter(parameter)
								.setCallback(function)
								.doMethod();
					}
					break;
				case 6:
					firstComeIn = true;
					bgaRefreshLayout.beginRefreshing();
					urlTitle = mWebViewExten.getTitle();
					Log.e(ContentValues.TAG, "handler msgWhat is 6 ");
					loadUrl(UrlUtil.getFormatJs("initPage(#result#)", ""));
					break;
				case 7:// 断网状态下刷新
					firstComeIn = true;
					bgaRefreshLayout.beginRefreshing();
					urlTitle = mWebViewExten.getTitle();
					mWebViewExten.loadUrl(UrlUtil.getLocalPath(urlStr));
					break;
				default:
					break;
			}
		}
	};
	/**
	 * 填充right菜单
	 */
	private ViewStub stub;
	/**
	 * 填充right 水平菜单
	 */
	private ViewStub stubHorizontal;
	/**
	 * 填充品论框
	 */
	private ViewStub viewStubPinlun;
	/***
	 * 是否填充 评论看布局
	 */
	private boolean isInflated;
	/**
	 * 填充 评论看布局
	 */
	private View inflatedStub;
	private RelativeLayout ll_mbs_fragmnet;
	/**
	 * 评论输入框
	 */
	private EditText inPutPinglun;
	/**
	 * 评论输入框 发送 按钮
	 */
	private Button btn_send;
	private Animation mShowAction;
	private Animation mHiddenAction;
	/**
	 * 返回事件
	 */
	private String backEvent;
	/**
	 * 头布局菜单按钮
	 */
	private CommonTabLayout commonTabLayout;
	/**
	 * 返回按钮
	 */
	private TextView search_back;
	/**
	 * 搜索框
	 */
	private EditText search_editext;
	/**
	 * 取消按钮
	 */
	private TextView search_cancel;
	/**
	 * 是否隐藏状态栏
	 */
	private boolean is_SystemUibar;
	/**
	 * 是否隐藏返回栏位
	 */
	private boolean is_hidebacklayout;

	public MbsWebFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment MbsWebFragment.
	 */
	public static MbsWebFragment newInstance(Bundle bundle) {
		MbsWebFragment fragment = new MbsWebFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate");

		mContext = getContext();
		if (getArguments() != null) {
			isRefreshEnable = getArguments().getBoolean(IS_REFRESH_ENABLE, false);
			urlStr = getArguments().getString(URL);

		}
	}

	/**
	 * 屏幕 横竖屏监听
	 */
	private OrientationEventListener orientationEventListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Log.e(TAG, "onCreateView");
		// Inflate the layout for this fragment
		View inflate = inflater.inflate(R.layout.fragment_mbs_web, container, false);
		initViews(inflate);
		// 2018年12月26 因为用不到代理，离线访问注释掉的
		//需要的话可以解除注释
//		setWebProxy();
		if (getArguments() != null)
			initData(getArguments());
		setWebSetting();
		setEvents();

		return inflate;
	}

	/***
	 * 初始化view
	 *
	 * @param inflate 视图
	 */
	protected void initViews(View inflate) {
		bgaRefreshLayout = (BGARefreshLayout) inflate.findViewById(R.id.swipeRefreshLayout);
		mWebViewExten = new HybridWebView(mContext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		mWebViewExten.setLayoutParams(params);
		bgaRefreshLayout.addWebView(mWebViewExten);
//		mWebViewExten.setBackgroundResource(R.color.trans);
		mWebViewExten.setListener(this);
		this.loadUrl(urlStr);

		toolbar = (Toolbar) inflate.findViewById(R.id.web_tool_bar);

		mLl_back = (LinearLayout) toolbar.findViewById(R.id.ll_back);
		ll_head_title = (LinearLayout) toolbar.findViewById(R.id.ll_head_title);
		mTv_head_title = (TextView) toolbar.findViewById(R.id.tv_head_title);
		tv_head_left = (TextView) toolbar.findViewById(R.id.tv_head_left);
		iv_head_left = (ImageView) toolbar.findViewById(R.id.iv_head_left);
		iv_head_title_menu = (ImageView) toolbar.findViewById(R.id.iv_head_title_menu);

		//nike
		ll_search = (LinearLayout) inflate.findViewById(R.id.search_ll);
		search_back = (TextView) inflate.findViewById(R.id.search_back);
		search_editext = (EditText) inflate.findViewById(R.id.search_editext);
		search_cancel = (TextView) inflate.findViewById(R.id.search_cancel);

		viewStubPinlun = (ViewStub) inflate.findViewById(R.id.view_stub_ping_lun);
		ll_mbs_fragmnet = (RelativeLayout) inflate.findViewById(R.id.ll_mbs_fragmnet);
		commonTabLayout = (CommonTabLayout) inflate.findViewById(R.id.table_layout);
		inintViewColor();
	}
	/**
	 * 设置代理
	 */
	private void setWebProxy() {
		if (presenter != null&&ProxyConfig.getConfig().isOpenProxy())
			presenter.setProxy();
	}

	private void initData(Bundle bundle) {
		AppConfing.ACCOUNT = accountStr;
		titleColor = bundle.getInt(TITLECOLOR, 0);
		showModel = bundle.getBoolean(SHOWMOUDLE, false);
		showModelSearchPage = bundle.getBoolean(SHOWMOUDLESEARCHPAGE, false);
		animRes = bundle.getInt(ANIMRES, R.anim.in_from_right);
		systemBarColor = bundle.getInt(SYSTEM_BAR_COLOR, Color.parseColor(getString(R.string.man_system_bar_color)));
		titleCenterTextColor = bundle.getInt(TITLE_CENTER_TEXT_COLOR, Color.WHITE);
		titleLeftTextColor = bundle.getInt(TITLE_LEFT_TEXT_COLOR, Color.WHITE);
		titleRightTextColor = bundle.getInt(TITLE_RIGHT_TEXT_COLOR, Color.WHITE);
		iconBack = bundle.getInt(ICON_BACK, R.drawable.back);
		iconTitleCenter = bundle.getInt(ICON_TITLE_CENTER, R.drawable.ic_gf_triangle_arrow);
		iconTitleRight = bundle.getInt(ICON_TITLE_RIGHT, R.drawable.ic_add_black_48dp);
		isLeftTextShow = bundle.getBoolean(IS_LEFT_TEXT_SHOW, false);
		isLeftIconShow = bundle.getBoolean(IS_LEFT_ICON_SHOW, false);
		isSystemBarShow = bundle.getBoolean(IS_SYSTEM_BAR_SHOW, true);
		isRefreshEnable = bundle.getBoolean(IS_REFRESH_ENABLE, false);
		isTransitionModeEnable = bundle.getBoolean(IS_TRANSITION_MODE_ENABLE, true);
		isTransitionMode = bundle.getString(IS_TRANSITION_MODE);
		is_hidebacklayout = bundle.getBoolean(IS_HideBackLayout, false);
		is_hidenavigation = bundle.getBoolean(IS_HIDENAVIGATION, false);
		is_SystemUibar = bundle.getBoolean(AppConfing.IS_SYSTEMUIBAR, false);

		if (is_hidenavigation || showModel || showModelSearchPage)
			hideTitle();
		if (is_SystemUibar)
			setSystemUiClolr();
		if (TextUtils.isEmpty(accountStr)) accountStr = "error";
		if (titleColor != 0) { // 设置title颜色 和沉浸式菜单栏
			toolbar.setBackgroundColor(titleColor);
		}
		if (isLeftTextShow){
			tv_head_left.setVisibility(GONE);
		}
		if (isLeftIconShow) {
			// true 显示 返回图标
			tv_head_left.setVisibility(View.VISIBLE);
			setNavigationIcon(iconBack);
		} else {
			// true 显示 返回图标
			setNavigationIcon(-1);
		}
		bgaRefreshLayout.setPullDownRefreshEnable(isRefreshEnable);
		if (!isSystemBarShow) {
			hideSystemBar();
		}
		if (is_hidebacklayout) // 返回栏位
			hideLeftMenu(BACK_LAYOUT);

	}


	/**
	 * 设置事件
	 */
	private void setEvents() {
		bgaRefreshLayout.setDelegate(this);

//		iv_head_left.setOnClickListener(this);
//		tv_head_left.setOnClickListener(this);
		tv_head_left.setClickable(false);
		ll_head_title.setOnClickListener(this);
		ll_head_title.setClickable(false);
		mLl_back.setOnClickListener(this);
		// 正常默认的下拉加载图片
//		bgaRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext.getApplicationContext(), true));
//// 蛇牌学院的下拉加载东花 逐帧动画
		bgaRefreshLayout.setRefreshViewHolder(new BGAYaoWanRefreshViewHolder(mContext.getApplicationContext(), true));
		search_cancel.setOnClickListener(this);
		search_back.setOnClickListener(this);
		if (presenter != null && !TextUtils.isEmpty(urlStr)) {
			Map<String, String> param = parseUrl(urlStr);
			presenter.doAction(param);
		}

	}


	/**
	 * 初始化，标题布局字体颜色
	 */
	private void inintViewColor() {
		toolbar.setBackgroundResource(ProxyConfig.getConfig().getBackgroundColorId());
		mTv_head_title.setTextColor(getColor(ProxyConfig.getConfig().getTextColorId()));
		tv_head_left.setTextColor(getColor(ProxyConfig.getConfig().getTextColorId()));
		setImageWithImageView(iv_head_left, null, ProxyConfig.getConfig().getBackIcon());
	}

	/**
	 * 设置webview
	 */
	private void setWebSetting() {
		// 设置可以使用localStorage
		WebSettings webSettings = mWebViewExten.getSettings();
		// init webview settings
		webSettings.setAllowContentAccess(true);
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		mWebViewExten.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式
		mWebViewExten.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
		mWebViewExten.getSettings().setUseWideViewPort(true);//扩大比例的缩放 //为图片添加放大缩小功能
		mWebViewExten.getSettings().setLoadWithOverviewMode(true);
		mWebViewExten.getSettings().setDisplayZoomControls(false);
		mWebViewExten.getSettings().setSupportZoom(true); // 支持缩放
		mWebViewExten.getSettings().setBuiltInZoomControls(true);// 设置出现缩放工具
		mWebViewExten.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放模式
		settingWebCache(webSettings);
	}

	/**
	 * 设置webview缓存
	 */
	private void settingWebCache(WebSettings settings) {
		if (ProxyConfig.getConfig().getCacheMode() > -2) {
			settings.setCacheMode(ProxyConfig.getConfig().getCacheMode());  //设置默认配置的缓存模式
		} else {
			if (NetworkUtils.isNetworkConnected(mContext))
				settings.setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 有网状态 不使用缓存
			else
				settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 断网状态的一使用缓存
		}

		//zhu.gf
		settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
		// 开启 DOM storage API 功能
		settings.setDomStorageEnabled(true);
		//开启 database storage API 功能
		settings.setDatabaseEnabled(true);

		String cacheDirPath = mContext.getCacheDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
		Log.i(ContentValues.TAG, "cacheDirPath=" + cacheDirPath);
		//设置数据库缓存路径
		settings.setDatabasePath(cacheDirPath);
		//设置  Application Caches 缓存目录
		settings.setAppCachePath(cacheDirPath);
		//开启 Application Caches 功能
		settings.setAppCacheEnabled(true);
		settings.setAppCacheMaxSize(200 * 1024);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}

	}



	@Override
	public void setNavigationIcon(int resId) {
		// true 显示 返回图标
		if (resId <= 0) {
			isLeftIconID = false;
			if (iv_head_left != null) {
				iv_head_left.setVisibility(GONE);
			}
		} else if (!isLeftIconID) {
			iv_head_left.setVisibility(GONE);
		} else {
			isLeftIconID = true;
			if (iv_head_left != null) {
				iv_head_left.setVisibility(View.VISIBLE);
				iv_head_left.setImageResource(resId);
				mLl_back.setVisibility(View.VISIBLE);
			}
		}
	}
	@Override
	public void onAttach(Context context) {

		super.onAttach(context);
		Log.e(TAG, "onAttach");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.e(TAG, "onDetach");
	}


	/**
	 * 刷新
	 *
	 * @param isFobin: true 禁止刷新 false；开启刷新
	 */
	@Override
	public void forbiddenRefresh(boolean isFobin) {
		bgaRefreshLayout.setPullDownRefreshEnable(!isFobin);
	}

	/**
	 * 加载url
	 *
	 * @param url 地址
	 */
	@Override
	public void loadUrl(String url) {
		if (mWebViewExten != null)
			mWebViewExten.loadUrl(url);
	}

	@Override
	public String getUrl() {
		return urlStr;
	}

	@Override
	public void setPresenter(MbsWebPluginContract.Presenter presenter) {
		this.presenter = (WebPluginPresenter) Preconditions.checkNotNull(presenter);

	}

	/**
	 * 下一个
	 *
	 * @param url    地址
	 * @param action 命令
	 */
	@Override
	public void openNextWebActivity(String url, String action) {


//        url = url.replace("?action=nextPage", "");
		Bundle bunde = new Bundle();
		bunde.putString(URL, url);
		bunde.putString("ACTION", action);

		if (accountStr != null) bunde.putString(ACCOUNT, accountStr);
		if (titleColor != 0) bunde.putInt(TITLECOLOR, titleColor);
		// 是否显示沉浸式状态栏
//        if (isSystemBarShow) {
//            bunde.putBoolean(IS_SYSTEM_BAR_SHOW, true);
//            bunde.putInt(SYSTEM_BAR_COLOR, systemBarColor);
//        }
		// 是否显示转圈
		if (action.equals(CMD.action_showModelPage))
			bunde.putBoolean(SHOWMOUDLE, true);
		// 搜索页面没有title模式 （非必须，有默认值）
		if (action.equals(CMD.action_showModelSearchPage))
			bunde.putBoolean(SHOWMOUDLESEARCHPAGE, true);
		/// activity是否支持打开方式 （非必须，有默认值） 转场动画

		if (isTransitionModeEnable) {
			bunde.putBoolean(IS_TRANSITION_MODE_ENABLE, true);
			bunde.putString(IS_TRANSITION_MODE, isTransitionMode);
		}
		bunde.putBoolean(AppConfing.IS_LEFT_ICON_SHOW, true);

		Intent intent = new Intent();
		intent.putExtras(bunde);
		if (url.contains(".pdf") || url.contains(".doc") || url.contains(".xls")
				|| url.contains(".txt") || url.contains(".ppt") || url.contains(".xlsx")) {
			presenter.startActivity(intent, TbsReaderActivity.class, INTENT_REQUEST_CODE);
		} else {
			presenter.startActivityForResult(intent, INTENT_REQUEST_CODE);

		}
//        intent.setClass(mContext, MbsWebActivity.class);
//        startActivityForResult(intent, INTENT_REQUEST_CODE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//设置回退
		//覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法

		if ((keyCode == KeyEvent.KEYCODE_BACK || event.getAction() == KeyEvent.KEYCODE_BACK)) {
			boolean handler = SuperVideoPlay.getInstance().onKeyDown();
			if (!handler) {
				return false;
			}
			// 处理 弹出框输入布局
			if (inPutPinglun != null && inPutPinglun.getVisibility() == View.VISIBLE) {
				inPutPinglun.setVisibility(View.GONE);
				viewStubPinlun.setVisibility(View.GONE);
				return true;
			}

			if (mWebViewExten.canGoBack() && mWebViewExten.needGoback()) {

				onFinish(TYPE_WEB);
				return true;
			} else {
				onFinish(TYPE_ACTIVITY);
				return true;
			}
		}
		return false;
	}

	@Override
	public void hideTitle() {
		Log.i("oye", "hideTitle()");
		toolbar.setVisibility(GONE);
//		setSystemUiClolr();

	}

	/**
	 * 设置状态栏的颜色
	 * 透明
	 */
	private void setSystemUiClolr() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = ((Activity) mContext).getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);

//			View decorView =((Activity) mContext).getWindow().getDecorView();
//			int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//			decorView.setSystemUiVisibility(option);
//			((Activity) mContext).getWindow().setStatusBarColor(Color.TRANSPARENT);


		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = ((Activity) mContext).getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}

	@Override
	public void TopMenuClick(List<MeunItem> list, int position) {
		Log.e(ContentValues.TAG, "点击   +  :" + position);
		if (!TextUtils.isEmpty(list.get(position).getCallback())) {// 回调函数
//            String json = String.format("javascript:" + list.get(position).getCallback() + "(%s)", "");
			loadUrl(UrlUtil.getFormatJs(list.get(position).getCallback(), ""));
		} else if (!TextUtils.isEmpty(list.get(position).getUrl())) {// 启动新页面
			if (list.get(position).getUrl().startsWith("http"))
				presenter.nextPage(list.get(position).getUrl(), CMD.action_nextPage);
			else
				presenter.nextPage(ProxyConfig.getConfig().getBaseUrl() + list.get(position).getUrl(), CMD.action_nextPage);
		}
	}

	@Override
	public void setTopRightMenuList() {
		Log.e(ContentValues.TAG, "点击   +setTopRightMenuList  :");
		creatRigthMenu();
	}

	/**
	 * 创建菜单
	 */
	private void creatRigthMenu() {
		PopupMenu menu = new PopupMenu(mContext);
		menu.setEntities(listMenuItem);
		menu.onCreatPopupMenu(ll_right);
		menu.setSubMintListener(new PopupMenu.SubMintListener() {
			@Override
			public void onCancle() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClick(View itemView, int position) {
				lv_single_Item = position;
				setMenus(listMenuItem.get(0), tv_head_right, img_right, ll_right);
				TopMenuClick(listMenuItem, lv_single_Item);

			}

			@Override
			public void onSubmint(List<String> labels, String type, String time) {

			}
		});
	}

	/**
	 * 初始化右上角菜单
	 */
	private void initRightMenu() {
		if (stub == null) {
			stub = (ViewStub) toolbar.findViewById(R.id.right_menu);
			View inflated = stub.inflate();
			tv_head_right = (TextView) inflated.findViewById(R.id.tv_head_right);
			ll_right = (RelativeLayout) inflated.findViewById(R.id.ll_right);
			img_right = (ImageView) inflated.findViewById(R.id.img_right);
			tipView = (MBSMsgView) inflated.findViewById(R.id.hebo_msg_tip);
			ll_right.setOnClickListener(this);
			ll_right.setClickable(false);
			tv_head_right.setTextColor(getColor(ProxyConfig.getConfig().getTextColorId()));
		}
	}

	/**
	 * 初始化右上角水平状态菜单
	 *
	 * @param listMenuItem
	 */
	private void initRightMenu2(List<MeunItem> listMenuItem) {
		if (stubHorizontal == null) {
			stubHorizontal = (ViewStub) toolbar.findViewById(R.id.right_menu_2);
			View inflated = stubHorizontal.inflate();
			tv_head_right_2 = (TextView) inflated.findViewById(R.id.tv_head_right);
			ll_right_2 = (RelativeLayout) inflated.findViewById(R.id.ll_right);
			img_right_2 = (ImageView) inflated.findViewById(R.id.img_right);
			tipView_2 = (MBSMsgView) inflated.findViewById(R.id.hebo_msg_tip);
			ll_right_2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					TopMenuClick(MbsWebFragment.this.listMenuItem, 0);
				}
			});
			ll_right.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					TopMenuClick(MbsWebFragment.this.listMenuItem, 1);
				}
			});
			ll_right_2.setClickable(false);
		}
		tv_head_left.setVisibility(GONE);
		tv_head_left.setClickable(false); // 左title文字可点击
		for (int i = 0; i < listMenuItem.size(); i++) {
			MeunItem meunItem = listMenuItem.get(i);
			if (i == 0) {
				showTipView(tipView_2, meunItem.isShowMsg());
				setMenus(meunItem, tv_head_right_2, img_right_2, ll_right_2);
			} else {
				showTipView(tipView, meunItem.isShowMsg());

				setMenus(meunItem, tv_head_right, img_right, ll_right);
			}

		}

	}

	/**
	 * 设置右上角菜单
	 *
	 * @param meunItem
	 * @param tv_head_right
	 * @param img_right
	 * @param ll_right
	 */
	private void setMenus(MeunItem meunItem, TextView tv_head_right, ImageView img_right, RelativeLayout ll_right) {
		String icon = meunItem.getIcon();
		if (!TextUtils.isEmpty(icon)) {// 显示图片
			tv_head_right.setVisibility(GONE);
			img_right.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(icon)) {
				Resources res = getResources();
				final String packageName = mContext.getPackageName();
				int imageResId = res.getIdentifier(icon, "drawable", packageName);
				int mipmapResId = res.getIdentifier(icon, "mipmap", packageName);
				if (icon.startsWith("http")) {
					Picasso.with(mContext).load(icon).into(img_right);
				} else if (imageResId > 0) {
					Picasso.with(mContext).load(imageResId).into(img_right);
				} else if (imageResId > 0) {
					Picasso.with(mContext).load(mipmapResId).into(img_right);
				}


			}
		} else if (!TextUtils.isEmpty(meunItem.getName())) { // 显示菜单名称
			img_right.setVisibility(View.INVISIBLE);
			tv_head_right.setVisibility(View.VISIBLE);
			tv_head_right.setText(listMenuItem.get(0).getName());
		} else { // 隐藏
			img_right.setVisibility(View.INVISIBLE);
			tv_head_right.setVisibility(GONE);
			tv_head_right.setText(R.string.menu);
		}
		ll_right.setClickable(true);
	}

	@Override
	public void setTitleMenu() {
		creatRigthMenu();
	}

	/**
	 * 设置标题的背景颜色，以及状态栏颜色
	 *
	 * @param color color #FFFFF
	 */
	@Override
	public void setTitleBg(String color) {
		if (TextUtils.isEmpty(color)) {
			int c = getColor(ProxyConfig.getConfig().getBackgroundColorId());
			setTitleBgColor(c);
		} else {
			int barColor = Color.parseColor(color);
			setTitleBgColor(barColor);
		}
	}

	/**
	 * 设置标题的背景颜色，以及状态栏颜色
	 *
	 * @param c color 的值
	 */
	private void setTitleBgColor(int c) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			((Activity) mContext).getWindow().setStatusBarColor(c);
		}
		toolbar.setBackgroundColor(c);
	}

	@Override
	public void setTitleColor(String color) {
		if (TextUtils.isEmpty(color)) {
			int colorId = ProxyConfig.getConfig().getTextColorId();
			int c = getColor(colorId);
			mTv_head_title.setTextColor(c);

		} else
			mTv_head_title.setTextColor(Color.parseColor(color));

	}

	/**
	 * 根据资源ID获取 颜色
	 *
	 * @param ResourceID 资源id Resource ID
	 * @return
	 */
	private int getColor(int ResourceID) {
		return getResources().getColor(ResourceID);
	}

	@Override
	public boolean getIsClearTask() {
		return isClearTask;
	}

	@Override
	public void setIsClearTask(boolean b) {
		this.isClearTask = b;
	}

	@Override
	public void setTopAndRightMenu(String json) {
		initRightMenu();
		setRightMenuText(true);
		farstMune = true;
		listMenuItem.clear();
		TopMenu menu = Utils.json2entity(json, TopMenu.class);
		showTipView(tipView, menu.isShowMsg());
//        UnreadMsgUtils.show(tipView, Integer.parseInt(menu.getMsgNum()));

		/**当返回菜单数组为空 隐藏菜单*/
		if (menu == null || menu.getItem() == null || menu.getItem().size() == 0) {
			img_right.setVisibility(View.INVISIBLE);
			tv_head_right.setVisibility(GONE);
			ll_right.setClickable(false);
			return;
		}
//		menu.setOrientation("HORIZONTAL");

		listMenuItem.addAll(menu.getItem());
		if (TextUtils.equals("HORIZONTAL", menu.getOrientation()) && listMenuItem.size() == 2) {// 是否为水平布局
			initRightMenu2(listMenuItem);
		} else {// 默认垂直布局
			if (listMenuItem.size() > 0) ll_right.setClickable(true); // 右title可点击
			if (showModel) { //showmodle模式
				tv_head_left.setVisibility(View.VISIBLE);
				tv_head_left.setText(listMenuItem.get(0).getName());
				tv_head_left.setClickable(true); // 左title文字可点击
				tv_head_right.setVisibility(GONE);
				if (listMenuItem.size() < 2) return;
				img_right.setVisibility(View.INVISIBLE);
				tv_head_right.setVisibility(View.VISIBLE);
				tv_head_right.setText(listMenuItem.get(1).getName());
			} else {
				tv_head_left.setVisibility(GONE);
				tv_head_left.setClickable(false); // 左title文字可点击
				MeunItem meunItem = menu.getItem().get(0);
				setMenus(meunItem, tv_head_right, img_right, ll_right);
			}
		}
	}

	/**
	 * 是否显示 消息提示按钮
	 *
	 * @param isShowMsg
	 */
	private void showTipView(View tipView, boolean isShowMsg) {
		if (tipView != null && !isShowMsg) {
			tipView.setVisibility(View.INVISIBLE);
		} else if (tipView != null && isShowMsg) {
			tipView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void showHud(String action, String message, String type) {
		if (mProgressDialog != null && mProgressDialog.getDialog().isShowing()) {
			mProgressDialog.dismissHud();
		}
		switch (type) {
			case "1":
				mProgressDialog = new ProgressDialogShepai(mContext, ProgressDialog.STYLE_SPINNER);
				mProgressDialog.getDialog().setCanceledOnTouchOutside(false);
				break;
			default:
				mProgressDialog = new CustomDialog(mContext, R.style.CustomDialog);
				mProgressDialog.getDialog().setCanceledOnTouchOutside(false);
				break;

		}

		if (!mProgressDialog.getDialog().isShowing()) {
			mProgressDialog.showHud();
		}
		mProgressDialog.setMessage(message);

//        mProgressDialog.show(); // 本地不show，拦截url响应事件
	}

	@Override
	public void hideHud() {
		if (mProgressDialog != null && mProgressDialog.getDialog().isShowing()) {
			mProgressDialog.dismissHud();
		}
	}

	/**
	 * 关闭页面之前调用 关闭检测方法
	 * 是否关闭当前页面
	 *
	 * @param type TYPE_WEB（调用web View.goback()）,TYPE_ACTIVITY(调用this.finish())
	 */
	private void onFinish(final String type) {
		if (!isNeedClose) {
			if (isDrawlayout && drawlayoutListener != null) {
				drawlayoutListener.setDrawerLayoutMenuOnClick();
			}
			return;
		}

		mWebViewExten.loadUrl(UrlUtil.getFormatJs("goBack", ""));
		if (Build.VERSION.SDK_INT >= 19)
			mWebViewExten.evaluateJavascript("closeAllQuestion('true')", new ValueCallback<String>() {

				@Override
				public void onReceiveValue(String value) {
					// value true: 可以继续关闭当前页面
					// value false: 不可以继续关闭当前页面   （停留在当前页面执行其他操作）
					Log.e("back", "value  " + value);
					Message message = new Message();
					if (TextUtils.equals(TYPE_WEB, type))
						message.what = 4;// 网页当前页面返回
					else
						message.what = 3;// actvity结束
					message.obj = !TextUtils.equals(value, "false");
					handler.sendMessage(message);

				}
			});
		else { // 4.3及其以下的暂时没有处理
			if (!isNeedClose) { // 调用 检查方法
//                String json2 = String.format("javascript:closeAllQuestion" + "(" + "'%s')", false);
				loadUrl(UrlUtil.getFormatJs("closeAllQuestion", ""));
			} else {// 直接关闭
				Message message = new Message();
				message.what = 3;
				message.obj = true;
				handler.sendMessage(message);
			}
		}
	}

	@Override
	public void setBackEvent(String event) {
		this.backEvent = event;
	}

	@Override
	public void setRefreshStyle(boolean isRefreshInitPage) {

	}

	@Override
	public void onDestroy() {

		//魅族和三星Galaxy 5.0webView 问题Android Crash Report - Native crash at /system/lib/libc.so caused by webvi
//        mWebViewExten.clearCache(true);
		if (mWebViewExten != null) {


			mWebViewExten.clearHistory();
			if (mWebViewExten.dialog != null && mWebViewExten.dialog.isShowing())
				mWebViewExten.dialog.dismiss();

			ViewGroup parent = (ViewGroup) mWebViewExten.getParent();
			if (parent != null) {
				parent.removeView(mWebViewExten);
			}
			if (presenter != null) {
				presenter.onDestroy();
			}
			mWebViewExten.removeAllViews();
			mWebViewExten.destroy();
			super.onDestroy();
			Recycler.release(this);

//            if(photoInfoList!=null)photoInfoList.clear();

		}

		Log.e(ContentValues.TAG, "销毁页面！onDestroy");
		// 解决退出activity时 dialog未dismiss而报错的bug
		try {
			if (mProgressDialog != null && mProgressDialog.getDialog().isShowing())
				mProgressDialog.dismissHud();
		} catch (Exception e) {
			e.printStackTrace();
//            System.out.println("myDialog取消，失败！");
		}

		// cleanCacheAndCookie();
		if (orientationEventListener != null)
			orientationEventListener.disable();

	}

	@Override
	public HybridWebView getWebView() {
		return mWebViewExten;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.ll_right) { //  右上菜单图片点击事件
			if (showModel) { // 左右菜单,右菜单点击事件
				TopMenuClick(listMenuItem, 1);
			} else {
				if (listMenuItem.size() == 1) {
					TopMenuClick(listMenuItem, 0);
				} else {
					setTopRightMenuList();
				}
			}
		} else if (v.getId() == R.id.tv_head_left) { // 左上角 取消 事件
			TopMenuClick(listMenuItem, 0);
		} else if (v.getId() == R.id.ll_back
				|| v.getId() == R.id.iv_head_left
				|| v.getId() == R.id.tv_head_left
				|| v.getId() == R.id.search_back) {// 左边，返回按钮 back
			onFinish(TYPE_ACTIVITY);

		} else if (v.getId() == R.id.ll_head_title) { // title点击事件
			if (listTitleMenuItem.size() == 1) {
				TopMenuClick(listTitleMenuItem, 0);
			} else {
				setTitleMenu();
			}
		} else if (v.getId() == R.id.search_cancel) {// 搜索按钮的取消事件
			search_editext.setText("");
		}
	}

	@Override
	public void onTitle(int type, String title) {
		switch (type) {
			case CMD.type_h5Title:// 获取h5中的title
				if (!issetTitle)
					this.mTv_head_title.setText(title);
				break;
			case CMD.type_kitappsTitle:// 获取h5 命令中的title
				issetTitle = true;
				this.mTv_head_title.setText(title);
				break;
			default:// 默认设置 h5中自带的title
				if (!issetTitle)
					this.mTv_head_title.setText(urlTitle);
				break;
		}
	}

	@Override
	public void setTitle(int type, String title) {
		onTitle(type, title);
	}

	@Override
	public void onCommand(String command, String paramter, String function) {

	}

	@Override
	public void onCommand(WebView view, String url) {
		Message msg = new Message();
		msg.what = 5;
		msg.obj = url;
		handler.sendMessage(msg);
	}

	/**
	 * 下一页
	 *
	 * @param url    地址
	 * @param action 截取的action命令
	 */
	@Override
	public boolean onNextPage(String url, String action) {

		return presenter.nextPage(url, action);
	}

	@Override
	public WebResourceResponse onSIRNextPage(String url, String action) {
		if (urlStr.equals(url)) return null;
		switch (action) {
			case CMD.action_nextPage:
			case CMD.action_showModelPage:
			case CMD.action_showModelSearchPage:
				openNextWebActivity(url, action);
				break;
			case CMD.action_homepage:
				presenter.onHomePage(url, action);
				break;
			case CMD.action_exit:
				ActivityCollector.finishAll(); // 销毁所有的webactivity
				break;
			case CMD.action_closePageAndRefreshAndPop:
				ToastUtil.showShortToast(mContext, CMD.action_closePageAndRefreshAndPop);
				break;
			case CMD.action_closePageAndPop:
				ToastUtil.showShortToast(mContext, CMD.action_closePageAndPop);
				break;
		}
		WebResourceResponse res;
		try {
			PipedOutputStream out = new PipedOutputStream();
			PipedInputStream in = new PipedInputStream(out);
			// 返回一个错误的响应，让连接失败，webview内就不跳转了(解决本activity的webview跳转问题)
			res = new WebResourceResponse("html", "utr-8", in);
		} catch (IOException e) {
			e.printStackTrace();
			// 返回一个错误的响应，让连接失败，webview内就不跳转了(解决本activity的webview跳转问题)
			res = new WebResourceResponse("html", "utr-8", in);
			return res;
		}
		return res;
	}


	@Override
	public boolean onClosePage(String url, String action) {

		return presenter.onClosePage(url, action);
	}

	@Override
	public boolean onClosePageReturnMain(String url, String action) {
		return presenter.onClosePageReturnMain(url, action);
	}

	/**
	 * 返回并且局部刷新
	 *
	 * @param backParam
	 * @param url
	 */
	@Override
	public void onLocalRefresh(String backParam, String url) {
		presenter.onLocalRefresh(backParam, url);
	}

	@Override
	public void onWebPageFinished() {
		mWebViewExten.setEnabled(true);
		// 解决 android 5.0 以下多次调用onPageFinished的方法，多次调用initpage() 导致页面重复加载的问题
		if (firstComeIn) firstComeIn = false;
		else return;

		/** 初始化工号*/
//        String json = String.format("javascript:initaccount(" + "'%s')", accountStr);
//        loadUrl(json);
		/**初始化页面，调js函数必须调用*/
//        String json2 = String.format("javascript:initPage(" + "'%s')", "");
//        Log.e(ContentValues.TAG, json2);
		loadUrl(UrlUtil.getFormatJs("initPage(#result#)", ""));
//		if(mProgressDialog!=null){
//
//		}
//        mProgressDialog.dismiss();
		handler.sendEmptyMessageDelayed(0, DELAY_MILLIS);
	}

	@Override
	public boolean onLightweightPage(String url, String action) {
		return presenter.onLightweightPage(url, action);
	}

	@Override
	public void onHrefLocation(boolean isNew) {
		firstComeIn = isNew;
	}


	@Override
	public void setMainUrl(String url) {
		loadUrl(url);
	}

	@Override
	public String getMainUrl() {
		return urlStr;
	}

	@Override
	public void reloadApp() {
		handler.sendEmptyMessage(1);
	}

	@Override
	public void reload() {
		Message message = new Message();
		message.what = 6;
		handler.sendMessageDelayed(message, 200);
	}


	@Override
	public void onResume() {
		super.onResume();
		if (presenter != null)
			presenter.start();
//		if (mAliyunVodPlayerView != null) {
//			mAliyunVodPlayerView.onResume();
//		}
//		MobclickAgent.onPageStart(TextUtils.isEmpty(urlTitle)?urlStr:urlTitle); //统计页面("MainScreen"为页面名称，可自定义)

	}
	@Override
	public void onPause() {
		super.onPause();
		if (presenter != null)
			presenter.onPause();
//		MobclickAgent.onPageEnd(TextUtils.isEmpty(urlTitle)?urlStr:urlTitle); //统计页面("MainScreen"为页面名称，可自定义)
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		SuperVideoPlay.getInstance().updatePlayerViewMode();
	}



	@Override
	public void release() {
		mWebViewExten = null;
		mContext = null;
		mTopMenuPopWin = null;
		mTitleMenuPopWin = null;
		mSingleSeletPopupWindow = null;
		mProgressDialog = null;
		bgaRefreshLayout = null;
		//       presenter = null;

	}

	@Override
	public void setRightMenuText(boolean isShow) {
		if (!isShow && ll_right != null) {
			tv_head_right.setText("");
			ll_right.setVisibility(View.INVISIBLE);
		} else if (isShow && ll_right != null) {
			ll_right.setVisibility(View.VISIBLE);
		}
		if (!isShow && ll_right_2 != null) {
			tv_head_right_2.setText("");
			ll_right_2.setVisibility(View.INVISIBLE);
		} else if (isShow && ll_right_2 != null) {
			ll_right_2.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
		setRightMenuText(false);
//        if (NetWorkUtils.isNetworkConnected(mContext)) {
//            mWebViewExten.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        } else {
////            handler.sendEmptyMessage(7);
//            mWebViewExten.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        }
		handler.sendEmptyMessage(1);
	}

	@Override
	public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
		return false;
	}

	@Override
	public void showInputWindow(String param, final String callBack) {
		if (!isInflated) {
			inflatedStub = viewStubPinlun.inflate();
			isInflated = true;
			btn_send = (Button) inflatedStub.findViewById(R.id.btn_send);
			inPutPinglun = (EditText) inflatedStub.findViewById(R.id.edit_input);
			// 发送
//            inPutPinglun.setImeOptions(EditorInfo.IME_ACTION_SEND);
			inPutPinglun.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			//水平滚动设置为False
			inPutPinglun.setHorizontallyScrolling(false);
			inPutPinglun.setMinLines(2);
			initAnimations_One();
//            initAnimations_Two();
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            inPutPinglun.setLayoutParams(params);
		}
		if (inflatedStub.getVisibility() == GONE) {
			inflatedStub.setVisibility(View.VISIBLE);
			inPutPinglun.setVisibility(View.VISIBLE);
		}
		try {
			JSONObject object = new JSONObject(param);

			inPutPinglun.setHint(object.optString("hint"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		inPutPinglun.requestFocus();
		final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(inPutPinglun, InputMethodManager.SHOW_FORCED);
		btn_send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("message", inPutPinglun.getText().toString());
					jsonObject.put("result", true);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				String json = UrlUtil.getFormatJs(callBack, jsonObject.toString());

				loadUrl(json);
				inflatedStub.setVisibility(GONE);
				inPutPinglun.setVisibility(GONE);
				inPutPinglun.setText(null);
				inPutPinglun.clearFocus();
				imm.hideSoftInputFromWindow(inPutPinglun.getWindowToken(), 0); //强制隐藏键盘

			}
		});

// 监听文字框
		inPutPinglun.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					if (btn_send.getVisibility() == View.GONE) {
						btn_send.setVisibility(View.VISIBLE);

						btn_send.startAnimation(mShowAction);
					}


				} else {
					btn_send.setVisibility(View.GONE);
					btn_send.startAnimation(mHiddenAction);

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void initAnimations_One() {
		mShowAction = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
		mHiddenAction = AnimationUtils.loadAnimation(mContext, R.anim.right_out);
	}

	/**
	 * 是否关闭页面
	 *
	 * @param isNeedClose
	 */
	@Override
	public void setNeedClose(boolean isNeedClose) {
		this.isNeedClose = isNeedClose;
	}

	/**
	 * 设置抽屉菜单的回掉方法
	 ******/
	private DrawerLayoutListener drawlayoutListener;
	/**
	 * 设置抽屉菜单的  false 否  true 是
	 ***/
	private boolean isDrawlayout = false;

	/**
	 * 设置抽屉布局参数的监听
	 *
	 * @param listener
	 */
	public void setDrawerLayoutListener(DrawerLayoutListener listener) {
		this.drawlayoutListener = listener;
	}

	/***
	 *设置右上角按钮
	 * @param param json
	 * @param item  是右上角菜单数据
	 * @param type  类型
	 * @param data
	 */
	@Override
	public void setLeftMenu(String param, List<Item> item, String type, List<Data> data) {
		if (TextUtils.equals(type, "slideBar") && drawlayoutListener != null) {//策划菜单
			isNeedClose = false;
			isDrawlayout = true;
			drawlayoutListener.setDrawerLayoutData(data);
		} else {
			isNeedClose = true;
			isDrawlayout = false;
		}

	}

	/**
	 * @param type 0 ALL，1 IMAGE，2 TEXT
	 */
	@Override
	public void hideLeftMenu(int type) {
		if (type == BACK_LAYOUT) {// 全部不显示
			mLl_back.setVisibility(View.GONE);
			iv_head_left.setVisibility(View.GONE);
			tv_head_left.setVisibility(View.GONE);
		} else if (type == BACK_IMAGE_BTN) {// 隐藏图片按钮、显示文字按钮
			mLl_back.setVisibility(View.VISIBLE);
			iv_head_left.setVisibility(View.GONE);
			tv_head_left.setVisibility(View.VISIBLE);
		} else if (type == BACK_TEXT_BTN) {// 隐藏文字按钮、显示图片按钮
			mLl_back.setVisibility(View.VISIBLE);
			tv_head_left.setVisibility(View.GONE);
			iv_head_left.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * @param type          0 ALL，1 IMAGE，2 TEXT
	 * @param imageResource 图片资源id
	 * @param imageUrl      图片资源url地址
	 * @param text          显示文本
	 */
	@Override
	public void showLeftMenu(int type, int imageResource, String imageUrl, String text) {
		mLl_back.setVisibility(View.VISIBLE);
		if (type == BACK_LAYOUT) {// 全部显示
			iv_head_left.setVisibility(View.VISIBLE);
			tv_head_left.setVisibility(View.VISIBLE);
			setImageWithImageView(iv_head_left, imageUrl, imageResource);
			setTextWithTextView(tv_head_left, text, 0);
		} else if (type == BACK_IMAGE_BTN) {// 显示图片按钮、隐藏文字
			tv_head_left.setVisibility(View.GONE);
			iv_head_left.setVisibility(View.VISIBLE);
			setImageWithImageView(iv_head_left, imageUrl, imageResource);

		} else if (type == BACK_TEXT_BTN) {// 显示文字按钮、隐藏图片
			iv_head_left.setVisibility(View.GONE);
			tv_head_left.setVisibility(View.VISIBLE);
			setTextWithTextView(tv_head_left, text, 0);
		}
	}

	/**
	 * 给imageView设置图片
	 *
	 * @param view
	 * @param url
	 * @param imageResource
	 */
	@Override
	public void setImageWithImageView(ImageView view, String url, int imageResource) {
		if (view != null && (!TextUtils.isEmpty(url) || imageResource > 0)) {
			if (!TextUtils.isEmpty(url)) {
				Picasso.with(mContext).load(url).fit().into(view);
			} else if (imageResource > 0) {
				Picasso.with(mContext).load(imageResource).fit().into(view);
			}
		}
	}

	/**
	 * 给textView设置文字
	 *
	 * @param view
	 * @param text
	 * @param resourceId
	 */
	private void setTextWithTextView(TextView view, String text, int resourceId) {
		if (view != null && (!TextUtils.isEmpty(text) || resourceId > 0)) {
			if (!TextUtils.isEmpty(text)) {
				view.setText(text);
			} else if (resourceId > 0) {
				view.setText(resourceId);
			}
		}
	}


	/**
	 * 播放视频的操作
	 *
	 * @param videoUrl 视频地址
	 * @param imageURL 视频的占位图地址
	 */
	@Override
	public CallBackResult<String> setPalyVideoView(String videoUrl, String imageURL, String type, String[] params) {
		toolbar.setVisibility(View.GONE);
		SuperVideoPlay superVideoPlay = SuperVideoPlay.getInstance();
		superVideoPlay.onCreatView();
		superVideoPlay.setContext(mContext);
		superVideoPlay.addView(bgaRefreshLayout, ll_mbs_fragmnet);
		CallBackResult<String> result;
		try {
			result = superVideoPlay.setPlaySource(videoUrl, imageURL, type, params, null);
			return result;
		} catch (Exception s) {
			result = new CallBackResult();
			result.setMsg(s.getMessage());
			result.setCode(500);
			return result;
		}

	}


	public void updatePlayerViewMode() {

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void startBGVideo() {
		SuperVideoPlay.getInstance().start();
	}

	@Override
	public void stopBGVideo() {
		SuperVideoPlay.getInstance().stop();
	}

	@Override
	public void pauseBGVideo() {
		SuperVideoPlay.getInstance().pause();

	}

	@Override
	public void setFullVideoBG(String videoUrl, String coverUrl) {

	}

	@Override
	public void videoProgress() {

	}


	/**
	 * 设置状态栏 隐藏以及显示
	 *
	 * @param statusBar 状态栏的状态
	 */
	@Override
	public void setStatusBar(String statusBar) {
		if (TextUtils.equals(StatusBar.Hied.toString(), statusBar)) {
			((Activity) mContext).getWindow().
					setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		} else if (TextUtils.equals(StatusBar.Transparent.toString(), statusBar)) {
			View decorView = ((Activity) mContext).getWindow().getDecorView();
			int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
			decorView.setSystemUiVisibility(option);
			if (Build.VERSION.SDK_INT >= 21) {
				ActivityManager.get().topActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
				ll_mbs_fragmnet.setFitsSystemWindows(false);
			}
		}
	}


	/**
	 * 设置状态栏位
	 */
	private void hideSystemBar() {
		View decorView = ActivityManager.get().topActivity().getWindow().getDecorView();
		int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
		decorView.setSystemUiVisibility(option);
		if (Build.VERSION.SDK_INT >= 21) {
			ActivityManager.get().topActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
			ll_mbs_fragmnet.setFitsSystemWindows(false);
		}
	}

	@Override
	public CommonTabLayout setCenterMenu() {
		if (commonTabLayout != null) {
			commonTabLayout.setVisibility(View.VISIBLE);
			mTv_head_title.setVisibility(View.GONE);
		}
		return commonTabLayout;
	}

	@Override
	public void loadCallback(String callback, String param) {
		loadUrl(UrlUtil.getFormatJs(callback, param));
	}

	@Override
	public void loadCallback(String callback, CallBackResult param) {
		loadCallback(callback, JSON.toJSONString(param));
	}


	@Override
	public void setSearchBar(String placeholder, SearchListener watcher) {
		toolbar.removeAllViews();
		SearchBar searchBar = new SearchBar(mContext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		searchBar.setLayoutParams(params);
		toolbar.addView(searchBar, 0);
		searchBar.setSerachLitener(watcher);
		searchBar.setPlaceholder(placeholder);
	}

	@Override
	public void ChangeScreenMode(String parasm) {

		CmdrBuilder.getInstance()
				.setContext(mContext)
				.setWebView(mWebViewExten)
				.setPresenter(presenter)
				.setContractView(MbsWebFragment.this)
				.setCmd(CMD.cmd_ChangeScreenMode)
				.setParameter(parasm)
				.setCallback("calback")
				.doMethod();
	}

	@Override
	public void AllowScreenMode(OrientationEventListener listener) {
		this.orientationEventListener = listener;
		boolean autoRotateOn = (android.provider.Settings.System.getInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
		//检查系统是否开启自动旋转
		if (autoRotateOn) {
			orientationEventListener.enable();
		}

	}
}
