package com.mobisoft.mbswebplugin.Cmd.Working;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Environment;
import android.util.Log;

import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.SafeDialogOper;
import com.mobisoft.mbswebplugin.proxy.Cache.MyHostnameVerifier;
import com.mobisoft.mbswebplugin.proxy.Cache.MyTrustManager;
import com.mobisoft.mbswebplugin.proxy.tool.YUtils;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class DefaultDownloadCreator implements DownloadCB {
    private ProgressDialog dialog;
    private Activity activity;

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

    }

    @Override
    public void downloadFile(final String fileUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(fileUrl);
                    if(fileUrl.contains("https")){
                        SSLContext sc = SSLContext.getInstance("TLS");
                        sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
                        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                        HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
                    }
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    connection.setUseCaches(false);
                    final long contentLength = connection.getContentLength();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String fileName = YUtils.getFileName(fileUrl);
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                + File.separator + "Download" + File.separator + "MBS");
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        final File fileDown = new File(file.getAbsolutePath() + File.separator + fileName);
                        FileOutputStream fileOutputStream = new FileOutputStream(fileDown);
                        byte[] buffer = new byte[1024];
                        int length = 0;
                        long offset = 0;
                        long start = System.currentTimeMillis();

                        while ((length = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, length);
                            offset += length;
                            long end = System.currentTimeMillis();
                            if (end - start > 1000) {
                                onUpdateProgress(offset, contentLength);
                            }
                        }

                        connection.disconnect();
                        fileOutputStream.close();
                        connection = null;
                        onUpdateComplete(fileDown);

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    onUpdateError(null);
                } catch (IOException e) {
                    e.printStackTrace();
                    onUpdateError(null);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void downloadCancel(String url) {
        SafeDialogOper.safeDismissDialog(dialog);
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
        dialog.setCanceledOnTouchOutside(false);
        SafeDialogOper.safeShowDialog(dialog);
    }

    @Override
    public void onUpdateComplete(final File file) {

        Utils.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                SafeDialogOper.safeDismissDialog(dialog);
                if (null != file)
                    ToastUtil.showShortToast(activity, activity.getString(R.string.download_finish) + file.getAbsolutePath());
                activity = null;
            }
        });

    }
}
