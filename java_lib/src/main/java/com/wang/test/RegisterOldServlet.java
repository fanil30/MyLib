package com.wang.test;

import com.google.gson.Gson;
import com.wang.java_util.TextUtil;
import com.wang.json_result.JsonResult;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RegisterOldServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonResult result = new JsonResult();
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (TextUtil.isEmpty(username, password)) {
            result.setState(JsonResult.EXCEPTION);
            result.setResult("参数为空");
        } else {
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

        OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream());
        osw.write(new Gson().toJson(result));
        osw.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
