package com.wang.servlet;

import com.google.gson.Gson;
import com.wang.java_util.CharsetUtil;
import com.wang.java_util.DebugUtil;
import com.wang.java_util.FileUtil;
import com.wang.java_util.TextUtil;
import com.wang.json_result.JsonResult;
import com.wang.web.CustomHttpServlet;
import com.wang.web.WebUtil;

import java.util.HashMap;

/**
 * by 王荣俊 on 2016/9/17.
 */
public class UploadText extends CustomHttpServlet {

    private JsonResult result;

    @Override
    protected String[] onGetParameterStart() {
        result = new JsonResult();
        return new String[]{"text"};
    }

//    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {

        String text = (String) parameterMap.get("text");
        if (!TextUtil.isEmpty(text)) {

            text = CharsetUtil.decode(text);
            try {
                FileUtil.write(text, WebUtil.getWebappDir() + "ROOT/file/content.txt");
            } catch (Exception e) {
                result.setState(JsonResult.EXCEPTION);
                result.setResult(DebugUtil.getExceptionStackTrace(e));
                return;
            }

            result.setState(JsonResult.OK);
            result.setResult("text upload succeed!!!");

        } else {
            result.setState(JsonResult.ERROR);
            result.setResult("text is null!!!");
        }

    }

    @Override
    protected String onWriteResultStart() {
        return new Gson().toJson(result);
    }

}
