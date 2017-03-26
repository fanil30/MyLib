package com.wang.db.basis;

/**
 * by wangrongjun on 2016/12/31.
 * 数据表字段的类型，一般在定义Bean，生成创建数据表的sql时用到
 */
public enum FieldType {
    TINYINT,
    INT,
    DOUBLE,

    TEXT,
    VARCHAR_10,
    VARCHAR_20,
    VARCHAR_50,
    VARCHAR_100,
    VARCHAR_500,

    EXTRA;

    public static String toFieldTypeString(DbType dbType, FieldType type) {
        String strType = "";
        switch (type) {
            case TINYINT:
                strType = "tinyint";
                break;
            case INT:
                if (dbType == DbType.MYSQL) {
                    strType = "int";
                } else if (dbType == DbType.SQLITE) {
                    strType = "integer";
                }
                break;
            case DOUBLE:
                if (dbType == DbType.MYSQL) {
                    strType = "double";
                } else if (dbType == DbType.SQLITE) {
                    strType = "real";
                }
                break;
            case VARCHAR_10:
                strType = "varchar(10)";
                break;
            case VARCHAR_20:
                strType = "varchar(20)";
                break;
            case VARCHAR_50:
                strType = "varchar(50)";
                break;
            case VARCHAR_100:
                strType = "varchar(100)";
                break;
            case VARCHAR_500:
                strType = "varchar(500)";
                break;
            case TEXT:
                strType = "text";
                break;
        }
        return strType;
    }
}
