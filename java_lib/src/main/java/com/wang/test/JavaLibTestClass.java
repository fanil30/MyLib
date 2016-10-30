package com.wang.test;

import com.wang.java_util.GsonUtil;
import com.wang.java_util.MathUtil;
import com.wang.maths.IOperation;
import com.wang.maths.matrix.Matrix;
import com.wang.maths.matrix.MatrixException;
import com.wang.maths.matrix.MatrixUtil;

import java.util.ArrayList;
import java.util.List;

public class JavaLibTestClass {

    public static void main(String[] args) throws MatrixException {

        Matrix<Double> matrix1 = new Matrix<>(MatrixUtil.toDoubleList(new int[]{
                2, -1, 1,
                4, 1, -1,
                1, 1, 1,
        }), 3, 3, IOperation.doubleIOperation);

        Matrix<Double> matrix2 = new Matrix<>(MatrixUtil.toDoubleList(new int[]{
                1, 1, 2,
                -1, 2, 0,
                1, 1, 3,
        }), 3, 3, IOperation.doubleIOperation);

        matrix1.show();
        System.out.println("\n\n----------------------------\n\n");

        matrix2.show();
        System.out.println("\n\n----------------------------\n\n");

        MatrixUtil.cheng(matrix1, matrix2).show();
        System.out.println("\n\n----------------------------\n\n");

        Matrix<Double> reverse = MatrixUtil.reverse(matrix2);
        reverse.show();
        System.out.println("\n\n----------------------------\n\n");
    }

    public static void sortTest(String[] args) throws Exception {

        List<User> users = getExample(10);
        List<User> users1 = SortHelper.copy(users);
        List<User> users2 = SortHelper.copy(users);
        List<User> users3 = SortHelper.copy(users);
        List<User> users4 = SortHelper.copy(users);
        List<User> users5 = SortHelper.copy(users);
        List<User> users6 = SortHelper.copy(users);

        SortHelper.ISort<User> iSort = new SortHelper.ISort<User>() {
            @Override
            public SortHelper.Compare compare(User entity1, User entity2) {
                if (entity1.getAge() < entity2.getAge()) {
                    return SortHelper.Compare.SMALLER;
                } else if (entity1.getAge() == entity2.getAge()) {
                    return SortHelper.Compare.EQUAL;
                } else {
                    return SortHelper.Compare.BIGGER;
                }
            }
        };

        long currentTimeMillis;
        double time;
/*
        System.out.println("��ʼð������");
        currentTimeMillis = System.currentTimeMillis();
        int sortBubble = SortHelper.sortBubble(users1, iSort);
        time = (System.currentTimeMillis() - currentTimeMillis) / 1000.0;
        System.out.println("ð����ʱ��" + time + " ��");
        System.out.println("ð�ݵĻ�������������" + sortBubble + "\n");

        System.out.println("��ʼѡ������");
        currentTimeMillis = System.currentTimeMillis();
        int sortSelect = SortHelper.sortSelect(users2, iSort);
        time = (System.currentTimeMillis() - currentTimeMillis) / 1000.0;
        System.out.println("ѡ���������ʱ��" + time + " ��");
        System.out.println("ѡ������Ļ�������������" + sortSelect + "\n");
*//*
        System.out.println("��ʼ�ϲ�����");
        currentTimeMillis = System.currentTimeMillis();
        SortHelper.sortMerge(users3, iSort);
        time = (System.currentTimeMillis() - currentTimeMillis) / 1000.0;
        System.out.println("�ϲ��������ʱ��" + time + " ��");
        System.out.println("�ϲ�����Ļ�������������" + SortHelper.basicOperationCount + "\n");

        System.out.println("��ʼ��������");
        currentTimeMillis = System.currentTimeMillis();
        int sortInsertion = SortHelper.sortInsertion(users4, iSort);
        time = (System.currentTimeMillis() - currentTimeMillis) / 1000.0;
        System.out.println("�����������ʱ��" + time + " ��");
        System.out.println("��������Ļ�������������" + sortInsertion + "\n");

        System.out.println("��ʼ������");
        currentTimeMillis = System.currentTimeMillis();
        int sortHeap = SortHelper.sortHeap(users5, iSort);
        time = (System.currentTimeMillis() - currentTimeMillis) / 1000.0;
        System.out.println("���������ʱ��" + time + " ��");
        System.out.println("������Ļ�������������" + sortHeap + "\n");

        System.out.println("��ʼ��������");
        currentTimeMillis = System.currentTimeMillis();
        int sortQuick = SortHelper.sortQuick(users6, iSort);
        time = (System.currentTimeMillis() - currentTimeMillis) / 1000.0;
        System.out.println("�����������ʱ��" + time + " ��");
        System.out.println("��������Ļ�������������" + sortHeap + "\n");
*/
        SortHelper.sortQuick(users, iSort);
        GsonUtil.printFormatJson(users);

    }

    public static List<User> getExample(int number) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int age = MathUtil.random(1, 10 * number);
            users.add(new User("user" + i, age));
        }
        return users;
    }

}
