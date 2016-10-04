package com.wang.test;

import com.wang.java_util.FileUtil;
import com.wang.java_util.GsonUtil;
import com.wang.java_util.HttpUtil;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaLibTestClass {


    public static void main(String[] args) throws Exception {

        String path="http://124.172.176.163/xdispatch/cv2.jikexueyuan.com/html5/course_buju1/01/video/c288b_01_hd_h264_1280_720.mp4?download&e=1475258197&token=kvniiGWb17-XSxTMHiIVkclXvpopl7nuzl4ANRHA:pzOSnbBc90Qz0GP3un1CK1hVNFw=";
        
        String url = "http://www.jikexueyuan.com/course/288_1.html";
        String cookie = FileUtil.read("E:/jikexueyuan-cookie.txt");

        HttpUtil.HttpRequest httpRequest = new HttpUtil.HttpRequest();
        HttpUtil.Result r = httpRequest.setCookie(cookie).setFirefoxUserAgent().request(url);

        if (r.state == HttpUtil.OK) {
            System.out.println(r.result);
            FileUtil.write(r.result, "E:/course228.html");
        } else {
            GsonUtil.printFormatJson(r);
        }

    }

    public static void login(String verifyCode) throws Exception {

        String url = "http://123.207.87.197/homework/pc/login";
        String output = "userName=11111&password=11111&role=1&verifyCode=" + verifyCode;

        HttpUtil.HttpRequest httpRequest = new HttpUtil.HttpRequest();
        HttpUtil.Result r = httpRequest.setRequestMethod("POST").setOutput(output).request(url);

        if (r.state == HttpUtil.OK) {

            System.out.println(r.result);

        } else {
            GsonUtil.printFormatJson(r);
        }
    }

    public static void verify() throws Exception {

        String url = "http://123.207.87.197/homework/verifyCode";

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);


        InputStream is = conn.getInputStream();
        FileOutputStream fos = new FileOutputStream("E:/verifyCode.jpg");
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }

        System.out.println("finish");

    }

}
