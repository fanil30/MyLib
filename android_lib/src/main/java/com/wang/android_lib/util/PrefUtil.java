package com.wang.android_lib.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.wang.java_util.GsonUtil;

/**
 * by Administrator on 2016/3/1.
 */
public class PrefUtil {

    public static void setEntity(Context context, String prefName, Object entity) {
        SharedPreferences pref = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE);
        String className = entity.getClass().getSimpleName();
        pref.edit().putString(className, GsonUtil.toJson(entity)).apply();
    }

    public static <T> T getEntity(Context context, String prefName, Class<T> entityClass) {
        SharedPreferences pref = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE);
        String json = pref.getString(entityClass.getSimpleName(), "");
        T entity = null;
        try {
            entity = GsonUtil.fromJson(json, entityClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (entity == null) {
            try {
                entity = entityClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    public static void clear(Context context, String prefName) {
        SharedPreferences pref = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE);
        pref.edit().clear().apply();
    }

}
