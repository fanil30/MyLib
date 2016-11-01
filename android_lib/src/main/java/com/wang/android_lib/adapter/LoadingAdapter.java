package com.wang.android_lib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wang.android_lib.R;
import com.wang.android_lib.util.AnimationUtil;

/**
 * by 王荣俊 on 2016/9/4.
 */
public class LoadingAdapter extends BaseAdapter {

    private Context context;

    public LoadingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.lv_loading, parent, false);
        ImageView ivLoading = (ImageView) convertView.findViewById(R.id.iv_loading);
        ivLoading.startAnimation(AnimationUtil.getConstantSpeedRotateAnim(1000));
        return convertView;
    }

}
