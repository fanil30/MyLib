package com.wang.test;

/**
 * by ���ٿ� on 2016/9/27.
 */
public class TestClass {

    public static void main(String[] a) {

        final int INCREMENT = 1;
        final int HOLE_NUMBER = 10;
        final int SEARCH_NUMBER = 1000;

        int[] holesCount = new int[HOLE_NUMBER];

        int position = 0;//�Ѷ�Ѩ11��Ӧ��0

        System.out.println("��������" + SEARCH_NUMBER + "�ε������");

        for (int i = 0; i < SEARCH_NUMBER; i = i + INCREMENT) {

            if (i % 20 == 0) {
                System.out.println("\n\n��" + (i + 1) + "-" + (i + 20) + "�Σ�");
            }

            position = (position + i) % HOLE_NUMBER;
            holesCount[position]++;
            System.out.printf(position + " ");

        }

        System.out.println("\n\n");

        for (int i = 0; i < HOLE_NUMBER; i++) {
            String s = "��" + i + "����Ѩ�ķ��ʴ�����" + holesCount[i];
            if (holesCount[i] == 0) {
                s += "�����ӿ������صĶ�Ѩ��";
            }
            System.out.println(s);
        }

    }

}
