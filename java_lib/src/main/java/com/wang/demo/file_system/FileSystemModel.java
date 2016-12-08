package com.wang.demo.file_system;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wang.java_util.FileUtil;
import com.wang.java_util.GsonUtil;
import com.wang.java_util.TextUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2016/12/8.
 */
public class FileSystemModel {

    private FileNode rootNode;
    private FileNode selectedFileNode;
    private String savePath;

    public FileSystemModel(String savePath) {
        this.savePath = savePath;
        rootNode = read();
    }

    public FileNode read() {
        String json = FileUtil.read(savePath);
        if (TextUtil.isEmpty(json)) {
            return null;
        }
        try {
            return new Gson().fromJson(json.replace("\r\n", ""), FileNode.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save() {
        String json = GsonUtil.formatJson(rootNode);
        try {
            FileUtil.write(json.replace("\n", "\r\n"), savePath);
            System.out.println("已保存");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSelectedFileNode(FileNode selectedFileNode) {
        this.selectedFileNode = selectedFileNode;
        System.out.println("你选择了：" + selectedFileNode);
    }

    public FileNode getRootNode() {
        return rootNode;
    }

    private static FileNode getParent(FileNode rootNode, FileNode fileNode) {

        List<FileNode> children = rootNode.getChildren();
        if (children != null && children.size() > 0) {
            boolean contains = children.contains(fileNode);
            if (!contains) {
                for (FileNode child : children) {
                    FileNode parent = getParent(child, fileNode);
                    if (parent != null) {
                        return parent;
                    }
                }
                return null;
            } else {
                return rootNode;
            }

        } else {
            return null;
        }

    }

    /**
     * @return 若创建成功，返回空字符串，否则返回错误信息
     */
    public String createNewFileUnderSelectedDir(String newFileName, boolean createDir) {

        if (selectedFileNode == null) {
            if (rootNode == null) {
                rootNode = new FileNode(new FileContent(newFileName, ""), null, createDir);
                return "";
            } else {
                return "请选择文件夹";
            }
        }
        if (!selectedFileNode.isDirectory()) {
            return "只能在文件夹下创建";
        }

        if (selectedFileNode.getChildren() == null) {
            selectedFileNode.setChildren(new ArrayList<FileNode>());
        }

        List<FileNode> children = selectedFileNode.getChildren();
        for (FileNode child : children) {
            if (child.getFileContent().getFileName().equals(newFileName)) {
                return "此位置已包含同名文件";
            }
        }

        children.add(new FileNode(new FileContent(newFileName, ""), null, createDir));
        System.out.println("你创建了：" + newFileName);
        return "";
    }

    public String updateSelectedFileName(String newFileName) {

        if (selectedFileNode == null) {
            return "请选择文件夹";
        }

        FileNode parent = getParent(rootNode, selectedFileNode);
        assert parent != null;
        List<FileNode> brothers = parent.getChildren();
        for (FileNode brother : brothers) {
            if (!brother.equals(selectedFileNode) &&
                    brother.getFileContent().getFileName().equals(newFileName)) {
                return "此位置已包含同名文件";
            }
        }

        selectedFileNode.getFileContent().setFileName(newFileName);
        System.out.println("你修改了：" + newFileName);
        return "";
    }

    public void deleteSelectedFileNode() {
        if (selectedFileNode == null) {
            return;
        }
        if (rootNode.equals(selectedFileNode)) {
            rootNode = null;
        } else {
            delete(rootNode, selectedFileNode);
        }
        selectedFileNode = null;
    }

    private static void delete(FileNode rootNode, FileNode deleteNode) {

        List<FileNode> children = rootNode.getChildren();
        if (children != null && children.size() > 0) {
            boolean contains = children.remove(deleteNode);
            if (!contains) {
                for (FileNode child : children) {
                    delete(child, deleteNode);
                }
            } else {
                System.out.println("你删除了：" + deleteNode);
            }
        }

    }

}
