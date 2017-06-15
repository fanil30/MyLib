package com.wang.db2.example.dao;

import com.wang.db2.BaseDao;

/**
 * by wangrongjun on 2017/6/14.
 */

public abstract class OADao<T> extends BaseDao<T> {
    public OADao() {
        super("root", "21436587", "rent_house", true);
    }
}
