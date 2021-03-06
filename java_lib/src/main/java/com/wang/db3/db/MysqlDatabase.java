package com.wang.db3.db;

import com.wang.db3.db.sql_creator.ISqlCreator;
import com.wang.db3.db.sql_creator.MysqlCreator;
import com.wang.java_util.DateUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

/**
 * by wangrongjun on 2017/8/21.
 */

public class MysqlDatabase extends DefaultDatabase {

    private String dbName;

    public MysqlDatabase(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public String getDriverName() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public String getUrl() {
        return "jdbc:mysql://localhost:3306/" + dbName + "?characterEncoding=utf-8";
    }

    @Override
    public ISqlCreator getSqlCreator() {
        return new MysqlCreator();
    }

    @Override
    public long insert(Connection conn, String tableName, boolean autoIncrement, String sql)
            throws SQLException {
        Statement stat = conn.createStatement();
        stat.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        int id = 0;
        if (autoIncrement) {
            ResultSet rs = stat.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();
        }
        stat.close();
        return id;
    }

    @Override
    public <T> String convertInsertOrUpdateValue(Field field, T entity) throws IllegalAccessException {
        Object value = field.get(entity);
        if (value == null) {
            return null;
        }
        switch (field.getType().getSimpleName()) {
            case "Date":
                return "'" + DateUtil.toDateTimeText((Date) field.get(entity)) + "'";
            case "String":
                return "'" + formatInputValue(field.get(entity).toString()) + "'";
            case "int":
            case "Integer":
            case "long":
            case "Long":
            case "float":
            case "Float":
            case "double":
            case "Double":
                return field.get(entity) + "";
            default:
                throw new RuntimeException("error: type of field " + field.getName() + " is wrong");
        }
    }

    @Override
    public void setRsValueToEntity(Field field, Object entity, Object value) throws IllegalAccessException {
        if (value == null) {
            field.set(entity, null);
            return;
        }
        if (field.getType().getName().equals("java.util.Date")) {
            Timestamp timestamp = (Timestamp) value;
            field.set(entity, new Date(timestamp.getTime()));
        } else {
            field.set(entity, value);
        }
    }

}
