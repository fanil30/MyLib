package com.wang.java_program.shopping_system.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class OrderDao extends CustomDao {

    public List<com.wang.java_program.shopping_system.bean.Orders> query(int userId) throws SQLException {
        return query(com.wang.java_program.shopping_system.bean.Orders.class, "userId", userId + "", false);
    }

}
