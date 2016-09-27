package com.wang.web;

import com.wang.java_util.JsonFormatUtil;
import com.wang.java_util.StreamUtil;
import com.wang.java_util.TextUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class CustomHttpServlet extends HttpServlet {

    /**
     * 服务端是否要读取从客户端传来的数据流，如客户端上传字符串（如json数据）,又或者上传文件
     */
    private boolean canRead = true;
    /**
     * 服务端是否写回字符串到客户端（一般都需要）
     */
    private boolean canWrite = true;
    /**
     * false:客户端为一般请求  true:客户端为文件上传请求
     */
    private boolean upload = false;
    /**
     * 全局的编码格式，默认utf-8
     */
    private String charset = "utf-8";

    private String cookie;

    protected void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    protected void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    protected void setUpload(boolean upload) {
        this.upload = upload;
    }

    protected void setCharset(String charset) {
        this.charset = charset;
    }

    protected void onCreate() {
    }

    protected String[] onGetParameterStart() {
        return null;
    }

    protected void onGetParameterFinish(HashMap parameterMap) {
    }

    protected String onGetUploadPath(String fileName) {
        return fileName;
    }

    protected void onReadInputFinish(String text) {
    }

    protected void onUploadFinish(boolean success, String msg) {
    }

    protected String onWriteResultStart() {
        return null;
    }

    protected void onFinish() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        onCreate();

        request.setCharacterEncoding(charset);
        response.setCharacterEncoding(charset);

        try {

//            0.获取cookie
            cookie = request.getHeader("Set-Cookie");


//            1.根据参数名获取参数值并把参数名-参数值以键值对的形式返回到onGetParamaterFinish的参数
            String parameters[] = onGetParameterStart();
            if (parameters != null) {
                HashMap<String, String> map = new HashMap<>();
                for (String parameter : parameters) {
                    String value = request.getParameter(parameter);
                    if (!TextUtil.isEmpty(value)) {
                        map.put(parameter, value);
                    }
                }
                onGetParameterFinish(map);
            }


//            2.从request读取数据，并返回到onReadInputFinish的参数或onUploadFinish的文件路径
//              a.若模式为文本上传如客户端上传json格式的数据。
            if (canRead && !upload) {
                String text = StreamUtil.readInputStream(request.getInputStream(), charset);
                if (!TextUtil.isEmpty(text)) {
                    System.out.println("client output: \n" +
                            TextUtil.limitLength(text, 300, "......") + "\n");
                    onReadInputFinish(text);
                }

//              b.若模式为客户端进行文件上传
            } else if (canRead) {

                try {
                    String fileName = request.getHeader("fileName");
                    if (!TextUtil.isEmpty(fileName)) {
                        InputStream is = request.getInputStream();
                        String path = onGetUploadPath(fileName);
                        FileOutputStream fos = new FileOutputStream(path);
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                        fos.close();
                        is.close();

                        onUploadFinish(true, path);
                    }
                } catch (IOException e) {
                    onUploadFinish(false, e.toString());
                }

            }


//            3.写出cookie
            if (!TextUtil.isEmpty(cookie)) {
                response.setHeader("Set-Cookie", cookie);
            }


//            4.调用onWriteOutputStart获取数据，并写出到response的输出流
            if (canWrite) {
                String out = onWriteResultStart();
                if (!TextUtil.isEmpty(out)) {
                    System.out.println(JsonFormatUtil.formatJson(out));
                    OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), charset);
                    osw.write(out);
                    osw.flush();
                    osw.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            onFinish();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    protected void setCookie(String cookie) {
        System.out.println("Cookie: " + cookie);
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

}
