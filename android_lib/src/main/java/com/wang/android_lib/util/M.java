package com.wang.android_lib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * by Administrator on 2016/2/29.
 */
public class M {

    public static void t(Context context, String text) {
        if (text == null) text = "";
        if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void tl(Context context, String text) {
        if (text == null) text = "";
        if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }

    public static void i(String info) {
        Log.i("info", info);
    }

    public static void e(String error) {
        Log.e("error", error);
    }

    @SuppressLint("SimpleDateFormat")
    public static void saveLog(String info) {

        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日  HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);

        String className = "";
        String methodName = "";

        try {
            int i = 1 / 0;
            i = i + 1;
        } catch (Exception e) {
            StackTraceElement[] element = e.getStackTrace();
            if (element != null && element.length > 1) {
                className = element[1].getClassName();
                methodName = element[1].getMethodName();
            }
        }

        String text = "time: " + time + "\n" + className + "-" + methodName
                + ":\n" + info + "\n\n";

        try {
            FileWriter writer = new FileWriter(
                    Environment.getExternalStorageDirectory() + "/log.txt");
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
