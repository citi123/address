package com.city.test2.province;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "tr")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tr implements Serializable {

    @XmlAttribute(name = "class")
    private String clazz;

    @XmlElement(name = "td")
    private Td[] td;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Td[] getTd() {
        return td;
    }

    public void setTd(Td[] td) {
        this.td = td;
    }
}
