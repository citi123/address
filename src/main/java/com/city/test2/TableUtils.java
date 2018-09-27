package com.city.test2;

import com.city.test.Address;
import com.city.test2.province.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableUtils {

    static Unmarshaller unmarshaller = null;
    static Unmarshaller villageUnmarshaller = null;

    static {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Table.class);
            unmarshaller = jaxbContext.createUnmarshaller();

            JAXBContext jaxbContext2 = JAXBContext.newInstance(VillageTable.class);
            villageUnmarshaller = jaxbContext2.createUnmarshaller();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Table extraTable(String url, Pattern pattern) throws Exception {
        List<Table> tables = new ArrayList<>();
        String content = Address.getContent(url);
        Matcher matcher = pattern.matcher(content);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table>");
        if (matcher.find()) {
            stringBuilder.append(matcher.group().replaceAll("<br/>", ""));
        }
        stringBuilder.append("</table>");
        synchronized (unmarshaller) {
            Table table = (Table) unmarshaller.unmarshal(new StringReader(stringBuilder.toString()));
            return table;
        }
    }

    public static VillageTable extraVillageTable(String url, Pattern pattern) throws Exception {
        List<Table> tables = new ArrayList<>();
        String content = Address.getContent(url);
        Matcher matcher = pattern.matcher(content);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table>");
        if (matcher.find()) {
            stringBuilder.append(matcher.group().replaceAll("<br/>", ""));
        }
        stringBuilder.append("</table>");
        synchronized (villageUnmarshaller) {
            VillageTable table = (VillageTable) villageUnmarshaller.unmarshal(new StringReader(stringBuilder.toString()));
            return table;
        }
    }

    public static List<BusinessBO> province(Table table) {
        List<BusinessBO> list = new ArrayList<>();
        for (Tr tr : table.getTrs()) {
            for (Td td : tr.getTd()) {
                if (td.getA() == null) {
                    continue;
                }
                BusinessBO businessBO = new BusinessBO();
                businessBO.setName(td.getA().getContent());
                businessBO.setCode(td.getA().getHref().split("[.]")[0]);
                businessBO.setUrl(Constant.BASE_URL + td.getA().getHref());

                list.add(businessBO);
            }
        }
        return list;
    }

    public static List<BusinessBO> city(Table table) {
        List<BusinessBO> list = new ArrayList<>();
        for (Tr tr : table.getTrs()) {
            Td[] tds = tr.getTd();
            if(tds[0].getA() == null){
                continue;
            }
            BusinessBO businessBO = new BusinessBO();
            businessBO.setName(tds[1].getA().getContent());
            businessBO.setCode(tds[0].getA().getContent());
            businessBO.setUrl(Constant.BASE_URL + tds[0].getA().getHref());
            list.add(businessBO);
        }
        return list;
    }

    public static List<BusinessBO> country(Table table,String urlPrefix){
        List<BusinessBO> list = new ArrayList<>();
        for (Tr tr : table.getTrs()) {
            Td[] tds = tr.getTd();
            if(tds[0].getA() == null){
                continue;
            }
            BusinessBO businessBO = new BusinessBO();
            businessBO.setName(tds[1].getA().getContent());
            businessBO.setCode(tds[0].getA().getContent());
            businessBO.setUrl(urlPrefix + "/" + tds[0].getA().getHref());
            list.add(businessBO);
        }
        return list;
    }

    public static List<BusinessBO> village(VillageTable table){
        List<BusinessBO> list = new ArrayList<>();
        try{
            for(VillageTr tr : table.getTrs()){
                VillageTd[] tds = tr.getTd();
                BusinessBO businessBO = new BusinessBO();
                businessBO.setName(tds[2].getContent());
                businessBO.setCode(tds[0].getContent());
                list.add(businessBO);
            }
        }catch (Exception e){
            int i = 0;
        }
        return list;
    }

    public static List<List<BusinessBO>> group(List<BusinessBO> list,int size) {
        if(size == 0){
            size = 10;
        }
        List<List<BusinessBO>> result = new ArrayList<>();

        List<BusinessBO> temp = new ArrayList<>();
        for (BusinessBO businessBO : list) {
            if (temp.size() > size) {
                result.add(temp);
                temp = new ArrayList<>();
                temp.add(businessBO);
            } else {
                temp.add(businessBO);
            }
        }
        result.add(temp);
        return result;
    }

}
