package com.wang.test.bean;

import com.wang.db.basis.Constraint;
import com.wang.db.basis.ConstraintAnno;
import com.wang.db.basis.FieldType;
import com.wang.db.basis.TypeAnno;

/**
 * 用户类
 */
public class User {

    public static final int GENDER_WOMAN = 0;
    public static final int GENDER_MAN = 1;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int userId;

    @TypeAnno(type = FieldType.VARCHAR_50)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String username;

    @TypeAnno(type = FieldType.VARCHAR_50)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String password;

    @TypeAnno(type = FieldType.TINYINT)
    @ConstraintAnno(constraint = Constraint.DEFAULT, defaultValue = "1")
    private int gender;//0或1

    @TypeAnno(type = FieldType.VARCHAR_50)
    private String nickname;

    public User() {
    }

    public User(String username, String password, int gender, String nickname) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.nickname = nickname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
