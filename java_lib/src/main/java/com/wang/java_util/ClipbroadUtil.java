package com.wang.java_util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * Created by Administrator on 2016/3/1.
 */
public class ClipbroadUtil {

    public static void setSystemClipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = new StringSelection(text);
        clipboard.setContents(contents, null);
    }

    public static String getSystemClipboard() throws Exception {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = clipboard.getContents(null);

        if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            String data = (String) t.getTransferData(DataFlavor.stringFlavor);
            return data;
        } else {
            throw new Exception("not string");
        }

    }
}
