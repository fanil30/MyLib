package com.wang.android_lib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.wang.java_util.DebugUtil;
import com.wang.java_util.GsonUtil;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.JsonFormatUtil;
import com.wang.java_util.TextUtil;

import java.io.InputStream;

/**
 * by 王荣俊 on 2016/5/19.
 */
public class AndroidHttpUtil {

    public interface OnSucceedListener {
        void onSucceed(HttpUtil.Result r);
    }

    public interface OnFailedListener {
        void onFailed(HttpUtil.Result r);
    }

    /**
     * 使用AsyncTask启动新线程进行网络连接，界面会显示有模式的提示框，结束后调用listener接口来通知。
     * 并通过接口方法的参数返回结果返回。
     * 结果会以Notification显示
     *
     * @param dialogHint    弹出连接网络提示窗口时要显示的内容。若为空，则不显示提示窗口
     * @param showToastHint 联网后是否提示联网出错状态（如网络不可用）
     */
    public static void startRequest(final Context context, final String dialogHint,
                                    final boolean showToastHint,
                                    final String url, final String requestMethod,
                                    final String cookie, final String output,
                                    final OnSucceedListener succeedListener,
                                    final OnFailedListener failedListener) {

        if (!isNetworkConnected(context)) {
            if (showToastHint) {
                M.t(context, "网络不可用");
            }
            return;
        }

        String content = "url: \n" + url + "\n\n\ncookie: \n" + cookie + "\n\n\noutput: \n" + output;
        NotificationUtil.showNotification(context, 1, NotificationUtil.ICON_WARNING,
                "Log-Request", content, false);

        new AsyncTask<Void, Void, HttpUtil.Result>() {

            @Override
            protected void onPreExecute() {

                if (!TextUtil.isEmpty(dialogHint)) {
                    DialogUtil.showProgressDialog(context, "提示", dialogHint, false);
                }
            }

            @Override
            protected HttpUtil.Result doInBackground(Void... params) {

                return new HttpUtil.HttpRequest()
                        .setRequestMethod(requestMethod)
                        .setOutput(output)
                        .setCharsetName("utf-8")
                        .setCookie(cookie)
                        .request(url);
            }

            @Override
            protected void onPostExecute(HttpUtil.Result r) {
                DialogUtil.cancelProgressDialog();

                String s = JsonFormatUtil.formatJson(GsonUtil.toJson(r));
                DebugUtil.println("\n" + s);
                NotificationUtil.showNotification(context, 0, android.R.drawable.stat_sys_warning,
                        "Log-Response", s, false);

                if (r.state == HttpUtil.NO_INTERNET) {
                    if (showToastHint)
                        M.t(context, "服务器连接失败");
                } else if (r.state == HttpUtil.TIME_OUT) {
                    if (showToastHint)
                        M.t(context, "连接超时");
                } else if (r.state == HttpUtil.SERVER_ERROR) {
                    if (showToastHint)
                        M.t(context, "服务器异常");
                } else if (r.state == HttpUtil.UNKNOWN_ERROR) {
                    if (showToastHint)
                        M.t(context, "未知错误");
                } else if (r.state == HttpUtil.OK) {
                    if (r.responseCode == 200) {
                        if (succeedListener != null) {
                            succeedListener.onSucceed(r);
                            return;
                        }
                    } else {
                        if (showToastHint)
                            M.t(context, "服务器异常，响应代码：" + r.responseCode);
                    }
                }

//            除了结果为OK且状态吗为200以外，其他情况都执行onRequestFailed
                if (failedListener != null) {
                    failedListener.onFailed(r);
                }

            }

        }.execute();
    }

    /**
     * @param showToastHint 上传出错后是否toast提示状态
     * @param fileName      上传文件的文件名
     * @param is            上传文件的输入流
     */
    public static void startUpload(final Context context, final String dialogHint,
                                   final boolean showToastHint, final String url,
                                   final String fileName, final InputStream is,
                                   final OnSucceedListener succeedListener,
                                   final OnFailedListener failedListener) {

        if (!isNetworkConnected(context)) {
            M.t(context, "网络不可用");
            return;
        }

        NotificationUtil.showNotification(context, 1, NotificationUtil.ICON_WARNING,
                "Log-Request", "url: " + url + "\n\nfileName: \n" + fileName, false);

        new AsyncTask<Void, Void, HttpUtil.Result>() {

            @Override
            protected void onPreExecute() {

                if (!TextUtil.isEmpty(dialogHint)) {
                    DialogUtil.showProgressDialog(context, "提示", dialogHint, false);
                }
            }

            @Override
            protected HttpUtil.Result doInBackground(Void... params) {
                return HttpUtil.upload(url, "POST", fileName, is, "utf-8");

            }

            @Override
            protected void onPostExecute(HttpUtil.Result r) {
                DialogUtil.cancelProgressDialog();

                String s = JsonFormatUtil.formatJson(GsonUtil.toJson(r));
                DebugUtil.println("\n" + s);
                NotificationUtil.showNotification(context, 0, android.R.drawable.stat_sys_warning,
                        "Log-Response", s, false);

                if (r.state == HttpUtil.NO_INTERNET) {
                    if (showToastHint)
                        M.t(context, "网络不可用");
                } else if (r.state == HttpUtil.TIME_OUT) {
                    if (showToastHint)
                        M.t(context, "连接超时");
                } else if (r.state == HttpUtil.SERVER_ERROR) {
                    if (showToastHint)
                        M.t(context, "服务器连接失败");
                } else if (r.state == HttpUtil.UNKNOWN_ERROR) {
                    if (showToastHint)
                        M.t(context, "未知错误");
                } else if (r.state == HttpUtil.OK) {
                    if (r.responseCode == 200) {
                        if (succeedListener != null) {
                            succeedListener.onSucceed(r);
                            return;
                        }
                    } else {
                        if (showToastHint)
                            M.t(context, "服务器异常，响应代码：" + r.responseCode);
                    }
                }

//            除了结果为OK且状态吗为200以外，其他情况都执行onRequestFailed
                if (failedListener != null) {
                    failedListener.onFailed(r);
                }

            }

        }.execute();

    }

    /**
     * 检测网络是否可用
     * 需要权限：android.permission.ACCESS_NETWORK_STATE
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    public static final int NOT_CONNECT = 0;
    public static final int NETTYPE_WIFI = 1;
    public static final int NETTYPE_CMWAP = 2;
    public static final int NETTYPE_CMNET = 3;

    /**
     * 获取当前网络类型
     * http://www.cnblogs.com/lee0oo0/archive/2013/09/25/3339948.html
     *
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    public int getNetworkType(Context context) {
        int netType = NOT_CONNECT;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtil.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

}
