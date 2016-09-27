package com.wang.java_util;

import java.util.Random;

/**
 * Created by 王荣俊 on 2016/4/1.
 */
public class MathUtil {

    /**
     * 5000位--6秒
     */
    public static String getPai(int length, int eachLineNumber) {
        int n = (int) (length * 1.5);
        int[] temp = new int[n];
        int[] pai = new int[n];
        int i, count = 0, up = 2, down = 5, result, leave;
        boolean finish = false;

        //初始化
        for (i = 0; i < n; i++) {
            pai[i] = 0;
            temp[i] = 6;
        }
        pai[0] = 2;
        temp[0] = 0;

        while (!finish && count++ < 1000000) {
            for (i = n - 1, leave = 0; i >= 0; i--)/*累加*/ {
                result = pai[i] + temp[i] + leave;
                pai[i] = result % 10;
                leave = result / 10;
            }

            for (i = n - 1, leave = 0; i >= 0; i--)/*乘分子*/ {
                result = temp[i] * up + leave;
                temp[i] = result % 10;
                leave = result / 10;
            }

            for (i = 0, leave = 0; i < n; i++)/*除分母*/ {
                result = temp[i] + leave * 10;
                temp[i] = result / down;
                leave = result % down;
            }

            finish = true;

            for (i = 0; i < n; i++)/*判断temp[]是否全为0,是则结束*/ {
                if (temp[i] != 0) {
                    finish = false;
                }
            }

            up++;
            down += 2;
        }

        StringBuilder builder = new StringBuilder();
        for (i = 0; i < length; i++) {
            builder.append(pai[i]);
            if (i == 0) {
                builder.append(".");
                continue;
            }
            if ((i + 1) % eachLineNumber == 0)
                builder.append("\r\n");
        }

        return builder.toString();
    }

    /**
     * 使接近0和接近100的这两边变缓慢。相当于3次方对称点在（50，50）的抛物线图，
     * 输入scale为横坐标，输出scale为纵坐标
     *
     * @param scale
     * @return 接近0和接近100的这两边变缓慢的新的scale
     */
    public static int toPowerScale(int scale) {
        return (int) ((scale - 50) * (scale - 50) * (scale - 50) / 2500.0 + 50);
    }

    /**
     * 得到left至right范围内的一个随机整数。注意当left>right时返回0。
     */
    public static int random(int left, int right) {
        if (left > right) return 0;
        Random random = new Random();
        int i = random.nextInt(right - left + 1);//例如random.nextInt(10)，会返回0至9的一个随机整数
        return i + left;
    }

}