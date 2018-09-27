package com.city.test2.portal;

import com.city.test2.BusinessBO;
import com.city.test2.Constant;
import com.city.test2.TableUtils;
import com.city.test2.jdbc.DBUtils;
import com.city.test2.province.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class City {
    public static void main(String[] args) throws Exception {

        List<BusinessBO> list = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("select * from t_address_province");) {

            while (rs.next()) {
                BusinessBO businessBO = new BusinessBO();
                businessBO.setId(rs.getInt("id"));
                businessBO.setUrl(rs.getString("url"));
                list.add(businessBO);
            }
        }

        List<List<BusinessBO>> result = TableUtils.group(list,10);

        for (List<BusinessBO> t : result) {
            new Task(t).start();
        }

    }


    static class Task extends Thread {
        List<BusinessBO> list;

        public Task(List<BusinessBO> list) {
            this.list = list;
        }

        @Override
        public void run() {
            try {
                for (BusinessBO businessBO : list) {

                    Table cityTable = TableUtils.extraTable(businessBO.getUrl(), Constant.CITY_PATTERN);
                    List<BusinessBO> citys = TableUtils.city(cityTable);
                    StringBuilder sb = new StringBuilder();
                    sb.append("insert into t_address_city (pId,name,code,url) values");
                    for (BusinessBO city : citys) {
                        sb.append("(");
                        sb.append(businessBO.getId()).append(",");
                        sb.append("'").append(city.getName()).append("'").append(",");
                        sb.append("'").append(city.getCode()).append("'").append(",");
                        sb.append("'").append(city.getUrl()).append("'").append(")").append(",");
                    }
                    if (citys.size() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    try (Connection connection = DBUtils.getConnection();
                         Statement statement = connection.createStatement();) {
                        statement.execute(sb.toString());
                    }
                    citys.clear();
                    cityTable = null;
                    sb = null;
                    System.out.println(businessBO.getId() + "end ....");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
