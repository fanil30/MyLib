package com.wang.math.sort;

import com.wang.java_util.GsonUtil;
import com.wang.java_util.MathUtil;
import com.wang.math.Heap;

import java.util.ArrayList;
import java.util.List;

/**
 * by 王荣俊 on 2016/10/12.
 */
public class SortHelper {

    private static <T> void swap(List<T> entityList, int index1, int index2) {
        T entity1 = entityList.get(index1);
        T entity2 = entityList.get(index2);
        entityList.set(index1, entity2);
        entityList.set(index2, entity1);
    }

    public static <T> List<T> copy(List<T> entityList, int beginIndex, int endIndex) {
        List<T> newEntityList = new ArrayList<>();
        for (int i = beginIndex; i < endIndex; i++) {
            newEntityList.add(entityList.get(i));
        }
        return newEntityList;
    }

    public static <T> List<T> copy(List<T> entityList) {
        List<T> newEntityList = new ArrayList<>();
        for (T entity : entityList) {
            newEntityList.add(entity);
        }
        return newEntityList;
    }

    /**
     * 冒泡排序
     */
    public static <T> int sortBubble(List<T> entityList, ISort<T> iSort) {
        int basicOperationCount = 0;
        for (int i = 0; i < entityList.size() - 1; i++) {
            for (int j = 0; j < entityList.size() - i - 1; j++) {
                basicOperationCount++;
                if (iSort.compare(entityList.get(j), entityList.get(j + 1)) == 1) {
                    swap(entityList, j, j + 1);
                }
            }
        }
        return basicOperationCount;
    }

    /**
     * 选择排序
     */
    public static <T> int sortSelect(List<T> entityList, ISort<T> iSort) {
        int basicOperationCount = 0;
        for (int i = 0; i < entityList.size() - 1; i++) {
            int k = i;
            for (int j = i + 1; j < entityList.size(); j++) {
                basicOperationCount++;
                if (iSort.compare(entityList.get(k), entityList.get(j)) == 1) {
                    k = j;
                }
            }
            if (k != i) {
                swap(entityList, i, k);
            }
        }
        return basicOperationCount;
    }

    /**
     * 插入排序
     */
    public static <T> int sortInsertion(List<T> entityList, ISort<T> iSort) {
        int basicOperationCount = 0;
        if (entityList == null || entityList.size() == 0) {
            return 0;
        }
        for (int position = 1; position < entityList.size(); position++) {
            int i = position - 1;
            basicOperationCount++;
            while (i >= 0 && iSort.compare(entityList.get(i), entityList.get(i + 1)) == 1) {
                swap(entityList, i, i + 1);
                i--;
            }
        }
        return basicOperationCount;
    }

    public static int basicOperationCount = 0;

    /**
     * 合并排序
     */
    public static <T> void sortMerge(List<T> entityList, ISort<T> iSort) {

        if (entityList == null || entityList.size() <= 1) {
            return;
        }

        int size = entityList.size();
        List<T> listA = copy(entityList, 0, size / 2);
        List<T> listB = copy(entityList, size / 2, size);
        sortMerge(listA, iSort);
        sortMerge(listB, iSort);
        merge(listA, listB, entityList, iSort);

    }

    /**
     * 对两个有序的listA和listB合并成有序的list，注意，list原来的内容会被覆盖。
     */
    private static <T> void merge(List<T> listA, List<T> listB, List<T> list, ISort<T> iSort) {
        list.clear();
        int positionA = 0;
        int positionB = 0;
        while (positionA < listA.size() && positionB < listB.size()) {
            basicOperationCount++;
            if (iSort.compare(listA.get(positionA), listB.get(positionB)) == 1) {
                list.add(listB.get(positionB));
                positionB++;
            } else {
                list.add(listA.get(positionA));
                positionA++;
            }
        }
        if (positionB == listB.size()) {//B数组已处理完，则A数组可能未处理完。
            for (int i = positionA; i < listA.size(); i++) {
                list.add(listA.get(i));
            }
        } else {
            for (int i = positionB; i < listB.size(); i++) {
                list.add(listB.get(i));
            }
        }
    }

    /**
     * 堆排序
     */
    public static <T> int sortHeap(List<T> entityList, ISort<T> iSort) {
        Heap<T> heap = new Heap<>(entityList, iSort);
        while (heap.popTop() != null) ;
        return Heap.basicOperationCount;
    }

