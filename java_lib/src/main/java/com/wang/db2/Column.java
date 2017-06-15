package com.wang.db2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * by wangrongjun on 2017/6/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    int length() default 0;

    boolean nullable() default true;

    boolean unique() default false;

}
