package com.mobisoft.mbswebplugin.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.mobisoft.mbswebplugin.Cmd.Working.UploadCB;
import com.mobisoft.mbswebplugin.MbsWeb.HybridWebView;
import com.mobisoft.mbswebplugin.R;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;
import com.mobisoft.mbswebplugin.utils.luban.CompressionPredicate;
import com.mobisoft.mbswebplugin.utils.luban.Luban;
import com.mobisoft.mbswebplugin.utils.luban.OnCompressListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

//import com.lzy.okserver.upload.UploadManager;

/**
 * 文件上传工具类
 *
 * @author Fan xuedong
 * @version V1.0
 * 2016年3月3日 下午12:39:21
 * UpLoadUtile上传文件
 */
public class UpLoadUtile {


	private static UpLoadUtile mLoad;
	private String url;
	private ArrayList<String> listImages;
	private JSONArray jsonArray;
	private int index;

	/**
	 * OKGo 上传文件
	 */
//    private UploadManager uploadManager;


	/**
	 * 获取当前上传工具类对象
	 *
	 * @return
	 */
	public static UpLoadUtile getInstance() {

		if (mLoad == null) {
			return mLoad = new UpLoadUtile();
		} else {
			return mLoad;
		}

	}

	/**
	 * 获取okGo 上传文件实例
	 *
	 * @return
	 */
//    public UploadManager getUploadManager() {
//        if (uploadManager == null) {
//            return uploadManager = UploadManager.getInstance();
//        } else {
//            return uploadManager;
//        }
//    }

	/**
	 * 图片压缩
	 */
	public String compress(Context context, String srcPath, int photoSize) {
		return getCompress(context, srcPath, photoSize);
	}

	/**
	 * 鲁班压缩文件
	 *
	 * @param context
	 * @param srcPath
	 * @param photoSize
	 */
	private void LuBanCompress(Context context, String srcPath, int photoSize, final OnCompressListener listener) {
		if (photoSize <= 0)
			photoSize = 500;
		Luban.with(context)
				.load(srcPath)
				.ignoreBy(photoSize)
				.setTargetDir(getPath(context))
				.filter(new CompressionPredicate() {
					@Override
					public boolean apply(String path) {
						return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
					}
				})
				.setCompressListener(new OnCompressListener() {
					@Override
					public void onStart() {
						// TODO 压缩开始前调用，可以在方法内启动 loading UI
						listener.onStart();
					}

					@Override
					public void onSuccess(File file) {
						// TODO 压缩成功后调用，返回压缩后的图片文件
						listener.onSuccess(file);
					}

					@Override
					public void onError(Throwable e) {
						// TODO 当压缩过程出现问题时调用
						listener.onError(e);
					}
				}).launch();
	}

	/**
	 * 常规途径获取压缩图片
	 *
	 * @param context
	 * @param srcPath
	 * @param photoSize
	 * @return
	 */
	private String getCompress(Context context, String srcPath, int photoSize) {
		if (photoSize <= 0) {
			photoSize = 100;
		}
		DisplayMetrics dm;
		String mypath = null;
		String path = getPath(context);
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

		float hh = dm.heightPixels;
		float ww = dm.widthPixels;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);

		opts.inJustDecodeBounds = true;
		opts.inJustDecodeBounds = false;
		int w = opts.outWidth;
		int h = opts.outHeight;
		int size = 0;
		if (w <= ww && h <= hh) {
			size = 1;
		} else {
			double scale = w >= h ? w / ww : h / hh;
			double log = Math.log(scale) / Math.log(2);
			double logCeil = Math.ceil(log);
			size = (int) Math.pow(2, logCeil);
		}
		opts.inSampleSize = size;
		bitmap = BitmapFactory.decodeFile(srcPath, opts);
		if (bitmap == null) {
			Log.e("UploadUtil", "bitmap is  null  srcPath is \n " + srcPath);
			return srcPath;
		}
//        bitmap = FileUtils.createWatermark(context,bitmap,"@国泰那些事",0);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int quality = 100;
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//        System.out.println(baos.toByteArray().length);

		while (baos.toByteArray().length > photoSize * 1024) {
			baos.reset();
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			quality -= 10;
//            System.out.println(baos.toByteArray().length);
		}
		try {

			mypath = path + System.currentTimeMillis() + ".jpg";
			baos.writeTo(new FileOutputStream(mypath));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baos.flush();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mypath == null ? "" : mypath;
	}

	/**
	 * 获取缓存路径
	 *
	 * @param context
	 * @return
	 */
	@NonNull
	private String getPath(Context context) {
		String path = Environment.getExternalStorageDirectory() + File.separator + context.getPackageName().toString() + File.separator + "NikeHead"
				+ File.separator; // 指定文件保存的路径 记得配置清单文件权限
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return path;
	}

