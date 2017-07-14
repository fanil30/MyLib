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
import com.wang.java_util.DateUtil;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * by wangrongjun on 2017/6/14.
 */

public class OADaoTest {

    private DepartmentDao departmentDao;
    private PositionDao positionDao;
    private EmployeeDao employeeDao;
    private EmployeeLoginDao employeeLoginDao;

    @Test
    public void testOADao() throws SQLException {
        dropAndCreate();
//        createTrigger();

        Department �з��� = new Department("�з���");
        Department ���۲� = new Department("���۲�");
        departmentDao.insert(�з���);
        departmentDao.insert(���۲�);

        Position ����Ա = new Position("����Ա", �з���);
        Position �����ܼ� = new Position("�����ܼ�", �з���);
        Position ����Ա = new Position("����Ա", ���۲�);
        Position �����ܼ� = new Position("�����ܼ�", ���۲�);
        positionDao.insert(����Ա);
        positionDao.insert(�����ܼ�);
        positionDao.insert(����Ա);
        positionDao.insert(�����ܼ�);

        Employee ���� = new Employee(1001, "����111", 1, ����Ա, 8000, DateUtil.toDate("2014-06-07 00:00:00"));
        Employee ���� = new Employee(1002, "����", 0, ����Ա, 7000, DateUtil.toDate("2014-06-08 00:00:00"));
        Employee ���� = new Employee(1003, "����", 1, �����ܼ�, 18000, DateUtil.toDate("2014-06-09 00:00:00"));
        Employee ���� = new Employee(1004, "����", 0, ����Ա, 4000, DateUtil.toDate("2014-06-10 00:00:00"));
        Employee ���� = new Employee(1005, "����", 0, ����Ա, 5000, DateUtil.toDate("2014-06-11 00:00:00"));
        Employee ½�� = new Employee(1006, "½��", 1, �����ܼ�, 15000, DateUtil.toDate("2014-09-01 00:00:00"));// ����ְ����δ����ְλ
        Employee ���� = new Employee(1007, "����", 0, null, 2000, DateUtil.toDate("2014-09-02 00:00:00"));// ʵϰ������ְ����δ����ְλ
        employeeDao.insert(����);
        employeeDao.insert(����);
        employeeDao.insert(����);
        employeeDao.insert(����);
        employeeDao.insert(����);
        employeeDao.insert(½��);
        employeeDao.insert(����);
        ����.setName("����");
        employeeDao.update(����);
        ����.setGender(1);
        employeeDao.update(����);

        employeeLoginDao.insert(new EmployeeLogin(����, "123"));
        employeeLoginDao.insert(new EmployeeLogin(����, "123"));
        employeeLoginDao.insert(new EmployeeLogin(����, "123"));
        employeeLoginDao.insert(new EmployeeLogin(����, "123"));
        employeeLoginDao.insert(new EmployeeLogin(����, "123"));
        employeeLoginDao.insert(new EmployeeLogin(½��, "123"));

        employeeDao.queryByDepartmentId(2);
        employeeDao.query(new Query().
                orderBy("-salary", "name").
                limit(1, 3).
                ignore("department")
        );
        employeeLoginDao.queryAll();
        employeeDao.queryCount(null);

        employeeDao.queryById(1006);

        employeeDao.queryNew();

    }

    /**
     * ����ʹ�ô�������Ŀ����Ϊ��ʹEmployee��departmentName��Department��nameͬ����
     */
    private void createTrigger() throws SQLException {
//        Connection conn = employeeDao.getConnection();
        Connection conn = null;

        String sql = "drop trigger if exists SyncDepartmentName;";
        PreparedStatement ps = conn.prepareStatement(sql);
        System.out.println(ps.executeUpdate());
        ps.close();

        sql = "create trigger SyncDepartmentName " +
                "before insert on Employee " +
                "for each row " +
                "set new.departmentName=(" +
                "select Department.name from Department,Position" +
                " where " +
                "Department.departmentId=Position.department" +
                " and " +
                "Position.positionId=new.position" +
                ");";
//        ps = conn.prepareStatement(sql);
//        System.out.println(ps.executeUpdate());
//        ps.close();

        conn.close();
    }

    private void dropAndCreate() {
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
    }

    @Before
    public void initDao() {
        departmentDao = new DepartmentDaoImpl();
        positionDao = new PositionDaoImpl();
        employeeDao = new EmployeeDaoImpl();
        employeeLoginDao = new EmployeeLoginDaoImpl();
    }

}
