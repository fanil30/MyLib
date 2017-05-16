package com.wang.data_structure;

import java.util.List;

/**
 * by wangrongjun on 2017/5/9.
 */
public class CursorList<T> {

    private List<T> list;
    private int nextPosition;

    public CursorList(List<T> list) {
        this.list = list;
        nextPosition = 0;
    }

    public void resetCursor() {
        nextPosition = 0;
    }

    public boolean hasElement() {
        return nextPosition < list.size();
    }

    public T next() {
        return list.get(nextPosition++);
    }

}
