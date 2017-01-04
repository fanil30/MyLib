package com.wang.test.shopping_system.dao;

import com.wang.test.shopping_system.bean.Orders;

import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class OrderDao extends CustomDao {

    public List<Orders> query(int userId) throws SQLException {
        return query(Orders.class, "userId", userId, false);
    }

}
