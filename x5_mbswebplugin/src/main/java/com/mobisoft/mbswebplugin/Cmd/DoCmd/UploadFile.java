package com.mobisoft.mbswebplugin.Cmd.DoCmd;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.mobisoft.mbswebplugin.Cmd.DoCmdMethod;
import com.mobisoft.mbswebplugin.Cmd.Working.DefaultUploadCreator;
import com.mobisoft.mbswebplugin.Cmd.Working.UploadCB;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsRequestPermissionsListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsResultListener;
import com.mobisoft.mbswebplugin.MvpMbsWeb.Interface.MbsWebPluginContract;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.utils.ToastUtil;
import com.mobisoft.mbswebplugin.utils.UpLoadUtile;
import com.mobisoft.mbswebplugin.utils.Utils;
import com.mobisoft.mbswebplugin.view.ActionSheetDialog;
import com.werb.pickphotoview.PickPhotoView;
import com.werb.pickphotoview.util.PickConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static com.mobisoft.mbswebplugin.base.AppConfing.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.mobisoft.mbswebplugin.base.AppConfing.NICK_REQUEST_CAMERA_CODE;
import static com.mobisoft.mbswebplugin.base.AppConfing.PICK_IMAGE_ACTIVITY_REQUEST_CODE;


/**
 * Author：Created by fan.xd on 2017/3/1.
 * Email：fang.xd@mobisoft.com.cn
 * Description： 上传文件
 */

public class UploadFile extends DoCmdMethod implements MbsResultListener {
	private static final int REQUEST_CODE_CROP_IMAGE = 0x776;
	private static final int CAMERA_PHOTO_DATA = 0x9949;
	private static final int PICK_PHOTO_DATA = 0x5521;
	private Context context;
	private String cmd;
	/**
	 * 参数
	 */
	private String mParamter;
	/**
	 * 回掉方法
	 */
	private String callBack;
	/**
	 * 照片选择路径
	 */
	private String picFileFullName;
	private Activity context1;
	private boolean isCrop;
	private UploadCB uploadCB;
	private Uri cropImageUri;
	private File outCropFile;

	@Override
	public String doMethod(HybridWebView webView, final Context context, MbsWebPluginContract.View view, MbsWebPluginContract.Presenter presenter, String cmd, String params, String callBack) {
		// TODO 上传文件，需要后续补上
		this.context = context;
		this.cmd = cmd;
		this.mParamter = params;
		this.callBack = callBack;
		context1 = (Activity) context;
		uploadCB = new DefaultUploadCreator();
		uploadCB.create(context1, view, callBack, 0);
		presenter.setResultListener(UploadFile.this);
		JSONObject json = null;
		try {
			json = new JSONObject(params);
			isCrop = json.optBoolean("isCrop", false);
			final int photoSize = json.optInt("size", 1);
			final int pickPhotoCount = json.optInt("pickPhotoCount", 1);
			presenter.setMbsRequestPermissionsResultListener(new MbsRequestPermissionsListener() {
				@Override
				public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
					if (requestCode == 200) {
						if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
							getPic(pickPhotoCount);
						} else {
							ToastUtil.showShortToast(context, context.getString(R.string.lack_camera_permiss));
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
							android.Manifest.permission.CAMERA
					};
					ActivityCompat.requestPermissions(context1, permission, 200);
				} else {
//                    getPic(null);
					getPic(pickPhotoCount);

				}

			} else {
//                getPic(null);
				getPic(pickPhotoCount);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param pickPhotoCount
	 */
	private void getPic(final int pickPhotoCount) {
		new ActionSheetDialog(context)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem(
						context.getString(R.string.taker_photo), ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								takePhoto();
//                                uploadCB.onUploadStart(pickPhotoCount);

							}
						})
				.addSheetItem(context.getString(R.string.taker_album), ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								new PickPhotoView.Builder((Activity) context)
										.setPickPhotoSize(pickPhotoCount)   //select max size
										.setShowCamera(false)   //is show camera
										.setSpanCount(3)       //SpanCount
										.setLightStatusBar(true)  // custom theme
										.setToolbarTextColor(R.color.mbs_web_plugin_title_color)
										.setStatusBarColor(R.color.main_system_status_bar)   // custom statusBar
										.setToolbarColor(R.color.main_system_status_bar)   // custom toolbar
										.start();
//                                uploadCB.onUploadStart(pickPhotoCount);

							}
						}).show();


	}

	@Override
	public void onActivityResult(Context context, MbsWebPluginContract.View view, int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
			case PICK_IMAGE_ACTIVITY_REQUEST_CODE:
				Uri uri = data.getData();
				if (uri != null) {
					String realPath = Utils.getRealPathFromURI((Activity) context, uri);
					UpLoadUtile.getInstance().postFile(context, realPath, mParamter, callBack, uploadCB, 1);
//                    String compress = UpLoadUtile.getInstance().compress(context, realPath, 100);
//                    File f = new File(compress);
//                    UpLoadUtile.getInstance().postFileFile(
//                            context, f, mParamter, callBack, uploadCB);

				} else {
					Log.e("TAG", "从相册获取图片失败");
				}
				break;
			case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: // 启动相机拍照获取 照片
				if (resultCode == -1) {
					Log.e("TAG", "获取图片成功，本地路径是：" + picFileFullName);
					//发送图片
					if (TextUtils.isEmpty(picFileFullName)) {
						ToastUtil.showShortToast(context, context.getString(R.string.camera_failure));
						return;
					}

					Log.d("TAG", "图片的本地路径是：" + picFileFullName);
					if (isCrop) {
						cropImage();
					} else {
						uploadCB.onUploadStart(1);
						UpLoadUtile.getInstance().postFile(context, picFileFullName, mParamter, callBack, uploadCB, 1);
					}

				}


				break;
			case REQUEST_CODE_CROP_IMAGE: // 裁剪数据
//                Uri uri2 = data.getData();

//                String compress = UpLoadUtile.getInstance().compress(context, outCropFile.getAbsolutePath(), 100);
//                File f = new File(compress);
				uploadCB.onUploadStart(1);

				if (outCropFile.exists()) {
//					UpLoadUtile.getInstance().postFileFile(
//							context, outCropFile, mParamter, callBack, uploadCB);
					UpLoadUtile.getInstance().postFile(context, outCropFile.getPath(), mParamter, callBack, uploadCB, 1);

				} else {
					uploadCB.onUploadComplete(null);

				}


				break;
			case CAMERA_PHOTO_DATA:
				if (data == null) {
					uploadCB.onUploadComplete(null);
					return;
				}
				List<String> selectPaths = (List<String>) data.getSerializableExtra(PickConfig.INSTANCE.getINTENT_IMG_LIST_SELECT());

				if (isCrop && selectPaths.size() == 1) {
					picFileFullName = selectPaths.get(0);
					cropImage();

				} else {
					uploadCB.onUploadStart(selectPaths.size());
					for (int i = 0; i < selectPaths.size(); i++) {
						UpLoadUtile.getInstance().postFile(context, selectPaths.get(i), mParamter, callBack, uploadCB, selectPaths.size());
					}
				}

				break;
			case PICK_PHOTO_DATA:
				if (data == null) {
					uploadCB.onUploadComplete(null);
					return;
				}
				List<String> selectPaths2 = (List<String>) data.getSerializableExtra(PickConfig.INSTANCE.getINTENT_IMG_LIST_SELECT());

				final List<String> list = selectPaths2;
				final int total = list.size();
				if (isCrop && total == 1) {
					picFileFullName = list.get(0);
					cropImage();

				} else {
					uploadCB.onUploadStart(total);
					for (int i = 0; i < total; i++) {
						UpLoadUtile.getInstance().postFile(context, list.get(i), mParamter, callBack, uploadCB, total);
					}
				}

				break;
			case 0:
				uploadCB.onUploadComplete("");
				break;
			default:
				break;
		}
	}

	/**
	 * 裁剪图片
	 */
	private void cropImage() {

		File cropName = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		outCropFile = new File(cropName, "Crop" + System.currentTimeMillis() + ".jpg");

		Intent intent = new Intent("com.android.camera.action.CROP");
		Uri uri1;
		Uri photoURI;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			photoURI = Uri.fromFile(outCropFile);
			uri1 = getImageContentUri(picFileFullName);
		} else {
			uri1 = Uri.fromFile(new File(picFileFullName));
			photoURI = Uri.fromFile(outCropFile);
		}

		//可以选择图片类型，如果是*表明所有类型的图片
		intent.setDataAndType(uri1, "image/*");
		// 下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 100);
