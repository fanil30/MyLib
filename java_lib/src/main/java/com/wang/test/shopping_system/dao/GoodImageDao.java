package com.wang.test.shopping_system.dao;

import com.wang.test.shopping_system.bean.GoodImage;

import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class GoodImageDao extends CustomDao {
    /**
     * 根据商品id查询该商品的所有图片
     */
    public List<GoodImage> query(int goodId) throws SQLException {
        return query(GoodImage.class, "goodId", goodId, false);
    }

}
