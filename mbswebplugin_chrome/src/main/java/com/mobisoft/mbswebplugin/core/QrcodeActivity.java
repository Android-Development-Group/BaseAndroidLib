package com.mobisoft.mbswebplugin.core;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.utils.ToastUtil;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

import static com.mobisoft.mbswebplugin.base.AppConfing.PERMISSIONS_REQUEST_CODE;


/**
 * Author：Created by fan.xd on 2016/8/30.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */
public class QrcodeActivity extends AppCompatActivity implements QRCodeView.Delegate {
    public static final String RESULT_STRING = "result";
    public static final int RESULT_QR_CODE = 102;
    private ZXingView mQRCodeView;
    private AlertDialog dialog;
    private ViewGroup ll_back;
    TextView scanTitle;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);
        initViews();

        initEvents();
    }


    protected void initViews() {
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)// 大于6.0 权限检查
            requestGalleryPermission();
        else {
            mQRCodeView.setDelegate(this);
            mQRCodeView.startSpot();
        }
        ll_back = (ViewGroup) findViewById(R.id.ll_back);
        scanTitle = (TextView) findViewById(R.id.tv_head_title);
        mQRCodeView.changeToScanBarcodeStyle();

    }

    protected void initEvents() {
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.open_flashlight) {
            mQRCodeView.openFlashlight();
        } else if (i == R.id.close_flashlight) {
            mQRCodeView.closeFlashlight();
        }
    }

    /**
     * 扫码成功
     *
     * @param result 二维码返回值
     */
    @Override
    public void onScanQRCodeSuccess(String result) {
        mQRCodeView.startSpot();
        Log.i("onScanQRCodeSuccess", "二维码：" + result);
        intent = getIntent();
        intent.putExtra("result", result);
        setResult(RESULT_OK, intent);
        finish();

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (intent != null) {
//            setResult(RESULT_QR_CODE, intent);
//        }
//    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        finish();
        ToastUtil.showLongToast(this, "打开相机出错，请重试");
    }

    private void requestGalleryPermission() {
        if (PermissionChecker.PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            mQRCodeView.setDelegate(this);
            mQRCodeView.startSpot();
        } else {
            // Ask for one permission
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
            mQRCodeView.setDelegate(this);
            mQRCodeView.startSpot();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mQRCodeView.stopCamera();
    }
}
