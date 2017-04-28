package com.wang.android_lib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * by wangrongjun on 2017/4/6.
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initData();
        initView();
        initEvent();
    }

    public abstract int getLayoutId();

    public abstract void initData();

    public abstract void initView();

    public abstract void initEvent();

}
