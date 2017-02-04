package com.wang.android_lib.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wang.db.exception.PrimaryKeyNotFoundException;
import com.wang.db.v2.Constraint;
import com.wang.db.v2.ConstraintAnno;
import com.wang.db.v2.SqlUtil;
import com.wang.db.v2.TypeAnno;
import com.wang.java_util.TextUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/2/3.
 */
public class SqliteUtil {

    public static <T> List<T> getResult(Class<T> entityClass, Cursor cursor) throws SQLException {

        List<T> entityList = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();

        while (cursor.moveToNext()) {

            T entity;
            try {
                entity = entityClass.newInstance();
            } catch (Exception e) {
                throw new SQLException(e.toString());
            }

            for (Field field : fields) {
                field.setAccessible(true);
                TypeAnno typeAnno = field.getAnnotation(TypeAnno.class);
                if (typeAnno == null) {
                    continue;
                }
                try {
                    switch (typeAnno.type()) {
                        case TEXT:
                        case VARCHAR_10:
                        case VARCHAR_20:
                        case VARCHAR_50:
                        case VARCHAR_100:
                        case VARCHAR_500:
                            String s = cursor.getString(cursor.getColumnIndex(field.getName()));
                            field.set(entity, s);
                            break;
                        case TINYINT:
                        case INT:
                            int i = cursor.getInt(cursor.getColumnIndex(field.getName()));
                            field.set(entity, i);
                            break;
                        case DOUBLE:
                            double d = cursor.getDouble(cursor.getColumnIndex(field.getName()));
                            field.set(entity, d);
                            break;
                        case EXTRA:
                            field.set(entity, null);
                            break;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            entityList.add(entity);
        }

        return entityList;
    }

    /**
     * 返回sqlite数据库最新的自增id
     */
    public static int getLatestAutoIncrementPrimaryKey(Class entityClass, SQLiteDatabase db)
            throws PrimaryKeyNotFoundException {

        String primaryKeyName = null;
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            TypeAnno typeAnno = field.getAnnotation(TypeAnno.class);
            if (typeAnno != null) {
                ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
                if (constraintAnno != null) {
                    if (constraintAnno.constraint() == Constraint.PRIMARY_KEY) {
                        primaryKeyName = field.getName();
                        break;
                    }
                }
            }
        }

        if (TextUtil.isEmpty(primaryKeyName)) {
            throw new PrimaryKeyNotFoundException();
        }

        String sql = SqlUtil.getLatestAutoIncrementNumberSql(
                entityClass.getSimpleName(), primaryKeyName);
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        int id = cursor.getInt(0);
        cursor.close();
        return id;
    }

}
