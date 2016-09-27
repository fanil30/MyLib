package com.wang.java_util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/4.
 */
public class ReflectUtil {

    public static void showDeclaredFields(Class cls) {
        getDeclaredFields(cls);
    }

    public static void showDeclaredFields(String packageName) {
        try {
            getDeclaredFields(Class.forName(packageName));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void showDeclaredMethods(Class cls) {
        getDeclaredMethods(cls);
    }

    public static void showDeclaredMethods(String packageName) {
        try {
            getDeclaredMethods(Class.forName(packageName));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> getDeclaredFields(Class cls) {
        ArrayList<String> strFields = new ArrayList<>();

        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String modifiers = Modifier.toString(fields[i].getModifiers());
            String type = TextUtil.getTextAfterLastPoint(fields[i].getType().toString());
            String name = fields[i].getName();
            String s = modifiers + " " + type + " " + name + ";";
            strFields.add(s);
            System.out.println(s);
        }

        return strFields;
    }

    private static ArrayList<String> getDeclaredMethods(Class cls) {
        ArrayList<String> strFields = new ArrayList<>();

        Method[] methods = cls.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            String modifiers = Modifier.toString(methods[i].getModifiers());
            String declare = TextUtil.getTextAfterLastPoint(methods[i].getReturnType().toString());
            declare = declare.replace(";", "");
            String name = methods[i].getName();
            String parameters = "";
            Class[] c = methods[i].getParameterTypes();
            for (int j = 0; j < c.length; j++) {
                parameters += TextUtil.getTextAfterLastPoint(c[j].toString()) + ", ";
            }
            if (c.length > 0) {
                parameters = parameters.substring(0, parameters.length() - 2);
            }

            String s = modifiers + " " + declare + " " + name + "(" + parameters + ")" + ";";
            strFields.add(s);
            System.out.println(s);
        }

        return strFields;
    }
}
