package com.wang.db.v2;

import com.wang.java_util.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成各种sql语句的工具类。使用之前需要先配置SqlUtil.dbType，默认为SqlUtil.TYPE_MYSQL
 */
public class SqlUtil {

    public static DbType dbType = DbType.MYSQL;

    public static String[] setCharsetSql(String charsetName) {
        String[] sqls = new String[5];
        sqls[0] = "set character_set_client = '" + charsetName + "';";
        sqls[1] = "set character_set_connection = '" + charsetName + "';";
        sqls[2] = "set character_set_database = '" + charsetName + "';";
        sqls[3] = "set character_set_results = '" + charsetName + "';";
        sqls[4] = "set character_set_server = '" + charsetName + "';";
        return sqls;
    }

    /**
     * @param charset 空则默认为utf8
     */
    public static String createDatabaseSql(String name, String charset) {
        charset = TextUtil.isEmpty(charset) ? "utf8" : charset;
        return "create database if not exists " + name + " character set " + charset + ";";
    }

    /**
     * 创建表的生存方法
     */
    public static String createTableSql(String tableName, List<TableField> tableFields) {
        StringBuilder sql = new StringBuilder();
        sql.append("create table if not exists ").append(tableName).append("(\n");
        for (int i = 0; i < tableFields.size(); i++) {
            TableField field = tableFields.get(i);
            String s = field.name + " " +
                    getType(field.type) +
                    (field.unsigned ? " unsigned" : "") +
                    (field.primaryKey ? " primary key auto_increment" : "") +
                    ((!field.primaryKey && field.notNull) ? " not null" : "") +
                    ((!field.primaryKey && field.unique) ? " unique key" : "") +
                    (field.defaultValue != null ? " default " + field.defaultValue.value : "") +
                    (i < tableFields.size() - 1 ? ",\n" : "\n) ");
            sql.append(s);
        }

        if (dbType == DbType.MYSQL) {
            sql.append("default charset=utf8;");
            return sql.toString();
        } else if (dbType == DbType.SQLITE) {
            sql.append(";");
            return sql.toString().replace("auto_increment", "autoincrement");
        } else {
            return "";
        }

    }

    public static String dropTableSql(String tableName) {
        return "drop table if exists " + tableName + ";";
    }

    public static String dropTableColumnSql(String tableName, String columnName) {
        return "alter table " + tableName + " drop " + columnName + ";";
    }

    public static String setAutoIncrementNumberSql(String tableName, int number) {
        return "alter table " + tableName + " auto_increment=" + number + ";";
    }

    public static String getLatestAutoIncrementNumberSql(String tableName, String primaryKeyName) {
        //第一种方法：select last_insert_rowid()，暂时不知执行sql后怎么在Cursor中获取结果。
        //第二种方法：select max(ID) from 表名
        return "select max(" + primaryKeyName + ") from " + tableName + ";";
    }

    public static String foreignKeySql(String mainTableName,
                                       String mainFieldName,
                                       String referenceTableName,
                                       String referenceFieldName,
                                       Action onDeleteAction,
                                       Action onUpdateAction) {

//        String sss = "ALTER TABLE `orders` ADD FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) " +
//                "ON DELETE CASCADE ON UPDATE SET NULL;";
        String sql = "alter table " + mainTableName + " add foreign key (" + mainFieldName + ") " +
                "references " + referenceTableName + "(" + referenceFieldName + ") ";

        String onDeleteActionSql = "";
        String onUpdateActionSql = "";

        switch (onDeleteAction) {
            case NO_ACTION:
                onDeleteActionSql = "on delete no action";
                break;
            case SET_NULL:
                onDeleteActionSql = "on delete set null";
                break;
            case CASCADE:
                onDeleteActionSql = "on delete cascade";
                break;
        }

        switch (onUpdateAction) {
            case NO_ACTION:
                onUpdateActionSql = "on update no action";
                break;
            case SET_NULL:
                onUpdateActionSql = "on update set null";
                break;
            case CASCADE:
                onUpdateActionSql = "on update cascade";
                break;
        }

        if (!TextUtil.isEmpty(onDeleteActionSql)) {
            sql += onDeleteActionSql + " ";
        }
        if (!TextUtil.isEmpty(onUpdateActionSql)) {
            sql += onUpdateActionSql + " ";
        }

        sql += ";";
        return sql;
    }

