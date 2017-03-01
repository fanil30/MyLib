package com.wang.db.basis;

/**
 * by 王荣俊 on 2016/5/12.
 */
public class TableValue {

    public String name;
    public Type type;
    public String value;

    public TableValue(String name, Type type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public TableValue(Type type, String value) {
        this.type = type;
        this.value = value;
    }

}
