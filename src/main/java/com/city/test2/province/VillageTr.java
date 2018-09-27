package com.city.test2.province;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement(name = "tr")
@XmlAccessorType(XmlAccessType.FIELD)
public class VillageTr implements Serializable {

    @XmlAttribute(name = "class")
    private String clazz;

    @XmlElement(name = "td")
    private VillageTd[] td;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public VillageTd[] getTd() {
        return td;
    }

    public void setTd(VillageTd[] td) {
        this.td = td;
    }
}