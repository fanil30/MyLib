package com.wang.test;

import com.wang.java_util.Pair;
import com.wang.java_util.TextUtil;

public class JavaLibTestClass {

    public static void main(String[] a) {
        Pair<Double, Double> point = Touch.getSingleTouchPoint(
                TouchExample.example1_X,
                TouchExample.example1_Y
        );
        String x = TextUtil.formatDouble(point.first, 2);
        String y = TextUtil.formatDouble(point.second, 2);
        System.out.println("x=" + x + "  y=" + y);

        point = Touch.getSingleTouchPoint(
                TouchExample.example2_X,
                TouchExample.example2_Y
        );
        x = TextUtil.formatDouble(point.first, 2);
        y = TextUtil.formatDouble(point.second, 2);
        System.out.println("x=" + x + "  y=" + y);
    }

}
