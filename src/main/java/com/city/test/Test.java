package com.city.test;

import com.sun.javafx.binding.StringFormatter;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

public class Test {
    public static void main1(String[] args) {
        int a = 1;
        System.out.println(StringUtils.isBlank("      "));
    }

    public static void main(String[] args) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/city?useAffectedRows=true","root","1qazxsw@");

        Statement stmt = conn.createStatement();


        String sql = "update test set name = '%s' where id = 1";
        String name = UUID.randomUUID().toString();
        int rows = stmt.executeUpdate(String.format(sql,name));
        System.out.println(rows);

        rows = stmt.executeUpdate(sql);
        System.out.println(rows);
    }
}
