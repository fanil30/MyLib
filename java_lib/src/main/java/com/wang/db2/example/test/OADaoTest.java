package com.wang.db2.example.test;

import com.wang.db2.Query;
import com.wang.db2.example.bean.Department;
import com.wang.db2.example.bean.Employee;
import com.wang.db2.example.bean.EmployeeLogin;
import com.wang.db2.example.bean.Position;
import com.wang.db2.example.dao.DepartmentDao;
import com.wang.db2.example.dao.EmployeeDao;
import com.wang.db2.example.dao.EmployeeLoginDao;
import com.wang.db2.example.dao.PositionDao;
import com.wang.db2.example.dao.impl.DepartmentDaoImpl;
import com.wang.db2.example.dao.impl.EmployeeDaoImpl;
import com.wang.db2.example.dao.impl.EmployeeLoginDaoImpl;
import com.wang.db2.example.dao.impl.PositionDaoImpl;
import com.wang.java_util.GsonUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * by wangrongjun on 2017/6/14.
 */

public class OADaoTest {

    private DepartmentDao departmentDao;
    private PositionDao positionDao;
    private EmployeeDao employeeDao;
    private EmployeeLoginDao employeeLoginDao;

    @Test
    public void testOADao() {

        employeeLoginDao.dropTable();
        employeeDao.dropTable();
        positionDao.dropTable();
        departmentDao.dropTable();

        departmentDao.createTable();
        positionDao.createTable();
        employeeDao.createTable();
        employeeLoginDao.createTable();

        positionDao.createForeignKey();
        employeeDao.createForeignKey();
        employeeLoginDao.createForeignKey();


        Department department1 = new Department("department_1");
        Department department2 = new Department("department_2");
        departmentDao.insert(department1);
        departmentDao.insert(department2);

        Position position1 = new Position("position_1", department1);
        Position position2 = new Position("position_2", department1);
        Position position3 = new Position("position_3", department2);
        Position position4 = new Position("position_4", department2);
        positionDao.insert(position1);
        positionDao.insert(position2);
        positionDao.insert(position3);
        positionDao.insert(position4);
        GsonUtil.printFormatJson(positionDao.queryAll());
        GsonUtil.printFormatJson(positionDao.queryById(2));

        Employee employee1 = new Employee(1001, "employee_111", 1, position1, 1000);
        Employee employee2 = new Employee(1002, "employee_2", 0, position2, 2000);
        Employee employee3 = new Employee(1003, "employee_3", 1, position3, 3000);
        Employee employee4 = new Employee(1004, "employee_4", 1, position4, 3000);
        Employee employee5 = new Employee(1005, "employee_5", 1, position1, 5000);
        employeeDao.insert(employee1);
        employeeDao.insert(employee2);
        employeeDao.insert(employee3);
        employeeDao.insert(employee4);
        employeeDao.insert(employee5);
        employee1.setName("employee_1");
        employeeDao.update(employee1);
        employee2.setGender(1);
        employeeDao.update(employee2);
        employeeDao.deleteById(1005);
        GsonUtil.printFormatJson(employeeDao.queryAll());

        employeeLoginDao.insert(new EmployeeLogin(employee1, "123"));
        employeeLoginDao.insert(new EmployeeLogin(employee2, "456"));
        employeeLoginDao.insert(new EmployeeLogin(employee3, "789"));
        employeeLoginDao.insert(new EmployeeLogin(employee4, "000"));
        GsonUtil.printFormatJson(employeeLoginDao.queryAll());

        GsonUtil.printFormatJson(positionDao.queryByDepartmentId(2));
        GsonUtil.printFormatJson(employeeDao.queryByDepartmentId(2));

        GsonUtil.printFormatJson(employeeDao.query(new Query().
                orderBy("-salary", "name").
                limit(1, 3).
                ignore("department")
        ));

    }

    @Before
    public void init() {
        departmentDao = new DepartmentDaoImpl();
        positionDao = new PositionDaoImpl();
        employeeDao = new EmployeeDaoImpl();
        employeeLoginDao = new EmployeeLoginDaoImpl();
    }

}
