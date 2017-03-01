package com.wang.db.connection;

import com.wang.db.basis.DbType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 负责数据库的连接
 */
public class DbcpDbHelper implements DbHelper {

    private String username;
    private String password;
    private String mysqlDbName;
    private String sqliteDbFilePath;

    private DbType dbType;

    private Connection connection;

    public DbType getDbType() {
        return dbType;
    }

    /**
     * Mysql的构造方法
     */
    public DbcpDbHelper(String username, String password, String mysqlDbName) {
        this.username = username;
        this.password = password;
        this.mysqlDbName = mysqlDbName;
        dbType = DbType.MYSQL;
    }

    /**
     * Sqlite的构造方法
     */
    public DbcpDbHelper(String sqliteDbFilePath) {
        this.sqliteDbFilePath = sqliteDbFilePath;
        dbType = DbType.SQLITE;
    }

    public Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }

        if (dbType == DbType.MYSQL) {
            return getMysqlConnection();
        } else {
            return getSqliteConnection();
        }
    }

    private Connection getMysqlConnection() throws SQLException {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost/" + mysqlDbName;
//        url += "?useUnicode=true&amp;characterEncoding=UTF-8";
//        connection = DriverManager.getConnection(url, username, password);
        connection = Dbcp.getConnection(url, driver, username, password);
        return connection;
    }

    private Connection getSqliteConnection() throws SQLException {
        String driver = "org.sqlite.JDBC";
        String url = "jdbc:sqlite:" + sqliteDbFilePath;
        connection = Dbcp.getConnection(url, driver, null, null);
        return connection;
    }

    @Override
    public void close() {
        connection = null;
    }

    public void close(Connection conn, PreparedStatement ps) {
        try {
            if (ps != null) ps.close();
            if (conn != null) {
                conn.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) {
                conn.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
