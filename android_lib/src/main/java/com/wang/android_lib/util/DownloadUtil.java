package com.wang.android_lib.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import com.wang.java_util.TextUtil;

/**
 * by 王荣俊 on 2016/7/6.
 */
public class DownloadUtil {
    /**
     * Android调用系统内部的下载程序下载文件(二)
     * http://blog.csdn.net/whyrjj3/article/details/8000740
     */
    public static void startDownload(Context context, String fileUrl, String savePath) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(
                Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(fileUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);

//        设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE |
                DownloadManager.Request.NETWORK_WIFI);

//        禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：
//         android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
//        request.setShowRunningNotification(false);

//        不显示下载界面
//        request.setVisibleInDownloadsUi(false);

//        设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置。
//        如果sdcard可用，下载后的文件在/mnt/sdcard/Android/data/packageName/files目录下面。
//        如果sdcard不可用,设置了下面这个将报错，不设置。下载后的文件在/cache这个目录下面
        if (!TextUtil.isEmpty(savePath)) {
            request.setDestinationInExternalFilesDir(context, null, savePath);
        }

        long id = downloadManager.enqueue(request);
//        TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面

    }
}
