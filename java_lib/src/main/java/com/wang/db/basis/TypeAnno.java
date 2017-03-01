package com.wang.db.basis;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * by wangrongjun on 2016/12/29.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeAnno {

    Type type() default Type.TEXT;

}
