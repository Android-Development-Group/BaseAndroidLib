package com.mobisoft.mbswebplugin.MvpMbsWeb;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.BaseWebActivity;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.Utils;
import com.mobisoft.mbswebplugin.view.progress.CustomDialog;
import com.tencent.smtt.sdk.TbsReaderView;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import cz.msebera.android.httpclient.Header;

/**
 * 加载显示PDF
 */
public class TbsReaderActivity extends BaseWebActivity implements TbsReaderView.ReaderCallback {

	public static final String TYPE = "TYPE";
	public static final String ISZOOM = "isZoom";
	private TbsReaderView mTbsReaderView;
	private String mFileName;
	private CustomDialog customDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tbs_reader);
		if (Build.VERSION.SDK_INT >= 21) {
			View decorView = getWindow().getDecorView();
			int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
			decorView.setSystemUiVisibility(option);
			getWindow().setStatusBarColor(Color.parseColor("#80000000"));
		}
		mTbsReaderView = new TbsReaderView(this, this);
		RelativeLayout rootRl = (RelativeLayout) findViewById(R.id.tbs_read_layout);
		rootRl.addView(mTbsReaderView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
		String mFileUrl = getIntent().getExtras().getString(MbsWebFragment.URL);
		String type = getIntent().getExtras().getString(TYPE, "pdf");
		mFileName = parseName(mFileUrl, type);
		File file = new File(getLocalFile(), mFileName);
		if (file.exists() && file.length() >= 1) {
			displayFile(file.getPath());
		} else {
			downLoadFile(mFileUrl);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTbsReaderView.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("文件阅读"); //手动统计页面("SplashScreen"为页面名称，可自定义)
//		MobclickAgent.onResume(this); //手动统计页面("SplashScreen"为页面名称，可自定义)

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("文件阅读"); //手动统计页面("SplashScreen"为页面名称，可自定义)
//		MobclickAgent.onPause(this); //手动统计页面("SplashScreen"为页面名称，可自定义)

	}

	@Override
	public void onCallBackAction(Integer integer, Object o, Object o1) {

		Log.e("TabsReader", integer + " ," + o + " ," + o1);
	}

	private void displayFile(String absoluteFile) {
		//增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
		String bsReaderTemp = "/storage/emulated/0/TbsReaderTemp";
		File bsReaderTempFile =new File(bsReaderTemp);

		if (!bsReaderTempFile.exists()) {
			boolean mkdir = bsReaderTempFile.mkdir();
			if (!mkdir) {
				Log.i("TabsReader", "创建/storage/emulated/0/TbsReaderTemp失败！！！！！");
			}
		}
		Bundle bundle = new Bundle();
		bundle.putString("filePath", absoluteFile);
		bundle.putString("tempPath", Environment.getExternalStorageDirectory().toString() + "/" + "TbsReaderTemp");
		boolean result = mTbsReaderView.preOpen(parseFormat(mFileName), false);
		if (result && mTbsReaderView != null && mTbsReaderView.getContext() != null) {
			mTbsReaderView.openFile(bundle);
		}

	}

	private void downLoadFile(String pdfUrl) {
		if (TextUtils.isEmpty(pdfUrl)) return;
		customDialog = new CustomDialog(this, R.style.ActionSheetDialogStyle);
		customDialog.show();
		customDialog.setMessage("正在加载文件....");
		File file = new File(getLocalFile(), mFileName);
		final AsyncHttpClient client;
		if (pdfUrl.startsWith("https")) {
			client = new AsyncHttpClient(true, 80, 443);//请求https的方式
		} else {
			client = new AsyncHttpClient();
		}

		client.get(pdfUrl, new FileAsyncHttpResponseHandler(file) {
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
				ToastUtil.showShortToast(TbsReaderActivity.this, "加载文件失败！");
				if (customDialog != null) customDialog.dismiss();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, File file) {
				displayFile(file.getPath());
				if (customDialog != null) customDialog.dismiss();

			}

			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				super.onProgress(bytesWritten, totalSize);
				if (customDialog != null && customDialog.isShowing()) {
					double s = (bytesWritten * 1.0 / totalSize) * 100;
					String sd = Utils.getDecimalFormat(s);
					customDialog.setMessage("已加载" + sd + "...");
				}

			}
		});
	}

	private File getLocalFile() {
		return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PDFTemp");
	}

	private String parseFormat(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	private String parseName(String url, String type) {
		String fileName = null;
		try {
			if (url.contains("?action"))
				fileName = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?action"));
			else if(url.contains("=")){
				fileName = url.substring(url.lastIndexOf("=") + 1);
			}else {
				fileName = url.substring(url.lastIndexOf("/") + 1);
			}
			if (!fileName.contains(".")) {
				if (TextUtils.isEmpty(type))
					fileName = fileName + ".pdf";
				else
					fileName = fileName + "." + type;
			}
		} finally {
			if (TextUtils.isEmpty(fileName)) {
				fileName = String.valueOf(System.currentTimeMillis());
			}
		}
		return fileName;
	}

	private String parseName(String url) {
		String fileName = null;
		try {
			if (url.contains("?action"))
				fileName = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?action"));
			else {
				fileName = url.substring(url.lastIndexOf("=") + 1);
			}
		} finally {
			if (TextUtils.isEmpty(fileName)) {
				fileName = String.valueOf(System.currentTimeMillis());
			}
		}
		return fileName;
	}
}
