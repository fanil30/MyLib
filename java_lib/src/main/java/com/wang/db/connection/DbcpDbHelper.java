package com.wang.db.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 负责数据库的连接
 */
public class DbcpDbHelper extends MysqlDbHelper {

    public DbcpDbHelper(String username, String password, String mysqlDbName) {
        super(username, password, mysqlDbName);
    }

    public DbcpDbHelper(String username, String password, String mysqlDbName, String hostIP) {
        super(username, password, mysqlDbName, hostIP);
    }

    @Override
    public Connection getConnection() throws SQLException {
        connection = Dbcp.getConnection(url, driver, username, password);
        return connection;
    }

    @Override
    public void close() {

    }

}
