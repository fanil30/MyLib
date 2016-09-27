package com.wang.android_lib.crash;

/**
 *  by 王荣俊 on 2016/7/2.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler crashHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (crashHandler == null) {
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }
    
}
