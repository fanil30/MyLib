package com.wang.db3.example;

import com.wang.db.connection.Dbcp;
import com.wang.db2.Query;
import com.wang.db3.example.bean.Dept;
import com.wang.db3.example.bean.Emp;
import com.wang.db3.example.bean.Pos;
import com.wang.db3.example.dao.DeptDao;
import com.wang.db3.example.dao.EmpDao;
import com.wang.db3.example.dao.PosDao;
import com.wang.java_util.DateUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

/**
 * by wangrongjun on 2017/8/23.
 */

public class OADaoTest {

    private DeptDao deptDao = new DeptDao();
    private PosDao posDao = new PosDao();
    private EmpDao empDao = new EmpDao();

    @Test
    public void test() {
        Dept �з��� = new Dept("�з���");
        Dept ���۲� = new Dept("���۲�");
        deptDao.insert(�з���);
        deptDao.insert(���۲�);

        Pos ����Ա = new Pos("����Ա", �з���);
        Pos �����ܼ� = new Pos("�����ܼ�", �з���);
        Pos ����Ա = new Pos("����Ա", ���۲�);
        Pos �����ܼ� = new Pos("�����ܼ�", ���۲�);
        posDao.insert(����Ա);
        posDao.insert(�����ܼ�);
        posDao.insert(����Ա);
        posDao.insert(�����ܼ�);

        Emp ���� = new Emp(1001L, "����111", 1L, ����Ա, 8000d, DateUtil.toDate("2014-06-07 10:00:00"));
        Emp ���� = new Emp(1002L, "����", 0L, ����Ա, 7000d, DateUtil.toDate("2014-06-08 11:00:00"));
        Emp ���� = new Emp(1003L, "����", 1L, �����ܼ�, 18000d, DateUtil.toDate("2014-06-09 12:00:00"));
        Emp ���� = new Emp(1004L, "����", 0L, ����Ա, 4000d, DateUtil.toDate("2014-06-10 13:00:00"));
        Emp ���� = new Emp(1005L, "����", 0L, ����Ա, 5000d, DateUtil.toDate("2014-06-11 14:00:00"));
        Emp ½�� = new Emp(1006L, "½��", 1L, �����ܼ�, 15000d, DateUtil.toDate("2014-09-01 15:00:00"));
        // ʵϰ�����Ÿ���ְ����δ����ְλ
        Emp ���� = new Emp(1007L, "����", 0L, null, 2000d, DateUtil.toDate("2014-09-02 16:00:00"));

        empDao.insert(����);
        empDao.insert(����);
        empDao.insert(����);
        empDao.insert(����);
        empDao.insert(����);
        empDao.insert(½��);
        empDao.insert(����);
        ����.setEname("����");
        empDao.update(����);
        ����.setGender(1L);
        empDao.update(����);
        empDao.deleteById(����.getEmpno());

        empDao.queryByDeptno(1);
        empDao.query(new Query().
                orderBy("-salary", "ename").
                limit(1, 3).
                ignore("dept")
        );
        empDao.queryCount(null);
        empDao.queryById(1006);
    }

    @Before
    public void init() {
        empDao.dropTable();
        posDao.dropTable();
        deptDao.dropTable();

        deptDao.createTable();
        posDao.createTable();
        empDao.createTable();
    }

    @After
    public void close() throws SQLException {
        Dbcp.close();
    }

}
