package com.wang.android_lib.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.wang.android_lib.R;

/*
<!--按钮小组件接收器-->
<receiver
android:name="com.wang.android_lib.receiver.ToggleAppWidgetProvider"
        android:label="@string/app_name">
<intent-filter>
<action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
<action android:name="com.wang.android_lib.action.widget.toggle.click" />
</intent-filter>

<meta-data
        android:name="android.appwidget.provider"
        android:resource="@xml/config_widget_toggle_button" />
</receiver>
*/

/**
 *  by 王荣俊 on 2016/6/28.
 */
public class ToggleAppWidgetProvider extends AppWidgetProvider {

    protected static boolean isOpen = false;
    /**
     * 是否可以点击切换状态，用来以防进行耗时操作尚未完成，用户又按一次，导致混乱。
     */
    protected static boolean canToggle = true;

    public static final String CLICK_ACTION = "com.wang.android_lib.action.widget.toggle.click";

    protected static String openText = "ON";
    protected static String closeText = "OFF";

    /**
     * 小组件第一次拖出来时调用3次，action为
     * android.appwidget.action.APPWIDGET_ENABLE
     * android.appwidget.action.APPWIDGET_UPDATE（同时调用onUpdate）
     * android.appwidget.action.APPWIDGET_UPDATE_OPTIONS
     * <p/>
     * 小组件删除时调用2次，action为
     * android.appwidget.action.APPWIDGET_DELETE
     * android.appwidget.action.APPWIDGET_DISABLE
     * <p/>
     * 小组件的按钮点击后调用一次。当然，不一定要使用这一个，可以使用其他Receiver
     * com.wang.cutsong.action.widget.toggle.click
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        onCreate(context);

        if (CLICK_ACTION.equals(intent.getAction()) && canToggle) {

            if (isOpen) {//在打开状态下，用户按了按钮，要开始进行关闭的操作
                if (!onClose(context, intent)) {
                    return;
                }
            } else {
                if (!onOpen(context, intent)) {
                    return;
                }
            }

            isOpen = !isOpen;
            updateWidget(context, AppWidgetManager.getInstance(context));
        }


    }

    /**
     * 小组件第一次拖出来调用的方法，一般用于小组件的初始化操作
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        updateWidget(context, appWidgetManager);
    }

    public void updateWidget(Context context, AppWidgetManager appWidgetManager) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_toggle_button);

        if (isOpen) {
            rv.setImageViewResource(R.id.iv_widget_toggle, R.mipmap.btn_toggle_selected);
            rv.setTextViewText(R.id.tv_toggle_widget, openText);
            rv.setTextColor(R.id.tv_toggle_widget, context.getResources().getColor(R.color.green));
        } else {
            rv.setImageViewResource(R.id.iv_widget_toggle, R.mipmap.btn_toggle_normal);
            rv.setTextViewText(R.id.tv_toggle_widget, closeText);
            rv.setTextColor(R.id.tv_toggle_widget, context.getResources().getColor(R.color.purple));
        }

        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, new Intent(CLICK_ACTION), 0);
        rv.setOnClickPendingIntent(R.id.btn_toggle_widget, pIntent);

        ComponentName cn = new ComponentName(context, getClass());
        appWidgetManager.updateAppWidget(cn, rv);
    }

    /**
     * @return 返回操作是否成功的情况
     */
    protected boolean onOpen(Context context, Intent intent) {
        return true;
    }

    /**
     * @return 返回操作是否成功的情况
     */
    protected boolean onClose(Context context, Intent intent) {
        return true;
    }

    protected void onCreate(Context context) {
    }

    /**
     * 用来在耗时操作后调用
     */
    protected void updateWidget(Context context) {
        updateWidget(context, AppWidgetManager.getInstance(context));
    }

}
