package com.wang.test.goods.bean;

import com.wang.db2.Column;
import com.wang.db2.Id;

import java.util.Date;

/**
 * by wangrongjun on 2017/8/24.
 */

public class QxUser {

    @Id
    private int userId;
    @Column(nullable = false, unique = true)
    private String userAccount;
    @Column(nullable = false)
    private String password;
    private String phone;
    private String email;
    private Date regDate;
    private double score;

    public QxUser() {
    }

    public QxUser(String userAccount, String password, String phone, String email, Date regDate, double score) {
        this.userAccount = userAccount;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.regDate = regDate;
        this.score = score;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
