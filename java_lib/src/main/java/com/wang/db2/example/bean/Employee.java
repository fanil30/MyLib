package com.wang.db2.example.bean;


import com.wang.db2.Column;
import com.wang.db2.Id;
import com.wang.db2.Ignore;
import com.wang.db2.Reference;

import java.util.Date;

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
    private int gender;
    @Reference
    private Position position;
    private double salary;
    private Date startTime;//入职时间

    private String departmentName;

    public Employee() {
    }

    public Employee(int employeeId, String name, int gender, Position position, double salary, 
                    Date startTime) {
        this.employeeId = employeeId;
        this.name = name;
        this.gender = gender;
        this.position = position;
        this.salary = salary;
        this.startTime = startTime;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
