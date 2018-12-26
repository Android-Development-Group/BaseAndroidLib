package com.mobisoft.mbswebplugin.utils;

import android.text.TextUtils;
import android.util.Log;

import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

/***
 * url解析工具类
 */
public class UrlUtil {
    /***
     *  利用正则表达式来解析url
     *  取得 param 参数 action 命令 cmd命令
     * @param url 地址
     * @return Map
     */
    public static Map<String, String> parseUrl(String url) {
        String param = url.substring(url.indexOf("?") + 1);
        param = param.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        String[] list = param.split("&");
        Map<String, String> result = new HashMap<String, String>();
        for (String str : list) {
            int first = str.indexOf("=");
            if (first != -1) {
                String[] keyval = new String[]{str.substring(0, first), str.substring(first + 1, str.length())};
                if (keyval.length == 2) {
                    try {
                        String pram = URLDecoder.decode(keyval[1], "UTF-8");
                        result.put(keyval[0], pram);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (Map.Entry<String, String> entry : result.entrySet()) {
            Log.i("parseUrl", "parseUrl()===key:" + entry.getKey() + "  value:" + entry.getValue());
        }

        return result;
    }


    /**
     * 判断是否为网址
     *
     * @param pInput
     * @return Boolean
     */
    public static Boolean isNewUrl(String pInput) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher1 = patt.matcher(pInput);
        boolean isMatch = matcher1.matches();
        return isMatch;
    }

    /**
     * 正则表达式匹配
     */
    private static Pattern ACTION = Pattern.compile("\\baction=(\\w+)\\b");

    /**
     * 截取action
     *
     * @param url
     * @return Param
     */
    private static String getActionParam(String url) {
        Matcher matcher = ACTION.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * 格式化 javascript
     *
     * @param function 回掉方法
     * @param paramter 参数
     * @return javascript方法
     */
    public static String getFormatJavascript(String function, String paramter) {
        return String.format("javascript:" + function + "('%s')", paramter);
    }


    /**
     * 格式化 javascript
     *
     * @param callBack 回掉方法
     * @param json     参数
     * @return javascript方法
     */
    public static String getFormatJs(String callBack, String json) {
        if (TextUtils.isEmpty(json)) {
            json = "{}";
        }
        String newFunction = callBack;

        if (callBack.contains("#result#")) {
            newFunction = callBack.replace("#result#",json);
        } else if (callBack.endsWith("(")) {
            newFunction = callBack.substring(0, callBack.length() - 1);
        } else {
            String json2 = String.format("javascript:" + newFunction + "('%s')", json);
            return json2;
        }
//		String function = callBack.replace("#result#", TextUtils.isEmpty(json)?"":json);
        String json1 = String.format("javascript:%s", newFunction);
        return json1;
    }

    /**
     * 格式化 javascript
     *
     * @param callBack 回掉方法
     * @param object   参数
     * @return javascript方法
     */
    public static String getFormatObj(String callBack, Object object) {
        String newFunction = callBack;

        if (callBack.contains("#result#")) {
            newFunction = callBack.replace("#result#", object == null ? "" : object.toString());
        } else if (callBack.endsWith("(")) {
            newFunction = callBack.substring(0, callBack.length() - 1);

        } else {
            String json2 = String.format("javascript:" + newFunction + "('%s')", object);
            return json2;
        }
//		String function = callBack.replace("#result#", TextUtils.isEmpty(json)?"":json);
        String json1 = String.format("javascript:%s", newFunction);
        return json1;
    }

    /**
     * 获取相对路径
     *
     * @param url
     * @return
     */
    public static String getUrlPath(String url) {
        int n = url.indexOf("?action");
        String path = url.replace("http:", "/");
        if (n > 0) {
            path = path.substring(0, n);
        }
        return path;
    }

    /**
     * 获取相对路径
     *
     * @param url
     * @return
     */
    public static String getLocalPath(String url) {
        String path = url;
        if (url.startsWith("http")) {
            path = url.replace(ProxyConfig.getConfig().getNetUrl(), ProxyConfig.getConfig().getLocalUlr());
        }
        if (url.contains("?")) {
            path = path.substring(0, path.indexOf("?"));
        }
        return path;
    }

    /**
     * 获取相对路径
     *
     * @param url
     * @return
     */
    public static String getNetPath(String url) {
        String path = url;
        if (url.startsWith("file:///catGateWay")) {
            path = url.replace("file:///catGateWay", ProxyConfig.getConfig().getBaseUrl());

        } else if (url.startsWith("file://")) {
            path = url.replace(ProxyConfig.getConfig().getLocalUlr(), ProxyConfig.getConfig().getBaseUrl());
        }
        return path;
    }


    /**
     * 读取响应头
     *
     * @param file
     * @return
     */
    public static String getResponseHeader(File file) {
        String path = file.getAbsolutePath();
        boolean isHtml = path.contains(".html");
        Log.e(TAG, "url地址，getResponseHeader：" + path);

        StringBuilder sbResponseHeader = new StringBuilder();

        sbResponseHeader.append("HTTP/1.1 200 OK");
        sbResponseHeader.append("\n");
        sbResponseHeader.append("Server: ");
        sbResponseHeader.append("mobisoft/1.13.5");
        sbResponseHeader.append("\n");
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 设置时区为GMT  +8为北京时间东八区
        String date = sdf.format(cd.getTime());
        System.out.println(date);
        sbResponseHeader.append("Date: ");
//        sbResponseHeader.append("Fri, 17 Feb 2017 11:29:57 GMT");
        sbResponseHeader.append(date);
        sbResponseHeader.append("\n");

        sbResponseHeader.append("Content-Type: ");
        String contentType = getContentType(path);

        sbResponseHeader.append(contentType);
        sbResponseHeader.append("\n");
        Log.e(TAG, "url地址，contentType：" + contentType);

        sbResponseHeader.append("Content-Length: ");
        sbResponseHeader.append(file.length());
        sbResponseHeader.append("\n");


        sbResponseHeader.append("Last-Modified: ");
        sbResponseHeader.append(date);
        sbResponseHeader.append("\n");


        sbResponseHeader.append("Connection: ");
        sbResponseHeader.append("close");
        sbResponseHeader.append("\n");

        sbResponseHeader.append("ETag: ");
        try {
            String etag = MD5Util.getFileMD5String(file);
            sbResponseHeader.append(etag);
        } catch (IOException e) {
            e.printStackTrace();
            sbResponseHeader.append(e.getMessage());
        }
        sbResponseHeader.append("\n");

        if (isHtml) {
            sbResponseHeader.append("Access-Control-Allow-Origin: *\n" +
                    "Access-Control-Allow-Methods: GET, POST, OPTIONS\n");
        }

//
        if (isHtml) {
            sbResponseHeader.append("Accept-Encoding: ");
            sbResponseHeader.append("identity");
        } else {
            sbResponseHeader.append("Accept-Ranges: ");
            sbResponseHeader.append("bytes");
        }
        sbResponseHeader.append("\n");
        sbResponseHeader.append("\n");


        return sbResponseHeader.toString();
    }

    private static String getContentType(String url) {
        String name = getFileExtensionFromUrl(url);
        switch (name) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "png":
            case "jpg":
                return "image/png";
            case "js":
                return "application/x-javascript";
            case "ico":
                return "image/x-icon";
            default:
                return "application/octet-stream";
        }
    }

    public static String getFileExtensionFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }

            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }

            int filenamePos = url.lastIndexOf('/');
            String filename =
                    0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if (!filename.isEmpty() &&
                    Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }

        return "";
    }


}
