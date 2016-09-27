package com.wang.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * by Administrator on 2016/3/18.
 */
public abstract class Table {

    protected Statement statement;

    protected String getDatabaseName() {
        return null;
    }

    protected String getUserName() {
        return null;
    }

    protected String getPassword() {
        return null;
    }

    public Table() {
        try {
            System.out.println(getDatabaseName() + getUserName() + getPassword());
            DbHelper dbHelper = new DbHelper(getDatabaseName(), getUserName(), getPassword());
            statement = dbHelper.getConnection().createStatement();
            setUTF_8();
            onCreateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setUTF_8() throws SQLException {
        statement.execute("set names utf8;");
        String[] sqls = SqlUtil.setCharsetSql("utf8");
        for (String sql : sqls) {
            statement.execute(sql);
        }

    }

    protected void onCreateTable() throws SQLException {
    }

    public synchronized void execute(String sql) throws SQLException {
        statement.execute(sql);
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        return statement.executeQuery(sql);
    }

    public synchronized int executeUpdate(String sql) throws SQLException {
        return statement.executeUpdate(sql);
    }

    /**
     * @return 返回自增主键的id值
     */
    public synchronized int executeInsert(String sql) throws SQLException {
        statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            long i = (long) rs.getObject(1);
            return (int) i;
        } else {
            throw new SQLException(sql + "\nfailed to get id after insert");
        }
    }

    public void close() {
        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
