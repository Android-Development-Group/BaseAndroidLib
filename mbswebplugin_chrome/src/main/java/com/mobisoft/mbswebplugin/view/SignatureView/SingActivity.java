package com.mobisoft.mbswebplugin.view.SignatureView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobisoft.mbswebplugin.Cmd.DoCmd.Signature;
import com.mobisoft.mbswebplugin.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author：Created by fan.xd on 2017/7/5.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class SingActivity extends AppCompatActivity implements View.OnClickListener {


	private PaintView paintView;
	private String singPath;
	private FrameLayout paint_view;
	private ImageView imageView;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sing);
		String signature = getIntent().getExtras().getString("signature", "请投保人签名");
		paint_view = (FrameLayout) findViewById(R.id.paint_view);
		paintView = new PaintView(this);
		imageView = new ImageView(this);
		imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		TextView textView = new TextView(this);
		textView.setText(TextUtils.isEmpty(signature) ? "请投保人签名！" : signature);
		paint_view.addView(imageView);
		paint_view.addView(paintView);

		Toolbar toobar = (Toolbar) findViewById(R.id.toolbar_sing);
		toobar.findViewById(R.id.text_sure_sing).setOnClickListener(this);
		toobar.findViewById(R.id.text_cancel_sing).setOnClickListener(this);
		toobar.findViewById(R.id.text_rest_sing).setOnClickListener(this);
		Picasso.with(this)
				.load(getIntent()
						.getExtras()
						.getString("BGimageUrl", "op"))
				.into(imageView);
	}

	public void CreatePaintImage() {
		Bitmap cachebBitmap = paintView.getCachebBitmap();
		String file = createFile(cachebBitmap);
		File file1 = new File(file);
		if (file1.exists()) {
//            Toast.makeText(this, "已生成签名文件，路径：" + file, Toast.LENGTH_SHORT).show();
			singPath = file1.getAbsolutePath();
			Intent intent = new Intent();
			intent.putExtra("path", singPath);
			setResult(Signature.REQUEST_CODE_SING, intent);
			this.finish();
		} else {
			singPath = null;
			Toast.makeText(this, "签名文件生成失败！请退出重试！", Toast.LENGTH_SHORT).show();

		}


	}

	/**
	 * 创建手写签名文件
	 *
	 * @return
	 */
	private String createFile(Bitmap cachebBitmap) {


		// 获得图片的宽高.
		int width = cachebBitmap.getWidth();
		int height = cachebBitmap.getHeight();
		// 计算缩放比例.
		float scaleWidth = ((float) 213) / width;
		float scaleHeight = ((float) 96) / height;
		// 取得想要缩放的matrix参数.
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片.
		Bitmap newbm = Bitmap.createBitmap(cachebBitmap, 0, 0, width, height, matrix, true);

		ByteArrayOutputStream baos = null;
		String _path = null;
		try {
			String sign_dir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+File.separator+"Signature";
			_path = sign_dir + System.currentTimeMillis() + ".png";
			baos = new ByteArrayOutputStream();
			newbm.compress(Bitmap.CompressFormat.PNG, 60, baos);
			byte[] photoBytes = baos.toByteArray();
			if (photoBytes != null) {
				new FileOutputStream(new File(_path)).write(photoBytes);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			baos = null;
		}
		return _path;
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.text_sure_sing) {
			if (paintView == null || !paintView.isPaint()) {
				Toast.makeText(this, "签名后再确认！", Toast.LENGTH_SHORT).show();
				return;
			}
//                activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 201);
//                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 201);
			final AlertDialog mdialog = new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("确认生成签名文件？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							CreatePaintImage();
							dialog.dismiss();
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (paintView != null) {
								paintView.clear();
								setView();
							}

							Toast.makeText(SingActivity.this, "操作已取消！", Toast.LENGTH_SHORT).show();
							dialog.dismiss();
						}
					})
					.create();
			mdialog.show();


		} else if (i == R.id.text_cancel_sing) {
			this.finish();

		} else if (i == R.id.text_rest_sing) {
			paintView.clear();
			setView();

		}

	}

	private void setView() {
		paint_view.removeAllViews();
		paintView = new PaintView(this);
		paint_view.addView(imageView);
		paint_view.addView(paintView);
	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}
}
