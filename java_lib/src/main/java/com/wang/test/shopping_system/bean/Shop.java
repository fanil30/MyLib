package com.wang.test.shopping_system.bean;

import com.wang.db.v2.Constraint;
import com.wang.db.v2.ConstraintAnno;
import com.wang.db.v2.Type;
import com.wang.db.v2.TypeAnno;

/**
 * by wangrongjun on 2016/12/15.
 * 商铺
 */
public class Shop {

    @TypeAnno(type = Type.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int shopId;

    @TypeAnno(type = Type.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY,
            foreignTable = "User", foreignField = "userId")
    private int ownerId;

    @TypeAnno(type = Type.VARCHAR_50)
    @ConstraintAnno(constraint = Constraint.UNIQUE_NOT_NULL)
    private String shopName;

    public Shop() {
    }

    public Shop(int ownerId, String shopName) {
        this.ownerId = ownerId;
        this.shopName = shopName;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
