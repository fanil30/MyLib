package com.wang.db3.example.dao;

import com.wang.db3.BaseDao;
import com.wang.db3.Config;
import com.wang.db3.Dao;
import com.wang.db3.db.MysqlDatabase;

/**
 * by wangrongjun on 2017/8/23.
 */

public class OADao<T> extends BaseDao<T> implements Dao<T> {
    public OADao() {
//        super(new Config().setUsername("wang").setPassword("123").setDb(new OracleDatabase("orcl")));
        super(new Config().setUsername("root").setPassword("21436587").setDb(new MysqlDatabase("oa")));
    }
}
