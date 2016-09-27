package com.android.internal.telephony;

import android.content.Context;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

/**
 * android怎么实现自动接听和挂断电话功能
 * http://zhidao.baidu.com/link?url=YSG2ke_ViFGXrsDvhhjpLT-KFVfBS_NL4bV_FHP_-jAJBbn-5kNAf957iY_cjEKfYLD5C9nDLR2FZfvd118EM8y7V1GKdG32scH99BKWqC3
 */
public class PhoneUtils {

    /**
     * 根据传入的TelephonyManager来取得系统的ITelephony实例.
     *
     * @param telephony
     * @return 系统的ITelephony实例
     * @throws Exception
     */
    public static ITelephony getITelephony(TelephonyManager telephony)
            throws Exception {
        Method getITelephonyMethod = telephony.getClass().getDeclaredMethod(
                "getITelephony");
        getITelephonyMethod.setAccessible(true);// 私有化函数也能使用
        return (ITelephony) getITelephonyMethod.invoke(telephony);
    }

    public static void endCall(Context context) {
        TelephonyManager telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        // int state = telephony.getCallState();

        try {
            ITelephony iTelephony = PhoneUtils.getITelephony(telephony);
            // iTelephony.answerRingingCall();// 自动接通电话
            iTelephony.endCall();// 自动挂断电话

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}