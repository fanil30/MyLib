package com.wang.db2.example.bean;


import com.wang.db2.Column;
import com.wang.db2.Id;
import com.wang.db2.Ignore;
import com.wang.db2.Reference;

/**
 * by wangrongjun on 2017/6/14.
 */

public class Employee {

    @Ignore
    public static final int GENDER_MAN = 1;
    @Ignore
    public static final int GENDER_WOMAN = 0;

    @Id(autoIncrement = false)
    private int employeeId;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String password;
    private int gender;
    @Reference
    private Position position;
    private double salary;

    public Employee() {
    }

    public Employee(int employeeId, String name, String password, int gender, Position position, double salary) {
        this.employeeId = employeeId;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.position = position;
        this.salary = salary;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
