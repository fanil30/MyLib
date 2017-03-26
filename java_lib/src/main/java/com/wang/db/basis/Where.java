package com.wang.db.basis;

import java.util.List;

/**
 * by wangrongjun on 2017/3/26.
 */
public class Where {

    /**
     * 查询条件的模式（等式查询，模糊查询）
     */
    enum QueryMode {
        EQUAL,
        LIKE
    }

    /**
     * 当前查询条件与下一个查询条件的逻辑（and,or,none）
     * 注意：若查询条件Equation为最后一个，则不存在下一个查询条件，也就没有所谓的逻辑，忽略该变量
     */
    enum QueryLogic {
        AND,
        OR,
    }

    private List<Equation> equationList;

    public List<Equation> getEquationList() {
        return equationList;
    }

    public void setEquationList(List<Equation> equationList) {
        this.equationList = equationList;
    }

    public Where add(String name, String value, ValueType type, QueryMode mode, QueryLogic logic) {
        Equation equation = new Equation(name, value, type, mode, logic);
        equationList.add(equation);
        return this;
    }

    public Where add(String name, String value, ValueType type) {
        Equation equation = new Equation(name, value, type, QueryMode.EQUAL, QueryLogic.AND);
        equationList.add(equation);
        return this;
    }

    static class Equation {
        public String name;
        public String value;
        public ValueType valueType;
        /**
         * 查询条件的模式（等式查询，模糊查询）
         */
        public QueryMode queryMode;
        /**
         * 当前查询条件与下一个查询条件的逻辑（and,or）
         */
        public QueryLogic queryLogic;

        public Equation(String name, String value, ValueType valueType, QueryMode queryMode,
                        QueryLogic queryLogic) {
            this.name = name;
            this.value = value;
            this.valueType = valueType;
            this.queryMode = queryMode;
            this.queryLogic = queryLogic;
        }
    }

}
