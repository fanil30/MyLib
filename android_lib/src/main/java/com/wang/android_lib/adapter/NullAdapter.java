package com.wang.android_lib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wang.android_lib.R;

/**
 * by 王荣俊 on 2016/9/4.
 */
public class NullAdapter extends BaseAdapter {

    public static final int NULL_ADAPTER_ID = -452342;

    private Context context;
    private String message;

    public NullAdapter(Context context, String message) {
        this.context = context;
        this.message = message;
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
        return NULL_ADAPTER_ID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.lv_null, parent, false);
        TextView tvMessage = (TextView) convertView.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        return convertView;
    }

}
