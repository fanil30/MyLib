package com.wang.db2.example.dao;

import com.wang.db2.example.bean.EmployeeLogin;

/**
 * by wangrongjun on 2017/6/15.
 */

public class EmployeeLoginDao extends OADao<EmployeeLogin> {
    @Override
    protected Class<EmployeeLogin> getEntityClass() {
        return EmployeeLogin.class;
    }
}
