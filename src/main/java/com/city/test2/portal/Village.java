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

public class Village {
    public static void main(String[] args) throws Exception {
        List<BusinessBO> towns = new ArrayList<>();
        try (Connection connection = DBUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("select * from t_address_town");) {
            while (rs.next()) {
                BusinessBO businessBO = new BusinessBO();
                businessBO.setId(rs.getInt("id"));
                businessBO.setUrl(rs.getString("url"));
                towns.add(businessBO);
            }
        }
        // fen zu
        List<List<BusinessBO>> result = TableUtils.group(towns, 1000);

        for (List<BusinessBO> temp : result) {
            new Task(temp).start();
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(5 * 1000);
                }catch (Exception e){}
                System.out.println("已完成: " + Counter.get());
            }
        });
        t.setName("jianshi");
        t.start();
    }

    static class Task extends Thread {

        List<BusinessBO> list;

        public Task(List<BusinessBO> list) {
            this.list = list;
        }

        @Override
        public void run() {
            try {
                for (BusinessBO town : list) {
                    if (StringUtils.isBlank(town.getUrl())) {
                        continue;
                    }
                    VillageTable villageTable = TableUtils.extraVillageTable(town.getUrl(), Constant.VILLAGE_PATTERN);
                    List<BusinessBO> villages = TableUtils.village(villageTable);
                    StringBuilder sb = new StringBuilder();
                    sb.append("insert into t_address_village (pId,name,code,url) values ");
                    for (BusinessBO village : villages) {
                        sb.append("(");
                        sb.append(town.getId()).append(",");
                        sb.append("'").append(village.getName()).append("'").append(",");
                        sb.append("'").append(village.getCode()).append("'").append(",");
                        sb.append("'").append("").append("'").append(")").append(",");
                    }
                    if (villages.size() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    try (Connection connection = DBUtils.getConnection();
                         Statement statement = connection.createStatement();) {
                        statement.execute(sb.toString());
                    }
                    villages.clear();
                    sb = null;
                    villageTable = null;
                    // System.out.println(town.getId() + " end ...");
                    Counter.add();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Counter{
        static Integer count = 0;

        static void add(){
            synchronized (count){
                count ++;
            }
        }

        static Integer get(){
            return count;
        }
    }
}
