package com.wang.android_lib.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 *  by 王荣俊 on 2016/7/9.
 */
public class FragmentUtil {

    public static void changeFragment(Activity activity, Fragment fragment, int replaceViewId) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(replaceViewId, fragment);
        //保证完全替换。否则切换次数多了容易出错。参考：http://bbs.csdn.net/topics/391842734
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commitAllowingStateLoss();
    }
    
}
