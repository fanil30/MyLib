package com.wang.test;

/**
 * by 王荣俊 on 2016/10/14.
 */
public class Result {

    private int state;
    private String result;

    public Result(int state, String result) {
        this.state = state;
        this.result = result;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
