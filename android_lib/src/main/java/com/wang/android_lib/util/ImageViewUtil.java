package com.wang.android_lib.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

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

}
