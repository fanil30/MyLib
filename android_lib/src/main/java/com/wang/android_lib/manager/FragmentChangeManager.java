package com.wang.android_lib.manager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * by 王荣俊 on 2016/8/19.
 * <p/>
 * https://yrom.net/blog/2013/03/10/fragment-switch-not-restart/
 * <p/>
 * 让多个Fragment 切换时不重新实例化
 * <p/>
 * replace()这个方法只是在上一个Fragment不再需要时采用的简便方法。
 * 正确的切换方式是add()，切换时hide()，add()另一个Fragment；再次切换时，
 * 只需hide()当前，show()另一个。这样就能做到多个Fragment切换不重新实例化：
 */
public class FragmentChangeManager {

    private FragmentManager manager;
    private Fragment[] fragments;

    public FragmentChangeManager(android.app.FragmentManager manager, Fragment[] fragments, int containerViewId) {
        this.manager = manager;
        this.fragments = fragments;
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment fragment : fragments) {
            transaction.add(containerViewId, fragment);
            transaction.hide(fragment);
        }
        transaction.commit();
    }

    public void change(int position) {
        if (position < 0 || position >= fragments.length) return;

        FragmentTransaction transaction = manager.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == position) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        transaction.commit();

    }

}
