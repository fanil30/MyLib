package com.wang.db.v2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * by wangrongjun on 2016/12/29.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstraintAnno {

    Constraint constraint() default Constraint.NULL;

    String defaultValue() default "0";

    String foreignTable() default "0";

    String foreignField() default "0";

    Action action() default Action.RESTRICT;

}
