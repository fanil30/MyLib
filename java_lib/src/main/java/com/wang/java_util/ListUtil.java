package com.wang.java_util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * by wangrongjun on 2017/8/14.
 */

public class ListUtil {

    @SafeVarargs
    public static <T> List<T> build(T... elementList) {
        List<T> list = new ArrayList<>();
        if (elementList != null && elementList.length > 0) {
            Collections.addAll(list, elementList);
        }
        return list;
    }

}
