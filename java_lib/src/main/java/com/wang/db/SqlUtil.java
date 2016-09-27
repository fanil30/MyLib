package com.wang.db;

import com.wang.java_util.TextUtil;

import java.util.ArrayList;

/**
 * 生成各种sql语句的工具类。使用之前需要先配置SqlUtil.dbType，默认为SqlUtil.TYPE_MYSQL
 */
public class SqlUtil {

    public static final int TYPE_MYSQL = 1;
    public static final int TYPE_SQLITE = 2;
    public static int dbType = TYPE_MYSQL;

    public static String[] setCharsetSql(String charsetName) {
        String[] sqls = new String[5];
        sqls[0] = "set character_set_client = '" + charsetName + "';";
        sqls[1] = "set character_set_connection = '" + charsetName + "';";
        sqls[2] = "set character_set_database = '" + charsetName + "';";
        sqls[3] = "set character_set_results = '" + charsetName + "';";
        sqls[4] = "set character_set_server = '" + charsetName + "';";
        return sqls;
    }

    public static String setAutoIncrementNumber(String tableName, int number) {
        return "alter table " + tableName + " auto_increment=" + number + ";";
    }

    /**
     * @param charset 空则默认为utf8
     */
    public static String createDatabaseSql(String name, String charset) {
        charset = TextUtil.isEmpty(charset) ? "utf8" : charset;
        return "create database if not exists " + name + " character set " + charset + ";";
    }

    public static String createTableSql(String tableName, ArrayList<com.wang.db.TableField> tableFields) {
        StringBuilder sql = new StringBuilder();
        sql.append("create table if not exists ").append(tableName).append("(");
        for (int i = 0; i < tableFields.size(); i++) {
            com.wang.db.TableField field = tableFields.get(i);
            String s = field.name + " " +
                    getType(field.type) +
                    (field.unsigned ? " unsigned" : "") +
                    (field.primaryKey ? " primary key auto_increment" : "") +
                    ((!field.primaryKey && field.notNull) ? " not null" : "") +
                    ((!field.primaryKey && field.unique) ? " unique key" : "") +
                    (field.defaultValue != null ? " default " + field.defaultValue.value : "") +
                    (i < tableFields.size() - 1 ? ",\n" : ")");
            sql.append(s);
        }

        if (dbType == TYPE_MYSQL) {
            sql.append("default charset=utf8;");
            return sql.toString();
        } else if (dbType == TYPE_SQLITE) {
            sql.append(";");
            return sql.toString().replace("auto_increment", "autoincrement");
        } else {
            return "";
        }

    }

    public static String insertSql(String tableName, ArrayList<TableValue> tableValues) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert ").append(tableName);
        String nameList = "(";
        String valueList = "values(";
        for (int i = 0; i < tableValues.size(); i++) {
            TableValue tv = tableValues.get(i);

            nameList += tv.name + (i < tableValues.size() - 1 ? "," : ")");

            if (tv.type == TableValue.TEXT) {
                //一定要进行特殊字符'和\的转义，否则sql语法出错！！！
                valueList += "'" + toCorrectValue((String) tv.value) + "'";
            } else if (tv.type == TableValue.INT) {
                valueList += Integer.parseInt(tv.value + "");
            } else if (tv.type == TableValue.DOUBLE) {
                valueList += Double.parseDouble(tv.value + "");
            }
            valueList += i < tableValues.size() - 1 ? "," : ")";

        }

        sql.append(nameList).append(" ").append(valueList).append(";");

