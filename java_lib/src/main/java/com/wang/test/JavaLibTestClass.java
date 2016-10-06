package com.wang.test;

import com.wang.java_util.PairList;

public class JavaLibTestClass {


    public static void main(String[] args) throws Exception {

        PairList<String, Integer> list = new PairList<>();
        list.add("abc", 1);
        list.add("def", 2);
        list.add("ghi", 3);

        System.out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.getLeft(i) + " - " + list.getRight(i));
        }

    }


}
