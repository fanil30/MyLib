package com.wang.example;

import com.wang.db.Dao;
import com.wang.db.connection.DbcpDbHelper;
import com.wang.web.Cookie;

import java.sql.SQLException;

/**
 * by 王荣俊 on 2016/6/4.
 */
public class ExampleDao extends Dao {

    static {
        Dao dao = new Dao(new DbcpDbHelper("root", "21436587", "ebook"));
        try {
//            dao.createTable(Book.class);
//            dao.createTable(User.class);
//            dao.createTable(Record.class);
            dao.createTable(Cookie.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("CustomDao:static  ---  create tables finished");
    }

    public ExampleDao() {
        super(new DbcpDbHelper("root", "21436587", "ebook"));
    }

}
