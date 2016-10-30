package com.wang.maths;

/**
 * by wangrongjun on 2016/10/30.
 */
public interface IOperation<T> {

    T jia(T element1, T element2);

    T jian(T element1, T element2);

    T cheng(T element1, T element2);

    T chu(T element1, T element2);

    int compare(T element1, T element2);

    /**
     * @return 零元（如0）
     */
    T getZero();

    /**
     * @return 单位元（如1）
     */
    T getUnit();

    /**
     * @return 相反数（如输入1，返回-1）
     */
    T opposite(T element);

    T clone(T element);

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

        @Override
        public int compare(Integer element1, Integer element2) {
            if (element1 > element2) return 1;
            if (element1 == element2) return 0;
            return -1;
        }

        @Override
        public Integer getZero() {
            return 0;
        }

        @Override
        public Integer getUnit() {
            return 1;
        }

        @Override
        public Integer opposite(Integer element) {
            if (element == null) element = 0;
            return -element;
        }

        @Override
        public Integer clone(Integer element) {
            int e = element;
            return e;
        }
    };

    IOperation<Double> doubleIOperation = new IOperation<Double>() {
        @Override
        public Double jia(Double element1, Double element2) {
            if (element1 == null) element1 = 0d;
            if (element2 == null) element2 = 0d;
            return element1 + element2;
        }

        @Override
        public Double jian(Double element1, Double element2) {
            if (element1 == null) element1 = 0d;
            if (element2 == null) element2 = 0d;
            return element1 - element2;
        }

        @Override
        public Double cheng(Double element1, Double element2) {
            if (element1 == null) element1 = 0d;
            if (element2 == null) element2 = 0d;
            return element1 * element2;
        }

        @Override
        public Double chu(Double element1, Double element2) {
            if (element1 == null) element1 = 0d;
            if (element2 == null) element2 = 0d;
            return element1 / element2;
        }

        @Override
        public int compare(Double element1, Double element2) {
            if (element1 == null) element1 = 0d;
            if (element2 == null) element2 = 0d;
            if (element1 > element2) return 1;
            if (element1.equals(element2)) return 0;
            return -1;
        }

        @Override
        public Double getZero() {
            return 0d;
        }

        @Override
        public Double getUnit() {
            return 1d;
        }

        @Override
        public Double opposite(Double element) {
            if (element == null) element = 0d;
            return -element;
        }

        @Override
        public Double clone(Double element) {
            double e = element;
            return e;
        }

    };

}
