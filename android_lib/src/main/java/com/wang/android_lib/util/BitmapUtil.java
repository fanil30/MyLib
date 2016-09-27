package com.wang.android_lib.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * by 王荣俊 on 2016/6/11.
 * http://www.jb51.net/article/34885.htm  Android图片特效:黑白特效、圆角效果、高斯模糊
 */
public class BitmapUtil {

    /**
     * @param quality 100为原图，小于100则压缩
     */
    public static void saveToFile(Bitmap bitmap, String path, int quality) {
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, new FileOutputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 高斯模糊
     */
    public static Bitmap convertToBlur(Bitmap bitmap) {

        double lightScale = 2;//亮度倍率，越大越暗，1为原始亮度。
        int blurScale = 6;//模糊系数，越大越模糊，0则相当于不进行模糊处理。

        int totalWeight = 0;
        int len = 2 * blurScale + 1;
        int[][] gauss = new int[len][len];// 高斯矩阵(模拟三围正态分布的权重)
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                int Ai = i <= len / 2 ? i + 1 : (len - i);
                int Aj = j <= len / 2 ? j + 1 : (len - j);
                int weight = Ai * Aj;
                gauss[i][j] = weight;
                totalWeight += weight;
            }
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBmp = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);

        int pixR;
        int pixG;
        int pixB;

        int pixColor;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = blurScale; i < height - blurScale; i = i + 1 + blurScale) {
            for (int j = blurScale; j < width - blurScale; j = j + 1 + blurScale) {
                for (int m = -blurScale; m <= blurScale; m++) {
                    for (int n = -blurScale; n <= blurScale; n++) {
                        pixColor = pixels[(i + m) * width + j + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + pixR * gauss[m + blurScale][n + blurScale];
                        newG = newG + pixG * gauss[m + blurScale][n + blurScale];
                        newB = newB + pixB * gauss[m + blurScale][n + blurScale];
                    }
                }

                newR = (int) (newR / totalWeight / lightScale);
                newG = (int) (newG / totalWeight / lightScale);
                newB = (int) (newB / totalWeight / lightScale);

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                for (int m = -blurScale; m <= blurScale; m++) {
                    for (int n = -blurScale; n <= blurScale; n++) {
                        pixels[(i + m) * width + j + n] = Color.argb(255, newR, newG, newB);
                    }
                }

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

        return newBmp;
    }


}
