package com.wang.test.shopping_system.dao;

import com.wang.test.shopping_system.bean.Good;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class GoodDao extends CustomDao {

    public List<Good> queryGoodListByShopId(int shopId) throws SQLException {
        return query(Good.class, "shopId", shopId, false);
    }

    /**
     * 获取按照销售量从大到小排序的商品列表
     */
    public List<Good> queryGoodListOrderBest() throws SQLException {
        String sql = "select * from Good,Orders where Good.goodId=Orders.goodId " +
                "group by Good.goodId order by sum(count) desc;";
        System.out.println(sql);
        System.out.println();

        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Good> goodList = getResult(Good.class, rs);
        dbHelper.close(conn, ps);
        return goodList;
    }

}
