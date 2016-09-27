package com.wang.android_lib;

import com.wang.java_util.DateUtil;
import com.wang.java_util.FileUtil;

/**
 * by 王荣俊 on 2016/5/18.
 */
public class AndroidLibUpdateClass {

    public static void main(String[] a) throws Exception {
        String fromDir = "C:\\IDE\\android-studio-project\\MyLib\\android_lib\\build\\outputs\\aar\\";
        String fromPath = fromDir + "android_lib-debug.aar";
        String toDir = "app\\libs\\";
        String toPath = toDir + "android_lib-debug.aar";
        FileUtil.copy(fromPath, toPath);
        System.out.println("\n" + DateUtil.getCurrentTime() + "\nandroid_lib.aar update succeed!!!\n");
    }
}
