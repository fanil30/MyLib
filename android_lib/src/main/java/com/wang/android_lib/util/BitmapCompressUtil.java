package com.wang.android_lib.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.wang.java_util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * by 王荣俊 on 2016/6/11.
 */
public class BitmapCompressUtil {
    /**
     * 质量压缩
     *
     * @param quality 1-100进行质量压缩，<=0则不进行质量压缩
     */
    public static void compressQuality(String imgaePath, String outputPath, int quality)
            throws IOException {

        if (quality > 0) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgaePath);
            FileOutputStream fos = new FileOutputStream(outputPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.close();
        }

    }

    /**
     * 尺寸压缩
     *
     * @param sizeScale 以其最接近的2次幂数值作为尺寸缩放倍数。<=0则不进行尺寸压缩
     *                  如sizeScale=5，则尺寸比例缩小4倍。sizeScale=7，则尺寸比例缩小8倍
     */
    public static void compressSize(String imgaePath, String outputPath, int sizeScale)
            throws IOException {

        if (sizeScale > 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sizeScale;
            Bitmap bitmap = BitmapFactory.decodeFile(imgaePath, options);
            FileOutputStream fos = new FileOutputStream(outputPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        }

    }

    /**
     * @param expectedLength 期望压缩到的大小（单位为Kb）
     */
    public static void compress(String imgaePath, int expectedLength) throws IOException {

        int length = (int) new File(imgaePath).length();
        double scale = length / 1024.0 / expectedLength;
        if (scale > 1) {//若图片大小比期望值大
            boolean compressSize = false;
            if (scale > 10) {//若大于10倍以上，先尺寸压缩。10倍以上压缩一半，20倍以上压缩1/4
                compressSize = true;
                compressSize(imgaePath, imgaePath, scale > 20 ? 4 : 2);
                //根据尺寸压缩后新文件的大小，重新估算质量压缩的比例
                length = (int) new File(imgaePath).length();
                System.out.println("new length: " + length / 1024);
                scale = length / 1024.0 / expectedLength;
            }
            //没经过尺寸压缩，quality应稍微小一点，即scale大一点
            scale = compressSize ? Math.sqrt(Math.sqrt(scale)) : scale * 1.3;
            int quality = (int) (1 / scale * 100);

            if (quality < 15) {//防止quality过小而导致图片比期待的大小要小太多，坏处是有可能大于期待值
                quality = 15;
            } else if (quality > 85) {//防止quality接近100时图片压缩后反而变大的情况
                quality = 85;
            }
            compressQuality(imgaePath, imgaePath, quality);
        }

    }

    public static void compress(String imagePath, String outPath, int expectedLength) throws IOException {

        FileUtil.copy(imagePath, outPath);
        compress(outPath, expectedLength);

    }

    public interface OnCompressFinishListener {
        void onCompressFinish(Exception e);
    }

    public static class CompressTask {
        private String imagePath;
        private String outPath;
        private int expectedLength;

        public CompressTask(String imagePath, String outPath, int expectedLength) {
            this.imagePath = imagePath;
            this.outPath = outPath;
            this.expectedLength = expectedLength;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getOutPath() {
            return outPath;
        }

        public int getExpectedLength() {
            return expectedLength;
        }
    }

    public static void startCompress(final List<CompressTask> tasks, final OnCompressFinishListener listener) {
        new AsyncTask<Void, Void, Exception>() {

            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    for (CompressTask task : tasks) {
                        compress(task.getImagePath(), task.getOutPath(), task.getExpectedLength());
                    }
                    return null;
                } catch (IOException e) {
                    return e;
                }
            }

            @Override
            protected void onPostExecute(Exception e) {
                if (listener != null) {
                    listener.onCompressFinish(e);
                }
            }
        }.execute();
    }

}