        if (dbType == TYPE_MYSQL) {
            return sql.toString();
        } else if (dbType == TYPE_SQLITE) {
            return sql.toString().replace("insert", "insert into");
        } else {
            return "";
        }

    }

    /**
     * @param wheres    若为空或长度为0，则查询所有记录
     * @param orderBy   根据该字段对结果排序，若为空或长度为0，则参数orderDesc无效，默认排序
     * @param orderDesc 是否倒序排列结果
     */
    public static String querySql(String tableName, ArrayList<TableValue> wheres,
                                  String orderBy, boolean orderDesc) {
        String sql = "select * from " + tableName;
        if (wheres != null && wheres.size() != 0) {
            sql += " where " + getExpressionList(wheres, " and ");
        }

        if (!TextUtil.isEmpty(orderBy)) {
            sql += " order by " + orderBy;
            if (orderDesc) {
                sql += " desc";
            }
        }
        sql += ";";
        return sql;
    }

    public static String querySql(String tableName, TableValue where,
                                  String orderBy, boolean orderDesc) {
        ArrayList<TableValue> wheres = new ArrayList<>();
        wheres.add(where);
        return querySql(tableName, wheres, orderBy, orderDesc);
    }

    public static String queryFuzzySql(String tableName, TableValue where,
                                       String orderBy, boolean orderDesc) {
        String sql = "select * from " + tableName;
        sql += " where " + where.name + " like '%" + where.value + "%'";

        if (!TextUtil.isEmpty(orderBy)) {
            sql += " order by " + orderBy;
            if (orderDesc) {
                sql += " desc";
            }
        }
        sql += ";";
        return sql;
    }

    public static String deleteSql(String tableName, ArrayList<TableValue> wheres) {
        String sql = "delete from " + tableName;
        if (wheres == null || wheres.size() == 0) {
            return sql + ";";
        } else {
            sql += " where " + getExpressionList(wheres, " and ");
        }

        sql += ";";
        return sql;
    }

    public static String deleteSql(String tableName, TableValue where) {
        ArrayList<TableValue> wheres = new ArrayList<>();
        wheres.add(where);
        return deleteSql(tableName, wheres);
    }

    public static String updateSql(String tableName, ArrayList<TableValue> setValues,
                                   ArrayList<TableValue> wheres) {
        String sql = "update " + tableName + " set ";
        sql += getExpressionList(setValues, ",");
        sql += " where " + getExpressionList(wheres, " and ") + ";";
        return sql;

    }

    public static String updateSql(String tableName, TableValue setValue,
                                   ArrayList<TableValue> wheres) {
        ArrayList<TableValue> setValues = new ArrayList<>();
        setValues.add(setValue);
        return updateSql(tableName, setValues, wheres);
    }

    public static String updateSql(String tableName, ArrayList<TableValue> setValues,
                                   TableValue where) {
        ArrayList<TableValue> wheres = new ArrayList<>();
        wheres.add(where);
        return updateSql(tableName, setValues, wheres);
    }

    public static String updateSql(String tableName, TableValue setValue, TableValue where) {
        ArrayList<TableValue> wheres = new ArrayList<>();
        wheres.add(where);
        ArrayList<TableValue> setValues = new ArrayList<>();
        setValues.add(setValue);
        return updateSql(tableName, setValues, wheres);
    }

    public static String dropTableSql(String tableName) {
        return "drop table if exists " + tableName + ";";
    }

    /**
     * @param separator 表达式之间的分隔符。常见的有" and "(where条件语句) 和 ","（update赋值语句）
     * @return 如 "name='wang',date='2015-03-11',sex=1" 或 "group_id=4 and date='2015-06-12'"
     */
    private static String getExpressionList(ArrayList<TableValue> tableValues, String separator) {
        String expressionList = "";
        for (int i = 0; i < tableValues.size(); i++) {
            TableValue tv = tableValues.get(i);
            String value = "";
            if (tv.type == TableValue.TEXT) {
                value += "'" + tv.value + "'";
            } else if (tv.type == TableValue.INT) {
                value += Integer.parseInt(tv.value + "");
            } else if (tv.type == TableValue.DOUBLE) {
                value += Double.parseDouble(tv.value + "");
            }

            expressionList += tv.name + "=" + value + (i < tableValues.size() - 1 ? separator : "");
        }

        return expressionList;
    }

    /**
     * @param after 空则插在第一位，否则名为after的列的后面
     */
    public static String addTableColumn(String tableName, com.wang.db.TableField field, String after) {
        String sql = "alter table " + tableName + " add ";
        sql += field.name + " " +
                getType(field.type) +
                (field.unsigned ? " unsigned" : "") +
                (field.primaryKey ? " primary key auto_increment" : "") +
                ((!field.primaryKey && field.notNull) ? " not null" : "") +
                ((!field.primaryKey && field.unique) ? " unique key" : "") +
                ((!TextUtil.isEmpty(after)) ? " after " + after : " first") +
                ";";
        return sql;
    }

    public static String dropTableColumn(String tableName, String columnName) {
        String sql = "alter table " + tableName + " drop " + columnName + ";";
        return sql;
    }

    /**
     * 对插入、查询等需要用到的value的特殊字符进行转义
     * <p/>
     * 1.若出现单引号，进行转义
     * <p/>
     * 2.末尾若出现转义字符，进行二次转义
     */
    public static String toCorrectValue(String value) {
        value = value.replaceAll("'", "\\'");
        if (value.endsWith("\\")) {
            value = value + "\\";
        }
        return value;
    }

    private static String getType(int type) {
        String strType = "";
        switch (type) {
            case com.wang.db.TableField.TYPE_TINYINT:
                strType = "tinyint";
                break;
            case com.wang.db.TableField.TYPE_INT:
                if (dbType == TYPE_MYSQL) {
                    strType = "int";
                } else if (dbType == TYPE_SQLITE) {
                    strType = "integer";
                }
                break;
            case com.wang.db.TableField.TYPE_DOUBLE:
                if (dbType == TYPE_MYSQL) {
                    strType = "double";
                } else if (dbType == TYPE_SQLITE) {
                    strType = "real";
                }
                break;
            case com.wang.db.TableField.TYPE_VARCHAR_20:
                strType = "varchar(20)";
                break;
            case com.wang.db.TableField.TYPE_VARCHAR_40:
                strType = "varchar(40)";
                break;
            case com.wang.db.TableField.TYPE_VARCHAR_200:
                strType = "varchar(200)";
                break;
            case com.wang.db.TableField.TYPE_TEXT:
                strType = "text";
                break;

        }

        return strType;
    }

}
