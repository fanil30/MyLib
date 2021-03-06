package com.wang.db3.db.sql_creator;

import com.wang.db2.Query;
import com.wang.db2.Where;
import com.wang.db3.DefaultTypeLength;
import com.wang.db3.main.TableField;
import com.wang.java_util.ListUtil;
import com.wang.java_util.TextUtil;

import java.util.List;

/**
 * by wangrongjun on 2017/8/21.
 */

public class OracleCreator extends DefaultCreator {

    @Override
    public List<String> createTableSql(String tableName, List<TableField> tableFieldList) {
        String createTableSql = "create table " + tableName + "(";
        String pkName = null;
        boolean autoIncrement = true;

        for (TableField tableField : tableFieldList) {
            createTableSql += "\n\t" + tableField.getName() + " " + getType(tableField);
            if (tableField.isPrimaryKey()) {
                pkName = tableField.getName();
                autoIncrement = tableField.isAutoIncrement();
                createTableSql += " primary key,";
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

        String createSequenceSql = "create sequence sequence_" + tableName;

        String createTriggerSql = "create or replace trigger trigger_" + tableName + "\n" +
                "\tbefore insert on " + tableName + " for each row\n" +
                "\tbegin\n" +
                "\t\tselect sequence_" + tableName + ".nextval into :new." + pkName + "\n" +
                "\t\t\tfrom dual;\n" +
                "\tend;";

        return ListUtil.build(
                createTableSql,
                autoIncrement ? createSequenceSql : null,
                autoIncrement ? createTriggerSql : null,
                "commit"
        );
    }

    @Override
    public List<String> dropTableSql(String tableName) {
        String dropTriggerSql = "drop trigger trigger_" + tableName;
        String dropSequenceSql = "drop sequence sequence_" + tableName;
        String dropTableSql = "drop table " + tableName;
        return ListUtil.build(
                dropTriggerSql,
                dropSequenceSql,
                dropTableSql,
                "commit"
        );
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
            sql = "select * from (select full_result_set.*,rownum rn from (\n" +
                    "\n" +
                    "\t" + sql + "\n" +
                    "\n" +
                    ") full_result_set where rownum<=" + (offset + rowCount) + "\n" +
                    ") where rn>" + offset;
        }

        return sql;
    }

    @Override
    public String queryAutoIncrementCurrentValue(String tableName) {
        return "select sequence_" + tableName + ".currval from dual";
    }

    private static String getType(TableField tableField) {
        int length = tableField.getLength();
        int decimalLength = tableField.getDecimalLength();
        switch (tableField.getType()) {
            case NUMBER_INT:
                if (length == 0) {
                    length = DefaultTypeLength.ORACLE_INT_LENGTH;
                }
                return "number(" + length + ",0)";
            case NUMBER_LONG:
                if (length == 0) {
                    length = DefaultTypeLength.ORACLE_LONG_LENGTH;
                }
                return "number(" + length + ",0)";
            case NUMBER_FLOAT:
                if (length == 0) {
                    length = DefaultTypeLength.ORACLE_FLOAT_LENGTH;
                }
                if (decimalLength == 0) {
                    decimalLength = DefaultTypeLength.ORACLE_FLOAT_DECIMAL_LENGTH;
                }
                return "number(" + length + "," + decimalLength + ")";
            case NUMBER_DOUBLE:
                if (length == 0) {
                    length = DefaultTypeLength.ORACLE_DOUBLE_LENGTH;
                }
                if (decimalLength == 0) {
                    decimalLength = DefaultTypeLength.ORACLE_DOUBLE_DECIMAL_LENGTH;
                }
                return "number(" + length + "," + decimalLength + ")";
            case TEXT:
                if (length == 0) {
                    length = DefaultTypeLength.ORACLE_STRING_LENGTH;
                }
                return "nvarchar2(" + length + ")";
            case DATE:
                return "date";
            default:
                return null;
        }
    }

}
