package com.wang.test.shopping_system.servlet;

import com.wang.java_util.GsonUtil;
import com.wang.java_util.TextUtil;
import com.wang.test.shopping_system.Response;
import com.wang.test.shopping_system.StateCode;
import com.wang.test.shopping_system.bean.User;
import com.wang.test.shopping_system.dao.UserDao;
import com.wang.web.CustomHttpServlet;

import java.util.HashMap;

/**
 * by wangrongjun on 2016/12/16.
 * 
 */
public class LoginServlet extends CustomHttpServlet {

    private Response<User> response;

    @Override
    protected String[] onGetParameterStart() {
        return new String[]{"phone", "password"};
    }

    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {
        String phone = (String) parameterMap.get("phone");
        String password = (String) parameterMap.get("password");

        if (TextUtil.isEmpty(phone, password)) {
            response = new Response<>(StateCode.PARAM_ERROR, "手机号或密码为空");
            return;
        }

        try {
            UserDao userDao = new UserDao();
            User user = userDao.query(phone, password);
            if (user != null) {
                response = new Response<>(StateCode.OK, user, null);
            } else {
                response = new Response<>(StateCode.ERROR_NORMAL, "手机号不存在或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response<>(StateCode.ERROR_UNKNOWN, e.toString());
        }

    }

    @Override
    protected String onWriteResultStart() {
        return GsonUtil.formatJson(response);
    }
}
