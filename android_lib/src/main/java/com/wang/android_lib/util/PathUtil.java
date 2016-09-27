package com.wang.android_lib.util;

import android.os.Environment;

import java.io.File;

/**
 *  by 王荣俊 on 2016/6/6.
 */
public class PathUtil {

    public static String getImageAlbumDir() {
        String s = File.separator;
        return Environment.getExternalStorageDirectory() + s + "DCIM" + s + "Camera" + s;
    }
}
