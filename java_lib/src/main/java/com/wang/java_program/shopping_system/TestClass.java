package com.wang.java_program.shopping_system;

import com.wang.java_util.DebugUtil;
import com.wang.java_util.GsonUtil;
import com.wang.java_util.HttpUtil;

/**
 * by wangrongjun on 2017/1/4.
 */
public class TestClass {

    private static void register() {
        String url = "http://localhost:8080/shopping_system/register?phone=15521302230&password=123";
        HttpUtil.Result r = new HttpUtil.HttpRequest().request(url);
        DebugUtil.println(url + "\n" + GsonUtil.printFormatJson(r));
    }

    public static void main(String[] a) {
        register();
    }

}
