package com.city.test2.province;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "table")
@XmlAccessorType(XmlAccessType.FIELD)
public class Table implements Serializable {

    @XmlElement(name = "tr")
    private List<Tr> trs;

    public List<Tr> getTrs() {
        return trs;
    }

    public void setTrs(List<Tr> trs) {
        this.trs = trs;
    }
}
