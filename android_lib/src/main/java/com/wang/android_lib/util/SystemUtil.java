package com.wang.android_lib.util;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.List;

/**
 *  by 王荣俊 on 2016/6/29.
 */
public class SystemUtil {

    /**
     * 开启管设备激活器
     *
     * @param adminReceiverClass 继承DeviceAdminReceiver的类对象，onReceive可以无操作
     * @param hint               设备激活器界面显示的提示语
     */
    public static void openAdmin(Context context, Class adminReceiverClass, String hint) {
        // 创建一个Intent
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        // 我要激活谁
        ComponentName mDeviceAdminSample = new ComponentName(context, adminReceiverClass);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        // 劝说用户开启管理员权限
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, hint);
        context.startActivity(intent);
    }

    /**
     * 清除设备管理器的激活
     *
     * @param adminReceiverClass 继承DeviceAdminReceiver的类对象，onReceive可以无操作
     */
    public static void removeAdmin(Context context, Class adminReceiverClass) {
        ComponentName deviceAdmin = new ComponentName(context, adminReceiverClass);
        DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        manager.removeActiveAdmin(deviceAdmin);
    }

    /**
     * 判断是否处于设备设备管理器的激活状态
     */
    public static boolean isAdminActive(Context context, Class adminReceiverClass) {
        ComponentName deviceAdmin = new ComponentName(context, adminReceiverClass);
        DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        return manager.isAdminActive(deviceAdmin);
    }

    /**
     * 打开app安装界面
     */
    public static void installApp(Context context, String appPath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + appPath), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 打开app卸载界面
     */
    public static void uninstallApp(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    /**
     * 检查机子是否安装的有相关APK
     *
     * @param packageName 如：com.adobe.flashplayer
     */
    public static boolean checkInstallApk(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> infoList = pm
                .getInstalledPackages(PackageManager.GET_SERVICES);
        for (PackageInfo info : infoList) {
            if (info.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

}
