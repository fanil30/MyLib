package com.wang.test;

import com.wang.data_structure.tree.BinaryTree;
import com.wang.data_structure.tree.BinaryTreeBuilder;

import java.util.ArrayList;
import java.util.List;

public class JavaLibTestClass {

    public static void main(String a[]) throws Exception {

//        final BinaryTree<String> binaryTree = BinaryTreeBuilder.createByLevelOrderExpression("ABCDEFG######H#");

        List<String> stringList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            stringList.add(i + "");
        }
        final BinaryTree<String> binaryTree = BinaryTreeBuilder.createByLevelOrderList(stringList);
        
/*        binaryTree.levelOrderTraverse(new BinaryTree.Visitor<String>() {
            @Override
            public void visit(BinaryTree<String> node) {
                System.out.println(node + "  floor : " + binaryTree.getFloor(node));
            }
        });
  */
        System.out.println(binaryTree);
    }

}
