package com.wang.db2;

import java.util.List;

public interface Dao<T> {

    boolean createTable();

    boolean dropTable();

    boolean createForeignKey();

    boolean insert(T entity);

    boolean delete(String whereName, String whereValue);

    boolean deleteById(int id);

    boolean deleteAll();

    boolean update(T entity);

    T queryById(int id);

    List<T> queryAll();

    List<T> query(String columnName, String value);

}
