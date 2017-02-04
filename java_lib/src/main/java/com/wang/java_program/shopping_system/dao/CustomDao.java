package com.wang.java_program.shopping_system.dao;

import com.wang.db.v2.DbHelper;
import com.wang.db.v2.Dao;
import com.wang.java_program.shopping_system.bean.GoodImage;

import java.sql.SQLException;

/**
 * by 王荣俊 on 2016/6/4.
 */
public class CustomDao extends Dao {

    static {
        DbHelper helper = new DbHelper("root", "21436587", "shopping_system");
        Dao dao = new Dao(helper);
        try {

            System.out.println("\n\n\n\n---------------------------------------------------------");

            dao.createTable(com.wang.java_program.shopping_system.bean.User.class);
            dao.createTable(com.wang.java_program.shopping_system.bean.Shop.class);
            dao.createTable(com.wang.java_program.shopping_system.bean.Orders.class);
            dao.createTable(com.wang.java_program.shopping_system.bean.Good.class);
            dao.createTable(GoodImage.class);
            dao.createTable(com.wang.java_program.shopping_system.bean.Favourite.class);
            dao.createTable(com.wang.java_program.shopping_system.bean.Comment.class);

            dao.createReferences(com.wang.java_program.shopping_system.bean.User.class);
            dao.createReferences(com.wang.java_program.shopping_system.bean.Shop.class);
            dao.createReferences(com.wang.java_program.shopping_system.bean.Orders.class);
            dao.createReferences(com.wang.java_program.shopping_system.bean.Good.class);
            dao.createReferences(GoodImage.class);
            dao.createReferences(com.wang.java_program.shopping_system.bean.Favourite.class);
            dao.createReferences(com.wang.java_program.shopping_system.bean.Comment.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("CustomDao:static  ---  create tables finished");
    }

    public CustomDao() {
        super(new DbHelper("root", "21436587", "shopping_system"));
    }

}
