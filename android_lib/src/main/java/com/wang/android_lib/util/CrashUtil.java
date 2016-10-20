package com.wang.android_lib.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.wang.java_util.GsonUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

/**
 * by 王荣俊 on 2016/10/9.
 */
public class CrashUtil {

    /**
     * @param showActivity 显示错误信息的界面，必须设置android:process=":your_tag"
     * @param showTag      intent.putExtra(showTag, message);
     */
    public static Thread.UncaughtExceptionHandler getUncaughtExceptionHandler(
            final Context context, final Class<? extends Activity> showActivity, final String showTag) {

        return new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {

                ex.printStackTrace();

                String error = getError(ex);
                String packageInfo = getPackageInfo(context);
                String mobileInfo = getMobileInfo();

                String message = error + "\n\n\n" + mobileInfo + "\n\n\n" + packageInfo;

//                NotificationUtil.showNotification(context, 0, "Error", message);

                Intent intent = new Intent(context, showActivity);
                intent.putExtra(showTag, message);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

                android.os.Process.killProcess(android.os.Process.myPid());

            }
        };
    }

    private static String getMobileInfo() {
        StringBuilder builder = new StringBuilder();
        Field[] fields = Build.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            String name = fields[i].getName();
            String value = null;
            try {
                value = fields[i].get(null).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            builder.append(name + " : " + value + "\n");
        }
        return builder.toString();
    }

    private static String getPackageInfo(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return GsonUtil.formatJson(info);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getError(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        return stringWriter.toString();
    }

}
