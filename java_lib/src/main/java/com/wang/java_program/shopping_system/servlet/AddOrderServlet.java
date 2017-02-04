package com.wang.java_program.shopping_system.servlet;

import com.wang.java_util.DateUtil;
import com.wang.java_util.GsonUtil;
import com.wang.java_program.shopping_system.bean.Orders;
import com.wang.web.CustomHttpServlet;

import java.util.HashMap;

/**
 * by wangrongjun on 2016/12/16.
 * http://localhost:8080/shopping_system/addOrder?userId=1&goodId=1&count=1
 */
public class AddOrderServlet extends CustomHttpServlet {

    private com.wang.java_program.shopping_system.Response<Orders> response;

    @Override
    protected String[] onGetParameterStart() {
        return new String[]{"userId", "goodId", "count"};
    }

    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {

        int userId;
        int goodId;
        int count;

        try {
            userId = Integer.parseInt((String) parameterMap.get("userId"));
            goodId = Integer.parseInt((String) parameterMap.get("goodId"));
            count = Integer.parseInt((String) parameterMap.get("count"));
        } catch (Exception e) {
            e.printStackTrace();
            response = new com.wang.java_program.shopping_system.Response<>(com.wang.java_program.shopping_system.StateCode.PARAM_ERROR, "参数不合理");
            return;
        }

        try {
            com.wang.java_program.shopping_system.bean.Orders order = new com.wang.java_program.shopping_system.bean.Orders(userId, goodId, count, DateUtil.getCurrentDate());
            int orderId = new com.wang.java_program.shopping_system.dao.OrderDao().insert(order);
            order.setOrderId(orderId);
            response = new com.wang.java_program.shopping_system.Response<>(com.wang.java_program.shopping_system.StateCode.OK, order, null);
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
