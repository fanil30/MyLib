package com.wang.db2.example.dao.impl;

import com.wang.db2.Where;
import com.wang.db2.example.bean.Department;
import com.wang.db2.example.dao.DepartmentDao;
import com.wang.db2.example.dao.OADao;

import java.util.List;

/**
 * by wangrongjun on 2017/6/15.
 */

public class DepartmentDaoImpl extends OADao<Department> implements DepartmentDao {
    @Override
    protected Class<Department> getEntityClass() {
        return Department.class;
    }

    @Override
    public List<Department> queryByName(String name) {
        return query(Where.build("name", name));
    }
}
