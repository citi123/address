package com.city.test2.portal;

import com.city.test2.BusinessBO;
import com.city.test2.Constant;
import com.city.test2.TableUtils;
import com.city.test2.jdbc.DBUtils;
import com.city.test2.province.Table;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class Province {

    public static void main(String[] args) throws Exception {

        Table provinceTable = TableUtils.extraTable(Constant.BASE_URL + "index.html", Constant.PROVINCE_PATTERN);
        List<BusinessBO> provinces = TableUtils.province(provinceTable);

        StringBuilder sb = new StringBuilder();
        sb.append("insert into t_address_province (name,code,url) values ");
        for (BusinessBO province : provinces) {
            sb.append("('").append(province.getName()).append("',");
            sb.append("'").append(province.getCode()).append("'").append(",");
            sb.append("'").append(province.getUrl()).append("'").append("),");
        }
        if (provinces.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        try (Connection connection = DBUtils.getConnection();
             Statement statement = connection.createStatement();) {
            statement.execute(sb.toString());
        }
    }

}
