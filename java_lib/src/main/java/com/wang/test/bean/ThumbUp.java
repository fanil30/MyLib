package com.wang.test.bean;

import com.wang.db.basis.Constraint;
import com.wang.db.basis.ConstraintAnno;
import com.wang.db.basis.FieldType;
import com.wang.db.basis.TypeAnno;

/**
 * by wangrongjun on 2017/3/5.
 * 点赞类（一个点赞对应一个用户和一条签到，一个用户对一条签到只能进行一次点赞）
 */
public class ThumbUp {

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int thumbUpId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY, foreignTable = "User", foreignField = "userId")
    private int userId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY, foreignTable = "Sign", foreignField = "signId")
    private int signId;

    public ThumbUp() {
    }

    public ThumbUp(int userId, int signId) {
        this.userId = userId;
        this.signId = signId;
    }

    public int getThumbUpId() {
        return thumbUpId;
    }

    public void setThumbUpId(int thumbUpId) {
        this.thumbUpId = thumbUpId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSignId() {
        return signId;
    }

    public void setSignId(int signId) {
        this.signId = signId;
    }
}
