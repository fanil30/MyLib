package com.wang.java_program.android_decompiling;

import com.wang.java_util.ConfigUtil;
import com.wang.java_util.FileUtil;
import com.wang.java_util.TextUtil;

import java.io.File;
import java.io.IOException;

/**
 * by wangrongjun on 2017/5/3.
 */
public class GetAllFileNameUnderDir {

    static class Config {
        public static boolean ignoreExtendName = false;
        public static String matchSign = "@#$";
        public static String pattern = "@#$";
    }

    public static void main(String[] a) throws IOException {

        ConfigUtil.read(GetAllFileNameUnderDir.Config.class, "config.txt", true);

        File dir = new File("dir");
        System.out.println("current path : " + dir.getAbsolutePath());

        if (!dir.exists()) {
            boolean b = dir.mkdirs();
            System.out.println("dir not exists");
            System.out.println(b ? "already create dir" : "create dir failed");
            return;
        }

        String[] fileNameList = dir.list();
        if (fileNameList == null || fileNameList.length == 0) {
            System.out.println("the number of children files is 0");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (String fileName : fileNameList) {
            if (Config.ignoreExtendName) {
                fileName = TextUtil.getTextExceptLastPoint(fileName);
            }
            String s = Config.pattern.replace(Config.matchSign, fileName);
            builder.append(s).append("\r\n");
        }
        String s = builder.toString();
        System.out.println("AllFileNameUnderDir:\n");
        System.out.println(s);
        FileUtil.write(s, "GetAllFileNameUnderDir.txt");
        System.out.println("\n\nwrite all file name finish");
    }

}
