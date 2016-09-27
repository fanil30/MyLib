package com.wang.android_lib.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * by 王荣俊 on 2016/8/12.
 * http://www.cnblogs.com/xilinch/p/4444833.html
 */
public class DimensionUtil {

    public static int dipToPx(Context context, double dip) {
//        方法1：
//        float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
//         方法2：COMPLEX_UNIT_DIP:dp转换为px（像素）
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) dip, metrics);
    }

    public static int spToPx(Context context, double sp) {
//        方法1：
//        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
//        return (int) (sp * fontScale + 0.5f);
//         方法2：COMPLEX_UNIT_SP:sp转换为px
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, (float) sp, metrics);
    }

    public static int pxToDip(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    public static int pxToSp(Context context, float px) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

}
