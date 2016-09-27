package com.wang.android_lib.util;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

/**
 * by 王荣俊 on 2016/7/20.
 */
public class WindowUtil {

    /**
     * 若安卓系统版本>=19，则设置透明状态栏
     * 在setContentView之前调用
     */
    public static void transStateBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}
