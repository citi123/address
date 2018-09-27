package com.city.test2.portal;

import com.city.test2.BusinessBO;
import com.city.test2.Constant;
import com.city.test2.TableUtils;
import com.city.test2.jdbc.DBUtils;
import com.city.test2.province.Table;
import com.city.test2.province.VillageTable;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Town {
    public static void main(String[] args) throws Exception {
        List<BusinessBO> countrys1 = new ArrayList<>();
        List<BusinessBO> countrys2 = new ArrayList<>();
        try (Connection connection = DBUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("select * from t_address_country where pId in (select id from t_address_city where name not in ('东莞市','中山市','儋州市'))");) {
            while (rs.next()) {
                BusinessBO businessBO = new BusinessBO();
                businessBO.setId(rs.getInt("id"));
                businessBO.setName(rs.getString("name"));
                businessBO.setCode(rs.getString("code"));
                businessBO.setUrl(rs.getString("url"));
                countrys1.add(businessBO);
            }
        }

        try (Connection connection = DBUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("select * from t_address_country where pId in (select id from t_address_city where name in ('东莞市','中山市','儋州市'))");) {
            while (rs.next()) {
                BusinessBO businessBO = new BusinessBO();
                businessBO.setId(rs.getInt("id"));
                businessBO.setName(rs.getString("name"));
                businessBO.setCode(rs.getString("code"));
                businessBO.setUrl(rs.getString("url"));
                countrys2.add(businessBO);
            }
        }


        List<List<BusinessBO>> countrys = TableUtils.group(countrys1, 200);
        for (List<BusinessBO> t : countrys) {
            new Task(t, Constant.TOWN_PATTERN).start();
        }

        List<List<BusinessBO>> towns = TableUtils.group(countrys2, 20);
        for (List<BusinessBO> t : towns) {
            new Task(t, Constant.VILLAGE_PATTERN).start();
        }
    }

    static class Task extends Thread {

        List<BusinessBO> list;
        Pattern pattern;

        public Task(List<BusinessBO> list, Pattern pattern) {
            this.list = list;
            this.pattern = pattern;
        }

        @Override
        public void run() {
            if (list == null || list.size() == 0) {
                return;
            }
            try {
                for (BusinessBO country : list) {
                    int position = country.getUrl().lastIndexOf("/");
                    List<BusinessBO> towns = new ArrayList<>();
                    if (pattern.equals(Constant.TOWN_PATTERN)) {
                        Table townTable = TableUtils.extraTable(country.getUrl(), pattern);
                        towns = TableUtils.country(townTable, country.getUrl().substring(0, position));
                    } else if (pattern.equals(Constant.VILLAGE_PATTERN)) {
                        VillageTable townTable = TableUtils.extraVillageTable(country.getUrl(), pattern);
                        towns = TableUtils.village(townTable);
                    }
                    if (towns.size() == 0) {
                        System.out.println("没有获取到信息:" + country.getName());
                        continue;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("insert into t_address_town (pId,name,code,url) values");
                    for (BusinessBO town : towns) {
                        sb.append("(");
                        sb.append(country.getId()).append(",");
                        sb.append("'").append(town.getName()).append("'").append(",");
                        sb.append("'").append(town.getCode()).append("'").append(",");
                        sb.append("'").append(StringUtils.isBlank(town.getUrl()) ? "" : town.getUrl()).append("'").append(")").append(",");
                    }
                    if (towns.size() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    try (Connection connection = DBUtils.getConnection();
                         Statement statement = connection.createStatement();) {
                        statement.execute(sb.toString());
                    }
                    System.out.println(country.getId() + " end ...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
