package com.wang.db2.example.dao.impl;

import com.wang.db2.example.bean.EmployeeLogin;
import com.wang.db2.example.dao.EmployeeLoginDao;
import com.wang.db2.example.dao.OADao;

/**
 * by wangrongjun on 2017/6/15.
 */

public class EmployeeLoginDaoImpl extends OADao<EmployeeLogin> implements EmployeeLoginDao {
    @Override
    protected Class<EmployeeLogin> getEntityClass() {
        return EmployeeLogin.class;
    }
}
