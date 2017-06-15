package com.wang.db2.example.test;

import com.wang.db2.example.bean.Department;
import com.wang.db2.example.bean.Employee;
import com.wang.db2.example.bean.Position;
import com.wang.db2.example.dao.DepartmentDao;
import com.wang.db2.example.dao.EmployeeDao;
import com.wang.db2.example.dao.PositionDao;
import com.wang.java_util.GsonUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * by wangrongjun on 2017/6/14.
 */

public class OADaoTest {

    private DepartmentDao departmentDao;
    private PositionDao positionDao;
    private EmployeeDao employeeDao;

    @Test
    public void testOADao() {

        employeeDao.dropTable();
        positionDao.dropTable();
        departmentDao.dropTable();
        departmentDao.createTable();
        positionDao.createTable();
        employeeDao.createTable();
        positionDao.createForeignKey();
        employeeDao.createForeignKey();

        Department develop = new Department("develop");
        Department sell = new Department("sell");
        departmentDao.insert(develop);
        departmentDao.insert(sell);

        Position developer = new Position("developer", develop);
        Position seller = new Position("seller", sell);
        positionDao.insert(developer);
        positionDao.insert(seller);
        GsonUtil.printFormatJson(positionDao.queryAll());
        GsonUtil.printFormatJson(positionDao.queryById(2));

        Employee wang = new Employee(10001, "wan_g", "123", 1, developer, 6000);
        Employee rong = new Employee(10002, "rong", "123456", 0, developer, 5000);
        Employee jun = new Employee(10003, "jun", "123", 1, seller, 4000);
        Employee yan = new Employee(10004, "yan", "123", 0, seller, 3000);
        employeeDao.insert(wang);
        employeeDao.insert(rong);
        employeeDao.insert(jun);
        employeeDao.insert(yan);
        wang.setName("wang");
        employeeDao.update(wang);
        rong.setPassword("123");
        rong.setGender(1);
        employeeDao.update(rong);
        employeeDao.deleteById(10004);
        GsonUtil.printFormatJson(employeeDao.queryAll());

    }

    @Before
    public void init() {
        departmentDao = new DepartmentDao();
        positionDao = new PositionDao();
        employeeDao = new EmployeeDao();
    }

    @After
    public void close() {
        departmentDao.close();
        positionDao.close();
        employeeDao.close();
    }

}
