package com.wang.web;

import com.wang.java_util.DateUtil;
import com.wang.java_util.FileUtil;

import java.io.File;

public class CopyClass {

    public static String buildClassDir = "Constraint:\\IDE\\android-studio-project\\MyLib\\java_lib\\";
    public static String serverName = "shopping_system";

    public static void main(String[] args) throws Exception {
        String fromDir = buildClassDir + "build/classes/main/";
        String toDir = "Constraint:/IDE/apache-tomcat-7.0.67/webapps/" + serverName + "/WEB-INF/";

        FileUtil.copyDir(new File(fromDir), new File(toDir));
        FileUtil.deleteDir(new File(toDir, "classes"));
        new File(toDir, "main").renameTo(new File(toDir, "classes"));

        fromDir = "Constraint:\\IDE\\android-studio-project\\MyLib\\java_lib\\build\\libs\\java_lib.jar";
        toDir = toDir + "lib/java_lib.jar";
        new File(toDir).delete();
        FileUtil.copy(fromDir, toDir);

        System.out.println(DateUtil.getCurrentTime());
        System.out.println("Web update succeed!");
    }

}
