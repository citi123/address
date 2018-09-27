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
import java.util.List;

public class Country {
    public static void main(String[] args) throws Exception {
        List<BusinessBO> citys = new ArrayList<>();
        try (Connection connection = DBUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("select * from t_address_city");) {
            while (rs.next()) {
                BusinessBO businessBO = new BusinessBO();
                businessBO.setName(rs.getString("name"));
                businessBO.setCode(rs.getString("code"));
                businessBO.setUrl(rs.getString("url"));
                businessBO.setId(rs.getInt("id"));
                citys.add(businessBO);
            }
        }

        List<List<BusinessBO>> result = TableUtils.group(citys,30);
        for(List<BusinessBO> t : result){
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
                for (BusinessBO city : list) {

                    int position = city.getUrl().lastIndexOf("/");
                    Table countryTable;
                    if (city.getName().contains("东莞")
                            || city.getName().contains("中山")
                            || city.getName().contains("儋州")) {
                        countryTable = TableUtils.extraTable(city.getUrl(), Constant.TOWN_PATTERN);
                    } else {
                        countryTable = TableUtils.extraTable(city.getUrl(), Constant.COUNTRY_PATTERN);
                    }


                    List<BusinessBO> countrys = TableUtils.country(countryTable, city.getUrl().substring(0, position));

                    StringBuilder sb = new StringBuilder();
                    sb.append("insert into t_address_country (pId,name,code,url) values");
                    for (BusinessBO country : countrys) {
                        sb.append("(");
                        sb.append(city.getId()).append(",");
                        sb.append("'").append(country.getName()).append("'").append(",");
                        sb.append("'").append(country.getCode()).append("'").append(",");
                        sb.append("'").append(country.getUrl()).append("'").append(")").append(",");
                    }
                    if (countrys.size() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }

                    try (Connection connection = DBUtils.getConnection();
                         Statement statement = connection.createStatement();) {
                        statement.execute(sb.toString());
                    }

                    sb = null;
                    countryTable = null;
                    countrys.clear();
                    System.out.println(city.getId() + " end ....");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
