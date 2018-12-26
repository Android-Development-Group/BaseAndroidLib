package com.mobisoft.mbswebplugin.proxy.DownZipWork;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.mobisoft.mbswebplugin.Cmd.Working.DownloadCB;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.SafeDialogOper;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;
import com.mobisoft.mbswebplugin.proxy.Work.DownloadSrcCallback;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.Utils;
import com.mobisoft.mbswebplugin.utils.ZipUtils;

import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;


/**
 * Author：Created by fan.xd on 2017/8/30.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class DownLoadZipCB implements DownloadCB {

    private ProgressDialog dialog;
    private Activity activity;
    private DownloadSrcCallback callback;

    public DownLoadZipCB(Activity activity) {
        this.activity = activity;
    }

    public DownLoadZipCB(Activity activity, DownloadSrcCallback callback) {
        this.activity = activity;
        this.callback = callback;

    }

    public void setCallback(DownloadSrcCallback callback) {
        this.callback = callback;
    }

    public DownLoadZipCB() {
    }

    @Override
    public void create(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            Log.e("DownDialogCreator--->", "show download dialog failed:activity was recycled or finished");
            return;
        }
        this.activity = activity;
        dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.setCancelable(true);
        dialog.setMessage(activity.getString(R.string.update_src));
        dialog.setCanceledOnTouchOutside(false);
        SafeDialogOper.safeShowDialog(dialog);
    }


    @Override
    public void onUpdateProgress(final long current, final long total) {
        Utils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                int percent = (int) (current * 1.0f / total * 100);
                dialog.setProgress(percent);
            }
        });

    }

    @Override
    public void onUpdateError(Throwable t) {
        SafeDialogOper.safeDismissDialog(dialog);
        if(callback!=null){
            callback.downLoadFailure("下载资源文件失败！");
        }
    }


    @Override
    public void onUpdateComplete(final File file) {

        SafeDialogOper.safeDismissDialog(dialog);
        if (null != file){
            ToastUtil.showShortToast(activity, activity.getString(R.string.download_finish) + file.getAbsolutePath());
            try {

              boolean s =   ZipUtils.unzipFile(file.getPath(),ProxyConfig.getConfig().getH5Path());
                Log.e("DownloadZipTool",s?"解压完毕":"解压是失败！！！！！！！！！！");
                if(callback!=null && s){
                    callback.downLoadFinish();
                }else if(callback!=null && !s){
                    callback.downLoadFailure("解压资源文件失败！");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("DownloadZipTool", e.getMessage());
                if(callback!=null){
                    callback.downLoadFailure("解压资源文件失败！"+e.getMessage());
                }
            }
        }
        activity = null;

    }

    @Override
    public void downloadFile(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setResponseTimeout(5 * 1000);
        File file = new File(activity.getCacheDir(), url.substring(url.lastIndexOf("/") + 1));

        if (url.contains("https")) {
            client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        }
        client.get(url, new FileAsyncHttpResponseHandler(file) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Log.e("DownloadZipTool", "statusCode: " + statusCode);
                Log.e("DownloadZipTool", "throwable: " + throwable.getMessage());
                // 下载成功后需要做的工作
                onUpdateError(throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                if (statusCode == 200) {
                    Log.i("DownloadZipTool", "文件下载路径：" + file.getAbsolutePath() + "\n 名称：" + file.getName() + "\n 大小：" + file.length() / 1024 + " KB");
                    onUpdateComplete(file);
                } else {
                    onUpdateError(null);                }
                // 下载成功后需要做的工作
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
//                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                // 下载进度显示
                onUpdateProgress(bytesWritten,totalSize);
                Log.e("下载 Progress>>>>>", bytesWritten + " / " + totalSize);
            }
        });
    }

    @Override
    public void downloadCancel(String url) {
        SafeDialogOper.safeDismissDialog(dialog);
    }
}
