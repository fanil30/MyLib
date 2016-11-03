package com.wang.test;

import com.wang.math.IOperation;
import com.wang.math.fraction.Fraction;
import com.wang.math.matrix.Matrix;
import com.wang.math.matrix.MatrixUtil;

public class JavaLibTestClass {

    public static void main(String[] args) throws Exception {
        Matrix<Fraction> keyMatrix = new Matrix<>(MatrixUtil.toFractionList(new int[]{
                1, -3, 7,
                2, 4, -3,
                -3, 7, 2,
        }), 3, 3, IOperation.fractionIOperation);

        keyMatrix.show();
        MatrixUtil.reverse(keyMatrix).show();
/*
//        Hill hill = new Hill(keyMatrix);
        Hill hill = null;
        String text = "pay more money";
        String encode = hill.encode(text);
        String decode = hill.decode(encode);

        System.out.println("text: " + text);
        System.out.println("encode: " + encode);
        System.out.println("decode: " + decode);*/
    }

}
