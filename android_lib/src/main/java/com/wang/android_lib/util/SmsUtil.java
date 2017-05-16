package com.wang.android_lib.util;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.wang.java_util.DebugUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * by wangrongjun on 2017/5/16.
 */
public class SmsUtil {

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public interface SmsHandler {
        void handleSms(String sender, String content, String sendTime);
    }

    /**
     * 接收短信，需要权限：android.permission.RECEIVE_SMS
     * 使用方法：
     * 1.创建BroadcastReceiver并重写onReceiver为：{SmsUtil.onReceive(intent, smsHandler);}
     * 2.注册BroadcastReceiver到AndroidManifests清单，并在里面设置：
     * <intent-filter android:priority="1000" >
     * <action android:name="android.provider.Telephony.SMS_RECEIVED" />
     * </intent-filter>
     */
    public static void onReceive(Intent intent, SmsHandler smsHandler) {
        DebugUtil.println(intent.getAction());
        if (!SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                byte[] pdu = (byte[]) pdus[i];
                messages[i] = SmsMessage.createFromPdu(pdu);
            }

            String sender = "";
            String content = "";
            String sendTime = "";
            for (SmsMessage msg : messages) {
                content += msg.getMessageBody();
                sender = msg.getOriginatingAddress();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.CHINA);
                sendTime = sdf.format(new Date(msg.getTimestampMillis()));
            }

            if (smsHandler != null) {
                smsHandler.handleSms(sender, content, sendTime);
            }
        }
    }

    /**
     * 发送短信，需要权限：android.permission.SEND_SMS
     *
     * @param receiverPhone 收信人号码，如10010
     * @param message       发送的内容
     */
    public static void send(String receiverPhone, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(receiverPhone, null, message, null, null);
    }

}
