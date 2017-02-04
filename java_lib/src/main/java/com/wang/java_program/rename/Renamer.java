package com.wang.java_program.rename;

import com.wang.java_util.TextUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * by 王荣俊 on 2016/7/26.
 */
public class Renamer {

    public static final int MODE_REPLACE = 0;
    public static final int MODE_APPEND = 1;

    private String dir;
    private String renameText;
    private int mode;
    private boolean ignoreFileSuffix;//是否忽略文件名后缀
    private String prefix;//前缀
    private String suffix;//后缀
    private String incrementBegin;

    private static final String INCREMENT_SIGN = "[auto_increment]";

    private List<RenameFile> renameFiles;

    /**
     * 注意：包含格式如[auto_increment]的字符串，会自动替换为自增的数字如01,02,03...
     */
    public Renamer(String dir, String renameText, int mode, boolean ignoreFileSuffix,
                   String prefix, String suffix, String incrementBegin) {
        this.dir = dir;
        this.renameText = renameText;
        this.mode = mode;
        this.ignoreFileSuffix = ignoreFileSuffix;
        this.prefix = prefix;
        this.suffix = suffix;
        this.incrementBegin = incrementBegin;
    }

    public String prepare() throws Exception {
        renameFiles = new ArrayList<>();
        String[] lines = toLines(renameText);
        File[] files = new File(dir).listFiles();
        StringBuilder builder = new StringBuilder();

        if (files == null) {
            throw new Exception();
        }

        for (int i = 0; i < lines.length && i < files.length; i++) {
            String oldName = files[i].getName();
            String newName = getNewName(oldName, lines[i]);
            newName = newName.replace(INCREMENT_SIGN, getIncrementString(i));//把递增匹配符转化为递增数字
            newName = TextUtil.correctFileName(newName, "");//去除非法符号
            RenameFile renameFile = new RenameFile(files[i], newName);
            renameFiles.add(renameFile);

            builder.append(oldName).append("\t").append(newName).append("\n");
        }

        return builder.toString();
    }

    /**
     * 比如incrementBegin = "004" ，那么index=0时，返回004，index=1时，返回005。以此类推。
     */
    private String getIncrementString(int index) throws Exception {
        int begin = Integer.parseInt(incrementBegin);
        int n = incrementBegin.length();
        String incrementString = (begin + index) + "";
        while (incrementString.length() < n) {
            incrementString = "0" + incrementString;
        }
        return incrementString;
    }

    /**
     * @return 返回文件重命名失败的列表
     */
    public String rename() {
        StringBuilder builder = new StringBuilder();
        for (RenameFile renameFile : renameFiles) {
            File file = renameFile.getFile();
            String newName = renameFile.getNewName();
            String newFilePath = file.getAbsolutePath().replace(file.getName(), newName);
            boolean succeed = file.renameTo(new File(newFilePath));
            if (!succeed) {
                builder.append("文件命名失败：").append(file.getName()).append("\n");
            }
        }
        return builder.toString();
    }

    private String getNewName(String oldName, String rename) {
        String fileSuffix = "." + TextUtil.getTextAfterLastPoint(oldName);
        String newName = "";
        if (mode == MODE_REPLACE) {
            if (ignoreFileSuffix && !TextUtil.isEmpty(fileSuffix)) {
                newName = prefix + rename + suffix + fileSuffix;
            } else {
                newName = prefix + rename + suffix;
            }

        } else if (mode == MODE_APPEND) {
            if (ignoreFileSuffix && !TextUtil.isEmpty(fileSuffix)) {
                newName = prefix + oldName.replace(fileSuffix, "") + suffix + fileSuffix;
            } else {
                newName = prefix + oldName + suffix;
            }
        }

        return newName;
    }

    private String[] toLines(String text) {
        return text.replace("\n\r", "\n").split("[\n]");
    }

    class RenameFile {
        private File file;
        private String newName;

        public RenameFile(File file, String newName) {
            this.file = file;
            this.newName = newName;
        }

        public File getFile() {
            return file;
        }

        public String getNewName() {
            return newName;
        }

    }

}
