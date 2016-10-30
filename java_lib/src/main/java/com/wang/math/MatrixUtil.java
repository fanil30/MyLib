package com.wang.math;

/**
 * by 王荣俊 on 2016/10/27.
 */
public class MatrixUtil {

    /**
     * 矩阵相乘
     *
     * @throws Exception 如果左矩阵的列不等于右矩阵的行，抛异常
     */
    public static <T> Matrix<T> cheng(Matrix<T> matrix1, Matrix<T> matrix2, IOperation<T> iOperation)
            throws Exception {
        if (matrix1.getColumn() != matrix2.getRow()) {//如果左矩阵的列不等于右矩阵的行，无法计算。
            throw new Exception("matrix1(column) != matrix2(row)");
        }
        int publicLength = matrix1.getColumn();
        Matrix<T> matrix = new Matrix<>(matrix1.getRow(), matrix2.getColumn());
        for (int i = 0; i < matrix.getRow(); i++) {
            for (int j = 0; j < matrix.getColumn(); j++) {
                T element = null;
                for (int k = 0; k < publicLength; k++) {
                    T temp = iOperation.cheng(
                            matrix1.get(i, k),
                            matrix2.get(k, j)
                    );
                    element = iOperation.jia(element, temp);
                    matrix.set(i, j, element);
                }
            }
        }

        return matrix;
    }

    public static <T> Matrix<T> forwardElimimation(Matrix<T> matrix1, Matrix<T> matrix2,
                                                   IOperation<T> iOperation) throws Exception {
        return null;
    }

    public static <T> Matrix<T> reverse(Matrix<T> matrix1, Matrix<T> matrix2, IOperation<T> iOperation)
            throws Exception {
        return null;
    }

}
