package com.wang.android_lib.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapCompress2Util {
    /**
     * http://blog.csdn.net/jian51868/article/details/45225445
     * android压缩照片到指定大小100%可靠
     * 最近，做到电子照片上传的功能，要求照片大小不能大于50kb，还有尽量保证质量！心想简单吧……
     * 是不是百度一下一箩筐！！！查看了一些发现一箩筐都是一样一样的。简单说一下网上的：
     * 1、bitmap质量压缩。2、bitmap图片比例缩放。3、根据path比例缩放图片。最后发现
     * baos.toByteArray().length取到的照片大小与实际保存的大小不符。这是个大问题，得不到真实的
     * 图片大小，不能准确的压缩图片。后来无意想起以前貌似在哪里看到资料说这个 inSampleSize
     * 好像是 2 的 N 次方。看了官方文档，果然这么说的：默认会找一个最接近的 2 的 N 次方的整数。
     * 很显然问题就出在这里，它所找的最接近的很可能并不是我们所需要的，可能已经超过了限制。
     * 我们需要先对这个压缩比取 2 的对数，然后向上取整，最后再取2的指数得到正确的 inSampleSize 。
     * 抓紧时间一试……正解！
     *
     * @param maxLength 期待的最大值，单位为KB
     */
    public static void compress(Activity activity, String srcPath, String outPath, int maxLength) {

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        float hh = dm.heightPixels;
        float ww = dm.widthPixels;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }
        opts.inSampleSize = size;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        System.out.println(baos.toByteArray().length);
        while (baos.toByteArray().length > maxLength * 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 20;
            System.out.println(baos.toByteArray().length);
        }
        try {
            baos.writeTo(new FileOutputStream(outPath));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
