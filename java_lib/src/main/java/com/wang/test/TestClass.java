package com.wang.test;

/**
 * by 王荣俊 on 2016/9/27.
 */
public class TestClass {

    public static void main(String[] a) {

        final int INCREMENT = 1;
        final int HOLE_NUMBER = 10;

        int[] holes = new int[HOLE_NUMBER];

        int result = 0;//把洞穴11对应成0

        for (int i = 1; i <= 1000; i = i + INCREMENT) {
            result = (result + i) % HOLE_NUMBER;
            holes[result]++;
            System.out.printf(result + " ");
            if (i % 20 == 0) {
                System.out.println();
            }
        }

        System.out.println("\n\n");

        for (int i = 0; i < HOLE_NUMBER; i++) {
            System.out.println(i + " - " + holes[i]);
        }

    }

}
