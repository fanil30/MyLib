package com.wang.android_lib.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * by 王荣俊 on 2016/6/8.
 */
public class ResourceUtil {

    public static int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static Uri getAssetsFile(String filePath) {
        return Uri.parse("file:///android_asset/" + filePath);
    }

}
