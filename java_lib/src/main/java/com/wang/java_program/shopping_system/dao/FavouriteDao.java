package com.wang.java_program.shopping_system.dao;

import com.wang.java_program.shopping_system.bean.Favourite;

/**
 * by wangrongjun on 2016/12/16.
 */
public class FavouriteDao extends CustomDao<Favourite> {
    @Override
    protected Class<Favourite> getEntityClass() {
        return Favourite.class;
    }

    @Override
    protected boolean isPrintSql() {
        return false;
    }
}
