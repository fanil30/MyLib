package com.wang.android_lib.util;

import android.text.Html;
import android.widget.TextView;

import com.wang.java_util.TextUtil;

/**
 *  by 王荣俊 on 2016/5/26.
 */
public class TextViewUtil {

    public static void setTextWithBlueColor(TextView textView, String text, String textWithColor) {
        if (TextUtil.isEmpty(textWithColor)) {
            textView.setText(text);
        } else {
            String s1 = "<a href=\"\">";
            String s2 = "</a>";
            text = text.replace(textWithColor, s1 + textWithColor + s2);
            textView.setText(Html.fromHtml(text));
        }
    }
}