//        intent.putExtra("outputY", 100);
		//裁剪时是否保留图片的比例，这里的比例是1:1
		intent.putExtra("scale", true);
		//是否是圆形裁剪区域，设置了也不一定有效
		//intent.putExtra("circleCrop", true);

		//设置输出的格式
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outCropFile));
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

		intent = Intent.createChooser(intent, context.getString(R.string.vrop_image));


		context1.startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}


	/**
	 * @param type
	 */
	public void getPic(String type) {
		new ActionSheetDialog(context)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem(context.getString(R.string.taker_photo), ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								takePhoto();

							}
						})
				.addSheetItem(context.getString(R.string.taker_album), ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								selPhoto();
							}
						}).show();

	}


	/**
	 * 获取相册
	 */
	public void selPhoto() {
		if (Build.VERSION.SDK_INT < 23) {
			openAlbum();
		} else {
			//6.0
//            System.out.println("sdk 6.0");
			if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				//该权限已经有了
				openAlbum();
			} else {
				//申请该权限
				ActivityCompat.requestPermissions((Activity) this.context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x2222);
			}
		}
	}

	/**
	 * 打开本地相册
	 */
	public void openAlbum() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} else {
			intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		((Activity) this.context).startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	/**
	 * 获取图片
	 */
	public void takePhoto() {
		if (Build.VERSION.SDK_INT < 23) {
			takePicture();
		} else {
			//6.0
			if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
				//该权限已经有了
				takePicture();
			} else {
				//申请该权限
				((Activity) this.context).requestPermissions(new String[]{Manifest.permission.CAMERA}, NICK_REQUEST_CAMERA_CODE);
			}
		}
	}

	/**
	 * 拍照
	 */
	public void takePicture() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File outDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}
			File outFile = new File(outDir, "PAI" + System.currentTimeMillis() + ".jpg");
			picFileFullName = outFile.getAbsolutePath();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileProvider", outFile);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

			} else {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));

			}
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			((Activity) this.context).startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		} else {
			Log.e("TAG", "请确认已经插入SD卡");
		}
	}


	private Uri getImageContentUri(String path) {
		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[]{MediaStore.Images.Media._ID},
				MediaStore.Images.Media.DATA + "=? ",
				new String[]{path}, null);
		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
			Uri baseUri = Uri.parse("content://media/external/images/media");
			return Uri.withAppendedPath(baseUri, "" + id);
		} else {
			ContentValues contentValues = new ContentValues(1);
			contentValues.put(MediaStore.Images.Media.DATA, path);
			return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
		}
	}

}
