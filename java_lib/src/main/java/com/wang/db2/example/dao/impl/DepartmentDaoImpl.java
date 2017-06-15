package com.wang.db2.example.dao.impl;

import com.wang.db2.example.bean.Department;
import com.wang.db2.example.dao.DepartmentDao;
import com.wang.db2.example.dao.OADao;

/**
 * by wangrongjun on 2017/6/15.
 */

public class DepartmentDaoImpl extends OADao<Department> implements DepartmentDao {
    @Override
    protected Class<Department> getEntityClass() {
        return Department.class;
    }
}
