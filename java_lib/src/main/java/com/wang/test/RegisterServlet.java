package com.wang.test;

import com.google.gson.Gson;
import com.wang.java_util.TextUtil;
import com.wang.json_result.JsonResult;
import com.wang.web.CustomHttpServlet;

import java.util.HashMap;

/**
 * by 王荣俊 on 2016/9/22.
 */
public class RegisterServlet extends CustomHttpServlet {

    private JsonResult result;

    @Override
    protected String[] onGetParameterStart() {
        result = new JsonResult();
        return new String[]{"username", "password"};
    }

    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {

        String username = (String) parameterMap.get("username");
        String password = (String) parameterMap.get("password");

        if (TextUtil.isEmpty(username, password)) {
            result.setState(JsonResult.EXCEPTION);
            result.setResult("参数为空");
            return;
        }

        try {
            boolean exists = false;
            //TODO 查询用户是否已存在
            if (exists) {
                //TODO 用户表插入新记录
                result.setState(JsonResult.OK);
                result.setResult("注册成功");
            } else {
                result.setState(JsonResult.ERROR);
                result.setResult("注册失败，用户已存在");
            }

        } catch (Exception e) {
            result.setState(JsonResult.EXCEPTION);
            result.setResult("服务器异常：" + e.toString());
        }

    }

    @Override
    protected String onWriteResultStart() {
        return new Gson().toJson(result);
    }

}
