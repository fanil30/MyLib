package com.wang.java_util;


import javax.swing.JOptionPane;

public class JOptionPaneUtil {

    public static void showError(String error) {
        JOptionPane.showMessageDialog(null, error, "����",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(String info) {
        JOptionPane.showMessageDialog(null, info, "��ʾ",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static String showInput(String title, String msg) {
        return JOptionPane.showInputDialog(null, msg, title,
                JOptionPane.PLAIN_MESSAGE);
        
    }
}
