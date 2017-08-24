package com.wang.db3.db.sql_creator;

import com.wang.data_structure.Pair;
import com.wang.db2.Query;
import com.wang.db2.Where;
import com.wang.db3.main.TableField;
import com.wang.db3.main.UpdateSetValue;

import java.util.List;

/**
 * by wangrongjun on 2017/8/21.
 */

public interface ISqlCreator {

    List<String> createTableSql(String tableName, List<TableField> fieldTypeList);

    List<String> dropTableSql(String tableName);

    String querySql(String tableName, Query query);

    String queryCountSql(String tableName, Where where);

    String queryAutoIncrementCurrentValue(String tableName);

    String insertSql(String tableName, List<Pair<String, String>> nameValuePairList);

    String updateSql(String tableName, UpdateSetValue setValue, Where where);

    String deleteSql(String tableName, Where where);

}
