package com.wang.db2.example.dao;

import com.wang.db2.example.bean.Employee;

/**
 * by wangrongjun on 2017/6/14.
 */

public class EmployeeDao extends OADao<Employee> {

    @Override
    protected Class<Employee> getEntityClass() {
        return Employee.class;
    }

}
