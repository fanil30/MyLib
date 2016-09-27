package com.wang.android_lib.util;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * by 王荣俊 on 2016/8/24.
 */
public class AnimationUtil {

    /**
     * http://blog.csdn.net/ck12ck12/article/details/42875687
     * RotateAnimation 匀速转动效果
     */
    public static RotateAnimation getConstantSpeedRotateAnim(int millisPerLoop) {
        RotateAnimation anim = new RotateAnimation(
                0,
                360,
                Animation.RESTART,
                0.5f,
                Animation.RESTART,
                0.5f
        );
        anim.setDuration(millisPerLoop);
        anim.setRepeatCount(Animation.INFINITE);// 设置重复次数，这里是无限
        anim.setRepeatMode(Animation.RESTART);// 设置重复模式  
        anim.setStartTime(Animation.START_ON_FIRST_FRAME);

        // 匀速转动的代码  
        LinearInterpolator lin = new LinearInterpolator();//匀速效果
        //AccelerateInterpolator为加速效果、DecelerateInterpolator为减速效果;
        //AccelerateDecelerateInterpolator为慢-快-慢效果

        anim.setInterpolator(lin);//表示设置旋转速率，但不是运行速度。一个插补属性，
        // 可以将动画效果设置为加速，减速，反复，反弹等。默认为开始和结束慢中间快，

        return anim;
    }

}
