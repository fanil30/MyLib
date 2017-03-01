package com.wang.db.connection;

import com.wang.db.basis.DbType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * by wangrongjun on 2017/2/14.
 */
public class MysqlDbHelper implements DbHelper {

    private static final String driver = "com.mysql.jdbc.Driver";

    private String url;
    private String username;
    private String password;
    private Connection connection;

    public MysqlDbHelper(String username, String password, String mysqlDbName) {
        this.username = username;
        this.password = password;
        this.url = "jdbc:mysql://localhost:3306/" + mysqlDbName;
    }

    @Override
    public DbType getDbType() {
        return DbType.MYSQL;
    }

    @Override
    public Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    @Override
    public void close() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection = null;
    }

}
