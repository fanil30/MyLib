package com.wang.java_util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class DataUtil {

    // 单向加密算法
    public static String encrypt(String text) {
        int len = text.length();
        int[] n = new int[len];
        for (int i = 0; i < len; i++) {
            n[i] = text.charAt(i) % 10;
        }

        long result = 0;

        for (int i = 0; i < len; i++) {
            long temp = 1;
            int power = (i + 3) % 5 + 1;
            for (int j = 0; j < power; j++) {
                temp *= n[i] * 43 + 124;
            }
            result += temp;
        }

        result *= 169;

        String s = get16by10Number(result, true);

        return s.charAt(s.length() - 1) + s.substring(1, s.length() - 2)
                + s.charAt(0);

    }

    /**
     * 从十进制数转化成十六进制
     *
     * @param bigLetter 返回大写字母或小写字母
     */
    public static String get16by10Number(long number, boolean bigLetter) {
        StringBuilder builder = new StringBuilder();

        while (number > 0) {
            // leave为余数
            int leave = (int) (number % 16);

            String s;
            if (leave < 10) {
                s = leave + "";
            } else {

                s = (char) (bigLetter ? 'A' : 'a' + leave - 10) + "";
            }

            builder.append(s);
            number /= 16;
        }

        String s = (new StringBuffer(builder.toString()).reverse()).toString();

        return s;
    }

    public static String md5(String text) {
        if (TextUtil.isEmpty(text)) return "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(md5.digest(text.getBytes()));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 文件转化为字符串
     */
    public static String toBase64String(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        return Base64.encodeToString(StreamUtil.toBytes(fis), Base64.DEFAULT);
    }

    /**
     * 字符串转化为文件
     */
    public static void fromBase64String(String base64String, String savePath) throws IOException {
        byte[] data = Base64.decode(base64String, Base64.DEFAULT);
        FileOutputStream fos = new FileOutputStream(savePath);
        fos.write(data);
        fos.flush();
        fos.close();
    }

}
