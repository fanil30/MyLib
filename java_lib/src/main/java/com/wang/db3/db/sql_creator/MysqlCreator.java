package com.wang.db3.db.sql_creator;

import com.wang.db2.Query;
import com.wang.db2.Where;
import com.wang.db3.main.TableField;
import com.wang.java_util.ListUtil;
import com.wang.java_util.TextUtil;

import java.util.List;

/**
 * by wangrongjun on 2017/8/21.
 */

public class MysqlCreator extends DefaultCreator {
    @Override
    public List<String> createTableSql(String tableName, List<TableField> tableFieldList) {
        String createTableSql = "create table if not exists " + tableName + "(";

        for (TableField tableField : tableFieldList) {
            createTableSql += "\n\t" + tableField.getName() + " " + getType(tableField);
            if (tableField.isPrimaryKey()) {
                if (tableField.isAutoIncrement()) {
                    createTableSql += " primary key auto_increment,";
                } else {
                    createTableSql += " primary key,";
                }
                continue;
            }
            if (tableField.isUnique()) {
                createTableSql += " unique";
            }
            if (!tableField.isNullable()) {
                createTableSql += " not null";
            }
            if (!TextUtil.isEmpty(tableField.getDefaultValue())) {
                createTableSql += " default '" + tableField.getDefaultValue() + "'";
            }
            if (tableField.isForeignKey()) {
                createTableSql += " references " + tableField.getReferenceTable() +
                        "(" + tableField.getReferenceColumn() + ")";
            }
            createTableSql += ",";
        }
        createTableSql = createTableSql.substring(0, createTableSql.length() - 1) + "\n)";

        return ListUtil.build(createTableSql);
    }

    @Override
    public List<String> dropTableSql(String tableName) {
        String dropTableSql = "drop table if exists " + tableName;
        return ListUtil.build(dropTableSql);
    }

    @Override
    public String querySql(String tableName, Query query) {
        String sql;

        Where where = query.getWhere();
        if (where != null && where.size() > 0) {
            sql = "select * from " + tableName + " where " + where;
        } else {
            sql = "select * from " + tableName;
        }

        String[] orderBy = query.getOrderBy();
        if (orderBy != null && orderBy.length > 0) {
            sql += " order by ";
            for (String s : orderBy) {
                if (TextUtil.isEmpty(s)) {//防止orderBy数组不为空，但元素为空的情况
                    continue;
                }
                if (s.startsWith("-")) {
                    sql += s.substring(1) + " desc,";
                } else {
                    sql += s + ",";
                }
            }
            sql = sql.substring(0, sql.length() - 1);//去掉最后多余的逗号
        }

        int offset = query.getOffset();
        int rowCount = query.getRowCount();
        if (rowCount > 0 && offset >= 0) {
            sql += " limit " + offset + "," + rowCount;
        }

        return sql;
    }

    @Override
    public String queryAutoIncrementCurrentValue(String tableName) {
        return null;
    }

    private static String getType(TableField tableField) {
        int length = tableField.getLength();
        switch (tableField.getType()) {
            case NUMBER_INT:
                return "int";
            case NUMBER_LONG:
                return "bigint";
            case NUMBER_FLOAT:
                return "float";
            case NUMBER_DOUBLE:
                return "double";
            case TEXT:
                return length == 0 ? "text" : "varchar(" + length + ")";
            case DATE:
                return "datetime";
            default:
                return null;
        }
    }

}
