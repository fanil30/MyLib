package com.wang.test;

import com.wang.java_util.MathUtil;
import com.wang.math.Hill;
import com.wang.math.IOperation;
import com.wang.math.fraction.Fraction;
import com.wang.math.matrix.Matrix;
import com.wang.math.matrix.MatrixException;
import com.wang.math.matrix.MatrixUtil;

import java.util.ArrayList;
import java.util.List;

public class JavaLibTestClass {

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {

            //生成随机整数矩阵
            List<Fraction> fractionList = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                int son = MathUtil.random(0, 100);
                fractionList.add(new Fraction(son, 1));
            }

            Matrix<Fraction> matrix = new Matrix<>(fractionList, 3, 3, IOperation.fractionIOperation);
            Matrix<Fraction> reverseMatrix;
            try {
                reverseMatrix = MatrixUtil.reverse(matrix);
            } catch (MatrixException e) {
                continue;
            }

            //判断元素是否全为正整数
            boolean isAllTrue = true;
            for (int j = 0; j < 9; j++) {
                Fraction fraction = reverseMatrix.get(j);
                if (fraction.getMother() != 1 || !fraction.isPositive()) {
                    isAllTrue = false;
                }
            }
            if (isAllTrue) {
                matrix.show();
                System.out.println();
                reverseMatrix.show();
                return;
            }
        }
    }

    public static void main111(String[] args) throws Exception {

        Matrix<Fraction> keyMatrix = new Matrix<>(MatrixUtil.toFractionList(new int[]{
//                17, 17, 5,
//                21, 18, 21,
//                2, 2, 19,
                1, 7, 3,
                0, 1, 5342,
                0, 0, 1,
        }), 3, 3, IOperation.fractionIOperation);

        keyMatrix.show();
        System.out.println();
        MatrixUtil.reverse(keyMatrix).show();

        Hill hill = new Hill(keyMatrix);
        String text = "pay more money";
        String encode = hill.encode(text);
        String decode = hill.decode(encode);

        System.out.println("text: " + text);
        System.out.println("encode: " + encode);
        System.out.println("decode: " + decode);
    }

}
