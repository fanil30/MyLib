package com.wang.test;

/**
 * by wangrongjun on 2016/11/10.
 */
public interface ISort<T> {

    int compare(T entity1, T entity2);

    ISort<Long> longISort = new ISort<Long>() {
        @Override
        public int compare(Long entity1, Long entity2) {
            if (entity1 > entity2) return 1;
            if (entity1 < entity2) return -1;
            return 0;
        }
    };

    ISort<Long> longISortDesc = new ISort<Long>() {
        @Override
        public int compare(Long entity1, Long entity2) {
            if (entity1 > entity2) return -1;
            if (entity1 < entity2) return 1;
            return 0;
        }
    };

}
