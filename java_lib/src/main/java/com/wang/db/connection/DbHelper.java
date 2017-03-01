package com.wang.db.connection;

import com.wang.db.basis.DbType;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * by wangrongjun on 2017/2/16.
 */
public interface DbHelper {
    DbType getDbType();

    Connection getConnection() throws SQLException;

    void close();
}
