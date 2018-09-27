package com.city.test2;

import java.util.regex.Pattern;

public class Constant {
    public final static String BASE_URL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/";

    public final static Pattern PROVINCE_PATTERN = Pattern.compile("<tr class=\'provincetr\'>[\\s\\S]*</tr>");
    public final static Pattern CITY_PATTERN = Pattern.compile("<tr class=\'citytr\'>[\\s\\S]*</tr>");
    public final static Pattern COUNTRY_PATTERN = Pattern.compile("<tr class=\'countytr\'>[\\s\\S]*</tr>");
    public final static Pattern TOWN_PATTERN = Pattern.compile("<tr class=\'towntr\'>[\\s\\S]*</tr>");
    public final static Pattern VILLAGE_PATTERN = Pattern.compile("<tr class=\'villagetr\'>[\\s\\S]*</tr>");
}
