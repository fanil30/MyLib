package com.wang.android_lib.view;

import android.content.Context;
import android.widget.TextView;

/**
 * by wangrongjun on 2017/5/22.
 * http://blog.csdn.net/heynine/article/details/8105109  Android TextView实现跑马灯效果
 * xml需要添加：
 * android:ellipsize="marquee"
 * android:marqueeRepeatLimit="marquee_forever"
 * android:singleLine="true"
 */
public class MarqueeTextView extends TextView {

    public MarqueeTextView(Context context) {
        super(context);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
