package com.wang.java_program.shopping_system.bean;

import com.wang.db.basis.Action;
import com.wang.db.basis.Constraint;
import com.wang.db.basis.ConstraintAnno;
import com.wang.db.basis.FieldType;
import com.wang.db.basis.TypeAnno;

/**
 * by wangrongjun on 2016/12/15.
 */
public class GoodImage {

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int goodImageId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY,
            foreignTable = "Good", foreignField = "goodId",
            onDeleteAction = Action.CASCADE, onUpdateAction = Action.CASCADE)
    private int goodId;

    @TypeAnno(type = FieldType.TEXT)
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
