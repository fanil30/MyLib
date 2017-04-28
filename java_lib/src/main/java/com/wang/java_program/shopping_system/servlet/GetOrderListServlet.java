package com.wang.java_program.shopping_system.servlet;

import com.wang.java_util.GsonUtil;
import com.wang.java_program.shopping_system.Response;
import com.wang.java_program.shopping_system.StateCode;
import com.wang.java_program.shopping_system.bean.Orders;
import com.wang.java_program.shopping_system.dao.OrderDao;
import com.wang.web.CustomHttpServlet;

import java.util.HashMap;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class GetOrderListServlet extends CustomHttpServlet {

    private Response<List<Orders>> response;

    @Override
    protected String[] onGetParameterStart() {
        return new String[]{"userId"};
    }

//    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {

        int userId;
        try {
            userId = Integer.parseInt((String) parameterMap.get("userId"));
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response<>(StateCode.PARAM_ERROR, "����������");
            return;
        }

        try {
            List<Orders> orderList = new OrderDao().query(userId);
            response = new Response<>(StateCode.OK, null, orderList);
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
