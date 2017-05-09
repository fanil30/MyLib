package com.wang.test;

import com.wang.java_util.FileUtil;

import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

public class JavaLibTestClass {

    public static void main(String a[]) throws Exception {

    }

    @Test
    public void testFindChildrenUnderDir() throws Exception {
        File file = new File("E:\\ttpod");

        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
//                System.out.println("check : " + pathname.getAbsolutePath());
//                return TextUtil.getTextAfterLastPoint(pathname.getName()).equals("png");
                return pathname.getName().contains("2");
            }
        };

//        List<File> fileList = new ArrayList<>();
//        FileUtil.findChildrenUnderDir(file, fileList, filter);
        List<File> fileList = FileUtil.findChildrenUnderDir(file, filter);

        for (File f : fileList) {
            System.out.println(f.getAbsolutePath());
        }
    }

}
