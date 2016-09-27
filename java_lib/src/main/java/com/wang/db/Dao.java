package com.wang.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 拥有已经实现好的万能增删查改等方法的类
 */
public class Dao {

    protected DbHelper dbHelper;

    public Dao(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
        if (dbHelper.type == DbHelper.TYPE_MYSQL) {
            SqlUtil.dbType = SqlUtil.TYPE_MYSQL;
        } else if (dbHelper.type == DbHelper.TYPE_SQLITE) {
            SqlUtil.dbType = SqlUtil.TYPE_SQLITE;
        }
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
     * <p/>
     * 4.entity类第一个成员变量应该为整型，将会作为自增主键。
     */
    public <T> void createTable(Class<T> entityClass) throws SQLException {
        ArrayList<TableField> tableFields = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();
        fields = filterStaticField(fields);

        for (int i = 0; i < fields.length; i++) {
            if (i == 0) {
                tableFields.add(new TableField(fields[0].getName(), TableField.TYPE_INT).primaryKey());
            } else {
                int type = TableField.getTypeFromClassName(fields[i].getType().getSimpleName());
                tableFields.add(new TableField(fields[i].getName(), type));
            }
        }

        String sql = SqlUtil.createTableSql(entityClass.getSimpleName(), tableFields);
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        dbHelper.close(conn, ps);

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
    public <T> ArrayList<T> queryAll(Class<T> entityClass) throws SQLException {
        ArrayList<T> arrayList = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();
        fields = filterStaticField(fields);

        String sql = SqlUtil.querySql(entityClass.getSimpleName(), new ArrayList<TableValue>(), null, false);
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        try {
            while (rs.next()) {
                T entity = entityClass.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(entity, rs.getObject(field.getName()));
                }
                arrayList.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbHelper.close(conn, ps, rs);
        }

        return arrayList;
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
     * <p/>
     * 4.entity类第一个成员变量作为自增主键。
     */
    public <T> T queryById(Class<T> entityClass, int id) throws SQLException {
        Field[] fields = entityClass.getDeclaredFields();
        fields = filterStaticField(fields);

        TableValue where = new TableValue(fields[0].getName(), TableValue.INT, id);
        String sql = SqlUtil.querySql(entityClass.getSimpleName(), where, null, false);
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        T entity = null;
        try {
            if (rs.next()) {
                entity = entityClass.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(entity, rs.getObject(field.getName()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dbHelper.close(conn, ps, rs);
        }

        return entity;
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
    public <T> ArrayList<T> query(Class<T> entityClass, String whereName, Object whereValue, boolean fuzzy)
            throws SQLException {

        String typeName;
        try {
            typeName = entityClass.getDeclaredField(whereName).getType().getSimpleName();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new SQLException(e.toString());
        }
        int whereType = TableValue.getTypeFromClassName(typeName);
        ArrayList<T> arrayList = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();
        fields = filterStaticField(fields);
        TableValue where = new TableValue(whereName, whereType, whereValue);

        String sql;
        if (fuzzy) {
            sql = SqlUtil.queryFuzzySql(entityClass.getSimpleName(), where, null, false);
        } else {
            sql = SqlUtil.querySql(entityClass.getSimpleName(), where, null, false);
        }

        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

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
            arrayList.add(entity);
        }

        dbHelper.close(conn, ps, rs);
        return arrayList;

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
        Field[] fields = entity.getClass().getDeclaredFields();
        fields = filterStaticField(fields);
        ArrayList<TableValue> values = new ArrayList<>();

        for (int i = 0; i < fields.length; i++) {
            if (i == 0) {
                continue;
            }
            String type = fields[i].getType().getSimpleName();
            fields[i].setAccessible(true);
            Object value = "";
            try {
                value = fields[i].get(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            switch (type) {
                case "int":
                    values.add(new TableValue(fields[i].getName(), TableValue.INT, value));
                    break;
                case "double":
                case "float":
                    values.add(new TableValue(fields[i].getName(), TableValue.DOUBLE, value));
                    break;
                case "String":
                    values.add(new TableValue(fields[i].getName(), TableValue.TEXT, value));
                    break;
            }
        }

        String sql = SqlUtil.insertSql(entity.getClass().getSimpleName(), values);
        Connection conn = dbHelper.getConnection();

        if (dbHelper.type == DbHelper.TYPE_SQLITE) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            dbHelper.close(conn, ps);
            return 0;
        } else {
            return executeInsert(conn, sql);
        }

    }

    /**
     * 执行插入的sql语句
     *
     * @return 返回自增主键的id值
     */
    private static int executeInsert(Connection conn, String sql) throws SQLException {
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

    /**
     * 这个方法默认第一个成员变量为id，并且依据这个id作为where条件修改全部字段的值
     *
     * @return 是否成功修改
     */
    public synchronized boolean updateById(Object entity) throws SQLException {
        Field[] fields = entity.getClass().getDeclaredFields();
        fields = filterStaticField(fields);
        TableValue where = null;
        ArrayList<TableValue> setValues = new ArrayList<>();

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);

            try {
                if (i == 0) {
                    fields[i].getName();
                    where = new TableValue(fields[i].getName(), TableValue.INT, fields[i].get(entity));
                } else {
                    int type = TableValue.getTypeFromClassName(fields[i].getType().getSimpleName());
                    setValues.add(new TableValue(fields[i].getName(), type, fields[i].get(entity)));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        String sql = SqlUtil.updateSql(entity.getClass().getSimpleName(), setValues, where);
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        int count = ps.executeUpdate();
        dbHelper.close(conn, ps);
        return count > 0;

    }

    /**
     * 这个方法默认第一个成员变量为id，并且依据这个id作为where条件修改某一个字段的值
     *
     * @return 是否成功修改
     */
    public synchronized boolean update(Class entityClass, int id, String name, Object value) throws SQLException {
        Field[] fields = filterStaticField(entityClass.getDeclaredFields());
        Field idField = fields[0];
        String typeName;
        try {
            typeName = entityClass.getDeclaredField(name).getType().getSimpleName();
        } catch (NoSuchFieldException e) {
            throw new SQLException(e.toString());
        }
        int type = TableValue.getTypeFromClassName(typeName);
        TableValue where = new TableValue(idField.getName(), TableValue.INT, id);
        TableValue setValue = new TableValue(name, type, value);

        String sql = SqlUtil.updateSql(entityClass.getSimpleName(), setValue, where);
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        int count = ps.executeUpdate();
        dbHelper.close(conn, ps);
        return count > 0;

    }

    public synchronized boolean deleteById(Class entityClass, int id) throws SQLException {
        Field[] fields = filterStaticField(entityClass.getDeclaredFields());
        Field idField = fields[0];
        idField.setAccessible(true);
        TableValue where = new TableValue(idField.getName(), TableValue.INT, id);

        String sql = SqlUtil.deleteSql(entityClass.getSimpleName(), where);
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        int count = ps.executeUpdate();
        dbHelper.close(conn, ps);

        return count > 0;

    }

    public synchronized boolean delete(Class entityClass, String whereName, Object whereValue) throws SQLException {
        Field field;
        try {
            field = entityClass.getDeclaredField(whereName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new SQLException(e.toString());
        }
        int type = TableValue.getTypeFromClassName(field.getType().getSimpleName());

        TableValue where = new TableValue(whereName, type, whereValue);
        String sql = SqlUtil.deleteSql(entityClass.getSimpleName(), where);
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        int count = ps.executeUpdate();
        dbHelper.close(conn, ps);
        return count > 0;

    }

    protected <T> ArrayList<T> getResult(Class<T> entityClass, ResultSet rs) {
        ArrayList<T> entityList = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();
        fields = filterStaticField(fields);

        try {
            while (rs.next()) {
                T entity = entityClass.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(entity, rs.getObject(field.getName()));
                }
                entityList.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return entityList;
    }

    private Field[] filterStaticField(Field[] fields) {
        ArrayList<Field> list = new ArrayList<>();
        for (Field field : fields) {
            if ((field.getModifiers() & Modifier.STATIC) == 0) {//若修饰符中没有static
                list.add(field);
            }
        }

        Field[] newFields = new Field[list.size()];
        for (int i = 0; i < list.size(); i++) {
            newFields[i] = list.get(i);
        }

        return newFields;

    }

}
