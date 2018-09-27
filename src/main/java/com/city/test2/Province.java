package com.city.test2;

import com.city.test2.province.Table;
import com.city.test2.province.VillageTable;

import java.util.List;

class Processor extends Thread {

    private BusinessBO province;

    public Processor(BusinessBO province) {
        this.province = province;
    }

    @Override
    public void run() {
        try {
            deal();
            System.out.println(province.getName() + " end...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deal() throws Exception {
        // System.out.println(province.getName());

        Table cityTable = TableUtils.extraTable(Constant.BASE_URL + province.getUrl(), Constant.CITY_PATTERN);
        List<BusinessBO> citys = TableUtils.city(cityTable);

        for (BusinessBO city : citys) {
            // System.out.println("\t" + city.getName());
            Table countryTable = TableUtils.extraTable(Constant.BASE_URL + city.getUrl(), Constant.COUNTRY_PATTERN);
            List<BusinessBO> countrys = TableUtils.city(countryTable);

            String prefix = city.getUrl().split("[/]")[0];
            for (BusinessBO country : countrys) {
                // System.out.println("\t\t" + country.getName());
                Table townTable = TableUtils.extraTable(Constant.BASE_URL + "/" + prefix + "/" + country.getUrl(), Constant.TOWN_PATTERN);
                List<BusinessBO> towns = TableUtils.city(townTable);

                String url = country.getUrl().split("[/]")[0];
                for (BusinessBO town : towns) {
                    System.out.println("\t\t\t" + town.getName());
                    VillageTable villageTable = TableUtils.extraVillageTable(Constant.BASE_URL + "/" + prefix + "/" + url + "/" + town.getUrl(), Constant.VILLAGE_PATTERN);
                    List<BusinessBO> villages = TableUtils.village(villageTable);
                    for (BusinessBO village : villages) {
                        // System.out.println("\t\t\t\t" + village.getName());
                    }
                }
            }
        }
    }
}