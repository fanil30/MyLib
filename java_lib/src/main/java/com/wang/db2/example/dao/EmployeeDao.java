package com.wang.db2.example.dao;

import com.wang.db2.Dao;
import com.wang.db2.example.bean.Employee;

import java.util.List;

/**
 * by wangrongjun on 2017/6/17.
 */

public interface EmployeeDao extends Dao<Employee> {

    List<Employee> queryByDepartmentId(int departmentId);

}
