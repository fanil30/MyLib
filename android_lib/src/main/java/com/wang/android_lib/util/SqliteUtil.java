package com.wang.android_lib.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wang.db.SqlEntityUtil;
import com.wang.db.SqlUtil;
import com.wang.db.basis.Constraint;
import com.wang.db.basis.ConstraintAnno;
import com.wang.db.basis.TypeAnno;
import com.wang.db.exception.PrimaryKeyNotFoundException;
import com.wang.java_util.HtmlCreateUtil;
import com.wang.java_util.PairList;
import com.wang.java_util.TextUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/2/3.
 */
public class SqliteUtil {

    /**
     * 先遍历cursor的每一行。对于每一行，先读取cursor中所有列，根据列名找entityClass的field并赋值。
     */
    public static <T> List<T> getResult(Class<T> entityClass, Cursor cursor) throws SQLException {

        List<T> entityList = new ArrayList<>();

        while (cursor.moveToNext()) {
            // 1.创建entity对象
            T entity;
            try {
                entity = entityClass.newInstance();
            } catch (Exception e) {
                throw new SQLException(e.toString());
            }

            // 2.把cursor的当前行的数据保存到entity，如果entityClass没有cursor下的某一列，则忽略
            int columnCount = cursor.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                int type = cursor.getType(i);
                String columnName = cursor.getColumnName(i);
                Object value = null;
                switch (type) {
                    case Cursor.FIELD_TYPE_STRING:
                        value = cursor.getString(i);
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        value = cursor.getInt(i);
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        value = cursor.getFloat(i);
                        break;
                    case Cursor.FIELD_TYPE_BLOB:
                        value = cursor.getBlob(i);
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        value = null;
                        break;
                }
                try {
                    Field field = entityClass.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(entity, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 3.把entity放进entityList
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

    public static List<PairList<String, Object>> iterator(Cursor cursor) {
        List<PairList<String, Object>> table = new ArrayList<>();
        while (cursor.moveToNext()) {
            int columnCount = cursor.getColumnCount();
            PairList<String, Object> row = new PairList<>();
            for (int i = 0; i < columnCount; i++) {
                int type = cursor.getType(i);
                String columnName = cursor.getColumnName(i);
                switch (type) {
                    case Cursor.FIELD_TYPE_STRING:
                        row.add(columnName + "(String)", cursor.getString(i));
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        row.add(columnName + "(Integer)", cursor.getInt(i));
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        row.add(columnName + "(Float)", cursor.getFloat(i));
                        break;
                    case Cursor.FIELD_TYPE_BLOB:
                        row.add(columnName + "(Blob)", cursor.getBlob(i));
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        row.add(columnName + "(Null)", null);
                        break;
                }
            }
            table.add(row);
        }
        return table;
    }

    public static HtmlCreateUtil.Table createHtmlTable(Class entityClass, SQLiteDatabase db) {
        List<String> headList = new ArrayList<>();
        List<List<String>> rowList = new ArrayList<>();

        try {
            String sql = SqlEntityUtil.queryAllSql(entityClass);
            Cursor cursor = db.rawQuery(sql, null);
            List<PairList<String, Object>> list = SqliteUtil.iterator(cursor);
            cursor.close();
            for (int i = 0; i < list.size(); i++) {
                PairList<String, Object> nameValueList = list.get(i);
                List<String> row = new ArrayList<>();
                for (int j = 0; j < nameValueList.size(); j++) {
                    if (i == 0) {
                        headList.add(nameValueList.getLeft(j));
                    }
                    row.add(nameValueList.getRight(j) + "");
                }
                rowList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new HtmlCreateUtil.Table(entityClass.getSimpleName(), headList, rowList);
    }

}
