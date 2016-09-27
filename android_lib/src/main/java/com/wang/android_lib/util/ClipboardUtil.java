package com.wang.android_lib.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 *  by Administrator on 2016/3/1.
 */
public class ClipboardUtil {

    public static void addClipboardChangeListener(Context context) {
        final ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {

            @Override
            public void onPrimaryClipChanged() {
//                tv.setText(getClipboard());
            }
        });
    }

    public static String getClipboard(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = cm.getPrimaryClip();
        ClipData.Item item = clipData.getItemAt(0);
        String text = item.getText().toString();
        return text;
    }

    public static void setClipboard(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("data", text));

    }
}
