package com.wang.test;

import com.wang.java_util.Pair;
import com.wang.math.Hill;
import com.wang.math.IOperation;
import com.wang.math.fraction.Fraction;
import com.wang.math.matrix.Matrix;
import com.wang.math.matrix.MatrixException;
import com.wang.math.matrix.MatrixUtil;

public class JavaLibTestClass {

    public static void main(String[] a) throws MatrixException {
        
        Matrix<Fraction> keyMatrix = new Matrix<>(MatrixUtil.toFractionList(new int[]{
                17, 17, 5,
                21, 18, 21,
                2, 2, 19,
        }), 3, 3, IOperation.fractionIOperation);

        Pair<Fraction, Matrix<Fraction>> matrixPair = MatrixUtil.reverseWithMultiple(keyMatrix);
        System.out.println(matrixPair.first);
        final Matrix<Fraction> matrix = matrixPair.second;
        matrix.iterator(new Matrix.Iterator<Fraction>() {
            @Override
            public void next(int i, int j, int index, Fraction element) {
                matrix.set(i, j, new Fraction(element.getSon() % 26, 1));
            }
        });
        matrix.show();

        Matrix<Fraction> rightMatrix = new Matrix<>(MatrixUtil.toFractionList(new int[]{
                15,
                0,
                24,
        }), 3, 1, IOperation.fractionIOperation);

        MatrixUtil.multiply(keyMatrix, rightMatrix).show();

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
