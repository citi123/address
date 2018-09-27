package com.city.test2.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtils {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/test?useSSL=false&useAffectedRows=false", "root", "123456");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
