package com.wang.test;

/**
 * by 王荣俊 on 2016/9/27.
 */
public class TestClass {

    public static void main(String[] a) {

        final int INCREMENT = 1;
        final int HOLE_NUMBER = 10;
        final int SEARCH_NUMBER = 1000;

        int[] holesCount = new int[HOLE_NUMBER];

        int position = 0;//把洞穴11对应成0

        System.out.println("狐狸找了" + SEARCH_NUMBER + "次的情况：");

        for (int i = 0; i < SEARCH_NUMBER; i = i + INCREMENT) {

            if (i % 20 == 0) {
                System.out.println("\n\n第" + (i + 1) + "-" + (i + 20) + "次：");
            }

            position = (position + i) % HOLE_NUMBER;
            holesCount[position]++;
            System.out.printf(position + " ");

        }

        System.out.println("\n\n");

        for (int i = 0; i < HOLE_NUMBER; i++) {
            String s = "第" + i + "个洞穴的访问次数：" + holesCount[i];
            if (holesCount[i] == 0) {
                s += "（兔子可能隐藏的洞穴）";
            }
            System.out.println(s);
        }

    }

}
