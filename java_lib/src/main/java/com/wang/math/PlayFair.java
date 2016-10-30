package com.wang.math;

import com.wang.java_util.TextUtil;

/**
 * by 王荣俊 on 2016/10/27.
 */
public class PlayFair {

    private Matrix<String> matrix = new Matrix<>(5, 5);

    public PlayFair(String key) {
        createMatrix(key);
        matrix.show();
    }

    public String encrypt(String clearText) {
        clearText = clearText.toUpperCase();
        clearText = insertKToRepeatLetters(clearText);

        return "";
    }

    public String decrypt(String cipher) {
        return "";
    }

    /**
     * 两个相同的字母之间插入K，插完后如果字符串长度为奇数，在尾部添加一个K。
     *
     * @param clearText 大写的明文，不能有多个连续的K
     */
    private String insertKToRepeatLetters(String clearText) {

        char previousChar = '\0';
        String result = "";
        for (int i = 0; i < clearText.length(); i++) {
            char currentChar = clearText.charAt(i);
            if (previousChar == currentChar) {
                result += "K";
            }
            result += currentChar;
            previousChar = currentChar;
        }
        if (result.length() % 2 == 1) {
            result += "K";
        }
        return result;
    }

    /**
     * 创建加密辅助数组
     *
     * @param key 不能包含i或j
     */
    private void createMatrix(String key) {

        assert !key.contains("I") && !key.contains("J");

        key = key.toUpperCase();
        key = deleteRepeat(key);
        if (key.contains("I") || key.contains("J")) {
            return;
        }

        int n = matrix.getRow();
        char c = 'A';
        for (int index = 0; index < n * n; index++) {
            if (index < key.length()) {
                matrix.set(index, key.charAt(index) + "");
            } else {
                while (key.contains(c + "")) {
                    c++;
                }
                if (c == 'I') {
                    matrix.set(index, c + "/J");
                    c++;
                } else if (c != 'J') {
                    matrix.set(index, c + "");
                }
                c++;
            }
        }
    }

    /**
     * 删除第二次或以上出现的字符（如aasssfca变为asfc）
     */
    private String deleteRepeat(String text) {
        if (TextUtil.isEmpty(text)) {
            return "";
        }
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!result.contains(c + "")) {
                result += c;
            }
        }
        return result;
    }

}
