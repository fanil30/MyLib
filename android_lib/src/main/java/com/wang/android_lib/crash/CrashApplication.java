package com.wang.android_lib.crash;

import android.app.Application;

import com.wang.android_lib.util.M;

/**
 * http://blog.csdn.net/kevinmeng_ini58/article/details/7440810
 * Android中处理崩溃异常和记录日志
 */
public class CrashApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler =
                Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                M.t(getApplicationContext(), ex.toString());
                defaultUncaughtExceptionHandler.uncaughtException(thread, ex);
            }

        });
    }
}
