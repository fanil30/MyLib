package com.wang.test;

public class JavaLibTestClass {

    public static void main(String[] args) throws Exception {
        String s1 = "123\r\n456\r789\n012";
        String s2 = s1.replaceAll("[^\r]\\n", "\r\n");
        System.out.println(s1.replace("\r", "\\r").replace("\n", "\\n"));
        System.out.println(s2.replace("\r", "\\r").replace("\n", "\\n"));
    }

}
