package com.wang.android_lib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.wang.android_lib.R;
import com.wang.java_util.FileUtil;


/**
 * by 王荣俊 on 2016/5/20.
 */
public class ShowNotificationActivity extends Activity {

    private TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification);
        tvShow = (TextView) findViewById(R.id.tv_show);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String txtFilePath = getIntent().getStringExtra("txtFilePath");
        String s = FileUtil.read(txtFilePath);
        if (s == null) {
            tvShow.setText("txt文件不存在，请确认是否有写外存的权限！");
        } else {
            tvShow.setText(s);
        }
    }

}
