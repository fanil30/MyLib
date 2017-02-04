package com.wang.java_program.shopping_system.dao;

import com.wang.java_program.shopping_system.bean.Shop;

import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class ShopDao extends CustomDao {

    /**
     * 根据商店id进行模精确查询
     */
    public Shop query(int shopId) throws SQLException {
        List<Shop> shopList = query(Shop.class, "shopId", shopId + "", false);
        if (shopList != null && shopList.size() > 0) {
            return shopList.get(0);
        }
        return null;
    }

    /**
     * 根据商店名进行模糊查询
     */
    public List<Shop> queryFuzzy(String shopName) throws SQLException {
        return query(Shop.class, "shopName", shopName, true);
    }

}
