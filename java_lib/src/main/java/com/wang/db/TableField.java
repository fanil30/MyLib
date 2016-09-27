package com.wang.db;

/**
 * 建表的字段
 */
public class TableField {

    public static final int TYPE_TINYINT = 1;
    public static final int TYPE_INT = 2;
    public static final int TYPE_DOUBLE = 7;
    public static final int TYPE_VARCHAR_20 = 3;
    public static final int TYPE_VARCHAR_40 = 4;
    public static final int TYPE_VARCHAR_200 = 5;
    public static final int TYPE_TEXT = 6;
    public static final int TYPE_EXTRA = 7;

    public String name;
    public int type;
    public boolean primaryKey = false;
    public boolean unsigned = false;
    public boolean notNull = false;
    public boolean unique = false;
    public TableValue defaultValue;

    public TableField(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public TableField primaryKey() {
        primaryKey = true;
        return this;
    }

    public TableField unsigned() {
        unsigned = true;
        return this;
    }

    public TableField notNull() {
        notNull = true;
        return this;
    }

    public TableField unique() {
        unique = true;
        return this;
    }

    /**
     * 该方法会先判断value的类型，若为字符串，则自动添加单引号
     */
    public TableField defaultValue(TableValue defaultValue) {
        if (defaultValue.type == TableValue.TEXT) {
            defaultValue.value = "'" + defaultValue.value + "'";
        }
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * 从类名转换为数据类型标记。
     * <p/>
     * 如：className="int"，返回 TableField.TYPE_INT
     */
    public static int getTypeFromClassName(String className) {
        int type = TableField.TYPE_EXTRA;
        switch (className) {
            case "int":
            case "Integer":
                type = TableField.TYPE_INT;
                break;
            case "float":
            case "Float":
            case "double":
            case "Double":
                type = TableField.TYPE_DOUBLE;
                break;
            case "String":
                type = TableField.TYPE_TEXT;
                break;
        }
        return type;
    }

}
