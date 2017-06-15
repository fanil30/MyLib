package com.wang.db2;

import java.util.List;

public interface Dao<T> {

    boolean createTable();

    boolean dropTable();

    boolean createForeignKey();

    boolean insert(T entity);

    boolean delete(Where where);

    boolean deleteById(int id);

    boolean deleteAll();

    boolean update(T entity);

    T queryById(int id);

    List<T> queryAll();

    List<T> query(Where where);

    List<T> query(Where where, int maxQueryForeignKeyLevel);

    void close();

}
