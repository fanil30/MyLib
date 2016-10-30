package com.wang.math;

/**
 * by wangrongjun on 2016/10/30.
 */
public interface IOperation<T> {

    T jia(T element1, T element2);

    T jian(T element1, T element2);

    T cheng(T element1, T element2);

    T chu(T element1, T element2);

    IOperation<Integer> integerIOperation = new IOperation<Integer>() {
        @Override
        public Integer jia(Integer element1, Integer element2) {
            if (element1 == null) element1 = 0;
            if (element2 == null) element2 = 0;
            return element1 + element2;
        }

        @Override
        public Integer jian(Integer element1, Integer element2) {
            if (element1 == null) element1 = 0;
            if (element2 == null) element2 = 0;
            return element1 - element2;
        }

        @Override
        public Integer cheng(Integer element1, Integer element2) {
            if (element1 == null) element1 = 0;
            if (element2 == null) element2 = 0;
            return element1 * element2;
        }

        @Override
        public Integer chu(Integer element1, Integer element2) {
            if (element1 == null) element1 = 0;
            if (element2 == null) element2 = 0;
            return element1 / element2;
        }
    };

}
