package com.wang.java_util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 王荣俊 on 2016/5/26.
 */
public class BinaryUtil {

    public static String toBinaryString(byte b) {
        int n = b & 0xff;  //一定要进行按位与获取低8位，因为byte类型数据有八位，如果正常转换为int，
        // byte从低到高第8位会成为int从低到高第32位(符号位)，而int的第八位为0。
        String s = "";
        int m = 128;
        while (m > 0) {
            if (n >= m) {
                n -= m;
                s += "1";
            } else {
                s += "0";
            }
            m /= 2;
        }
        return s;
    }

    public static String toBinaryString(byte[] bytes, String separator) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(toBinaryString(b) + (separator == null ? "" : separator));
        }
        return builder.toString();
    }

    public static String toBinaryString(byte[] bytes, String separator, int off, int len) {
        StringBuilder builder = new StringBuilder();
        for (int i = off; i < len; i++) {
            builder.append(toBinaryString(bytes[i]) + (separator == null ? "" : separator));
        }
        return builder.toString();
    }

    /**
     * 把字符串11110000等转换成一个字节
     *
     * @param binaryString 长度为8
     */
    public static byte toByte(String binaryString) {
        if (binaryString.length() != 8) {
            return 0;
        }

        byte b = 0;
        int m = 128;

        for (int i = 0; i < 8; i++) {
            String c = binaryString.charAt(i) + "";
            if (c.equals("1")) {
                b += m;
            }
            m /= 2;
        }
        return b;
    }

    /**
     * 把字符串1111000011110000等转换成字节数组
     *
     * @param binaryString
     * @return
     */
    public static byte[] toBytes(String binaryString) {
        int len = binaryString.length();
        if (len % 8 != 0) {
            return null;
        }

        len /= 8;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            String s = binaryString.substring(i * 8, i * 8 + 8);
            bytes[i] = toByte(s);
        }

        return bytes;
    }

    /**
     * 把任意一个文件转换成以0和1字符存储的文本
     *
     * @param binaryFilePath
     * @param textFilePath
     */
    public static void binaryToText(String binaryFilePath, String textFilePath) {
        try {
            FileInputStream fis = new FileInputStream(binaryFilePath);
            int len;
            byte[] buf = new byte[1024];
            StringBuilder builder = new StringBuilder();
            while ((len = fis.read(buf)) != -1) {
                builder.append(BinaryUtil.toBinaryString(buf, null, 0, len));
            }
            fis.close();
            FileUtil.write(builder.toString(), textFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把以0和1字符存储的文本转换成文件
     *
     * @param textFilePath
     * @param binaryFilePath
     */
    public static void textToBinary(String textFilePath, String binaryFilePath) {
        try {
            String s = FileUtil.read(textFilePath);
            byte[] bytes = toBytes(s);
            if (bytes != null && bytes.length > 0) {
                FileOutputStream fos = new FileOutputStream(binaryFilePath);
                fos.write(bytes);
                fos.flush();
                fos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
