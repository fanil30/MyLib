package com.wang.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * by wangrongjun on 2017/5/16.
 */
public class SimpleHttpServer {

    private boolean isRunnable;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private MessageHandler messageHandler;
    private UrlRouter urlRouter;

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setUrlRouter(UrlRouter urlRouter) {
        this.urlRouter = urlRouter;
    }

    public interface MessageHandler {
        void onMessage(String message);
    }

    public interface UrlRouter {
        /**
         * @param method  请求方法，一般为GET或POST
         * @param urlPath 客户端请求url的相对路径
         * @return 响应体的状态码和内容
         */
        Response route(String method, String urlPath);

        class Response {
            public int code;
            public String responseBody;

            public Response(int code, String responseBody) {
                this.code = code;
                this.responseBody = responseBody;
            }
        }
    }

    /**
     * 启动服务器（异步）
     */
    public void startAsync() {
        isRunnable = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();
    }

    private void start() {
        try {
            serverSocket = new ServerSocket(5000);
            threadPool = Executors.newCachedThreadPool();
            while (isRunnable) {
                final Socket socket = serverSocket.accept();
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        execute(socket);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            messageHandler.onMessage(e.toString());
        }
    }

    private void execute(Socket socket) {
        try {
            messageHandler.onMessage("----------------  start get request  ----------------");
            InputStream is = socket.getInputStream();
            String line = readLine(is);// GET /bbs/index.html HTTP/1.1\r\n
            String[] strings = line.split(" ");
            String method = strings[0];
            String urlPath = strings[1];

            UrlRouter.Response response = urlRouter.route(method, urlPath);
            String responseString = responseString(response);

            OutputStream os = socket.getOutputStream();
            os.write(responseString.getBytes());
            os.flush();
            os.close();
            messageHandler.onMessage(responseString);

        } catch (IOException e) {
            e.printStackTrace();
            messageHandler.onMessage(e.toString());
        }
    }

    /**
     * 关闭服务器（异步）
     */
    public void stopAsync() {
        if (isRunnable) {
            isRunnable = false;
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    messageHandler.onMessage(e.toString());
                }
            }
            serverSocket = null;
            if (threadPool != null) {
                threadPool.shutdown();
                threadPool = null;
            }
        }
    }

    private static String readLine(InputStream is) throws IOException {
        StringBuilder builder = new StringBuilder();
        int c1 = 0;
        int c2 = 0;
        while (c2 != -1 && !(c1 == '\r' && c2 == '\n')) {
            c1 = c2;
            c2 = is.read();
            builder.append((char) c2);
        }
        return builder.toString();
        /*
        InputStreamReader isr=new InputStreamReader(is);
        int a;
        while ((a = isr.read()) != -1) {
            System.out.print(((char) a + "").replace("\r", "\\r"));
            builder.append((char) a);
        }

        isr.close();
        return builder.toString();*/
    }

    private static String responseString(UrlRouter.Response response) {
        String s = "";
        s += "HTTP/1.1 " + response.code + " OK" + "\r\n";
        s += "\r\n";
        s += response.responseBody;
        return s;
    }

}
