package com.wang.test;

import com.wang.java_util.FileUtil;
import com.wang.java_util.GsonUtil;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.TextUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2016/12/5.
 */
public class Tomcat {

    public static final String dir = "E:/";

    public static void mai1n(String[] a) {
        HttpUtil.Result request = HttpUtil.request("http://192.168.199.219:5555/asd/we=234");
        GsonUtil.printFormatJson(request);
    }

    public static void main(String[] a) throws IOException {
        InetAddress localHost = InetAddress.getLocalHost();
        String hostAddress = localHost.getHostAddress();
        String hostName = localHost.getHostName();
        System.out.println("hostAddress: " + hostAddress);
        System.out.println("hostName: " + hostName);
        System.out.println();
        startUp();
    }

    public static void startUp() throws IOException {
        ServerSocket serverSocket = new ServerSocket(5555);

        while (true) {
            try {
                Socket socket = serverSocket.accept();

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String header = reader.readLine();//header = "GET /homework/login?a=123 HTTP/1.1"
                if (!TextUtil.isEmpty(header)) {
                    String[] strings = header.split(" ");
                    if (strings.length == 3 &&
                            (strings[0].contains("GET") || strings[0].contains("POST")) &&
                            strings[2].contains("HTTP/1.1")) {
                        doResponse(socket.getOutputStream(), strings[1]);
                    } else {
                        System.out.println("111");
                        notFound(socket.getOutputStream());
                    }

                } else {
                    System.out.println("222");
                    notFound(socket.getOutputStream());
                }

                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void notFound(OutputStream os) {
        String s = FileUtil.read(dir + "404.html");
        try {
            os.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void doResponse(OutputStream os, String path) {
        File file = new File(dir + path);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);

                int len;
                byte[] buf = new byte[1024];
                while ((len = fis.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                fis.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("333");
            notFound(os);
        }
    }

    private static List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User("姓名" + i, i);
            userList.add(user);
        }
        return userList;
    }

}
