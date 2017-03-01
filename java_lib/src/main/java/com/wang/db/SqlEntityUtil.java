package com.wang.db;

import com.wang.db.basis.Constraint;
import com.wang.db.basis.ConstraintAnno;
import com.wang.db.basis.DbSpecialCharacterChanger;
import com.wang.db.basis.DbType;
import com.wang.db.basis.TableField;
import com.wang.db.basis.TableValue;
import com.wang.db.basis.Type;
import com.wang.db.basis.TypeAnno;
import com.wang.db.exception.FieldNotFoundException;
import com.wang.db.exception.PrimaryKeyNotFoundException;
import com.wang.java_util.TextUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/1/23.
 */
public class SqlEntityUtil {

    public static DbType dbType = DbType.MYSQL;
    public static DbSpecialCharacterChanger changer = new DbSpecialCharacterChanger();

    public static String createTableSql(Class entityClass) {
        List<TableField> tableFields = new ArrayList<>();

        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {

            TypeAnno typeAnno = field.getAnnotation(TypeAnno.class);
            if (typeAnno == null) {
                continue;
            }
            Type type = typeAnno.type();
            TableField tableField = new TableField(field.getName(), type);

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
                        //先对特殊字符进行转义，至于之后是否需要还原，看情况（比如\\就不用）
                        String value = changer.encode(constraintAnno.defaultValue());
                        tableField.defaultValue(new TableValue(type, value));
                        break;
                    case FOREIGN_KEY:
                        break;
                }
            }

            tableFields.add(tableField);

        }

        return SqlUtil.createTableSql(entityClass.getSimpleName(), tableFields);
    }

    /**
     * 创建某个表的所有外键sql列表（最好先创建好所有表）
     */
    public static List<String> createReferenceSqlList(Class entityClass) {

        List<String> sqlList = new ArrayList<>();

        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            TypeAnno typeAnno = field.getAnnotation(TypeAnno.class);
            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (typeAnno == null || constraintAnno == null) {
                continue;
            }
            if (constraintAnno.constraint() == Constraint.FOREIGN_KEY) {
                String foreignTable = constraintAnno.foreignTable();
                String foreignField = constraintAnno.foreignField();
                String sql = SqlUtil.foreignKeySql(
                        entityClass.getSimpleName(),
                        field.getName(),
                        foreignTable,
                        foreignField,
                        constraintAnno.onDeleteAction(),
                        constraintAnno.onUpdateAction()
                );
                sqlList.add(sql);
            }
        }

        return sqlList;
    }

    public static String queryByIdSql(Class entityClass, String id) throws PrimaryKeyNotFoundException {

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
            throw new PrimaryKeyNotFoundException();
        }

        TableValue where = new TableValue(primaryKeyName, primaryKeyType, changer.encode(id));
        return SqlUtil.querySql(entityClass.getSimpleName(), where, null, false);
    }

    public static String queryAllSql(Class entityClass) throws SQLException {
        return SqlUtil.querySql(entityClass.getSimpleName(), new ArrayList<TableValue>(),
                null, false);
    }

    public static String querySql(Class entityClass, String whereName, String whereValue,
                                  boolean fuzzy) throws FieldNotFoundException {
        Type whereType;
        try {
            whereType = entityClass.getDeclaredField(whereName).getAnnotation(TypeAnno.class).type();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FieldNotFoundException(whereName);
        }

        TableValue where = new TableValue(whereName, whereType, changer.encode(whereValue));

        String sql;
        if (fuzzy) {
            sql = SqlUtil.queryFuzzySql(entityClass.getSimpleName(), where, null, false);
        } else {
            sql = SqlUtil.querySql(entityClass.getSimpleName(), where, null, false);
        }
        return sql;
    }

    public static String insertSql(Object entity) {

        Field[] fields = entity.getClass().getDeclaredFields();
        List<TableValue> values = new ArrayList<>();

        for (Field field : fields) {

            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (constraintAnno != null && constraintAnno.constraint() == Constraint.PRIMARY_KEY) {
                continue;
            }

            //如果constraintAnno不为空，则TypeAnno不可能为空，所有不用判空。
            Type type = field.getAnnotation(TypeAnno.class).type();
            String value = "";
            field.setAccessible(true);
            try {
                value = field.get(entity) + "";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            values.add(new TableValue(field.getName(), type, changer.encode(value)));
        }

        return SqlUtil.insertSql(entity.getClass().getSimpleName(), values);
    }

    public static String updateByIdSql(Object entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        TableValue where = null;
        List<TableValue> setValues = new ArrayList<>();

        for (Field field : fields) {

            TypeAnno typeAnno = field.getAnnotation(TypeAnno.class);
            if (typeAnno == null) {
                continue;
            }
            Type type = typeAnno.type();

            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (constraintAnno != null && constraintAnno.constraint() == Constraint.PRIMARY_KEY) {
                String whereValue = "";
                field.setAccessible(true);
                try {
                    whereValue = field.get(entity) + "";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                where = new TableValue(field.getName(), type, changer.encode(whereValue));

            } else {
                String setValue = "";
                field.setAccessible(true);
                try {
                    setValue = field.get(entity) + "";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                setValues.add(new TableValue(field.getName(), type, changer.encode(setValue)));
            }

        }

        return SqlUtil.updateSql(entity.getClass().getSimpleName(), setValues, where);
    }

    public static String updateSql(Class entityClass, String id, String setName, String setValue)
            throws PrimaryKeyNotFoundException, FieldNotFoundException {

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
            throw new PrimaryKeyNotFoundException();
        }

//        2.获取修改字段的类型        
        Type setType;
        try {
            Field field = entityClass.getDeclaredField(setName);
            setType = field.getAnnotation(TypeAnno.class).type();
        } catch (Exception e) {
            throw new FieldNotFoundException("setName");
        }

//        3.生成并返回sql语句
        TableValue where = new TableValue(idName, idType, changer.encode(id));
        TableValue tableValue = new TableValue(setName, setType, changer.encode(setValue));
        return SqlUtil.updateSql(entityClass.getSimpleName(), tableValue, where);
    }

    public static String deleteByIdSql(Class entityClass, String id) throws PrimaryKeyNotFoundException {

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
            throw new PrimaryKeyNotFoundException();
        }

//        2.生成并返回sql语句
        TableValue where = new TableValue(idName, idType, changer.encode(id));
        return SqlUtil.deleteSql(entityClass.getSimpleName(), where);
    }

    public static String deleteAllSql(Class entityClass) {
        return SqlUtil.deleteSql(entityClass.getSimpleName(), new ArrayList<TableValue>());
    }

    public static String deleteSql(Class entityClass, String whereName, String whereValue)
            throws FieldNotFoundException {

//        1.获取作为查询条件的字段的类型
        Type whereType;
        try {
            Field field = entityClass.getDeclaredField(whereName);
            whereType = field.getAnnotation(TypeAnno.class).type();
        } catch (Exception e) {
            throw new FieldNotFoundException("whereName");
        }

        TableValue where = new TableValue(whereName, whereType, changer.encode(whereValue));
        return SqlUtil.deleteSql(entityClass.getSimpleName(), where);
    }

}
