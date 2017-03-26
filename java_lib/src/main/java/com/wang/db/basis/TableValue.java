package com.wang.db.basis;

/**
 * by 王荣俊 on 2016/5/12.
 */
public class TableValue {

    /**
     * 查询条件的模式（等式查询，模糊查询），作为where时才用到
     */
    enum QueryMode {
        EQUAL,
        LIKE
    }

    public String name;
    public FieldType type;
    public String value;
    public QueryMode queryMode = QueryMode.EQUAL;

    public TableValue(String name, FieldType type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * 作为defaultValue时才用到
     */
    public TableValue(FieldType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TableValue(String name, FieldType type, String value, QueryMode queryMode) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.queryMode = queryMode;
    }

}
