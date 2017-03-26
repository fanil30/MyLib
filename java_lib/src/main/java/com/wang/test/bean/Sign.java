package com.wang.test.bean;

import com.wang.db.basis.Constraint;
import com.wang.db.basis.ConstraintAnno;
import com.wang.db.basis.FieldType;
import com.wang.db.basis.TypeAnno;

/**
 * 签到类（一个用户对应多条签到，每天一条）
 */
public class Sign {

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int signId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY, foreignTable = "User", foreignField = "userId")
    private int userId;//签到者

    @TypeAnno(type = FieldType.VARCHAR_10)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String signDate;//签到日期，格式：yyyy-MM-dd

    @TypeAnno(type = FieldType.VARCHAR_10)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String signTime;//签到时间

    @TypeAnno(type = FieldType.VARCHAR_500)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String signText;//签到语

    public Sign() {
    }

    public Sign(int userId, String signDate, String signTime, String signText) {
        this.userId = userId;
        this.signDate = signDate;
        this.signTime = signTime;
        this.signText = signText;
    }

    public int getSignId() {
        return signId;
    }

    public void setSignId(int signId) {
        this.signId = signId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getSignText() {
        return signText;
    }

    public void setSignText(String signText) {
        this.signText = signText;
    }
}
