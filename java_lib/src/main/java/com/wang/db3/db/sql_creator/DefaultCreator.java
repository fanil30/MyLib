package com.wang.db3.db.sql_creator;

import com.wang.data_structure.Pair;
import com.wang.db2.Where;
import com.wang.db3.main.UpdateSetValue;

import java.util.List;

/**
 * by wangrongjun on 2017/8/21.
 */

public abstract class DefaultCreator implements ISqlCreator {

    @Override
    public String insertSql(String tableName, List<Pair<String, String>> nameValuePairList) {
        String nameList = "";
        String valueList = "";
        for (Pair<String, String> nameValuePair : nameValuePairList) {
            nameList += nameValuePair.first + ",";
            valueList += "" + nameValuePair.second + ",";
        }
        nameList = nameList.substring(0, nameList.length() - 1);
        valueList = valueList.substring(0, valueList.length() - 1);
        return "insert into " + tableName + "(" + nameList + ") values(" + valueList + ")";
    }

    @Override
    public String updateSql(String tableName, UpdateSetValue setValue, Where where) {
        return "update " + tableName + " set " + setValue + " where " + where;
    }

    @Override
    public String deleteSql(String tableName, Where where) {
        return "delete from " + tableName + " where " + where;
    }

    @Override
    public String queryCountSql(String tableName, Where where) {
        String sql = "select count(*) from " + tableName;
        sql += where == null || where.size() == 0 ? "" : (" where " + where);
        return sql;
    }

}
