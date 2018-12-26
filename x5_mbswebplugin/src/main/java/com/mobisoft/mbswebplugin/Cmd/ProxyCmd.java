package com.mobisoft.mbswebplugin.Cmd;

import com.mobisoft.mbswebplugin.Cmd.DoCmd.AlbumOrCamera;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.AllowChangeScreen;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.Camera;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.Cellphone;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.ChangeScreenMode;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.CheckForUpdate;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.CheckVideo;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.CleanCache;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.ClearTask;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.ClearWebCache;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.ConfirmMethod;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.CutAndCopy;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.DefaultHomePage;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.Delete_Video;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.DownloadFile;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.DownloadVideo;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.ErrorMethod;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.ForbidRefresh;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.GetCourseVideo;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.GetDatabase;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.GetLocalFile;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.GetNetworkStatus;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.GetVersion;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.GoBack;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.HideNavigation;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.ImageBrowse;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.Location;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.Logout;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.MD5;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.OpenBrowser;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.OpenCache;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.OpenContacts;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.OpenFile;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.PhoneInfo;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.PlayVideo;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.PlayWebVideo;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.RecvMessage;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.RefreshPage;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.RouterApp;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SendEmail;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SendMSM;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SendMessageMethod;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SetBottomMenu;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SetCenterMenu;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SetDataMethod;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SetDataTimeMethod;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SetDatabase;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SetHeaderRefreshing;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SetNavigationBgColor;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SetRightMenuMethod;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SetSearchBar;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SetTimeMethod;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.SetTitle;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.Sha1Encryption;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.ShowHudMethod;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.ShowInPutWindows;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.ShowTipsMethod;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.ShowToast;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.Signature;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.StartBGVideo;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.StopBGVideo;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.UploadFile;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.dbDelete;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.dbGet;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.dbSet;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.getBase64ByPath;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.getCache;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.setAccount;
import com.mobisoft.mbswebplugin.Cmd.DoCmd.setLeftMenu;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Base.Preconditions;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.WxAuthListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author：Created by fan.xd on 2017/2/24.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class ProxyCmd {
	/**
	 * 定义策略方案组
	 */
	Map<String, String> hashmap = new ConcurrentHashMap<>();

	public static ProxyCmd cmd;
	private HomePage homePage;
	private WxAuthListener wxAuthListener;

	public HomePage getHomePage() {
		if (homePage == null) {
			homePage = new DefaultHomePage();
		}
		return homePage;
	}

	public WxAuthListener getWxAuthListener() {
		return wxAuthListener;
	}

	public void setWxAuthListener(WxAuthListener wxAuthListener) {
		this.wxAuthListener = wxAuthListener;
	}

	public void clearWxListner() {
		this.wxAuthListener = null;
	}

	public ProxyCmd setHomePage(HomePage homePage) {
		this.homePage = Preconditions.checkNotNull(homePage);
		return cmd;
	}

	public static synchronized ProxyCmd getInstance() {
		if (cmd == null) {
			cmd = new ProxyCmd();
			cmd.createMap();
		}
		return cmd;
	}

	/**
	 * 创建固定策略
	 */
	public void createMap() {
		hashmap.put(CMD.cmd_showToast, ShowToast.class.getName());
		hashmap.put(CMD.cmd_showHud, ShowHudMethod.class.getName());
		hashmap.put(CMD.cmd_setDate, SetDataTimeMethod.class.getName());
		hashmap.put(CMD.cmd_setDateTime, SetDataMethod.class.getName());
		hashmap.put(CMD.cmd_setTime, SetTimeMethod.class.getName());
		hashmap.put(CMD.cmd_setRightMenu, SetRightMenuMethod.class.getName());
		hashmap.put(CMD.cmd_setBottomMenu, SetBottomMenu.class.getName());
		hashmap.put(CMD.cmd_sendMessage, SendMessageMethod.class.getName());
		hashmap.put(CMD.cmd_recvMessage, RecvMessage.class.getName());
		hashmap.put(CMD.cmd_pagetips, ShowTipsMethod.class.getName());
		hashmap.put(CMD.cmd_uploadFile, UploadFile.class.getName());
		hashmap.put(CMD.cmd_logout, Logout.class.getName());
		hashmap.put(CMD.cmd_forbidRefresh, ForbidRefresh.class.getName());
		hashmap.put(CMD.cmd_getNetworkStatus, GetNetworkStatus.class.getName());
		hashmap.put(CMD.cmd_setTitle, SetTitle.class.getName());
		hashmap.put(CMD.cmd_setNavigationBgColor, SetNavigationBgColor.class.getName());
		hashmap.put(CMD.cmd_setDatabase, SetDatabase.class.getName());
		hashmap.put(CMD.cmd_getDatabase, GetDatabase.class.getName());
		hashmap.put(CMD.cmd_openContacts, OpenContacts.class.getName());
		hashmap.put(CMD.cmd_playVideo, PlayVideo.class.getName());
		hashmap.put(CMD.cmd_aesculaVideo, PlayWebVideo.class.getName());
		hashmap.put(CMD.cmd_getVersion, GetVersion.class.getName());
		hashmap.put(CMD.cmd_checkForUpdate, CheckForUpdate.class.getName());
		hashmap.put(CMD.cmd_clearTask, ClearTask.class.getName());
		hashmap.put(CMD.cmd_downloadFile, DownloadFile.class.getName());
		hashmap.put(CMD.cmd_getLocalFile, GetLocalFile.class.getName());
		hashmap.put(CMD.cmd_email, SendEmail.class.getName());
		hashmap.put(CMD.cmd_db_get, dbGet.class.getName());
		hashmap.put(CMD.cmd_db_set, dbSet.class.getName());
		hashmap.put(CMD.cmd_cellphone, Cellphone.class.getName());
		hashmap.put(CMD.cmd_sendMSM, SendMSM.class.getName());
		hashmap.put(CMD.cmd_openBrowser, OpenBrowser.class.getName());
		hashmap.put(CMD.cmd_getImage, AlbumOrCamera.class.getName());
		hashmap.put(CMD.cmd_placeholder, ShowInPutWindows.class.getName());
		hashmap.put(CMD.cmd_ImageBrowse, ImageBrowse.class.getName());
		hashmap.put(CMD.cmd_OpenProxy, OpenCache.class.getName());
		hashmap.put(CMD.cmd_Location, Location.class.getName());
		hashmap.put(CMD.cmd_OpenApp, RouterApp.class.getName());
		hashmap.put(CMD.cmd_db_delete, dbDelete.class.getName());
		hashmap.put(CMD.cmd_isNeedClose, GoBack.class.getName());
		hashmap.put(CMD.cmd_confirmlus, ConfirmMethod.class.getName());
		hashmap.put(CMD.cmd_confirm, ShowTipsMethod.class.getName());
		hashmap.put(CMD.cmd_clearCache, ClearWebCache.class.getName());
		hashmap.put(CMD.cmd_openCamera, Camera.class.getName());
		hashmap.put(CMD.cmd_refreshPage, RefreshPage.class.getName());
		hashmap.put(CMD.cmd_getBase64ByPath, getBase64ByPath.class.getName());
		hashmap.put(CMD.cmd_setAccount, setAccount.class.getName());
		hashmap.put(CMD.cmd_setLeftMenu, setLeftMenu.class.getName());
		hashmap.put(CMD.cmd_downloadVideo, DownloadVideo.class.getName());
		hashmap.put(CMD.cmd_PhoneInfo, PhoneInfo.class.getName());
		hashmap.put(CMD.cmd_checkVideo, CheckVideo.class.getName());
		hashmap.put(CMD.cmd_setCenterMenu, SetCenterMenu.class.getName());
		hashmap.put(CMD.cmd_cutAndCopy, CutAndCopy.class.getName());
		hashmap.put(CMD.cmd_getCourseVideo, GetCourseVideo.class.getName());
		hashmap.put(CMD.cmd_startBGVideo, StartBGVideo.class.getName());
		hashmap.put(CMD.cmd_stopBGVideo, StopBGVideo.class.getName());
		hashmap.put(CMD.cmd_ChangeScreenMode, ChangeScreenMode.class.getName());
		hashmap.put(CMD.cmd_setSearchBar, SetSearchBar.class.getName());
		hashmap.put(CMD.cmd_setHeaderRefreshing, SetHeaderRefreshing.class.getName());
		hashmap.put(CMD.action_hideNavigation, HideNavigation.class.getName());
		hashmap.put(CMD.cmd_deleteVideo, Delete_Video.class.getName());
		hashmap.put(CMD.cmd_OpenFile, OpenFile.class.getName());
		hashmap.put(CMD.CMD_setSignature, Signature.class.getName());
		hashmap.put(CMD.cmd_MessageDigest, MD5.class.getName());
		hashmap.put(CMD.cmd_cleanCacheAndCookie, CleanCache.class.getName());
		hashmap.put(CMD.cmd_getCache, getCache.class.getName());
		hashmap	.put(CMD.cmd_shaone, Sha1Encryption.class.getName());
		hashmap.put(CMD.cmd_AllowChangeScreen, AllowChangeScreen.class.getName());
	}

	/**
	 * 增加某些策略
	 *
	 * @param cmd
	 * @param className
	 */
	public ProxyCmd putCmd(String cmd, String className) {
		hashmap.put(cmd, className);
		return this.cmd;
	}

	/**
	 * 删除某些策略
	 *
	 * @param cmd
	 */
	public void deleteCmd(String cmd) {
		if (hashmap.containsKey(cmd))
			hashmap.remove(cmd);
	}

	/**
	 * 根绝包名，通过反射获取到类
	 *
	 * @param cmd
	 * @return
	 */
	public Class reflectMethod(final String cmd) {

		String className = hashmap.get(cmd);
//        if (className != null) {
//
//        }

		try {
			Class doCmdMethod = Class.forName(className);
			return doCmdMethod;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return ErrorMethod.class;
	}

	/**
	 * 根绝cmd获取策略
	 * @param cmd
	 * @return
	 */
//    public DoCmdMethod getMethod(final String cmd){
//        DoCmdMethod method = hashmap.get(cmd);
//        return null!=method?method:new DoCmdMethod() {
//            @Override
//            public String doMethod(WebView webView, Context context, String cmd, String params, String callBack) {
//                String msg = String.format("这个%s没有被定义!", cmd);
////                    ToastUtil.showLongToast(context, msg);
//                new Throwable(msg).printStackTrace();
//                return msg;
//            }
//        };
//
//    }

	/**
	 * 获取全部策略
	 *
	 * @return
	 */
	public Map<String, String> getMap() {
		return this.hashmap;
	}

	//使用时都通过这个接口调用，具体如何处理自己设计
//    Replace replace = hashmap.get(key);
//    replace.dealString();

}
