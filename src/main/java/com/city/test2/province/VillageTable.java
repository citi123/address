package com.city.test2.province;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "table")
@XmlAccessorType(XmlAccessType.FIELD)
public class VillageTable implements Serializable {

    @XmlElement(name = "tr")
    private List<VillageTr> trs;

    public List<VillageTr> getTrs() {
        return trs;
    }

    public void setTrs(List<VillageTr> trs) {
        this.trs = trs;
    }
}