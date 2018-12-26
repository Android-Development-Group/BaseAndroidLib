package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsRequestPermissionsListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsResultListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.base.AppConfing;
import com.mobisoft.mbswebplugin.utils.Base64Util;
import com.mobisoft.mbswebplugin.utils.FileUtils;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.UrlUtil;
import com.mobisoft.mbswebplugin.utils.Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.mobisoft.mbswebplugin.utils.Utils.FILE_PATH;

/**
 * Author：Created by fan.xd on 2017/4/28.
 * Email：fang.xd@mobisoft.com.cn
 * Description：拍照上传照片
 */

public class Camera extends DoCmdMethod implements MbsResultListener {
    public static final int OPEN_GALLERY_CODE = 2001;
    public static final String ACTION = "xiangce.test.ccc.fxd.image";

    private MbsWebPluginContract.Presenter presenter;
    private Activity context1;
    /**
     * h5传递参数
     */
    private String params;
    /**
     * h5回掉方法
     */
    private String callBack;
    /**
     * 图片名称
     */
    private String filename;


    @Override
    public String doMethod(HybridWebView webView, final Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {

        context1 = (Activity) context;
        this.presenter = presenter;
        this.params = params;
        this.callBack = callBack;
        presenter.setResultListener(Camera.this);
        File file = new File(Utils.FILE_PATH);
        if (!file.exists()) {
            file.mkdir();
        }

        presenter.setMbsRequestPermissionsResultListener(new MbsRequestPermissionsListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCode == 200) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        showSelectDialog(context, context1);

                    } else {
                        ToastUtil.showShortToast(context, "缺少相关权限无法使此功能！");
                    }
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)// 大于6.0 权限检查
        {
            if (ContextCompat.checkSelfPermission(context1,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permission = new String[]{
                        Manifest.permission.CAMERA
                };
                ActivityCompat.requestPermissions(context1, permission, 200);
            } else {
                showSelectDialog(context, context1);
            }

        } else {
            showSelectDialog(context, context1);
        }

        return null;
    }

    @Override
    public void onActivityResult(final Context context, final MbsWebPluginContract.View view, final int requestCode, int resultCode, final Intent data) {
//            final String path = Utils.getAbsoluteImagePath(context, data.getData());
        /**
         * 选则相册
         */
        if (requestCode == AppConfing.PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (data == null || data.getData() == null) {
                Context applicationContext = context.getApplicationContext();
                ToastUtil.showShortToast(applicationContext, applicationContext.getString(R.string.getImage_error));
                return;
            }
            Uri uri = data.getData();

            final String path2 = Utils.copyPhotoToTemp(context1, uri);

            setImageSrc(path2, view, context);
        } else if (requestCode == AppConfing.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            /**
             * 选择 相机拍摄
             */
            File file = new File(FILE_PATH + filename + ".jpg");
            try {
                // 华硕的PAD报错 所以废除，具体表现为  uriString =null
//                String uriString = MediaStore.Images.Media.insertImage(context1.getContentResolver(),
//                        file.getAbsolutePath(), filename + ".jpg", null);
//                Uri u = Uri.parse(uriString);
//                final String path3 = Utils.copyPhotoToTemp(context1, u);
                final String path3 = Utils.copyPhotoToTemp(context1,   file.getAbsolutePath());
                setImageSrc(path3, view, context);

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                FileUtils.deleteFile(file);
            }

        }
    }

    /**
     * 给webView设置图片
     *
     * @param path
     * @param view
     * @param context
     */
    private void setImageSrc(final String path, final MbsWebPluginContract.View view, final Context context) {
        try {
            File file = new File(path);
            String fileName = file.getParent() + System.currentTimeMillis() + ".jpg";


            Calendar cd = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd EEE HH:mm:ss");
            String waterMark = sdf.format(cd.getTime());

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            Bitmap bitmap1 = FileUtils.createWatermark(context, bitmap, "-", 0);
            FileUtils.saveBitmap2file(bitmap1, fileName);

            String base64File = Base64Util.encodeBase64File(fileName);
            JSONObject object = new JSONObject();
            object.put("base64", base64File);
            object.put("path", fileName);
            final String json = object.toString();
            view.loadUrl(UrlUtil.getFormatJs(callBack, json));
            FileUtils.deleteFile(path);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示打开相机相册窗口
     *
     * @param context
     * @param context1
     */
    private void showSelectDialog(final Context context, final Activity context1) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HH_mmss");
        Date date = new Date(System.currentTimeMillis());
        filename = format.format(date);
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        File file = new File(FILE_PATH + filename + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String fileProvider = context.getPackageName() + ".fileProvider";
//                                    String fileProvider = "com.mobisoft.mbsDemo.fileProvider";
            uri = FileProvider.getUriForFile(context, fileProvider, file);
        } else
            uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        (context1).startActivityForResult(intent, AppConfing.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }


}
