package com.wang.android_lib.util;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Toast;

public class DoubleClickToExit {

    private static long exitTime = 0;

    public static boolean onKeyDown(int keyCode, KeyEvent event, Activity activity) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime >= 2000) {
                exitTime = System.currentTimeMillis();
                Toast.makeText(activity, "再按一次 退出程序", Toast.LENGTH_SHORT).show();
            } else {
                activity.finish();
            }
        }

        return true;
    }

}
