package com.wang.java_program.shopping_system.dao;

import com.wang.java_util.DebugUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class GoodDao extends CustomDao {

    public List<com.wang.java_program.shopping_system.bean.Good> queryGoodListByShopId(int shopId) throws SQLException {
        return query(com.wang.java_program.shopping_system.bean.Good.class, "shopId", shopId + "");
    }

    /**
     * 获取按照销售量从大到小排序的商品列表
     */
    public List<com.wang.java_program.shopping_system.bean.Good> queryGoodListOrderBest() throws SQLException {
        String sql = "select * from Good,Orders where Good.goodId=Orders.goodId " +
                "group by Good.goodId order by sum(count) desc;";
        DebugUtil.println(sql + "\n");
        List<com.wang.java_program.shopping_system.bean.Good> goods = executeQuery(com.wang.java_program.shopping_system.bean.Good.class, sql);
        return goods;
    }

}
