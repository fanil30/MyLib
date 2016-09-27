package com.wang.android_lib.util;

import android.view.View;

/**
 * by 王荣俊 on 2016/8/14.
 */
public class ViewUtil {

    /**
     * 自定义view的重写onMeasure模版
     * <p/>
     * 使用：setMeasuredDimension(measureWidth(...), measureHeight(...));
     * <p/>
     * 使用时请删除super.onMeasure
     */
    public static int measureWidth(int widthMeasureSpec, int defaultWidth) {

        int width;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = defaultWidth;
            if (widthMode == View.MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        return width;

    }

    public static int measureHeight(int heightMeasureSpec, int defaultHeight) {

        int height;
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = defaultHeight;
            if (heightMode == View.MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        return height;

    }

}
