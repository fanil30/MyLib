package com.wang.android_lib.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.wang.java_util.FileUtil;

import com.wang.android_lib.activity.ShowNotificationActivity;

/**
 * by 王荣俊 on 2016/5/20.
 */
public class NotificationUtil {

    public static final int ICON_WARNING = android.R.drawable.stat_sys_warning;

    public static void showNotification(Context context, int notificationId, String title, String content) {
        showNotification(context, notificationId, ICON_WARNING, title, content, false);
    }

    public static void showNotification(Context context, int notificationId, int smallIconId,
                                        String title, String content, boolean allowCancel) {

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String txtFilePath = Environment.getExternalStorageDirectory() +
                "/notification_" + notificationId + ".txt";
        Intent intent = new Intent(context, ShowNotificationActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("txtFilePath", txtFilePath);
        try {
            FileUtil.write(content, txtFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, notificationId);

        if (content == null) {
            content = "";
        }

        if (content.length() > 100) {
            content = content.substring(0, 100);//防止内容过多造成通知栏卡顿
        }
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(smallIconId)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .getNotification();

//        振动和发音
//        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
//        是否可取消
        notification.flags |= allowCancel ? Notification.FLAG_AUTO_CANCEL : Notification.FLAG_INSISTENT;

        manager.notify(notificationId, notification);

    }
}
