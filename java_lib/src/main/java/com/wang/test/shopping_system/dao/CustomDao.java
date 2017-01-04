package com.wang.test.shopping_system.dao;

import com.wang.db.Dao;
import com.wang.db.DbHelper;
import com.wang.test.shopping_system.bean.Comment;
import com.wang.test.shopping_system.bean.Favourite;
import com.wang.test.shopping_system.bean.Good;
import com.wang.test.shopping_system.bean.GoodImage;
import com.wang.test.shopping_system.bean.Orders;
import com.wang.test.shopping_system.bean.Shop;
import com.wang.test.shopping_system.bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * by 王荣俊 on 2016/6/4.
 */
public class CustomDao extends Dao {

    static {
        DbHelper helper = new DbHelper("root", "21436587", "shopping_system");
        Dao dao = new Dao(helper);
        try {
            dao.createTable(User.class);
            dao.createTable(Shop.class);
            dao.createTable(Orders.class);
            dao.createTable(Good.class);
            dao.createTable(GoodImage.class);
            dao.createTable(Favourite.class);
            dao.createTable(Comment.class);

            addConstraint(helper);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("CustomDao:static  ---  create tables finished");
    }

    private static void addConstraint(DbHelper helper) throws SQLException {
        Connection conn = helper.getConnection();
        String sql = "ALTER TABLE Shop ADD FOREIGN KEY (ownerId) REFERENCES User (userId);" +
                "ALTER TABLE Orders ADD FOREIGN KEY (userId) REFERENCES User (userId);" +
                "ALTER TABLE Orders ADD FOREIGN KEY (goodId) REFERENCES Good (goodId);" +
                "ALTER TABLE GoodImage ADD FOREIGN KEY (goodId) REFERENCES Good (goodId);" +
                "ALTER TABLE Favourite ADD FOREIGN KEY (goodId) REFERENCES Good (goodId);" +
                "ALTER TABLE Favourite ADD FOREIGN KEY (userId) REFERENCES User (userId);" +
                "ALTER TABLE Comment ADD FOREIGN KEY (userId) REFERENCES User (userId);" +
                "ALTER TABLE Comment ADD FOREIGN KEY (goodId) REFERENCES Good (goodId);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        helper.close(conn, ps);
    }

    public CustomDao() {
        super(new DbHelper("root", "21436587", "shopping_system"));
    }

}
