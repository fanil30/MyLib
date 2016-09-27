package com.wang.android_lib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.ViewGroup;

/**
 * http://blog.csdn.net/baiyuliang2013/article/details/29835391
 * Dialog样式的Activity
 */
public class DialogActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(getIntent().getIntExtra("layoutId", 0));
//        设置全屏模式。必须在setContentViw之后设置全屏，否则不会生效。
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
