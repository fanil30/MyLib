package com.wang.db2;

import com.wang.java_util.DebugUtil;
import com.wang.java_util.ReflectUtil;
import com.wang.java_util.TextUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao<T> implements Dao<T> {

    private String username;
    private String password;
    private String dbName;
    private static boolean printSql;
    protected Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public BaseDao(String username, String password, String databaseName, boolean printSql) {
        this.username = username;
        this.password = password;
        this.dbName = databaseName;
        BaseDao.printSql = printSql;
    }

    protected abstract Class<T> getEntityClass();

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + dbName + "?characterEncoding=utf-8",
                    username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String formatInputValue(String value) {
        if (value.endsWith("\\")) {
            value = value + "\\";
        }
        return value.replace("'", "\\'");
    }

    private Field getIdField() {
        return ReflectUtil.findByAnno(getEntityClass(), Id.class);
    }

    private int getIdValue(Object entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                field.setAccessible(true);
                try {
                    return field.getInt(entity);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    private void setIdValue(Object entity, int value) {
        Field idField = getIdField();
        assert idField != null;
        idField.setAccessible(true);
        try {
            idField.setInt(entity, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private String getTableName() {
        return getEntityClass().getSimpleName();
    }

    private static void print(String sql) {
        if (printSql) {
            System.out.println(DebugUtil.getDebugMessage(sql, "BaseDao") + "\n");
        }
    }

    private boolean executeUpdate(String sql) {
        boolean succeed = false;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            if (ps.executeUpdate() > 0) {
                succeed = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            succeed = false;
        } finally {
            close();
        }
        return succeed;
    }

    @Override
    public boolean createTable() {
        String sql = TableUtil.createTableSql(getEntityClass());
        print(sql);
        return executeUpdate(sql);
    }

    @Override
    public boolean dropTable() {
        String sql = TableUtil.dropTableSql(getEntityClass());
        print(sql);
        return executeUpdate(sql);
    }

    @Override
    public boolean createForeignKey() {
        boolean succeed = true;
        List<String> sqlList = TableUtil.foreignKeySql(getEntityClass());
        for (String sql : sqlList) {
            print(sql);
            if (!executeUpdate(sql)) {
                succeed = false;
            }
        }
        return succeed;
    }

    @Override
    public boolean insert(T entity) {
        boolean succeed = false;
        String tableName = getTableName();
        String columnStringList = "";// (userId,username,password)
        String valueList = "";// ('1','wang','123')
        for (Field field : getEntityClass().getDeclaredFields()) {
            if (field.getAnnotation(Ignore.class) != null) {
                continue;
            }
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno != null && idAnno.autoIncrement()) {
                continue;
            }

            field.setAccessible(true);
            try {
                columnStringList += "," + field.getName();
                if (field.getAnnotation(Reference.class) != null) {
                    Object innerEntity = field.get(entity);
                    int idValue = getIdValue(innerEntity);
                    valueList += ",'" + idValue + "'";
                } else {
                    String value = field.get(entity) + "";
                    valueList += ",'" + formatInputValue(value) + "'";
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        columnStringList = columnStringList.substring(1);
        valueList = valueList.substring(1);

        String sql = "insert into " +
                tableName + " (" + columnStringList + ") values (" + valueList + ");";
        print(sql);
        int id = 0;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long l = (long) rs.getObject(1);
                id = (int) l;
            }
            setIdValue(entity, id);
            succeed = true;
        } catch (SQLException e) {
            e.printStackTrace();
            succeed = false;
        } finally {
            close();
        }
        return succeed;
    }

    @Override
    public boolean delete(String whereName, String whereValue) {
        boolean succeed = false;
        String tableName = getTableName();
        String sql;
        if (TextUtil.isEmpty(whereName)) {
            sql = "delete from " + tableName + ";";
        } else {
            sql = "delete from " + tableName + " where " + whereName + "='" + whereValue + "';";
        }
        print(sql);
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            if (ps.executeUpdate() > 0) {
                succeed = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            succeed = false;
        } finally {
            close();
        }
        return succeed;
    }

    @Override
    public boolean deleteById(int id) {
        String idName = getIdField().getName();
        return delete(idName, id + "");
    }

    @Override
    public boolean deleteAll() {
        return delete(null, null);
    }

    @Override
    public boolean update(T entity) {
        String tableName = getTableName();
        String setValueList = "";// (userId='1',username='wang',password='123')
        for (Field field : getEntityClass().getDeclaredFields()) {
            if (field.getAnnotation(Ignore.class) != null) {
                continue;
            }
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno != null) {// don't need: && idAnno.autoIncrement()
                continue;
            }

            field.setAccessible(true);
            try {
                setValueList += "," + field.getName() + "=";
                if (field.getAnnotation(Reference.class) != null) {
                    Object innerEntity = field.get(entity);
                    int idValue = getIdValue(innerEntity);
                    setValueList += "'" + idValue + "'";
                } else {
                    String value = field.get(entity) + "";
                    setValueList += "'" + formatInputValue(value) + "'";
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        setValueList = setValueList.substring(1);

        String sql = "update " + tableName + " set " + setValueList + " where " +
                getIdField().getName() + "='" + getIdValue(entity) + "';";
        print(sql);
        return executeUpdate(sql);
    }

    /**
     * @param currentLevel 当前查询层次，首次递归为0
     * @param maxLevel     最大查询层次。如果为0，则不查询外键对象（列表）。
     *                     如果为1，查询外键对象（列表），但不查询
     *                     外键对象（列表）的外键对象（列表）。如此类推
     */
    private static <T> List<T> query(Class<T> entityClass, Where where, Connection conn,
                                     int currentLevel, int maxLevel) {
        if (currentLevel > maxLevel) {
            return null;
        }

        List<T> entityList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String tableName = entityClass.getSimpleName();

        String sql;
        if (where != null && where.size() > 0) {
            sql = "select * from " + tableName + " where " + where + ";";
        } else {
            sql = "select * from " + tableName + ";";
        }
        print(sql);
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                T entity = entityClass.newInstance();
                for (Field field : entityClass.getDeclaredFields()) {
                    if (field.getAnnotation(Ignore.class) != null) {
                        continue;
                    }
                    field.setAccessible(true);
                    if (field.getAnnotation(Reference.class) == null) {
                        // 正常赋值
                        field.set(entity, rs.getObject(field.getName()));
                    } else {
                        // 查询外键对象并赋值
                        Class innerEntityClass = field.getType();
                        // TODO innerEntityClass是Set集合的处理
                        Field innerIdField = ReflectUtil.findByAnno(innerEntityClass, Id.class);
                        assert innerIdField != null;
                        innerIdField.setAccessible(true);
                        String innerIdName = innerIdField.getName();
                        Object innerIdValue = rs.getObject(field.getName());
                        Where innerWhere = new Where().add(innerIdName, innerIdValue + "");
                        List innerEntityList = query(innerEntityClass, innerWhere, conn,
                                currentLevel + 1, maxLevel);
                        if (innerEntityList != null && innerEntityList.size() > 0) {
                            field.set(entity, innerEntityList.get(0));
                        }
                    }
                }
                entityList.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entityList;
    }

    @Override
    public T queryById(int id) {
        Where where = new Where().add(getIdField().getName(), id + "");
        List<T> list = query(getEntityClass(), where, getConnection(), 0, 255);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<T> queryAll() {
        return query(getEntityClass(), null, getConnection(), 0, 1);
    }

    @Override
    public List<T> query(String columnName, String value) {
        return query(getEntityClass(), new Where().add(columnName, value), getConnection(), 0, 255);
    }

}
