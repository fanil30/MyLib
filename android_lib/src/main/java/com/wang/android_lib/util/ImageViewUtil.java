package com.wang.android_lib.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.wang.java_util.Pair;

import java.io.ByteArrayOutputStream;

import static junit.framework.Assert.assertTrue;

/**
 * by 王荣俊 on 2016/5/30.
 */
public class ImageViewUtil {

    public static Bitmap getBitmap(ImageView imageView) {
        //设置false以清空画图缓冲区。否则，下一次从ImageView对象iv_photo中获取的图像，还是原来的图像
        imageView.setDrawingCacheEnabled(false);
        //设置true。否则，无法从ImageView对象iv_photo中获取图像
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();
        return bitmap;
    }

    /**
     * ImageView转化为Bitmap再转化为字节数组
     * http://bbs.csdn.net/topics/390436899
     *
     * @param width    设定宽度。若width与height任一个小于等于0，则不进行尺寸的修改
     * @param height   设定高度。
     * @param compress 压缩百分比。如compress=90，则压缩为原来大小的90%。100则不压缩。
     */
    public static byte[] toBytes(ImageView imageView, int width, int height, int compress) {
        if (imageView == null) {
            return new byte[0];
        }

        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();
        imageView.setDrawingCacheEnabled(false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //这个函数能够设定图片的宽度与高度。filter一般设定为true。
        // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
        Bitmap bm = Bitmap.createScaledBitmap(bitmap, width, height, true);
        bm.compress(Bitmap.CompressFormat.JPEG, compress, baos);

        return baos.toByteArray();
    }

    /**
     * 计算fixCenter模式下内层的ImageView在外层的ViewGroup内所占的宽度和高度
     *
     * @param imageScale 内层的ImageView的宽高比
     * @return 宽度和高度
     */
    public static Pair<Integer, Integer> fixCenter(int parentWidth, int parentHeight,
                                                   double imageScale) {
        int imageWidth;
        int imageHeight;
        double parentScale = 1.0 * parentWidth / parentHeight;
        if (parentScale > imageScale) {
            double scale = imageScale / parentScale;// scale一定落在0到1
            imageWidth = (int) (parentWidth * scale);
            imageHeight = parentHeight;
        } else {
            double scale = parentScale / imageScale;// scale一定落在0到1
            imageWidth = parentWidth;
            imageHeight = (int) (parentHeight * scale);
        }
        return new Pair<>(imageWidth, imageHeight);
    }

    //    @Test
    public void testFixCenter() {
        int parentWidth = 100;
        int parentHeight = 200;
        double imageScale = 0.3;
        Pair<Integer, Integer> pair = fixCenter(parentWidth, parentHeight, imageScale);
        assertTrue(pair.first < parentWidth);
        assertTrue(pair.second == parentHeight);

        parentWidth = 100;
        parentHeight = 200;
        imageScale = 0.7;
        pair = fixCenter(parentWidth, parentHeight, imageScale);
        assertTrue(pair.first == parentWidth);
        assertTrue(pair.second < parentHeight);

        parentWidth = 200;
        parentHeight = 100;
        imageScale = 1;
        pair = fixCenter(parentWidth, parentHeight, imageScale);
        assertTrue(pair.first < parentWidth);
        assertTrue(pair.second == parentHeight);

        parentWidth = 200;
        parentHeight = 100;
        imageScale = 3;
        pair = fixCenter(parentWidth, parentHeight, imageScale);
        assertTrue(pair.first == parentWidth);
        assertTrue(pair.second < parentHeight);
    }

}
