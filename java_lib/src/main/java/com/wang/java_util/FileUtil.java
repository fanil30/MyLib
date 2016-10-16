package com.wang.java_util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class FileUtil {

    /**
     * 递归删除非空文件夹
     */
    public static void deleteDir(File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }
        if (!dir.isDirectory()) {
            dir.delete();
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                file.delete();
            }
        }

        dir.delete();
    }

    public static boolean delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static void copy(String fromPath, String toPath) throws IOException {
        copy(new File(fromPath), new File(toPath));
    }

    public static void copy(File fromFile, File toFile) throws IOException {
        FileInputStream fis = new FileInputStream(fromFile);
        FileOutputStream fos = new FileOutputStream(toFile);

        FileChannel fromChannel = fis.getChannel();
        FileChannel toChannel = fos.getChannel();

        fromChannel.transferTo(0, fromChannel.size(), toChannel);

        fromChannel.close();
        toChannel.close();
        fis.close();
        fos.close();

    }

    /**
     * 递归复制指定目录及其以下的所有文件到另一个目录之下。如E:/a/这个文件夹复制到F:/test/下
     * 则 copyDir(new File("E:/a/"), new File("E:/test/"));
     */
    public static void copyDir(File fromDir, File toDir) throws Exception {
        if (fromDir == null || !fromDir.exists()) {
            return;
        }
        File nextToDir = new File(toDir, fromDir.getName());
        if (!fromDir.isDirectory()) {//若不是目录
            copy(fromDir, nextToDir);
            return;
        }

        nextToDir.mkdirs();

        File[] files = fromDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    copyDir(file, nextToDir);
                } else {
                    copy(file, new File(nextToDir, file.getName()));
                }
            }
        }

    }


    public static Object readObject(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            ois.close();
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeObject(Object object, String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
    }

    /**
     * @return 文件不存在，返回null。文件存在但无内容，返回""。
     */
    public static String read(File textFile) {
        if (!textFile.exists()) {
            return null;
        }
        try {
            return StreamUtil.readInputStream(new FileInputStream(textFile), null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 文件不存在，返回null。文件存在但无内容，返回""。
     */
    public static String read(String filePath) {
        return read(filePath, "utf-8");
    }

    public static String read(String filePath, String charset) {
        File textFile = new File(filePath);
        if (!textFile.exists()) {
            return null;
        }
        try {
            return StreamUtil.readInputStream(new FileInputStream(textFile), charset);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> readLines(String filePath) throws Exception {
        ArrayList<String> lines = new ArrayList<>();
        InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath));
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        isr.close();
        return lines;
    }

    public static void write(String content, String path) throws Exception {
        write(content, path, "utf-8");
    }

    public static void write(String content, String path, String charset) throws Exception {

        if (TextUtil.isEmpty(path)) {
            throw new Exception("filePath is null");
        }

        if (content == null) {
            content = "";
        }

        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }

        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(path), charset);
        osw.write(content);
        osw.flush();
        osw.close();
//        FileWriter fw = new FileWriter(path);
//        fw.write(content);
//        fw.flush();
//        fw.close();
    }

    public static boolean mkdirs(String dir) {
        return new File(dir).mkdirs();
    }

    public static boolean mkdirs(String parentDir, String dirName) {
        return new File(parentDir, dirName).mkdirs();
    }

    public interface OnDownloadListener {

        // speed的单位：Byte/ms(即KB/s)
        void onDownloadProgressUpdate(int status, String text,
                                      double currentBytes, double totalBytes, double speed);

    }

    // 网络连接，读取的限时
    public static final int CONNECT_TIMEOUT = 10 * 1000;
    public static final int READ_TIMEOUT = 10 * 1000;

    // 帮助计算时间间隔
    private static long preTime;
    // 帮助计算下载速度
    private static int preBytes;

    public static final int OK = 1;
    public static final int DOWNLOADING = 2;
    public static final int DOWNLOAD_FINISH = 3;
    public static final int DOWNLOAD_FAILURE = -1;
    public static final int DOWNLOAD_AGAIN = -2;

    /**
     * @param duration listener回调通知进度的时间间隔，0则只有下载完成时回调
     */
    public static void download(String strUrl, String cookie, String filePath,
                                OnDownloadListener listener, int duration) throws Exception {

        InputStream is = null;
        FileOutputStream fos = null;
        File file = null;

        try {
            URL url = new URL(strUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            if (cookie != null && cookie.length() > 0) {
                conn.setRequestProperty("Cookie", cookie);
            }

            file = new File(filePath);

            if (file.exists()) {
                return;
            }

            int fileLength = conn.getContentLength();

            // 有时可以得到conn，但长度为空，这样会下载到一个大小为0的文件
            if (fileLength == 0) {
                if (file != null) {
                    file.delete();
                }
                throw new Exception(DebugUtil.println("长度为空,下载失败"));
            }

            is = conn.getInputStream();

            file.createNewFile();

            fos = new FileOutputStream(file);

            preBytes = 0;
            preTime = System.currentTimeMillis();
            int bytes = 0;
            int len = 0;
            byte[] buf = new byte[1024];

            if (listener != null) {
                listener.onDownloadProgressUpdate(DOWNLOADING, file.getName(),
                        0, fileLength, 0);
            }

            while ((len = is.read(buf)) >= 0) {
                bytes += len;
                fos.write(buf, 0, len);

                if (listener != null) {
                    long delay = System.currentTimeMillis() - preTime;
                    if (delay > duration && duration != 0) {

                        // speed的单位：Byte/ms(即KB/s)
                        double speed = 1.0 * (bytes - preBytes) / delay;

                        listener.onDownloadProgressUpdate(FileUtil.DOWNLOADING,
                                file.getName(), bytes, fileLength, speed);
                        preTime = System.currentTimeMillis();
                        preBytes = bytes;
                    }
                }
            }

            fos.flush();
            is.close();
            fos.close();

            // 有时没有异常，但下载会提前结束，文件有大小，却损坏
            if (bytes / fileLength < 0.95) {
                if (file != null & is != null && fos != null) {
                    is.close();
                    fos.close();
                    file.delete();
                }
                throw new Exception(DebugUtil.println(" 下载失败，下载提前结束  "
                        + strUrl));
            }

        } catch (Exception e) {
            if (file != null & is != null && fos != null) {
                is.close();
                fos.close();
                file.delete();
            }
            throw new Exception(" 下载失败，网络连接不稳定， " + strUrl
                    + DebugUtil.println(e.toString()));

        }

        if (listener != null) {
            listener.onDownloadProgressUpdate(DOWNLOAD_FINISH, file.getName() + " 下载成功", 0,
                    0, 0);
        }

    }
}
