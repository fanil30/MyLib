package com.wang.db2;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/3/26.
 */
public class Where {

    /**
     * 查询条件的模式（相等，不相等，小于，大于，模糊查询）
     */
    public enum QueryMode {
        EQUAL,
        NOT_EQUAL,
        LESS,
        MORE,
        LIKE
    }

    /**
     * 当前查询条件与下一个查询条件的逻辑（and,or）
     * 注意：若查询条件Equation为最后一个，则不存在下一个查询条件，也就没有所谓的逻辑，忽略该变量
     */
    public enum QueryLogic {
        AND,
        OR,
    }

    private List<Equation> equationList;

    public Where() {
        equationList = new ArrayList<>();
    }

    public Equation get(int position) {
        return equationList.get(position);
    }

    public int size() {
        return equationList.size();
    }

    public Where add(String name, String value, QueryMode mode, QueryLogic logic) {
        Equation equation = new Equation(name, value, mode, logic);
        equationList.add(equation);
        return this;
    }

    public Where add(String name, String value, QueryMode mode) {
        Equation equation = new Equation(name, value, mode, QueryLogic.AND);
        equationList.add(equation);
        return this;
    }

    public Where add(String name, String value, QueryLogic logic) {
        Equation equation = new Equation(name, value, QueryMode.EQUAL, logic);
        equationList.add(equation);
        return this;
    }

    public Where add(String name, String value) {
        Equation equation = new Equation(name, value, QueryMode.EQUAL, QueryLogic.AND);
        equationList.add(equation);
        return this;
    }

    public static Where build(String whereName, String whereValue) {
        return new Where().add(whereName, whereValue);
    }

    /**
     * @return username='wang' and password='123' or gender='1' or nickname like '%abc%'
     */
    @Override
    public String toString() {
        if (size() == 0) {
            return "";
        }
        String sql = "";
        for (int i = 0; i < size(); i++) {
            Where.Equation equation = get(i);
            // 1.字段名字
            sql += equation.name;
            // 2.查询模式
            switch (equation.queryMode) {
                case EQUAL:
                    sql += "=";
                    break;
                case NOT_EQUAL:
                    sql += "!=";
                    break;
                case LIKE:
                    sql += " like ";
                    break;
            }
            // 3.赋值
            sql += "'" + equation.value + "'";
            // 4.如果不是最后一个查询条件，添加查询逻辑
            if (i < size() - 1) {
                switch (equation.queryLogic) {
                    case AND:
                        sql += " and ";
                        break;
                    case OR:
                        sql += " or ";
                        break;
                }
            }
        }
        return sql;
    }

    public class Equation {
        public String name;
        public String value;
        public QueryMode queryMode;
        public QueryLogic queryLogic;

        public Equation(String name, String value, QueryMode queryMode,
                        QueryLogic queryLogic) {
            this.name = name;
            this.value = value;
            this.queryMode = queryMode;
            this.queryLogic = queryLogic;
        }
    }

}
