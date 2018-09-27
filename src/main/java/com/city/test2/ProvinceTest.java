package com.city.test2;

import com.city.test2.province.Table;

import java.util.List;

public class ProvinceTest {

    static final String baseUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/";
    static final String provinceUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/index.html";

    public static void main(String[] args) throws Exception {



        /*Pattern provincePattern = Pattern.compile("<tr class=\'provincetr\'>[\\s\\S]*</tr>");
        Table provinceTable = extraTable(provinceUrl, provincePattern);
        List<BusinessBO> provinces = TableUtils.province(provinceTable);

        Pattern cityPattern = Pattern.compile("<tr class=\'citytr\'>[\\s\\S]*</tr>");
        Pattern countryPattern = Pattern.compile("<tr class=\'countytr\'>[\\s\\S]*</tr>");
        for(BusinessBO province : provinces){
            Table cityTable = extraTable(baseUrl + province.getUrl(),cityPattern);
            List<BusinessBO> citys = TableUtils.city(cityTable);

            for(BusinessBO city : citys){
                Table countryTable = extraTable(baseUrl + city.getUrl(),countryPattern);
                List<BusinessBO> countrys = TableUtils.city(cityTable);
            }
        }*/

        Table provinceTable = TableUtils.extraTable(Constant.BASE_URL + "index.html", Constant.PROVINCE_PATTERN);
        List<BusinessBO> provinces = TableUtils.province(provinceTable);

        for (BusinessBO province : provinces) {
            if (province.getName().contains("云南")) {
                Processor processor = new Processor(province);
                processor.start();
            }
        }
    }


}
