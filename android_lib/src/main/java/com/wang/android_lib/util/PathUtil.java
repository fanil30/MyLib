package com.wang.android_lib.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * by 王荣俊 on 2016/6/6.
 */
public class PathUtil {

    public static String getImageAlbumDir() {
        String s = File.separator;
        return Environment.getExternalStorageDirectory() + s + "DCIM" + s + "Camera" + s;
    }

    /**
     * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/0821/1660.html
     * android图片文件的路径地址与Uri的相互转换
     */
    public static String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
