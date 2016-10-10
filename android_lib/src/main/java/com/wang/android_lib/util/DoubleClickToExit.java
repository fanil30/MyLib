package com.wang.android_lib.util;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Toast;

public class DoubleClickToExit {

    private static long exitTime = 0;

    /**
     * @return true-按了返回键。false-需要自行处理（自己返回super.onKeyDown）
     * 如：return DoubleClickToExit.onKeyDown(keyCode, event, this) || super.onKeyDown(keyCode, event);
     */
    public static boolean onKeyDown(int keyCode, KeyEvent event, Activity activity) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime >= 2000) {
                exitTime = System.currentTimeMillis();
                Toast.makeText(activity, "再按一次 退出程序", Toast.LENGTH_SHORT).show();
            } else {
                activity.finish();
            }
            return true;
        }
        return false;
    }

}
