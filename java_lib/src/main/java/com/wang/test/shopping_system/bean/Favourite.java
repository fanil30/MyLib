package com.wang.test.shopping_system.bean;

/**
 * by wangrongjun on 2016/12/15.
 * 收藏
 */
public class Favourite {

    private int userId;//主键之一
    private int goodId;//主键之一

    public Favourite(int userId, int goodId) {
        this.userId = userId;
        this.goodId = goodId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }
}
