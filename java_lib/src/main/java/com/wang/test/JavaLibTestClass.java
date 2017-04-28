package com.wang.test;

import com.wang.java_util.CharsetUtil;
import com.wang.java_util.StreamUtil;
import com.wang.java_util.TextUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaLibTestClass {

    public static void main(String a[]) throws Exception {
        String filePath = "E:/aaa/aaa.mp4";
        String url = "http://192.168.199.219:8080/uploadFile";
        String fileName = TextUtil.getTextAfterLastSlash(filePath);
        long totalSize = new File(filePath).length();

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);
        conn.addRequestProperty("fileName", CharsetUtil.encode(fileName));
        System.out.println("fileName:  " + fileName);

        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        FileInputStream fis = new FileInputStream(filePath);

        long currentSize = 0;
        int sizeOfEachUnit = 0;

        long previousTime = 0;

        byte[] buf = new byte[128];
        int len;
        while ((len = fis.read(buf)) != -1) {
            os.write(buf, 0, len);
            os.flush();
            currentSize += len;
            sizeOfEachUnit += len;
            long currentTime = System.currentTimeMillis();
            if (currentTime - previousTime > 500) {
                previousTime = currentTime;
                long speed = sizeOfEachUnit / 500;
                sizeOfEachUnit = 0;
                int progress = (int) (100.0 * currentSize / totalSize);
                System.out.println(currentSize + "/" + totalSize + "  " + speed + "  " + progress);
//                publishProgress(currentSize, totalSize, speed);
            }
        }
        System.out.println("upload finish");
        os.close();

        InputStream is = conn.getInputStream();
        String result = StreamUtil.readInputStream(is, "utf-8");
        is.close();

        System.out.println(result);
    }

}
