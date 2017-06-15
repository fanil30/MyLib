package com.wang.db2.example.dao;


import com.wang.db2.example.bean.Position;

/**
 * by wangrongjun on 2017/6/14.
 */

public class PositionDao extends OADao<Position> {
    @Override
    protected Class<Position> getEntityClass() {
        return Position.class;
    }
}
