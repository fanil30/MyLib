package com.wang.demo.file_system;

import com.wang.java_util.JOptionPaneUtil;
import com.wang.java_util.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * by wangrongjun on 2016/12/8.
 */
public class FileSystemDemo implements TreeSelectionListener {

    private JTree tree;
    private FileSystemModel model;

    public static void main(String a[]) {
        new FileSystemDemo();
    }

    public FileSystemDemo() {

        //��ʼ������
        model = new FileSystemModel("treeJson.txt");

        //��ʼ���˵�
        JMenuBar menuBar = initMenuBar();

        //��ʼ����
        tree = new JTree(toTreeNode(model.getRootNode()));
        tree.addTreeSelectionListener(this);

        //��ʼ��������
        JFrame frame = new JFrame("FileSystemDemo");
        frame.setJMenuBar(menuBar);
        frame.add(tree);
        frame.setSize(600, 800);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                model.save();
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private JMenuBar initMenuBar() {
        JMenuBar menuBar = new JMenuBar();


        final JMenu menuCreate = new JMenu("�½�");
        JButton btnCreateFile = new JButton("�ļ�");
        btnCreateFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewFileUnderSelectedDir(false);
            }
        });
        JButton btnCreateDir = new JButton("�ļ���");
        btnCreateDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewFileUnderSelectedDir(true);
            }
        });
        menuCreate.add(btnCreateFile);
        menuCreate.add(btnCreateDir);
        menuBar.add(menuCreate);


        JButton btnRename = new JButton("������");
        btnRename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedFileName();
            }
        });
        menuBar.add(btnRename);


        JButton btnDelete = new JButton("ɾ��");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedFileNode();
            }
        });
        menuBar.add(btnDelete);


        JButton btnExpand = new JButton("չ��");
        btnExpand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 10; i++) {
                    expand();
                }
            }
        });
        menuBar.add(btnExpand);


        JButton btnCollapse = new JButton("����");
        btnCollapse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collapse();
            }
        });
        menuBar.add(btnCollapse);


        return menuBar;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                .getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        Object object = node.getUserObject();
        com.wang.demo.file_system.FileNode selectedFileNode = (com.wang.demo.file_system.FileNode) object;
        model.setSelectedFileNode(selectedFileNode);
    }

    private DefaultMutableTreeNode toTreeNode(com.wang.demo.file_system.FileNode fileNode) {
        if (fileNode == null) {
            return null;
        }
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(fileNode);
        List<com.wang.demo.file_system.FileNode> children = fileNode.getChildren();
        if (children != null && children.size() > 0) {
            for (com.wang.demo.file_system.FileNode childNode : children) {
                treeNode.add(toTreeNode(childNode));
            }
        }
        return treeNode;
    }

    private void updateTree() {
        com.wang.demo.file_system.FileNode rootNode = model.getRootNode();
        tree.setModel(new DefaultTreeModel(toTreeNode(rootNode)));
        for (int i = 0; i < 10; i++) {
            expand();
        }
    }

    private void expand() {
        int rowCount = tree.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            tree.expandRow(i);
        }
    }

    private void collapse() {
        int rowCount = tree.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            tree.collapseRow(i);
        }
    }

    private void createNewFileUnderSelectedDir(boolean isDir) {
        String newFileName = JOptionPaneUtil.showInput("��������", "");
        if (TextUtil.isEmpty(newFileName)) {
            return;
        }
        String message = model.createNewFileUnderSelectedDir(newFileName, isDir);
        if (TextUtil.isEmpty(message)) {
            updateTree();
        } else {
            JOptionPaneUtil.showError(message);
        }
    }

    private void updateSelectedFileName() {
        String newFileName = JOptionPaneUtil.showInput("�޸�����", "");
        if (TextUtil.isEmpty(newFileName)) {
            return;
        }
        String message = model.updateSelectedFileName(newFileName);
        if (TextUtil.isEmpty(message)) {
            updateTree();
        } else {
            JOptionPaneUtil.showError(message);
        }
    }

    public void deleteSelectedFileNode() {
        model.deleteSelectedFileNode();
        updateTree();
    }

}
