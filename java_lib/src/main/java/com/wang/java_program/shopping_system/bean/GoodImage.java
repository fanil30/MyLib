package com.wang.java_program.shopping_system.bean;

import com.wang.db.v2.Action;
import com.wang.db.v2.Constraint;
import com.wang.db.v2.ConstraintAnno;
import com.wang.db.v2.Type;
import com.wang.db.v2.TypeAnno;

/**
 * by wangrongjun on 2016/12/15.
 */
public class GoodImage {

    @TypeAnno(type = Type.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int goodImageId;

    @TypeAnno(type = Type.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY,
            foreignTable = "Good", foreignField = "goodId",
            onDeleteAction = Action.CASCADE, onUpdateAction = Action.CASCADE)
    private int goodId;

    @TypeAnno(type = Type.TEXT)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String imageUrl;

    public GoodImage() {
    }

    public GoodImage(int goodId, String imageUrl) {
        this.goodId = goodId;
        this.imageUrl = imageUrl;
    }

    public int getGoodImageId() {
        return goodImageId;
    }

    public void setGoodImageId(int goodImageId) {
        this.goodImageId = goodImageId;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
