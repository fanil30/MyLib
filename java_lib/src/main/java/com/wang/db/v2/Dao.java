package com.wang.db.v2;

import com.wang.java_util.DebugUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 拥有已经实现好的万能增删查改等方法的类
 */
public class Dao {

    protected DbHelper dbHelper;

    public Dao(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
        SqlUtil.dbType = dbHelper.getDbType();
        DaoUtil.dbType = dbHelper.getDbType();
    }

    /**
     * 根据entityClass创建数据表，字段属性为可空。只支持int，double，String三种数据类型，
     * 在数据表中分别对应int，double,text。
     * <p/>
     * 使用前提：
     * <p/>
     * 1.已配置好dbHelper。
     * <p/>
     * 2.创建的数据表名将会与entity类名相同。
     * <p/>
     * 3.创建的数据表所有字段将会与entity类对应成员变量名相同。
     */
    public void createTable(Class entityClass) throws SQLException {
        String createTableSql = DaoUtil.createTableSql(entityClass);
        printSql(createTableSql);
        execute(createTableSql);
    }

    /**
     * 创建某个表的外键（最好先创建好所有表）
     */
    public void createReferences(Class entityClass) throws SQLException {
        List<String> referenceSqlList = DaoUtil.createReferenceSqlList(entityClass);
        for (String sql : referenceSqlList) {
            printSql(sql);
            execute(sql);
        }
    }

    /**
     * 根据主键查询表中的一个entity
     * <p/>
     * 使用前提：
     * <p/>
     * 1.已配置好dbHelper。
     * <p/>
     * 2.数据表名与entity类名相同。
     * <p/>
     * 3.数据表所有字段与entity类对应成员变量名相同。
     */
    public <T> T queryById(Class<T> entityClass, String id) throws SQLException {

        String sql = DaoUtil.queryByIdSql(entityClass, id);
        printSql(sql);
        List<T> entityList = executeQuery(entityClass, sql);
        if (entityList.size() == 0) {
            try {
                return entityClass.newInstance();
            } catch (Exception e) {
                throw new SQLException(e.toString());
            }
        } else {
            return entityList.get(0);
        }
    }

    /**
     * 根据指定的一个条件查询多个entity
     * <p/>
     * 使用前提：
     * <p/>
     * 1.已配置好dbHelper。
     * <p/>
     * 2.数据表名与entity类名相同。
     * <p/>
     * 3.数据表所有字段与entity类对应成员变量名相同。
     *
     * @param whereName  查询条件的字段名称
     * @param whereValue 查询条件的字段值，可以为整型，浮点型，字符串等
     * @param fuzzy      是否进行模糊查询
     */
    public <T> List<T> query(Class<T> entityClass, String whereName, String whereValue, boolean fuzzy)
            throws SQLException {
        String sql = DaoUtil.querySql(entityClass, whereName, whereValue, fuzzy);
        printSql(sql);
        return executeQuery(entityClass, sql);
    }

    /**
     * 查询表中所有entity
     * <p/>
     * 使用前提：
     * <p/>
     * 1.已配置好dbHelper。
     * <p/>
     * 2.数据表名与entity类名相同。
     * <p/>
     * 3.数据表所有字段与entity类对应成员变量名相同。
     */
    public <T> List<T> queryAll(Class<T> entityClass) throws SQLException {
        String sql = DaoUtil.queryAllSql(entityClass);
        printSql(sql);
        return executeQuery(entityClass, sql);
    }

    /**
     * 根据主键插入一个entity
     * <p/>
     * 使用前提：
     * <p/>
     * 1.已配置好dbHelper。
     * <p/>
     * 2.数据表名与entity类名相同。
     * <p/>
     * 3.数据表所有字段与entity类对应成员变量名相同。
     * <p/>
     * 4.entity类第一个成员变量作为自增主键。
     *
     * @return 返回插入后自增的主键id
     */
    public synchronized int insert(Object entity) throws SQLException {

        String sql = DaoUtil.insertSql(entity);
        printSql(sql);
        Connection conn = dbHelper.getConnection();

        if (dbHelper.getDbType() == DbType.SQLITE) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            dbHelper.close(conn, ps);
            return 0;
        } else {
            return executeInsert(conn, sql);
        }

    }

    /**
     * 根据主键找到相应的记录并修改所有非主键z字段的值
     *
     * @return 成功修改的记录数目
     */
    public synchronized int updateById(Object entity) throws SQLException {
        String sql = DaoUtil.updateByIdSql(entity);
        printSql(sql);
        return executeUpdate(sql);
    }

    /**
     * 依据id作为where条件修改某一个字段的值
     *
     * @return 成功修改的记录数目
     */
    public synchronized int update(Class entityClass, String id, String setName, String setValue)
            throws SQLException {
        String sql = DaoUtil.updateSql(entityClass, id, setName, setValue);
        printSql(sql);
        return executeUpdate(sql);
    }

    /**
     * 依据id作为where条件删除某一条记录
     *
     * @return 成功删除的记录数目
     */
    public synchronized int deleteById(Class entityClass, String id) throws SQLException {
        String sql = DaoUtil.deleteByIdSql(entityClass, id);
        printSql(sql);
        return executeUpdate(sql);
    }

    /**
     * 删除某一条记录
     *
     * @return 成功删除的记录数目
     */
    public synchronized int delete(Class entityClass, String whereName, String whereValue)
            throws SQLException {
        String sql = DaoUtil.deleteSql(entityClass, whereName, whereValue);
        printSql(sql);
        return executeUpdate(sql);
    }

    public void execute(String sql) throws SQLException {
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        dbHelper.close(conn, ps);
    }

    public <T> List<T> executeQuery(Class<T> entityClass, String sql) throws SQLException {
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<T> entityList = getResult(entityClass, rs);
        dbHelper.close(conn, ps, rs);
        return entityList;
    }

    private <T> List<T> getResult(Class<T> entityClass, ResultSet rs) throws SQLException {

        List<T> entityList = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();

        while (rs.next()) {
            T entity;
            try {
                entity = entityClass.newInstance();
            } catch (Exception e) {
                throw new SQLException(e.toString());
            }
            for (Field field : fields) {
                field.setAccessible(true);
                Object object = rs.getObject(field.getName());
                try {
                    field.set(entity, object);
                } catch (IllegalAccessException e) {
                    throw new SQLException(e.toString());
                }
            }
            entityList.add(entity);
        }
        return entityList;
    }

    /**
     * 执行插入的sql语句
     *
     * @return 返回自增主键的id值
     */
    public int executeInsert(Connection conn, String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            long i = (long) rs.getObject(1);
            rs.close();
            ps.close();
            return (int) i;
        } else {
            rs.close();
            ps.close();
            throw new SQLException(sql + "\nfailed to get id after insert");
        }
    }

    public int executeUpdate(String sql) throws SQLException {
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        int count = ps.executeUpdate();
        dbHelper.close(conn, ps);
        return count;
    }

    private void printSql(String sql) {
        System.out.println(DebugUtil.getDebugMessage(sql + "\n", 2));
    }

}
