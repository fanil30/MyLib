package com.wang.java_program.shopping_system.servlet;

import com.wang.java_util.GsonUtil;
import com.wang.java_util.TextUtil;
import com.wang.web.CustomHttpServlet;

import java.util.HashMap;

/**
 * by wangrongjun on 2016/12/16.
 * http://localhost:8080/shopping_system/register?phone=15521302230&password=123
 */
public class RegisterServlet extends CustomHttpServlet {

    private com.wang.java_program.shopping_system.Response<String> response;

    @Override
    protected String[] onGetParameterStart() {
        return new String[]{"phone", "password"};
    }

//    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {
        String phone = (String) parameterMap.get("phone");
        String password = (String) parameterMap.get("password");

        if (TextUtil.isEmpty(phone, password)) {
            response = new com.wang.java_program.shopping_system.Response<>(com.wang.java_program.shopping_system.StateCode.PARAM_ERROR, "�ֻ��Ż�����Ϊ��");
            return;
        }

        try {
            com.wang.java_program.shopping_system.dao.UserDao userDao = new com.wang.java_program.shopping_system.dao.UserDao();
            if (userDao.query(phone) != null) {
                response = new com.wang.java_program.shopping_system.Response<>(com.wang.java_program.shopping_system.StateCode.ERROR_NORMAL, "���ֻ�����ע��");
            } else {
                com.wang.java_program.shopping_system.bean.User user = new com.wang.java_program.shopping_system.bean.User(phone, password, null, null, 0);
                userDao.insert(user);
                response = new com.wang.java_program.shopping_system.Response<>(com.wang.java_program.shopping_system.StateCode.OK, "ע��ɹ�");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = new com.wang.java_program.shopping_system.Response<>(com.wang.java_program.shopping_system.StateCode.ERROR_UNKNOWN, e.toString());
        }

    }

    @Override
    protected String onWriteResultStart() {
        return GsonUtil.formatJson(response);
    }
}
