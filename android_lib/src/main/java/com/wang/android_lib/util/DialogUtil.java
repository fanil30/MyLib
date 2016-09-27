package com.wang.android_lib.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

/**
 *  by Administrator on 2016/3/13.
 */
public class DialogUtil {

    public interface OnInputFinishListener {
        void onInputFinish(String text);
    }

    public interface OnSingleChoiceConfirmListener {
        void onSingleChoiceConfirm(int checkedItem, String item);
    }

    public static void showConfirmDialog(Context context, String title, String message, OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
//        builder.setIcon(icon);
        if (positiveListener != null) {
            builder.setPositiveButton("确认", positiveListener);
        }
        builder.setNegativeButton("取消", null);
//        builder.setNeutralButton("忽略", dialogOnclicListener);
        builder.create().show();
    }

    public static void showItemsDialog(Context context, String title, String[] items, OnClickListener onItemClickListener, OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        if (positiveListener != null) {
            builder.setPositiveButton("确认", positiveListener);
        }
        builder.setItems(items, onItemClickListener);
        builder.create().show();
    }

    private static int checked = 0;

    /**
     * 回调方法的which -1：确认。 >=0:选项id
     */
    public static void showSingleChoiceDialog(Context context, String title, final String[] items,
                                              int checkedItem,
                                              final OnSingleChoiceConfirmListener listener) {
        checked = checkedItem;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onSingleChoiceConfirm(checked, items[checked]);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setSingleChoiceItems(items, checkedItem, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checked = which;
            }
        });
        builder.create().show();
    }

    public static void showMultiChoiceDialog(Context context, String title, String[] items, boolean[] checkedItems, OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        if (positiveListener != null) {
            builder.setPositiveButton("确认", positiveListener);
        }
        builder.setMultiChoiceItems(items, checkedItems, null);
        builder.create().show();
    }

    public static void showInputDialog(Context context, String title, String defaultText, final OnInputFinishListener listener) {
        showInputDialog(context, title, null, defaultText, listener);
    }

    public static void showInputDialog(Context context, String title, String hint, String defaultText, final OnInputFinishListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        final EditText editText = new EditText(context);
        editText.setText(defaultText);
        if (hint != null) {
            editText.setHint(hint);
        }
        builder.setView(editText);
        builder.setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onInputFinish(editText.getText().toString());
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    private static ProgressDialog dialog = null;

    public static void showProgressDialog(Context context, String title, String message,
                                          boolean cancelable) {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setCancelable(cancelable);
            dialog.show();
        }
    }

    public static void showProgressDialog(Context context, String message) {
        showProgressDialog(context, "提示", message, false);
    }

    public static void cancelProgressDialog() {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

}
