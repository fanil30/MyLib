package com.wang.test;

import com.wang.db.v2.ConstraintAnno;
import com.wang.db.v2.TypeAnno;
import com.wang.test.shopping_system.bean.Orders;

import java.lang.reflect.Field;
import java.sql.SQLException;

public class JavaLibTestClass {

    public static void main(String[] args) throws SQLException {
        Field[] fields = Orders.class.getDeclaredFields();
        for (Field field : fields) {

            System.out.println(field.getName());

            TypeAnno typeAnno = field.getAnnotation(TypeAnno.class);
            if (typeAnno != null) {
                System.out.println("typeAnno: " + typeAnno.type());
            }

            ConstraintAnno constraint = field.getAnnotation(ConstraintAnno.class);
            if (constraint != null) {
                System.out.println(constraint.constraint());
                switch (constraint.constraint()) {
                    case FOREIGN_KEY:
                        System.out.println("foreignTable: " + constraint.foreignTable());
                        System.out.println("foreignField: " + constraint.foreignField());
                        break;
                    case DEFAULT:
                        System.out.println("defaultValue: " + constraint.defaultValue());
                        break;
                }
            }

            System.out.println("----------------------------------------------");

        }
    }

}
