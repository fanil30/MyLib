package com.wang.test.shopping_system.bean;

import com.wang.db.v2.Constraint;
import com.wang.db.v2.ConstraintAnno;
import com.wang.db.v2.Type;
import com.wang.db.v2.TypeAnno;

/**
 * by wangrongjun on 2016/12/15.
 * 商品
 */
public class Good {

    @TypeAnno(type = Type.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int goodId;

    @TypeAnno(type = Type.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY,
            foreignTable = "Shop", foreignField = "shopId")
    private int shopId;

    @TypeAnno(type = Type.VARCHAR_50)
    @ConstraintAnno(constraint = Constraint.UNIQUE_NOT_NULL)
    private String goodName;

    @TypeAnno(type = Type.VARCHAR_500)
    private String description;
    private int price;

    public Good() {
    }

    public Good(int shopId, String goodName, String description, int price) {
        this.shopId = shopId;
        this.goodName = goodName;
        this.description = description;
        this.price = price;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