    private static <T> int hoarePartition(List<T> entityList, ISort<T> iSort, int left, int right) {

        int middle = left;
        while (true) {
            while (iSort.compare(entityList.get(left), entityList.get(middle)) != 1) {//left<=middle
                left++;
            }
            while (iSort.compare(entityList.get(right), entityList.get(middle)) != 1) {//right<=middle
                right--;
            }
            if (left < right) {
                swap(entityList, left, right);
            } else {
                swap(entityList, middle, right);
                return right;
            }
        }
    }

    private static <T> int sortQuick(List<T> entityList, ISort<T> iSort, int left, int right) {
        if (left < right) {
            int middle = hoarePartition(entityList, iSort, left, right);
            sortQuick(entityList, iSort, left, middle - 1);
            sortQuick(entityList, iSort, middle, right);
        }
        return 0;
    }

    /**
     * 快速排序
     */
    public static <T> int sortQuick(List<T> entityList, ISort<T> iSort) {
        sortQuick(entityList, iSort, 0, entityList.size() - 1);
        return 0;
    }

    public static void sortTest(String[] args) throws Exception {

        List<User> users = getExample(10);
        List<User> users1 = copy(users);
        List<User> users2 = copy(users);
        List<User> users3 = copy(users);
        List<User> users4 = copy(users);
        List<User> users5 = copy(users);
        List<User> users6 = copy(users);

        ISort<User> iSort = new ISort<User>() {
            @Override
            public int compare(User entity1, User entity2) {
                if (entity1.getAge() < entity2.getAge()) {
                    return -1;
                } else if (entity1.getAge() == entity2.getAge()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };

        long currentTimeMillis;
        double time;

        System.out.println("开始进行冒泡");
        currentTimeMillis = System.currentTimeMillis();
        int sortBubble = sortBubble(users1, iSort);
        time = (System.currentTimeMillis() - currentTimeMillis) / 1000.0;
        System.out.println("用时：" + time + " 秒");
        System.out.println("基本操作次数：" + sortBubble + "\n");

        System.out.println("开始进行选择");
        currentTimeMillis = System.currentTimeMillis();
        int sortSelect = sortSelect(users2, iSort);
        time = (System.currentTimeMillis() - currentTimeMillis) / 1000.0;
        System.out.println("用时：" + time + " 秒");
        System.out.println("基本操作次数：" + sortSelect + "\n");

        System.out.println("开始进行合并");
        currentTimeMillis = System.currentTimeMillis();
        sortMerge(users3, iSort);
        time = (System.currentTimeMillis() - currentTimeMillis) / 1000.0;
        System.out.println("用时：" + time + " 秒");
        System.out.println("基本操作次数：" + basicOperationCount + "\n");

        System.out.println("开始进行插入");
        currentTimeMillis = System.currentTimeMillis();
        int sortInsertion = sortInsertion(users4, iSort);
        time = (System.currentTimeMillis() - currentTimeMillis) / 1000.0;
        System.out.println("用时：" + time + " 秒");
        System.out.println("基本操作次数：" + sortInsertion + "\n");

        System.out.println("开始进行堆排序");
        currentTimeMillis = System.currentTimeMillis();
        int sortHeap = sortHeap(users5, iSort);
        time = (System.currentTimeMillis() - currentTimeMillis) / 1000.0;
        System.out.println("用时：" + time + " 秒");
        System.out.println("基本操作次数：" + sortHeap + "\n");

        System.out.println("开始进行快速");
        currentTimeMillis = System.currentTimeMillis();
        int sortQuick = sortQuick(users6, iSort);
        time = (System.currentTimeMillis() - currentTimeMillis) / 1000.0;
        System.out.println("用时：" + time + " 秒");
        System.out.println("基本操作次数：" + sortQuick + "\n");

        sortQuick(users, iSort);
        GsonUtil.printFormatJson(users);

    }

    static class User {
        private String username;
        private int age;

        public User(String username, int age) {
            this.username = username;
            this.age = age;
        }

        public String getUsername() {
            return username;
        }

        public int getAge() {
            return age;
        }
    }

    public static List<User> getExample(int number) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int age = MathUtil.random(1, 10 * number);
            users.add(new User("user" + i, age));
        }
        return users;
    }

}
