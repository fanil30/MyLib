package com.wang.db.v2;

import com.wang.db.DbHelper;
import com.wang.java_util.DebugUtil;
import com.wang.java_util.TextUtil;

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
    public void createTable(Class<Type> entityClass) throws SQLException {

        List<TableField> tableFields = new ArrayList<>();
        String foreignKeySql = "";

        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {

            TypeAnno typeAnno = field.getAnnotation(TypeAnno.class);
            if (typeAnno == null) {
                continue;
            }
            Type type = typeAnno.type();
            TableField tableField = new TableField(entityClass.getSimpleName(), type);

            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (constraintAnno != null) {
                Constraint constraint = constraintAnno.constraint();
                switch (constraint) {
                    case NULL:
                        break;
                    case PRIMARY_KEY:
                        tableField.primaryKey();
                        break;
                    case UNIQUE:
                        tableField.unique();
                        break;
                    case NOT_NULL:
                        tableField.notNull();
                        break;
                    case UNIQUE_NOT_NULL:
                        tableField.notNull().unique();
                        break;
                    case UNSIGNED:
                        tableField.unsigned();
                    case DEFAULT:
                        tableField.defaultValue(new TableValue(type, constraintAnno.defaultValue()));
                        break;
                    case FOREIGN_KEY:
                        String foreignTable = constraintAnno.foreignTable();
                        String foreignField = constraintAnno.foreignField();
                        foreignKeySql += SqlUtil.foreignKeySql(
                                field.getName(),
                                entityClass.getSimpleName(),
                                foreignTable,
                                foreignField
                        );
                        foreignKeySql += ";\n";
                        break;
                }
            }

            tableFields.add(tableField);

        }

        String sql = SqlUtil.createTableSql(entityClass.getSimpleName(), tableFields);
        sql += foreignKeySql;
        printSql(sql);
        execute(sql);
    }

    public void execute(String sql) throws SQLException {
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
    public List<T> queryAll(Class<T> entityClass) throws SQLException {
        String sql = SqlUtil.querySql(entityClass.getSimpleName(), new ArrayList<TableValue>(),
                null, false);
        printSql(sql);
        return executeQuery(entityClass, sql);
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
    public T queryById(Class<T> entityClass, String id) throws SQLException {

        String primaryKeyName = null;
        Type primaryKeyType = null;
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (constraintAnno != null && constraintAnno.constraint() == Constraint.PRIMARY_KEY) {
                primaryKeyName = field.getName();
                primaryKeyType = field.getAnnotation(TypeAnno.class).type();
            }
        }
        if (TextUtil.isEmpty(primaryKeyName)) {
            throw new SQLException("primary key not exists");
        }

        TableValue where = new TableValue(primaryKeyName, primaryKeyType, id);
        String sql = SqlUtil.querySql(entityClass.getSimpleName(), where, null, false);
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
    public List<T> query(Class<T> entityClass, String whereName, String whereValue, boolean fuzzy)
            throws SQLException {

        Type whereType;
        try {
            whereType = entityClass.getDeclaredField(whereName).getAnnotation(TypeAnno.class).type();
        } catch (Exception e) {
            throw new SQLException(whereName + " : " + e.toString());
        }

        TableValue where = new TableValue(whereName, whereType, whereValue);

        String sql;
        if (fuzzy) {
            sql = SqlUtil.queryFuzzySql(entityClass.getSimpleName(), where, null, false);
        } else {
            sql = SqlUtil.querySql(entityClass.getSimpleName(), where, null, false);
        }
        printSql(sql);
        return executeQuery(entityClass, sql);
    }

    public List<T> executeQuery(Class<T> entityClass, String sql) throws SQLException {

        List<T> entityList = new ArrayList<>();

        Field[] fields = entityClass.getDeclaredFields();

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
            entityList.add(entity);
        }
        dbHelper.close(conn, ps, rs);

        return entityList;
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
        List<TableValue> values = new ArrayList<>();

        for (Field field : fields) {

            Constraint constraint = field.getAnnotation(ConstraintAnno.class).constraint();
            if (constraint == Constraint.PRIMARY_KEY) {
                continue;
            }

            Type type = field.getAnnotation(TypeAnno.class).type();
            String value = "";
            field.setAccessible(true);
            try {
                value = (String) field.get(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            values.add(new TableValue(field.getName(), type, value));
        }

        String sql = SqlUtil.insertSql(entity.getClass().getSimpleName(), values);
        printSql(sql);
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

    /**
     * 根据主键找到相应的记录并修改所有非主键z字段的值
     *
     * @return 成功修改的记录数目
     */
    public synchronized int updateById(Object entity) throws SQLException {
        Field[] fields = entity.getClass().getDeclaredFields();
        TableValue where = null;
        List<TableValue> setValues = new ArrayList<>();

        for (Field field : fields) {

            Constraint constraint = field.getAnnotation(ConstraintAnno.class).constraint();
            if (constraint == Constraint.PRIMARY_KEY) {
                Type type = field.getAnnotation(TypeAnno.class).type();
                String whereValue = "";
                field.setAccessible(true);
                try {
                    whereValue = (String) field.get(entity);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                where = new TableValue(field.getName(), type, whereValue);

            } else {
                Type type = field.getAnnotation(TypeAnno.class).type();
                String setValue = "";
                field.setAccessible(true);
                try {
                    setValue = (String) field.get(entity);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                setValues.add(new TableValue(field.getName(), type, setValue));
            }

        }

        String sql = SqlUtil.updateSql(entity.getClass().getSimpleName(), setValues, where);
        printSql(sql);
        return executeUpdate(sql);
    }

    public int executeUpdate(String sql) throws SQLException {
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        int count = ps.executeUpdate();
        dbHelper.close(conn, ps);
        return count;
    }

    /**
     * 依据id作为where条件修改某一个字段的值
     *
     * @return 成功修改的记录数目
     */
    public synchronized int update(Class entityClass, String id, String setName, String setValue)
            throws SQLException {

//        1.获取主键的名称和类型
        Field[] fields = entityClass.getDeclaredFields();
        String idName = null;
        Type idType = null;
        for (Field field : fields) {
            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (constraintAnno != null && constraintAnno.constraint() == Constraint.PRIMARY_KEY) {
                idName = field.getName();
                idType = field.getAnnotation(TypeAnno.class).type();
                break;
            }
        }
        if (TextUtil.isEmpty(idName) || idType == null) {
            throw new SQLException("primary key not exists");
        }

//        2.获取修改字段的类型        
        Type setType;
        try {
            Field field = entityClass.getDeclaredField(setName);
            setType = field.getAnnotation(TypeAnno.class).type();
        } catch (Exception e) {
            throw new SQLException("NoSuchField : " + setName + "\n" + e.toString());
        }

//        3.生成并执行sql语句
        TableValue where = new TableValue(idName, idType, id);
        TableValue tableValue = new TableValue(setName, setType, setValue);
        String sql = SqlUtil.updateSql(entityClass.getSimpleName(), tableValue, where);
        printSql(sql);
        return executeUpdate(sql);
    }

    /**
     * 依据id作为where条件删除某一条记录
     *
     * @return 成功删除的记录数目
     */
    public synchronized int deleteById(Class entityClass, String id) throws SQLException {

//        1.获取主键的名称和类型
        Field[] fields = entityClass.getDeclaredFields();
        String idName = null;
        Type idType = null;
        for (Field field : fields) {
            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (constraintAnno != null && constraintAnno.constraint() == Constraint.PRIMARY_KEY) {
                idName = field.getName();
                idType = field.getAnnotation(TypeAnno.class).type();
                break;
            }
        }
        if (TextUtil.isEmpty(idName) || idType == null) {
            throw new SQLException("primary key not exists");
        }

//        2.生成并执行sql语句
        TableValue where = new TableValue(idName, idType, id);
        String sql = SqlUtil.deleteSql(entityClass.getSimpleName(), where);
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

//        1.获取作为查询条件的字段的类型
        Type whereType;
        try {
            Field field = entityClass.getDeclaredField(whereName);
            whereType = field.getAnnotation(TypeAnno.class).type();
        } catch (Exception e) {
            throw new SQLException("NoSuchField : " + whereName + "\n" + e.toString());
        }

        TableValue where = new TableValue(whereName, whereType, whereValue);
        String sql = SqlUtil.deleteSql(entityClass.getSimpleName(), where);
        printSql(sql);
        return executeUpdate(sql);
    }

    private void printSql(String sql) {
        DebugUtil.println(sql);
    }

}