    /**
     * 生成插入语句
     */
    public static String insertSql(String tableName, List<TableValue> tableValues) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert ").append(tableName);
        String nameList = "(";
        String valueList = "values(";
        for (int i = 0; i < tableValues.size(); i++) {
            TableValue tv = tableValues.get(i);

            nameList += tv.name + (i < tableValues.size() - 1 ? "," : ")");

            switch (tv.type) {
                case TINYINT:
                case INT:
                    valueList += Integer.parseInt(tv.value + "");
                    break;
                case DOUBLE:
                    valueList += Double.parseDouble(tv.value + "");
                    break;
                case VARCHAR_10:
                case VARCHAR_20:
                case VARCHAR_50:
                case VARCHAR_100:
                case VARCHAR_500:
                case TEXT:
                    //一定要进行特殊字符'和\的转义，否则sql语法出错！！！
                    valueList += "'" + toCorrectValue(tv.value) + "'";
                    break;
                case EXTRA:
                    break;
            }

            valueList += i < tableValues.size() - 1 ? "," : ")";
        }

        sql.append(nameList).append(" ").append(valueList).append(";");

        if (dbType == DbType.MYSQL) {
            return sql.toString();
        } else if (dbType == DbType.SQLITE) {
            return sql.toString().replace("insert", "insert into");
        } else {
            return "";
        }

    }

    /**
     * 生成删除语句 - 具体实现
     */
    public static String deleteSql(String tableName, List<TableValue> wheres) {
        String sql = "delete from " + tableName;
        if (wheres == null || wheres.size() == 0) {
            return sql + ";";
        } else {
            sql += " where " + getExpressionList(wheres, " and ");
        }

        sql += ";";
        return sql;
    }

    /**
     * 生成删除语句 - 接口实现
     */
    public static String deleteSql(String tableName, TableValue where) {
        List<TableValue> wheres = new ArrayList<>();
        wheres.add(where);
        return deleteSql(tableName, wheres);
    }

    /**
     * 生成更新语句 - 具体实现
     */
    public static String updateSql(String tableName, List<TableValue> setValues,
                                   List<TableValue> wheres) {
        String sql = "update " + tableName + " set ";
        sql += getExpressionList(setValues, ",");
        sql += " where " + getExpressionList(wheres, " and ") + ";";
        return sql;
    }

    /**
     * 生成更新语句 - 接口实现1
     */
    public static String updateSql(String tableName, TableValue setValue,
                                   List<TableValue> wheres) {
        List<TableValue> setValues = new ArrayList<>();
        setValues.add(setValue);
        return updateSql(tableName, setValues, wheres);
    }

    /**
     * 生成更新语句 - 接口实现2
     */
    public static String updateSql(String tableName, List<TableValue> setValues,
                                   TableValue where) {
        List<TableValue> wheres = new ArrayList<>();
        wheres.add(where);
        return updateSql(tableName, setValues, wheres);
    }

    /**
     * 生成更新语句 - 接口实现3
     */
    public static String updateSql(String tableName, TableValue setValue, TableValue where) {
        List<TableValue> wheres = new ArrayList<>();
        wheres.add(where);
        List<TableValue> setValues = new ArrayList<>();
        setValues.add(setValue);
        return updateSql(tableName, setValues, wheres);
    }

    /**
     * 生成查询语句 - 具体实现
     *
     * @param wheres    若为空或长度为0，则查询所有记录
     * @param orderBy   根据该字段对结果排序，若为空或长度为0，则参数orderDesc无效，默认排序
     * @param orderDesc 是否倒序排列结果
     */
    public static String querySql(String tableName, List<TableValue> wheres,
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

    /**
     * 生成查询语句 - 接口实现
     */
    public static String querySql(String tableName, TableValue where,
                                  String orderBy, boolean orderDesc) {
        List<TableValue> wheres = new ArrayList<>();
        wheres.add(where);
        return querySql(tableName, wheres, orderBy, orderDesc);
    }

    /**
     * 生成模糊查询语句 - 具体实现
     */
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


    /**
     * @param separator 表达式之间的分隔符。常见的有" and "(where条件语句) 和 ","（update赋值语句）
     * @return 如 "name='wang',date='2015-03-11',sex=1" 或 "group_id=4 and date='2015-06-12'"
     */
    private static String getExpressionList(List<TableValue> tableValues, String separator) {
        String expressionList = "";
        for (int i = 0; i < tableValues.size(); i++) {
            TableValue tv = tableValues.get(i);
            String value = "";
            switch (tv.type) {
                case TINYINT:
                case INT:
                    value += Integer.parseInt(tv.value + "");
                    break;
                case DOUBLE:
                    value += Double.parseDouble(tv.value + "");
                    break;
                case VARCHAR_10:
                case VARCHAR_20:
                case VARCHAR_50:
                case VARCHAR_100:
                case VARCHAR_500:
                case TEXT:
                    value += "'" + tv.value + "'";
                    break;
                case EXTRA:
                    break;
            }
            expressionList += tv.name + "=" + value + (i < tableValues.size() - 1 ? separator : "");
        }

        return expressionList;
    }

    /**
     * @param after 空则插在第一位，否则名为after的列的后面
     */
    public static String addTableColumn(String tableName, TableField field, String after) {
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

    private static String getType(Type type) {
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
