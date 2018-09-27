package com.city.test2.province;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws Exception {
        A a1 = new A();
        a1.setHref("123.html");
        a1.setContent("test1<br/>");
        Td td1 = new Td();
        td1.setA(a1);

        A a2 = new A();
        a2.setHref("345.html");
        a2.setContent("test2");
        Td td2 = new Td();
        td2.setA(a2);

        Tr tr = new Tr();
        tr.setClazz("provincetr");
        tr.setTd(new Td[]{td1, td2});

        Table table = new Table();
        table.setTrs(Arrays.asList(tr));

        StringWriter stringWriter = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(Table.class);

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(table, stringWriter);
        System.out.println(stringWriter);


        String wocaole = "<table><tr class='provincetr'><td><a href='11.html'>北京市<br/></a></td></tr></table>";
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Table table1 = (Table) unmarshaller.unmarshal(new StringReader(wocaole));
        System.out.println("aaaaa");
    }
}
