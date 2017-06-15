package com.wang.db2.example.dao;

import com.wang.db2.example.bean.Department;

/**
 * by wangrongjun on 2017/6/15.
 */

public class DepartmentDao extends OADao<Department> {
    @Override
    protected Class<Department> getEntityClass() {
        return Department.class;
    }
}
