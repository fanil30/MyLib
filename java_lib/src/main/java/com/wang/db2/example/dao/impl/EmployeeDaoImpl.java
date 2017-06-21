package com.wang.db2.example.dao.impl;

import com.wang.db2.example.bean.Employee;
import com.wang.db2.example.dao.EmployeeDao;
import com.wang.db2.example.dao.OADao;

import java.util.List;

/**
 * by wangrongjun on 2017/6/14.
 */

public class EmployeeDaoImpl extends OADao<Employee> implements EmployeeDao {
    @Override
    protected Class<Employee> getEntityClass() {
        return Employee.class;
    }

    @Override
    public List<Employee> queryByDepartmentId(int departmentId) {
        String sql = "select Employee.* from Employee,Position,Department" +
                " where " +
                "Employee.position=Position.positionId" +
                " and " +
                "Position.department=Department.departmentId" +
                " and " +
                "Department.departmentId='" + departmentId + "';";
        // maxQueryForeignKeyLevel=1：只查到Employee中的Position就够了。不再继续查询Position中的Department
        return executeQuery(sql, 1, null, null);
    }
}
