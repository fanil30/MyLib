package com.wang.test;

import com.wang.java_util.CharsetUtil;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.TextUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaLibTestClass {

    public static void main(String[] args) throws Exception {

        String url = "http://www.imooc.com/learn/515";
//        String cookie = FileUtil.read("E:/lesson/imooc.txt");

//        Imooc imooc = new Imooc(url, Imooc.VIDEO_QUALITY_L, null);
//        Course course = imooc.getCourse();
//        String json = GsonUtil.formatJsonHtmlEscaping(course);
//        System.out.println(json);

        HttpUtil.Result r = HttpUtil.request(url);
        System.out.println(r.result);

    }

    public static void download(String[] args) throws Exception {

        String url = "http://v2.mukewang.com/1c4a8fe3-1fa4-44a5-9b4b-31e6d71f39f9/L.mp4?" +
                "auth_key=1474686275-0-0-f9e7f72c71fe6e6372333f2b08738247";

        String dir = "E:/lesson/";

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        int size = conn.getContentLength();

        if (size <= 0) {
            System.out.println("size<=0");
            return;
        }

        InputStream is = conn.getInputStream();
        byte[] buffer = new byte[1024];
        int len;
        long divideTime = 1000;
        int currSize = 0;
        FileOutputStream fos = new FileOutputStream(dir + "LL.mp4");
        long former = System.currentTimeMillis();
        while ((len = is.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            currSize += len;
            if (System.currentTimeMillis() - former >= divideTime) {
                former = System.currentTimeMillis();
                System.out.println("��ȣ�" + 1f * currSize / size * 100 + "%   " +
                        "����ɣ�" + TextUtil.transferFileLength(currSize, 1) + "   " +
                        "�ܴ�С��" + TextUtil.transferFileLength(size, 1)
                );
            }
        }
        fos.close();

    }

    public static void code3() {

        String url = "http://wangrongjun.cn/test/uploadText";
        String output = "text=" + CharsetUtil.encode("hello_���ٿ�");
        String cookie = "...";
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14";

        HttpUtil.HttpRequest httpRequest = new HttpUtil.HttpRequest();

        HttpUtil.Result result = httpRequest.setConnectTimeOut(3000).setReadTimeOut(3000).
                setCharsetName("utf-8").setRequestMethod("POST").setCookie(cookie).setOutput(output).
                addRequestProperty("User-Agent", userAgent).request(url);

        if (result.state == HttpUtil.OK) {
            System.out.println(result.result);
        } else {
            System.out.println("���ʳ��?�����룺" + result.state);
        }

    }

    public static void code2() {

        String url = "http://wangrongjun.cn/test/uploadText";
        String output = "text=" + CharsetUtil.encode("hello_���ٿ�");
        String cookie = "...";
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14";

        HttpUtil.Result result = HttpUtil.request(url, output, "POST");

        if (result.state == HttpUtil.OK) {
            System.out.println(result.result);
        } else {
            System.out.println("���ʳ��?�����룺" + result.state);
        }

    }

    public static void code1() {

        String url = "http://wangrongjun.cn/test/uploadText";
        String output = "text=" + CharsetUtil.encode("hello_���ٿ�");
        String cookie = "...";
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14";

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", userAgent);
            conn.setRequestProperty("Set-Cookie", cookie);

            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            osw.write(output);
            osw.close();

            InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "utf-8");
            char[] buf = new char[1024];
            StringBuilder builder = new StringBuilder();
            int len;
            while ((len = isr.read(buf)) != -1) {
                builder.append(new String(buf, 0, len));
            }
            isr.close();

            String response = builder.toString();
            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
