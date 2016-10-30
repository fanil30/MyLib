package com.wang.test;

import com.wang.java_util.GsonUtil;
import com.wang.java_util.MathUtil;
import com.wang.math.IOperation;
import com.wang.math.Matrix;
import com.wang.math.MatrixUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaLibTestClass {

    public static void main(String[] args) throws Exception {

        Matrix<Integer> matrix1 = new Matrix<>(3, 3, Arrays.asList(
                1, 2, 3,
                4, 5, 6,
                7, 8, 9
        ));
        Matrix<Integer> matrix2 = new Matrix<>(3, 2, Arrays.asList(
                1, 2,
                4, 5,
                7, 8
        ));

        matrix1.show();
        System.out.println("\n\n----------------------------\n\n");
        matrix2.show();
        System.out.println("\n\n----------------------------\n\n");

        MatrixUtil.cheng(matrix1, matrix2, IOperation.integerIOperation).show();

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
