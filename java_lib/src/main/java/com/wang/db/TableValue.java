package com.wang.db;

/**
 * by 王荣俊 on 2016/5/12.
 */
public class TableValue {

    public static final int INT = 1;
    public static final int DOUBLE = 2;
    public static final int TEXT = 3;
    public static final int EXTRA = 4;

    public String name;
    public int type;
    public Object value;

    public TableValue(String name, int type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value != null ? value : "";//避免字符串为空时会存进字符串"null"
    }

    public TableValue(int type, Object value) {
        this.type = type;
        this.value = value != null ? value : "";
    }

    /**
     * 从类名转换为数据类型标记。
     * <p/>
     * 如：className="int"，返回 TableValue.INT
     */
    public static int getTypeFromClassName(String className) {
        int type = TableValue.EXTRA;
        switch (className) {
            case "int":
            case "Integer":
                type = TableValue.INT;
                break;
            case "float":
            case "Float":
            case "double":
            case "Double":
                type = TableValue.DOUBLE;
                break;
            case "String":
                type = TableValue.TEXT;
                break;
        }
        return type;
    }

}
