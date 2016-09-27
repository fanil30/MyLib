package com.wang.java_program.rename;

import com.wang.java_util.TextUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * by 王荣俊 on 2016/7/26.
 */
public class Renamer {

    public static final int MODE_REPLACE = 0;
    public static final int MODE_APPEND = 1;

    private String dir;
    private String renameText;
    private int mode;
    private boolean ignoreFileSuffix;
    private String prefix;
    private String suffix;

    private static final String INCREMENT_SIGN = "[auto_increment]";

    private ArrayList<RenameFile> renameFiles;

    /**
     * 注意：包含格式如[auto_increment]的字符串，会自动替换为自增的数字如01,02,03...
     */
    public Renamer(String dir, String renameText, int mode, boolean ignoreFileSuffix,
                   String prefix, String suffix) {
        this.dir = dir;
        this.renameText = renameText;
        this.mode = mode;
        this.ignoreFileSuffix = ignoreFileSuffix;
        this.prefix = prefix;
        this.suffix = suffix;
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
            String index = (i + 1) + "";
            newName = newName.replace(INCREMENT_SIGN, (i < 9 ? "0" : "") + index);
            RenameFile renameFile = new RenameFile(files[i], newName);
            renameFiles.add(renameFile);

            builder.append(oldName).append("\t").append(newName).append("\n");
        }

        return builder.toString();
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
