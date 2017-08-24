package com.wang.java_util;

import com.wang.db2.Column;
import com.wang.db2.Id;
import com.wang.db2.Ignore;
import com.wang.db2.Reference;
import com.wang.db2.TableUtil;

import java.lang.reflect.Field;

/**
 * by wangrongjun on 2017/8/14.
 */

public class TableOracleUtil extends TableUtil {

    public static String createTableSql(Class entityClass) {
        String sql = "create table if not exists " + entityClass.getSimpleName() + " (\n";
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.getAnnotation(Ignore.class) != null) {
                continue;
            }
            sql += field.getName() + " " + getType(field);
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno != null) {
                if (idAnno.autoIncrement()) {
                    sql += " primary key auto_increment,\n";
                } else {
                    sql += " primary key,\n";
                }
                continue;
            }
            Column columnAnno = field.getAnnotation(Column.class);
            if (columnAnno != null) {
                if (!columnAnno.nullable()) {
                    sql += " not null";
                }
                if (columnAnno.unique()) {
                    sql += " unique key";
                }
            }
            sql += ",\n";
        }
//        sql = sql.substring(0, sql.length() - 2) + "\n) default charset=utf8;";
        return sql;
    }

    /**
     * String -> varchar(20)
     * User -> int/bigint
     */
    protected static String getType(Field field) {
        if (field.getAnnotation(Reference.class) != null) {
            Field innerIdField = ReflectUtil.findByAnno(field.getType(), Id.class);
            Column columnAnno = innerIdField.getAnnotation(Column.class);
            int length = 8;
            int decimalLength = 0;
            if (columnAnno != null) {
                length = columnAnno.length() == 0 ? length : columnAnno.length();
                decimalLength = columnAnno.decimalLength();
            }
            switch (innerIdField.getType().getSimpleName()) {
                case "int":
                case "Integer":
                    length += 8;
                case "long":
                case "Long":
                    return "number(" + length + "," + decimalLength + ")";
            }
        }
        switch (field.getType().getSimpleName()) {
            case "int":
            case "Integer":
                return "int";
            case "long":
            case "Long":
                return "bigint";
            case "float":
            case "Float":
                return "float";
            case "double":
            case "Double":
                return "double";
            case "String":
                Column column = field.getAnnotation(Column.class);
                if (column == null || column.length() <= 0) {
                    return "text";
                }
                return "varchar(" + column.length() + ")";
            case "Date":
                return "datetime";
        }
        return null;
    }

    public static String dropTableSql(Class entityClass) {
        return "drop table " + entityClass.getSimpleName() + ";";
    }

}