	//    朱桂飞 2016/10/8 14:07:58
	public void postFileFile(final Context context, final File file, final String mParamter, final String picFunction, final UploadCB uploadCB) {
		index = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (listImages != null) {
					listImages.clear();
				} else {
					listImages = new ArrayList<>();

				}
				jsonArray = new JSONArray();

				if (!TextUtils.isEmpty(mParamter)) {
					try {
						JSONObject json = new JSONObject(mParamter);
						String url1 = json.optString("url");
						final String filename = json.optString("filename");
						final String buz_no = json.optString("buz_no");
						final String type = json.optString("type");
						final String waterMark = json.optString("waterMark");
						url = ProxyConfig.getConfig().getImageBaseUrl() + url1;
						Utils.getMainHandler().post(new Runnable() {
							@Override
							public void run() {
								sendPost(context, file, url, picFunction, uploadCB, 1, filename, buz_no, type, waterMark);
							}
						});

						Log.e("url", url);
					} catch (JSONException e) {
						e.printStackTrace();
						uploadCB.onUploadFinish(context.getString(R.string.upload_farlure));
					}
				} else {
					uploadCB.onUploadFinish(R.string.upload_param_is_null);

				}
			}
		}).start();

	}

	//    朱桂飞 2016/10/8 14:07:58

	/**
	 * @param context
	 * @param imagePaths
	 * @param uploadCB
	 * @param ares
	 */
	public void postFileFile(final Context context, final List<String> imagePaths, final UploadCB uploadCB, final String... ares) {
		index = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				final int size = imagePaths.size();

				if (listImages != null) {
					listImages.clear();
				} else {
					listImages = new ArrayList<>();
				}
				jsonArray = new JSONArray();

				if (!TextUtils.isEmpty(ares[0])) {
					try {
						JSONObject json = new JSONObject(ares[0]);
						String url1 = json.optString("url");
						int photoSize = json.optInt("size");
						final String filename = json.optString("filename");
						final String buz_no = json.optString("buz_no");
						final String type = json.optString("type");
						final String waterMark = json.optString("waterMark");
//                uploadCB.onUploadStart(pickPhotoCount);
						final String imageUrl = ProxyConfig.getConfig().getImageBaseUrl() + url1;
						for (int i = 0; i < size; i++) {
							String compress = UpLoadUtile.getInstance().compress(context, imagePaths.get(i), photoSize);
							final File compressFile = new File(compress);
							final int finalI = i;
							Utils.getMainHandler().post(new Runnable() {
								@Override
								public void run() {
									sendPost(context, compressFile, imageUrl, ares[1], uploadCB, size, filename, buz_no, type, waterMark);

								}
							});
							uploadCB.onUploadProgress(i, size);
							Log.e("url", imageUrl);


						}


					} catch (JSONException e) {
						e.printStackTrace();
						uploadCB.onUploadFinish(context.getString(R.string.upload_farlure));
//                ToastUtil.showShortToast(context, "上传图像失败！");
					}
				} else {
//            ToastUtil.showShortToast(context, "参数为空上传图像失败！");
					uploadCB.onUploadFinish(R.string.upload_param_is_null);

				}
			}
		}).start();


	}


	/**
	 * 将照片，转换未64位字符串
	 *
	 * @param context
	 * @param filePath    照片路径
	 * @param mParamter
	 * @param picFunction 回掉方法
	 * @param webView     朱桂飞 2016/10/8 14:07:58
	 */
	public void encodeBase64File(final Context context, final String filePath, final String mParamter, final String picFunction, final HybridWebView webView, final String selectPicNum) {
		try {

			String bytes = null;
			bytes = Base64Util.encodeBase64File(filePath);
			String a = "{" + "\"base64\"" + ":" + "\"" + bytes + "\"" + "," + "\"num\"" + ":" + "\"" + selectPicNum + " \"" + "}";
//            String josn2 = String.format("javascript:" + picFunction + "(" + "'%s')", a);
			webView.loadUrl(UrlUtil.getFormatJs(picFunction, a));


		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * 上传照片
	 *
	 * @param context
	 * @param file        文件
	 * @param imageUrl    js返回参数
	 * @param picFunction 回掉方法
	 * @param uploadCB
	 * @param size
	 * @param filename
	 * @param buz_no
	 * @param type
	 * @param waterMark   水印
	 */
	public void sendPost(Context context, final File file, String imageUrl, final String picFunction, final UploadCB uploadCB, final int size, String filename, String buz_no, String type, String waterMark) {
		try {
			if (file == null || !file.exists()) {
				uploadCB.onUploadComplete(null);

				return;
			}
			AsyncHttpClient client = new AsyncHttpClient();
			if (imageUrl.contains("https")) {
				client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
			}
			RequestParams requestParams = new RequestParams();
			File file1 = null;
			String name = file.getParent() + System.currentTimeMillis() + ".jpg";
			if (!TextUtils.isEmpty(waterMark)) {
				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
				Bitmap bitmap1 = FileUtils.createWatermark(context, bitmap, waterMark, 0);
				FileUtils.saveBitmap2file(bitmap1, name);
				file1 = new File(name);
				requestParams.put("file", file1);
			} else {
				requestParams.put("file", file);
			}

			requestParams.put("filename", filename);
			requestParams.put("buz_no", buz_no);
			requestParams.put("type", type);
			requestParams.put("waterMark", waterMark);
			client.post(imageUrl, requestParams, new AsyncHttpResponseHandler() {
				@Override
				public void onStart() {
					super.onStart();
					uploadCB.onUploadProgress(0, 1);
				}

				@Override
				public void onProgress(long bytesWritten, long totalSize) {
					super.onProgress(bytesWritten, totalSize);
					uploadCB.onUploadProgress(bytesWritten, totalSize);
				}

				@Override
				public void onSuccess(int i, Header[] headers, byte[] bytes) {
					index++;
					if (i == 200) {
						String imageInfo = new String(bytes);
						try {
							JSONObject image = new JSONObject(imageInfo);
							image.put("size", file.length());
							uploadCB.onUpLoadCallBack(image);

						} catch (JSONException e) {
							e.printStackTrace();
							uploadCB.onUploadError(e.getMessage());
						}
//						try {
//							JSONObject image = new JSONObject(new String(bytes));
//							jsonArray.put(image);
//							if (index == size) {
//								JSONObject jsonObject = new JSONObject();
//								jsonObject.put("images", jsonArray);
//								uploadCB.onUploadComplete(UrlUtil.getFormatJs(picFunction, jsonObject.toString()));
//							} else {
//								uploadCB.onUpLoadCallBack(new String(bytes));
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//							uploadCB.onUploadError(R.string.upload_farlure + e.getMessage());
//
//						}

					} else {
						uploadCB.onUploadError(R.string.upload_farlure);
//						if (!TextUtils.isEmpty(picFunction)) {
//							uploadCB.onUploadFinish(R.string.upload_farlure);
//						}
					}

				}

				@Override
				public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
					uploadCB.onUploadError(R.string.upload_farlure);
					//					index++;
//					if (index == size) {
//						if (jsonArray.length() <= 0) {
//							uploadCB.onUploadFinish(R.string.upload_farlure);
//							return;
//						}
//						JSONObject jsonObject = new JSONObject();
//						try {
//							jsonObject.put("images", jsonArray);
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//						uploadCB.onUploadComplete(UrlUtil.getFormatJs(picFunction, jsonObject.toString()));
//					}

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

//        Map<String, String> params = new HashMap<>(); params.put("id", "1");
//        //...如果有其他参数添加到这里
//         String request = uploadFile(file, url, params, "image");

//        HttpURLConnection uploadConnection = null;
//        DataOutputStream outputStream;
//        String boundary = "********";
//        String CRLF = "\r\n";
//        String Hyphens = "--";
//        int bytesRead, bytesAvailable, bufferSize;
//        int maxBufferSize = 1024 * 1024;
//        byte[] buffer;
//
//        try {
//            FileInputStream fileInputStream = new FileInputStream(file);
//            URL url = new URL(this.url);
//            uploadConnection = (HttpURLConnection) url.openConnection();
//            uploadConnection.setDoInput(true);
//            uploadConnection.setDoOutput(true);
//            uploadConnection.setRequestMethod("POST");
//
//            uploadConnection.setRequestProperty("Connection", "Keep-Alive");
//            uploadConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//            uploadConnection.setRequestProperty("uploaded_file", file.getAbsolutePath());
//
//            outputStream = new DataOutputStream(uploadConnection.getOutputStream());
//
//            outputStream.writeBytes(Hyphens + boundary + CRLF);
//
//            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + file.getAbsolutePath() + "\"" + CRLF);
//            outputStream.writeBytes(CRLF);
//
//            bytesAvailable = fileInputStream.available();
//            bufferSize = Math.min(bytesAvailable, maxBufferSize);
//            buffer = new byte[bufferSize];
//            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//            while (bytesRead > 0) {
//                outputStream.write(buffer, 0, bufferSize);
//                bytesAvailable = fileInputStream.available();
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//            }
//
//            outputStream.writeBytes(CRLF);
//            outputStream.writeBytes(Hyphens + boundary + Hyphens + CRLF);
//
//            InputStreamReader resultReader = new InputStreamReader(uploadConnection.getInputStream());
//            BufferedReader reader = new BufferedReader(resultReader);
//            String line = "";
//            String response = "";
//            while ((line = reader.readLine()) != null) {
//                response += line;
//            }
//
//            final String finalResponse = response;
//            if (uploadConnection.getResponseCode() == 200) {
//                String josn2 = String.format("javascript:" + picFunction + "(" + "'%s')", finalResponse);
//                view.loadUrl(josn2);
//                Log.e("url", josn2);
//                ToastUtil.showShortToast(context, "上传图像成功");
//            } else {
//                ToastUtil.showShortToast(context, "上传图像失败！");
//
//            }
//
//            fileInputStream.close();
//            outputStream.flush();
//            outputStream.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

	}


	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 10 * 1000;
	//超时时间
	private static final String CHARSET = "utf-8";
	private static final String BOUNDARY = UUID.randomUUID().toString();
	//边界标识随机生成
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data";
	//内容类型

	/**
	 * 上传文件 * @param file 文件 * @param RequestURL post地址 * @param params 除文件外其他参数 * @param uploadFieldName 上传文件key * @return
	 */
	public static String uploadFile(File file, String RequestURL, Map<String, String> params, String uploadFieldName) {
		String result = null;
		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = new StringBuffer();
			sb.append(getRequestData(params));
			if (file != null) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				sb.append("Content-Disposition: form-data; name=\"" + uploadFieldName + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
				sb.append(LINE_END);
			}
			dos.write(sb.toString().getBytes());
			if (file != null) {
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
				dos.write(end_data);
			}
			dos.flush();
			int res = conn.getResponseCode();
			Log.e(TAG, "response code:" + res);
			if (res == 200) {
				Log.e(TAG, "request success");
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				Log.i(TAG, "result : " + result);
			} else {
				Log.e(TAG, "request error");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 对post参数进行编码处理 * @param params post参数 * @return
	 */
	private static StringBuffer getRequestData(Map<String, String> params) {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				stringBuffer.append(PREFIX);
				stringBuffer.append(BOUNDARY);
				stringBuffer.append(LINE_END);
				stringBuffer.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
				stringBuffer.append(LINE_END);
				stringBuffer.append(URLEncoder.encode(entry.getValue(), CHARSET));
				stringBuffer.append(LINE_END);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}


	/**
	 * HttpUrlConnection支持所有Https免验证，不建议使用
	 *
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
//    private StringBuffer result = null;

//    public String cheCheckUpdate(String murl) {
//        try {
//            URL url = new URL(murl);
//            Log.i("Update", murl);
//            SSLContext context = SSLContext.getInstance("TLS");
//            context.init(null, new TrustManager[]{new TrustAllManager()}, null);
//            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
//            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String arg0, SSLSession arg1) {
//                    return true;
//                }
//            });
//            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.setDoOutput(false);
//            connection.setRequestMethod("GET");
//            connection.connect();
//            InputStream in = connection.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            String line = "";
//            result = new StringBuffer();
//            while ((line = reader.readLine()) != null) {
//                result.append(line);
//            }
//            in.close();
//        } catch (KeyManagementException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            return null;
//        } catch (NoSuchAlgorithmException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        } catch (ProtocolException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        }
////        System.out.println("返回数据Update:" + result.toString());
//
//        Log.e("Update", result.toString());
//
//
//        return result.toString();
//    }

//    public class TrustAllManager implements X509TrustManager {
//
//        @Override
//        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
//                throws CertificateException {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
//                throws CertificateException {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//            // TODO Auto-generated method stub
//            return null;
//        }
//    }


	/**
	 * 上传图片
	 *
	 * @param context
	 * @param filePath
	 * @param mParamter
	 * @param picFunction
	 * @param uploadCB
	 */
	public synchronized void postFile(final Context context, String filePath, final String mParamter, final String picFunction, final UploadCB uploadCB, final int total) {

		if (!TextUtils.isEmpty(mParamter)) {
			try {
				JSONObject json = new JSONObject(mParamter);
				String url1 = json.optString("url");
				final String filename = json.optString("filename");
				final String buz_no = json.optString("buz_no");
				final String type = json.optString("type");
				final String waterMark = json.optString("waterMark");
				final int size = json.optInt("size");
				url = ProxyConfig.getConfig().getImageBaseUrl() + url1;
				LuBanCompress(context, filePath, size, new OnCompressListener() {
					@Override
					public void onStart() {
						Log.e("LuBanCompress", "LuBanCompress onStart");
					}

					@Override
					public void onSuccess(File file) {
						sendPost(context, file, url, picFunction, uploadCB, total, filename, buz_no, type, waterMark);

					}

					@Override
					public void onError(Throwable e) {
						uploadCB.onUploadError(e.getMessage());
						Log.e("LuBanCompress", e.getMessage());

					}
				});
				Log.e("url", url);
			} catch (JSONException e) {
				e.printStackTrace();
				uploadCB.onUploadFinish(context.getString(R.string.upload_farlure));
			}
		} else {
			uploadCB.onUploadFinish(R.string.upload_param_is_null);

		}
	}


}
