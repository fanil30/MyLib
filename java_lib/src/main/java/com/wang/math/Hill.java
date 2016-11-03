package com.wang.math;

import com.wang.java_util.TextUtil;
import com.wang.math.matrix.Matrix;
import com.wang.math.matrix.MatrixUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * by 王荣俊 on 2016/10/27.
 */
public class Hill {

    private int m;//连续的明文字母，也是加密密钥矩阵的维度。
    private Matrix<Integer> keyMatrix;//加密密钥矩阵
    private Matrix<Integer> reverseKeyMatrix;

    public Hill(Matrix<Integer> keyMatrix) throws Exception {
        if (keyMatrix == null || keyMatrix.getRow() != keyMatrix.getColumn()) {
            throw new Exception("Error: row != column");
        }
        this.keyMatrix = keyMatrix;
        m = keyMatrix.getRow();
    }

    public String encode(String text) throws Exception {
        text = text.replace(" ", "");//先去掉空格
        if (TextUtil.isEmpty(text)) {
            throw new Exception("Error: text is null");
        }
        while (text.length() % m != 0) {//如果分组时不能分成m个一组，补充字母x直到可以分组
            text += "x";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length() / m; i++) {
            String textUnit = "";
            for (int j = 0; j < m; j++) {
                textUnit += text.charAt(i * m + j);
            }
            String s = encodeUnit(textUnit);
            builder.append(s);
        }
        return builder.toString();
    }

    public String decode(String text) throws Exception {
        text = text.replace(" ", "");//先去掉空格
        if (TextUtil.isEmpty(text) || text.length() % m != 0) {
            throw new Exception("Error: text is null or length%m != 0");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length() / m; i++) {
            String textUnit = "";
            for (int j = 0; j < m; j++) {
                textUnit += text.charAt(i * m + j);
            }
            String s = decodeUnit(textUnit);
            System.out.println(textUnit + "  " + s);
            builder.append(s);
        }
        return builder.toString();
    }

    /**
     * 对长度为m的单位字符串进行加密
     */
    public String encodeUnit(String unitText) throws Exception {

        if (unitText == null || unitText.length() != m) {
            throw new Exception("Error: length != m");
        }

        List<Integer> textMatrixList = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            textMatrixList.add(toNumber(unitText.charAt(i)) % 26);
        }
        Matrix<Integer> textMatrix = new Matrix<>(textMatrixList, m, 1, IOperation.integerIOperation);

        Matrix<Integer> resultMatrix = MatrixUtil.cheng(keyMatrix, textMatrix);

        String s = "";
        for (int i = 0; i < m; i++) {
            s += toLetter(resultMatrix.get(i, 0) % 26);
        }
        return s;
    }

    /**
     * 对长度为m的单位字符串进行解密
     */
    public String decodeUnit(String unitText) throws Exception {

        if (unitText == null || unitText.length() != m) {
            throw new Exception("Error: length != m");
        }

        List<Integer> textMatrixList = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            textMatrixList.add(toNumber(unitText.charAt(i)) % 26);
        }

        Matrix<Integer> textMatrix = new Matrix<>(textMatrixList, m, 1, IOperation.integerIOperation);

        //与encodeUnit唯一有区别的地方
        if (reverseKeyMatrix == null) {
            reverseKeyMatrix = MatrixUtil.reverse(keyMatrix);
        }
        Matrix<Integer> resultMatrix = MatrixUtil.cheng(reverseKeyMatrix, textMatrix);

        String s = "";
        for (int i = 0; i < m; i++) {
            s += toLetter(resultMatrix.get(i, 0) % 26);
        }
        return s;
    }

    private char toLetter(int number) {
        return (char) (number + 'a');
    }

    private int toNumber(char letter) {
        return letter - 'a';
    }

}
