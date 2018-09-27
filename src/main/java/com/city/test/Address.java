package com.city.test;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Address {

    public static String provincePatternStart = "<tr class=\'provincetr\'>";
    public static String provincePatternEnd = "</tr>";

    public static String cityPatternStart = "<tr class=\'citytr\'>";
    public static String cityPatternEnd = "</tr>";

    public static String countryPatternStart = "<tr class=\'countytr\'>";
    public static String countryPatternEnd = "</tr>";

    public static String townPatternStart = "<tr class=\'towntr\'>";
    public static String townPatternEnd = "</tr>";

    public static String villagePatternStart = "<tr class=\'villagetr\'>";
    public static String villagePatternEnd = "</tr>";

    public static String filter_a_Begin = "<a href=";
    public static String filter_a_End = "</a>";

    public static String filter_td_Begin = "<td>";
    public static String filter_td_End = "</td>";

    public static void main1(String[] args) throws Exception {
        String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/13/04/26/130426102.html";
        String content = getContent(url);
        content = filter(content, villagePatternStart, villagePatternEnd);
        content = filter(content, "<td>", "</td>");
        List<AddressBO> village = extraVillage(content);
        for (AddressBO addressBO : village) {
            System.out.println(addressBO.getName());
        }
    }


    public static void main(String[] args) throws Exception {
        String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/index.html";
        String content = getContent(url);
        content = filter(content, provincePatternStart, provincePatternEnd);
        content = filter(content, "<a href=", "</a>");
        List<AddressBO> provinces = extraProvince(content, url);

        int i = 0;
        for (AddressBO province : provinces) {
            /*i++;
            if(i != 8){
                continue;
            }*/
            content = getContent(province.getChildrenUrl());
            // content = filter(content, cityPatternStart, cityPatternEnd);
            // content = filter(content, "<a href=", "</a>");
            content = filter(content, cityPatternStart, cityPatternEnd, filter_a_Begin, filter_a_End);
            List<AddressBO> citys = extraCity(content, province.getChildrenUrl());

            for (AddressBO city : citys) {
                content = getContent(city.getChildrenUrl());
                // content = filter(content, countryPatternStart, countryPatternEnd);
                // content = filter(content, "<a href=", "</a>");
                content = filter(content, countryPatternStart, countryPatternEnd, filter_a_Begin, filter_a_End);
                List<AddressBO> countrys = extraCity(content, city.getChildrenUrl());
                for (AddressBO country : countrys) {
                    content = getContent(country.getChildrenUrl());
                    // content = filter(content, townPatternStart, townPatternEnd);
                    // content = filter(content, "<a href=", "</a>");
                    content = filter(content, townPatternStart, townPatternEnd, filter_a_Begin, filter_a_End);
                    List<AddressBO> towns = extraCity(content, country.getChildrenUrl());
                    for (AddressBO town : towns) {
                        content = getContent(town.getChildrenUrl());
                        // content = filter(content, villagePatternStart, villagePatternEnd);
                        // content = filter(content, "<td>", "</td>");
                        content = filter(content, villagePatternStart, villagePatternEnd, filter_td_Begin, filter_td_End);
                        List<AddressBO> villages = extraVillage(content);
                        for (AddressBO village : villages) {
                            String format = "省:%s，市:%s，县:%s，镇:%s，村:%s";
                            System.out.println(String.format(format,
                                    province.getName(),
                                    city.getName(),
                                    country.getName(),
                                    town.getName(),
                                    village.getName()
                            ));
                        }
                    }
                }
            }
        }
    }

    public static List<AddressBO> extraVillage(String content) throws Exception {
        Document document = wrapperContent(content);
        NodeList nodeList = document.getElementsByTagName("td");
        List<AddressBO> list = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            String code = nodeList.item(i).getTextContent() + "_" +
                    nodeList.item(++i).getTextContent();
            String name = nodeList.item(++i).getTextContent();
            AddressBO addressBO = new AddressBO();
            addressBO.setName(name);
            addressBO.setCode(code);
            list.add(addressBO);
        }
        return list;
    }

    public static List<AddressBO> extraCity(String content, String url) throws Exception {
        long begin = System.currentTimeMillis();
        Document document = wrapperContent(content);
        NodeList provinces = document.getElementsByTagName("a");
        String prefix = url.substring(0, url.lastIndexOf("/") + 1);
        List<AddressBO> list = new ArrayList<>();
        for (int i = 0; i < provinces.getLength() / 2; i++, i++) {
            Node node = provinces.item(i + 1);
            AddressBO addressBO = new AddressBO();
            addressBO.setName(node.getTextContent());
            addressBO.setCode(provinces.item(i).getTextContent());
            addressBO.setChildrenUrl(prefix + node.getAttributes().item(0).getTextContent());
            list.add(addressBO);
        }
        long end = System.currentTimeMillis();
        if ((end - begin) / 1000 > 3) {
            System.out.println("filter: " + (end - begin) / 1000);
        }
        return list;
    }

    public static List<AddressBO> extraProvince(String content, String url) throws Exception {
        Document document = wrapperContent(content);
        NodeList provinces = document.getElementsByTagName("a");
        String prefix = url.substring(0, url.lastIndexOf("/") + 1);
        List<AddressBO> list = new ArrayList<>();
        for (int i = 0; i < provinces.getLength(); i++) {
            Node node = provinces.item(i);
            AddressBO addressBO = new AddressBO();
            addressBO.setName(node.getTextContent());
            addressBO.setCode(node.getAttributes().item(0).getTextContent().split("[.]")[0]);
            addressBO.setChildrenUrl(prefix + node.getAttributes().item(0).getTextContent());
            list.add(addressBO);
        }
        return list;
    }


    static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    static DocumentBuilder builder;

    static {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static Document wrapperContent(String content) throws Exception {
        content = "<wocao>" + content + "</wocao>";
        Document document = builder.parse(new ByteArrayInputStream(content.getBytes()));
        return document;
    }

    public static String filter(String content, String patternStart, String patternEnd) {
        StringBuilder sb = new StringBuilder();
        int start = content.indexOf(patternStart);
        int end = content.indexOf(patternEnd, start);
        while (start != -1 && end != -1) {
            sb.append(content.substring(start, end + patternEnd.length()));
            content = content.substring(end + patternEnd.length());
            start = content.indexOf(patternStart);
            end = content.indexOf(patternEnd, start);
        }
        return sb.toString();
    }

    public static String getContent(String url) throws IOException {
        long beign = System.currentTimeMillis();
        URL u = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
        urlConnection.setConnectTimeout(10 * 1000);
        urlConnection.setReadTimeout(50 * 1000);
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        try{
            if (urlConnection.getResponseCode() == 200) {
                StringBuilder sb = new StringBuilder();
                char[] buffer = new char[2048];
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "GBK"));
                int position = reader.read(buffer);
                while (position != -1) {
                    sb.append(new String(buffer, 0, position));
                    position = reader.read(buffer);
                }
                long end = System.currentTimeMillis();
                if ((end - beign) / 1000 > 3) {
                    System.out.println("cost:" + ((end - beign) / 1000) + " " + url);
                }
                return sb.toString();
            }
            try {
                Thread.sleep(500);
            } catch (Exception ee) {
            }
            System.out.println("retry1: " + url);
            return getContent(url);
        }catch (SocketTimeoutException e){
            try {
                Thread.sleep(500);
            } catch (Exception ee) {
            }
            System.out.println("retry2: " + url);
            return getContent(url);
        }

    }

    public static String filter(String content, String pattern1Start, String pattern1End, String filter1Start, String filter1End) {
        long begin = System.currentTimeMillis();
        content = filter(content, pattern1Start, pattern1End);
        content = filter(content, filter1Start, filter1End);
        long end = System.currentTimeMillis();
        if ((end - begin) / 1000 > 3) {
            System.out.println("filter: " + (end - begin) / 1000);
        }
        return content;
    }
}

