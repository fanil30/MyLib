package com.wang.test;

import com.wang.math.Hill;
import com.wang.math.IOperation;
import com.wang.math.fraction.Fraction;
import com.wang.math.matrix.Matrix;
import com.wang.math.matrix.MatrixUtil;

public class JavaLibTestClass {

    public static void main(String[] args) throws Exception {

        Matrix<Fraction> keyMatrix = new Matrix<>(MatrixUtil.toFractionList(new int[]{
                1, 7, 3, 2,
                0, 1, 4, 3,
                0, 0, 1, 9,
                0, 0, 0, 1
        }), 4, 4, IOperation.fractionIOperation);

        keyMatrix.show();
        System.out.println();

        Hill hill = new Hill(keyMatrix);

        hill.getReverseKeyMatrix().show();
        System.out.println();

        String text = "we will attack tomorrow six";
        String encode = hill.encode(text);
        String decode = hill.decode(encode);

        System.out.println("text: " + text);
        System.out.println("encode: " + encode);
        System.out.println("decode: " + decode);
    }

}
