package com.wang.test;

import com.wang.java_util.FileUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JFrameExample extends JFrame implements ActionListener {

    private static final String TITLE = "Wang";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    JButton btnStart = new JButton("Start");
    JButton btnStop = new JButton("Stop");
    JLabel labelState = new JLabel("State");
    JTextArea taMessage = new JTextArea("Message");

    private SimpleHttpServer httpServer;

    public JFrameExample() {
        super(TITLE);
        initTopPanel();
        initCenterPanel();
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                httpServer.stopAsync();
            }
        });
        initHttpServer();
    }

    private void initHttpServer() {
        httpServer = new SimpleHttpServer();
        httpServer.setMessageHandler(new SimpleHttpServer.MessageHandler() {
            @Override
            public void onMessage(String message) {
                taMessage.append(message + "\n\n");
            }
        });
        httpServer.setUrlRouter(new SimpleHttpServer.UrlRouter() {
            @Override
            public Response route(String method, String urlPath) {
//                String s = "Hello World!!!\n" + urlPath + "\n" + DateUtil.getCurrentDateAndTime();
//                return new Response(200, s);
                String content = getFileContentFromUrlPath("E:/web/", urlPath);
                if (content != null) {
                    return new Response(200, content);
                } else {
                    String s = FileUtil.read("E:/web/404.html");
                    return new Response(404, s);
                }

            }
        });
    }

    private String getFileContentFromUrlPath(String rootDir, String urlPath) {
        if ("/".equals(urlPath)) {
            urlPath += "index.html";
        }
        String path = rootDir + urlPath.replace("/", File.separator);
        taMessage.append(path + "\n\n");
        if (!new File(path).exists()) {
            return null;
        }
        return FileUtil.read(path);
    }

    public void initTopPanel() {
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);

        JPanel topPanel = new JPanel();
        topPanel.add(btnStart);
        topPanel.add(btnStop);
        topPanel.add(labelState);
        add(topPanel, BorderLayout.NORTH);
    }

    public void initCenterPanel() {
        JScrollPane scrollPane = new JScrollPane(taMessage);
//        scrollPane.setBounds(100, 90, 450, 300);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent paramActionEvent) {
        Object source = paramActionEvent.getSource();
        if (source == btnStart) {
            labelState.setText("Now is Running");
            httpServer.startAsync();
        } else if (source == btnStop) {
            labelState.setText("Now is Stop");
            httpServer.stopAsync();
        }
    }

    public static void main(String[] args) throws Exception {
        new JFrameExample();
    }

}
