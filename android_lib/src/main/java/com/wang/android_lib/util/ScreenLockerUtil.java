package com.wang.android_lib.util;

import android.app.admin.DevicePolicyManager;
import android.content.Context;

/**
 *  by 王荣俊 on 2016/6/29.
 */
public class ScreenLockerUtil {

    public static void lock(Context context) {
        DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        manager.lockNow();
    }

}
