package com.wang.java_program.shopping_system.servlet;

import com.wang.java_util.GsonUtil;
import com.wang.java_program.shopping_system.Response;
import com.wang.java_program.shopping_system.StateCode;
import com.wang.java_program.shopping_system.bean.Good;
import com.wang.java_program.shopping_system.dao.GoodDao;
import com.wang.web.CustomHttpServlet;

import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class GetGoodListServlet extends CustomHttpServlet {

    @Override
    protected String onWriteResultStart() {
        Response<List<Good>> response;

        try {
            List<Good> goodList = new GoodDao().queryGoodListOrderBest();
            response = new Response<>(StateCode.OK, null, goodList);
        } catch (SQLException e) {
            e.printStackTrace();
            response = new Response<>(StateCode.ERROR_UNKNOWN, e.toString());
        }

        return GsonUtil.formatJson(response);
    }
}
