package com.wang.java_util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Locale;

public class TextUtil {

    // 判断字符串是否为空
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /**
     * @return 只要有一个为空或长度为0，则返回true
     */
    public static boolean isEmpty(String... args) {
        if (args != null) {
            for (String s : args) {
                if (TextUtil.isEmpty(s)) return true;
            }
        } else {
            return true;
        }

        return false;
    }

    /**
     * @param text 输入带一个或多个小数点的字符串，如文件名，包名
     * @return 返回最后一个小数点后面的字符串。若无小数点，返回text
     */
    public static String getTextAfterLastPoint(String text) {
        try {
            String[] s = text.split("[.]");
            return s[s.length - 1];
        } catch (Exception e) {
            return text;
        }
    }

    /**
     * @param postName true:返回后缀（不含点） false：返回前缀（不含点）
     */
    public static String parseFileName(String fileName, boolean postName) {
        try {
            String[] s = fileName.split("[.]");
            if (postName) {
                return s[s.length - 1];
            } else {
                return fileName.replace("." + s[s.length - 1], "");
            }

        } catch (Exception e) {
            return fileName;
        }
    }

    /**
     * 从path获取文件名
     *
     * @param text 输入带一个或多个斜杠/或\的字符串，如文件路径，url
     * @return 返回最后一个斜杠后面的字符串。若无斜杠，返回text
     * 例如：E:\\files\\a.txt 转换为 a.txt
     */
    public static String getTextAfterLastSlash(String text) {
        try {
            String[] s = text.split("[\\\\/]");
            return s[s.length - 1];
        } catch (Exception e) {
            return text;
        }
    }

    /**
     * 把path转换为父目录的dir
     *
     * @param text 输入带一个或多个斜杠/或\的字符串，如文件路径，url
     * @return 返回除了最后一个斜杠后面的字符串的其他字符。若无斜杠，返回text
     * 例如：E:\\files\\a.txt 转换为 E:\\files\\
     */
    public static String getTextExceptLastSlash(String text) {
        try {
            String[] s = text.split("[\\\\/]");
            return text.replace(s[s.length - 1], "");
        } catch (Exception e) {
            return text;
        }
    }

    // URL中文编码转换
    public static String encodeStringToUTF_8(String s) throws Exception {
        return URLEncoder.encode(s, "utf-8");
    }

    // URL中文解码转换
    public static String decodeStringToUTF_8(String s) throws Exception {
        return URLDecoder.decode(s, "utf-8");
    }

    /**
     * 文件名规范化(替换掉 \ / : * ? < > | \n \r 等非法字符)
     *
     * @param replaceChar 非法字符替换后的字符，不能是非法字符
     */
    public static String correctFileName(String fileName, String replaceChar) {
        if (fileName == null || fileName.length() == 0) {
            return "error";
        }

        String[] errorChar = {"\\", "/", ":", "*", "?", "\"", "<", ">", "|",
                "\n", "\r"};
        for (int i = 0; i < errorChar.length; i++) {
            fileName = fileName.replace(errorChar[i], replaceChar);
        }

        return fileName;
    }

    /**
     * 控制小数点位数
     *
     * @param leave 保留小数点的位数
     */
    public static String formatDouble(double number, int leave) {
        if (leave < 0) {
            return "";
        }
        String pattern = "#";
        for (int i = 0; i < leave; i++) {
            if (i == 0) {
                pattern = pattern + ".";
            }
            pattern = pattern + "0";
        }

        DecimalFormat df = new DecimalFormat(pattern);
        String newNumber = df.format(number);
        if (newNumber.indexOf(".") == 0) {
            newNumber = "0" + newNumber;
        }

        return newNumber;
    }

    /**
     * @param length 文件大小，单位为Byte
     * @param leave  单位为MB时小数点后保留的位数
     * @return 以KB或MB为单位的文件大小字符串
     */
    public static String transferFileLength(long length, int leave) {
        String strLength;
        if (length < 1024) {
            strLength = length + "B";
        } else if (length < 1024 * 1024) {
            strLength = (long) ((double) length / 1024) + "K";
        } else {
            strLength = TextUtil.formatDouble((double) length / 1024 / 1024, leave) + "M";
        }

        return strLength;
    }

    /**
     * 根据中文字符串的开头拼音进行大小比较。
     *
     * @return 1，0，-1
     */
    public static int compareChinaPinYin(String s1, String s2) {
        Comparator<Object> cmp = Collator.getInstance(Locale.CHINA);
        int i = cmp.compare(s1, s2);
        return i;
    }

    /**
     * 对字符串进行裁剪。若字符串长度>maxLength，则裁剪成maxLength，否则不处理。
     *
     * @param maxLength  裁剪后的最大长度
     * @param lastString 若需要裁剪，裁剪后的字符串再加上lastString
     */
    public static String limitLength(String text, int maxLength, String lastString) {
        if (text.length() > maxLength) {
            return text.substring(0, maxLength) + lastString;
        } else {
            return text;
        }
    }

    /**
     * 返回如2016-07-11格式的日期字符串
     */
    public static String getDate(int year, int month, int day) {
        String m = month + "";
        String d = day + "";
        if (month < 10) m = "0" + m;
        if (day < 10) d = "0" + d;
        return year + "-" + m + "-" + d;
    }

}