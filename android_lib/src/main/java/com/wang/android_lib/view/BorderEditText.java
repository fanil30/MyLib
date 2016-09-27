package com.wang.android_lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.android_lib.R;
import com.wang.android_lib.util.DimensionUtil;
import com.wang.android_lib.util.EditTextUtil;
import com.wang.java_util.TextUtil;

/**
 * by 王荣俊 on 2016/8/12.
 * <p/>
 * http://blog.csdn.net/houshunwei/article/details/17392409
 * Android代码设置Shape,corners,Gradient
 */
public class BorderEditText extends LinearLayout {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_NUMBER = 1;
    public static final int TYPE_PASSWORD = 2;

    private ImageView ivLeftIcon;
    private EditText editText;
    private ImageView btnClear;
    private TextView tvName;

    public BorderEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(HORIZONTAL);
        setMinimumHeight(DimensionUtil.dipToPx(context, 40));


//        0.初始化放置EditText的LinearLayout
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(HORIZONTAL);


//        1.获取属性值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BorderEditText);

        Drawable leftIcon = ta.getDrawable(R.styleable.BorderEditText_leftIcon);
        String text = ta.getString(R.styleable.BorderEditText_text);
        String hint = ta.getString(R.styleable.BorderEditText_hint);
        String name = ta.getString(R.styleable.BorderEditText_name);
        int inputType = ta.getInteger(R.styleable.BorderEditText_inputType, 0);
        float textSize = ta.getDimension(R.styleable.BorderEditText_textSize, -1);
        int textColor = ta.getColor(R.styleable.BorderEditText_textColor, Color.BLACK);
        Drawable clearBtnIcon = ta.getDrawable(R.styleable.BorderEditText_clearBtnIcon);
        int solidColor = ta.getColor(R.styleable.BorderEditText_solidColor, 0);
        int borderColor = ta.getColor(R.styleable.BorderEditText_borderColor, 0);
        float borderWidth = ta.getDimension(R.styleable.BorderEditText_borderWidth, 0);
        float radius = ta.getDimension(R.styleable.BorderEditText_radius, 0);

        ta.recycle();


//        2.初始化左图标
        if (leftIcon != null) {
            ivLeftIcon = new ImageView(context);
            ivLeftIcon.setImageDrawable(leftIcon);
            ivLeftIcon.setAlpha(0.5f);
            LayoutParams leftParams = new LayoutParams(
                    DimensionUtil.dipToPx(context, 25),
                    DimensionUtil.dipToPx(context, 25)
            );
            leftParams.gravity = Gravity.CENTER_VERTICAL;
            leftParams.leftMargin = DimensionUtil.dipToPx(context, 10);
            linearLayout.addView(ivLeftIcon, leftParams);
        }


//        3.初始化EditText
        editText = new EditText(context);
        editText.setText(text);
        editText.setTextColor(textColor);
        editText.setHint(hint);
        setInputType(inputType);
        if (textSize > 0) {
            editText.setTextSize(textSize);
        }
        editText.setBackgroundDrawable(null);
        LayoutParams etParams = new LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1
        );
        etParams.gravity = Gravity.CENTER_VERTICAL;
        int px = DimensionUtil.dipToPx(context, 3);
        etParams.setMargins(px, 0, px, 0);
        linearLayout.addView(editText, etParams);


//        4.初始化清除按钮
        if (clearBtnIcon != null) {
            btnClear = new ImageView(context);
            btnClear.setImageDrawable(clearBtnIcon);
            px = DimensionUtil.dipToPx(context, 10);
            btnClear.setPadding(0, px, 0, px);
            LayoutParams btnClearParams = new LayoutParams(
                    DimensionUtil.dipToPx(context, 40),
                    DimensionUtil.dipToPx(context, 40)
            );
            btnClearParams.gravity = Gravity.CENTER_VERTICAL;
            linearLayout.addView(btnClear, btnClearParams);
        }


//        5.初始化背景
        GradientDrawable drawable = new GradientDrawable();
//        int colors[] = {0xff255779, 0xff3e7492, 0xffa6c0cd};//设置渐变色
//        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        drawable.setColor(solidColor);
        drawable.setStroke((int) borderWidth, borderColor);
        drawable.setCornerRadius(radius);
        linearLayout.setBackgroundDrawable(drawable);


//        6.清空按钮
        if (clearBtnIcon != null) {
            EditTextUtil.setClearButton(editText, btnClear);
        }


//        7.设置输入框左边的名字（在背景框的左边）
        if (!TextUtil.isEmpty(name)) {
            tvName = new TextView(context);
            tvName.setText(name);
            tvName.setTextColor(textColor);
            if (textSize > 0) {
                tvName.setTextSize(textSize);
            }
            LayoutParams tvNameParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            );
            tvNameParams.gravity = Gravity.CENTER_VERTICAL;
            tvNameParams.rightMargin = DimensionUtil.dipToPx(context, 3);
            addView(tvName, tvNameParams);
        }


//        8.把放置EditText的LinearLayout加入
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
        addView(linearLayout, params);

    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public void setInputType(int inputType) {
        switch (inputType) {
            case TYPE_NORMAL:
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case TYPE_NUMBER:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case TYPE_PASSWORD:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
        }
    }

}
