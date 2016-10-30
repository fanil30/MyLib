package com.wang.math;

import java.util.ArrayList;
import java.util.List;

/**
 * by 王荣俊 on 2016/10/27.
 */
public class Matrix<T> {

    private int row;
    private int column;
    private List<T> matrix;

    public Matrix(int row, int column) {
        matrix = new ArrayList<>();
        for (int i = 0; i < row * column; i++) {
            matrix.add(null);
        }
        this.row = row;
        this.column = column;
    }

    public Matrix(int row, int column, List<T> matrix) {
        this.row = row;
        this.column = column;
        this.matrix = matrix;
    }

    public T get(int i, int j) {
        return matrix.get(i * column + j);
    }

    public void set(int i, int j, T element) {
        matrix.set(i * column + j, element);
    }

    public void set(int index, T element) {
        matrix.set(index, element);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void show() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                builder.append(get(i, j) + "     ");
                if (j == column - 1) {
                    builder.append("\n\n");
                }
            }
        }
        System.out.println(builder.toString());
    }

}
